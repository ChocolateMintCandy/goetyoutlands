package com.mint.goetyoutlands.common.entity;

import com.Polarice3.Goety.api.entities.IOwned;
import com.Polarice3.Goety.common.entities.ally.Summoned;
import com.Polarice3.Goety.utils.MobUtil;
import com.hollingsworth.arsnouveau.common.entity.EntityChimeraProjectile;
import com.mint.goetyoutlands.Register.ModEntities;
import com.mint.goetyoutlands.common.data.WildenHuntData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.UUID;

public final class WildenGuardianServant extends Summoned implements GeoEntity {
    private static final EntityDataAccessor<Boolean> DEFENDING = SynchedEntityData.defineId(WildenGuardianServant.class, EntityDataSerializers.BOOLEAN);
    private static final int DEFENSE_TIME = 100, DEFENSE_COOLDOWN = 200, SCATTER_COUNT = 20;
    private static final RawAnimation IDLE = RawAnimation.begin().thenPlay("idle");
    private static final RawAnimation RUN = RawAnimation.begin().thenPlay("run");
    private static final RawAnimation DEFEND = RawAnimation.begin().thenPlay("defending");
    private final AnimatableInstanceCache animations = GeckoLibUtil.createInstanceCache(this);
    private int defenseTicks, defenseCooldown;

    public WildenGuardianServant(EntityType<? extends WildenGuardianServant> type, Level level) { super(type, level); }

    @Override
    protected void registerGoals() {
        super.registerGoals(); // Goety supplies retaliation, owner assistance, patrol and follow.
        goalSelector.addGoal(0, new Goal() {
            { setFlags(EnumSet.of(Flag.MOVE, Flag.JUMP)); }
            @Override public boolean canUse() { return isDefending(); }
            @Override public void start() { getNavigation().stop(); }
        });
        goalSelector.addGoal(1, new FloatGoal(this));
        goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.2D, true) {
            @Override public boolean canUse() { return canPursue() && super.canUse(); }
            @Override public boolean canContinueToUse() { return canPursue() && super.canContinueToUse(); }
        });
        goalSelector.addGoal(8, new WanderGoal<>(this, 1.0D));
        goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        goalSelector.addGoal(8, new RandomLookAroundGoal(this));
    }

    private boolean canPursue() { return !isDefending() && !isStaying() && !isCommanded(); }

    @Override
    public void commandMode() {
        if (!isDefending()) super.commandMode();
    }

    @Override
    public void travel(Vec3 input) {
        // Ignore locomotion input during defense, but retain gravity, currents and external knockback.
        super.travel(isDefending() ? Vec3.ZERO : input);
    }

    @Override
    public boolean canAttack(LivingEntity target) {
        if (target == this || !super.canAttack(target) || getPacifyTick() > 0 || isAlliedTo(target) || MobUtil.areAllies(this, target)) return false;
        UUID owner = getOwnerId();
        if (owner != null && (owner.equals(target.getUUID()) || target instanceof IOwned owned && owner.equals(owned.getOwnerId())
                || target instanceof OwnableEntity pet && owner.equals(pet.getOwnerUUID()))) return false;
        return !(target instanceof Player player && getMasterOwner() instanceof Player master && !master.canHarmPlayer(player));
    }

    public boolean maySpike(LivingEntity target) {
        return isAlive() && target.isAlive() && canAttack(target) && (target == getTarget()
                || ((!isGuardingArea() || isWithinGuard(target.blockPosition())) && MobUtil.isOwnedTargetable(this, target)));
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        return !isDefending() && target instanceof LivingEntity living && canAttack(living) && super.doHurtTarget(target);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        return !source.is(DamageTypes.DROWN) && super.hurt(source, amount);
    }

    @Override
    protected void actuallyHurt(DamageSource source, float amount) {
        if (!level().isClientSide && amount > 0.0F) {
            if (defenseCooldown == 0) {
                defenseCooldown = DEFENSE_COOLDOWN;
                defenseTicks = DEFENSE_TIME;
                entityData.set(DEFENDING, true);
                getNavigation().stop();
            }
            if (isDefending() && !source.is(DamageTypeTags.BYPASSES_ARMOR)) {
                amount *= 0.75F;
                if (!source.is(DamageTypes.THORNS) && source.getEntity() instanceof LivingEntity attacker
                        && distanceToSqr(attacker) <= 4.0D && canAttack(attacker)) {
                    attacker.hurt(damageSources().thorns(this), 3.0F);
                }
            }
        }
        super.actuallyHurt(source, amount);
    }

    @Override
    public void tick() {
        super.tick();
        if (!(level() instanceof ServerLevel level)) return;
        if (defenseCooldown > 0) --defenseCooldown;
        if (defenseTicks > 0) {
            getNavigation().stop();
            if (--defenseTicks == 0) {
                entityData.set(DEFENDING, false);
                if (isAlive()) releaseSpikes(level);
            }
        }
    }

    private void releaseSpikes(ServerLevel level) {
        for (int i = 0; i < SCATTER_COUNT; i++) {
            EntityChimeraProjectile spike = createSpike(level);
            if (spike == null) continue;
            spike.shootFromRotation(this, random.nextInt(360), random.nextInt(360), 0.0F, 1.0F + random.nextFloat() * 0.5F, 1.0F);
            level.addFreshEntity(spike);
        }
        LivingEntity target = getTarget();
        if (target == null || !maySpike(target)) return;
        EntityChimeraProjectile aimed = createSpike(level);
        if (aimed != null) {
            double dx = target.getX() - getX(), dz = target.getZ() - getZ();
            double dy = target.getY(1.0D / 3.0D) - aimed.getY() + Math.sqrt(dx * dx + dz * dz) * 0.2D;
            aimed.shoot(dx, dy, dz, 1.6F, 1.0F);
            level.addFreshEntity(aimed);
        }
    }

    private EntityChimeraProjectile createSpike(ServerLevel level) {
        EntityChimeraProjectile spike = ModEntities.WILDEN_GUARDIAN_SPIKE.get().create(level);
        if (spike != null) {
            spike.setOwner(this);
            spike.setPos(getX(), getY() + 1.0D, getZ());
        }
        return spike;
    }

    @Override
    public void onAddedToWorld() {
        super.onAddedToWorld();
        if (isAlive()) updateRoster(getOwnerId());
    }

    @Override
    public void setOwnerId(@Nullable UUID owner) {
        super.setOwnerId(owner);
        if (isAlive()) updateRoster(owner);
    }

    @Override
    public void lifeSpanDamage() { dismiss(); }

    @Override
    public void die(DamageSource source) {
        super.die(source);
        if (dead) updateRoster(null);
    }

    @Override
    public void remove(RemovalReason reason) {
        // Unloading or changing dimension does not release a summon slot.
        if (reason.shouldDestroy()) updateRoster(null);
        super.remove(reason);
    }

    private void updateRoster(@Nullable UUID owner) {
        if (isAddedToWorld() && level() instanceof ServerLevel level) WildenHuntData.get(level).setGuardianOwner(getUUID(), owner);
    }

    public boolean isDefending() { return entityData.get(DEFENDING); }

    @Override
    protected float getWaterSlowDown() { return 0.98F; }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(DEFENDING, false);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("DefenseTicks", defenseTicks);
        tag.putInt("DefenseCooldown", defenseCooldown);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        defenseTicks = Mth.clamp(tag.getInt("DefenseTicks"), 0, DEFENSE_TIME);
        defenseCooldown = Mth.clamp(tag.getInt("DefenseCooldown"), defenseTicks, DEFENSE_COOLDOWN);
        entityData.set(DEFENDING, defenseTicks > 0);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "movement", 1, state -> {
            state.getController().setAnimation(isDefending() ? DEFEND : state.isMoving() ? RUN : IDLE);
            return PlayState.CONTINUE;
        }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() { return animations; }
}

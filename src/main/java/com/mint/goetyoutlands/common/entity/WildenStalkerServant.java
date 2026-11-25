package com.mint.goetyoutlands.common.entity;


import com.Polarice3.Goety.utils.MobUtil;
import com.mint.goetyoutlands.common.data.WildenHuntData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.goal.*;
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

public final class WildenStalkerServant extends MagicSchoolServant implements GeoEntity {
    private static final EntityDataAccessor<Boolean> DIVING = SynchedEntityData.defineId(WildenStalkerServant.class, EntityDataSerializers.BOOLEAN);
    private static final int DIVE_COOLDOWN = 400;
    private static final RawAnimation FLY = RawAnimation.begin().thenPlay("fly");
    private static final RawAnimation RUN = RawAnimation.begin().thenPlay("run");
    private static final RawAnimation IDLE = RawAnimation.begin().thenPlay("idle");
    private final AnimatableInstanceCache animations = GeckoLibUtil.createInstanceCache(this);
    private int diveCooldown;

    public WildenStalkerServant(EntityType<? extends WildenStalkerServant> type, Level level) {
        super(type, level);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals(); // Goety handles ownership, retaliation, target selection and orders.
        goalSelector.addGoal(1, new FloatGoal(this));
        goalSelector.addGoal(2, new DiveGoal());
        goalSelector.addGoal(3, new LeapAtTargetGoal(this, 0.3F) {
            @Override
            public boolean canUse() { return canPursue() && super.canUse(); }
        });
        goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.2D, true) {
            @Override
            public boolean canUse() { return canPursue() && super.canUse(); }

            @Override
            public boolean canContinueToUse() { return canPursue() && super.canContinueToUse(); }
        });
        goalSelector.addGoal(8, new WanderGoal<>(this, 1.0D));
        goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        goalSelector.addGoal(8, new RandomLookAroundGoal(this));
    }

    private boolean canPursue() {
        return !isStaying() && !isCommanded() && !isPassenger();
    }

    @Override
    public boolean canAttack(LivingEntity target) {
        return super.canAttack(target) && !isAlliedTo(target) && !MobUtil.areAllies(this, target);
    }

    @Override
    public boolean doHurtTarget(Entity entity) {
        if (!(entity instanceof LivingEntity target) || !canAttack(target)) return false;
        // A successful hurt callback alone doesn't guarantee damage (e.g. damage reduced to zero).
        float health = target.getHealth(), absorption = target.getAbsorptionAmount();
        boolean hit = super.doHurtTarget(target);
        if (hit && !level().isClientSide
                && (target.getHealth() < health || target.getAbsorptionAmount() < absorption)) {
            target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 40, 0), this);
        }
        return hit;
    }

    private final class DiveGoal extends Goal {
        private int targetId;

        DiveGoal() { setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.JUMP)); }

        private boolean valid(LivingEntity target) {
            return target != null && target.isAlive() && target.level() == level() && canAttack(target)
                    && (!(target instanceof Player p) || (!p.isCreative() && !p.isSpectator()));
        }

        @Override
        public boolean canUse() { return canPursue() && diveCooldown == 0 && valid(getTarget()); }

        @Override
        public boolean canContinueToUse() {
            LivingEntity target = getTarget();
            return isDiving() && canPursue() && valid(target) && target.getId() == targetId;
        }

        @Override
        public void start() {
            targetId = getTarget().getId();
            diveCooldown = DIVE_COOLDOWN;
            getNavigation().stop();
            entityData.set(DIVING, true);
            push(0.0D, 0.5D, 0.0D);
        }

        @Override
        public boolean requiresUpdateEveryTick() { return true; }

        @Override
        public void tick() {
            // The target selector may change targets between goal-selection ticks.
            if (!canContinueToUse() || hurtTime > 0) {
                stop();
                return;
            }
            LivingEntity target = getTarget();
            if (getBoundingBox().inflate(0.2D).intersects(target.getBoundingBox())) {
                doHurtTarget(target);
                stop();
                return;
            }
            double dx = target.getX() - getX(), dy = target.getY(0.5D) - getY(0.5D), dz = target.getZ() - getZ();
            double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);
            double scale = 1.0D / Math.max(1.0D, distance);
            Vec3 motion = getDeltaMovement();
            setDeltaMovement(Mth.lerp(0.2D, motion.x, dx * scale), Mth.lerp(0.2D, motion.y, dy * scale), Mth.lerp(0.2D, motion.z, dz * scale));
            if (dx * dx + dz * dz > 1.0E-6D) {
                setYRot(Mth.approachDegrees(getYRot(), (float) Math.toDegrees(Mth.atan2(dz, dx)) - 90.0F, 12.0F));
                yBodyRot = getYRot();
                setYHeadRot(yBodyRot);
            }
        }

        @Override
        public void stop() { entityData.set(DIVING, false); }
    }

    @Override
    public void onAddedToWorld() {
        super.onAddedToWorld();
        if (level() instanceof ServerLevel level && isAlive()) WildenHuntData.get(level).setStalkerOwner(getUUID(), getOwnerId());
    }

    @Override
    public void setOwnerId(@Nullable UUID owner) {
        super.setOwnerId(owner);
        // A crouching spawn egg binds the servant after insertion.
        if (isAddedToWorld() && isAlive() && level() instanceof ServerLevel level) WildenHuntData.get(level).setStalkerOwner(getUUID(), owner);
    }

    @Override
    public void lifeSpanDamage() { dismiss(); }

    @Override
    public void die(DamageSource source) {
        super.die(source);
        if (dead) releaseSlot();
    }

    @Override
    public void remove(RemovalReason reason) {
        // Keep membership while unloaded or travelling between dimensions.
        if (reason.shouldDestroy()) releaseSlot();
        super.remove(reason);
    }

    private void releaseSlot() {
        if (isAddedToWorld() && level() instanceof ServerLevel level) WildenHuntData.get(level).setStalkerOwner(getUUID(), null);
    }

    public boolean isDiving() { return entityData.get(DIVING); }

    @Override
    public void travel(Vec3 input) {
        if (!isDiving()) {
            super.travel(input);
            return;
        }
        // Only the attack bypasses ground navigation/gravity; following never enables flight.
        if (!level().isClientSide) {
            move(MoverType.SELF, getDeltaMovement());
            setDeltaMovement(getDeltaMovement().scale(isInWater() ? 0.8D : isInLava() ? 0.5D : 0.91D));
            if (horizontalCollision || onGround()) entityData.set(DIVING, false);
        }
        calculateEntityAnimation(false);
    }

    @Override
    protected int calculateFallDamage(float distance, float multiplier) { return 0; }

    @Override
    public void tick() {
        if (!level().isClientSide && diveCooldown > 0) --diveCooldown;
        super.tick();
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(DIVING, false);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("DiveCooldown", diveCooldown);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        // The previous target isn't restored by Goety. Land normally instead of resuming a stale dive.
        entityData.set(DIVING, false);
        diveCooldown = Mth.clamp(tag.getInt("DiveCooldown"), 0, DIVE_COOLDOWN);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "movement", 1, state -> {
            state.getController().setAnimation(isDiving() ? FLY : state.isMoving() ? RUN : IDLE);
            return PlayState.CONTINUE;
        }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() { return animations; }
}

package com.mint.goetyoutlands.common.entity;

import com.Polarice3.Goety.common.entities.ModEntityType;
import com.Polarice3.Goety.common.entities.ally.BlackWolf;

import com.Polarice3.Goety.utils.MobUtil;
import com.mint.goetyoutlands.common.data.WildenHuntData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LeapAtTargetGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.ForgeEventFactory;
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

public class WildenHunterServant extends MagicSchoolServant implements GeoEntity {
    private static final EntityDataAccessor<Boolean> HOWLING = SynchedEntityData.defineId(WildenHunterServant.class, EntityDataSerializers.BOOLEAN);
    private static final RawAnimation HOWL = RawAnimation.begin().thenPlay("howl_master").thenPlay("idle");
    private int howlCooldown;
    private static final RawAnimation IDLE = RawAnimation.begin().thenPlay("idle");
    private static final RawAnimation RUN = RawAnimation.begin().thenPlay("run");
    private final AnimatableInstanceCache animations = GeckoLibUtil.createInstanceCache(this);

    public WildenHunterServant(EntityType<? extends WildenHunterServant> type, Level level) {
        super(type, level);
    }

    @Override
    protected void registerGoals() {
        // Goety supplies owner defense, target selection, patrol and follow; don't inherit the hostile hunter's targets.
        super.registerGoals();
        goalSelector.addGoal(1, new FloatGoal(this));
        goalSelector.addGoal(2, new HowlGoal());
        goalSelector.addGoal(3, new LeapAtTargetGoal(this, 0.3F) {
            @Override
            public boolean canUse() { return !isStaying() && super.canUse(); }
        });
        goalSelector.addGoal(4, new MeleeAttackGoal(this, 2.0D, true) {
            @Override
            public boolean canUse() { return !isStaying() && super.canUse(); }

            @Override
            public boolean canContinueToUse() { return !isStaying() && super.canContinueToUse(); }
        });
        goalSelector.addGoal(8, new WanderGoal<>(this, 1.0D));
        goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        goalSelector.addGoal(8, new RandomLookAroundGoal(this));
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "movement", 1, state -> {
            state.getController().setAnimation(entityData.get(HOWLING) ? HOWL : state.isMoving() ? RUN : IDLE);
            return PlayState.CONTINUE;
        }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() { return animations; }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(HOWLING, false);
    }

    @Override
    public void tick() {
        if (!level().isClientSide && howlCooldown > 0) --howlCooldown;
        super.tick();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("HowlCooldown", howlCooldown);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        howlCooldown = Math.max(0, tag.getInt("HowlCooldown"));
    }

    @Override
    public void onAddedToWorld() {
        super.onAddedToWorld();
        if (level() instanceof ServerLevel level && isAlive()) WildenHuntData.get(level).setHunterOwner(getUUID(), getOwnerId());
    }

    @Override
    public void setOwnerId(@Nullable UUID owner) {
        super.setOwnerId(owner);
        // Goety also binds eggs after insertion; temporary entities created from jar/NBT previews do not count.
        if (isAddedToWorld() && isAlive() && level() instanceof ServerLevel level) WildenHuntData.get(level).setHunterOwner(getUUID(), owner);
    }

    @Override
    public void lifeSpanDamage() { dismiss(); }

    @Override
    public void die(DamageSource source) {
        super.die(source);
        if (dead && isAddedToWorld() && level() instanceof ServerLevel level) {
            WildenHuntData.get(level).retireHunter(level, getUUID(), getOwnerId());
        }
    }

    @Override
    public void remove(RemovalReason reason) {
        if (reason.shouldDestroy() && isAddedToWorld() && level() instanceof ServerLevel level) {
            WildenHuntData.get(level).retireHunter(level, getUUID(), dead || reason == RemovalReason.KILLED ? getOwnerId() : null);
        }
        super.remove(reason);
    }

    private boolean hasHowlTarget() {
        LivingEntity target = getTarget();
        return target != null && target.isAlive() && canAttack(target) && !MobUtil.areAllies(this, target);
    }

    private final class HowlGoal extends Goal {
        private int elapsed;

        HowlGoal() { setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK)); }

        @Override
        public boolean canUse() {
            return howlCooldown == 0 && hasHowlTarget() && level() instanceof ServerLevel level
                    && WildenHuntData.get(level).wolfSlots(getUUID()) > 0;
        }

        @Override
        public boolean canContinueToUse() { return elapsed < 45 && isAlive(); }

        @Override
        public boolean isInterruptable() { return false; }

        @Override
        public void start() {
            elapsed = 0;
            howlCooldown = 400;
            getNavigation().stop();
            entityData.set(HOWLING, true);
            level().playSound(null, blockPosition(), SoundEvents.WOLF_HOWL, SoundSource.NEUTRAL, 1.0F, 0.3F);
        }

        @Override
        public void tick() {
            // Use Ars' goal cadence: spawn in the middle of howl_master, not immediately on start.
            if (++elapsed == 20 && hasHowlTarget() && level() instanceof ServerLevel level) summonPack(level);
        }

        @Override
        public void stop() { entityData.set(HOWLING, false); }
    }

    private void summonPack(ServerLevel level) {
        WildenHuntData data = WildenHuntData.get(level);
        for (int i = 0; i < 2 && data.wolfSlots(getUUID()) > 0; i++) {
            BlackWolf wolf = ModEntityType.BLACK_WOLF.get().create(level);
            if (wolf == null || !placeSummon(level, this, wolf)) continue;
            wolf.setTrueOwner(this);
            wolf.getPersistentData().putUUID(WildenHuntData.PACK_TAG, getUUID());
            wolf.setLimitedLife(600);
            wolf.setPersistenceRequired();
            ForgeEventFactory.onFinalizeSpawn(wolf, level, level.getCurrentDifficultyAt(wolf.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
            wolf.setTarget(getTarget());
            if (level.addFreshEntity(wolf)) data.recordWolf(getUUID(), wolf.getUUID());
        }
    }

    /** Search only loaded ground near the caster, including enough margin for the summon's hitbox. */
    public static boolean placeSummon(ServerLevel level, LivingEntity caster, Mob summon) {
        int first = level.random.nextInt(25);
        for (int i = 0; i < 25; i++) {
            int cell = (first + i) % 25;
            int x = caster.getBlockX() + cell % 5 - 2, z = caster.getBlockZ() + cell / 5 - 2;
            if (!level.hasChunksAt(x - 2, z - 2, x + 2, z + 2)) continue;
            for (int dy = 1; dy >= -2; dy--) {
                BlockPos pos = new BlockPos(x, caster.getBlockY() + dy, z);
                AABB box = summon.getType().getDimensions().makeBoundingBox(x + 0.5D, pos.getY(), z + 0.5D);
                if (box.minY < level.getMinBuildHeight() || box.maxY > level.getMaxBuildHeight() || !level.getWorldBorder().isWithinBounds(box)) continue;
                BlockPos below = pos.below();
                if (!level.getBlockState(below).isFaceSturdy(level, below, Direction.UP) || !level.noCollision(summon, box)) continue;
                summon.moveTo(pos, caster.getYRot(), 0.0F);
                return true;
            }
        }
        return false;
    }

    @Override
    protected SoundEvent getAmbientSound() { return SoundEvents.WOLF_GROWL; }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) { return SoundEvents.WOLF_HURT; }

    @Override
    protected SoundEvent getDeathSound() { return SoundEvents.WOLF_DEATH; }

    @Override
    protected float getSoundVolume() { return 0.4F; }

    @Override
    public void playSound(SoundEvent sound, float volume, float pitch) {
        super.playSound(sound, volume, pitch - 0.5F);
    }
}

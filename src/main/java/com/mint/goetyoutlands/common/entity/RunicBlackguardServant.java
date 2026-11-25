package com.mint.goetyoutlands.common.entity;

import com.Polarice3.Goety.common.entities.ally.undead.zombie.BlackguardServant;
import com.Polarice3.Goety.config.AttributesConfig;
import com.Polarice3.Goety.utils.MobUtil;
import com.mint.goetyoutlands.compat.ars.RunicBlackguardArsMagic;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;

import java.util.UUID;

public final class RunicBlackguardServant extends BlackguardServant {
    private static final EntityDataAccessor<Boolean> RUSHING = SynchedEntityData.defineId(RunicBlackguardServant.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<String> ATTUNEMENT = SynchedEntityData.defineId(RunicBlackguardServant.class, EntityDataSerializers.STRING);
    private static final String ATTUNEMENT_TAG = "RunicAttunement", NONE = "none", WATER = "water";
    private static final AttributeModifier IMPACT = new AttributeModifier(UUID.fromString("59475453-0a31-4910-84fa-08205eba2dad"),
            "Runic charge impact", 0.5D, AttributeModifier.Operation.ADDITION);
    private int impactTarget = -1;
    private long nextRush;

    public RunicBlackguardServant(EntityType<? extends RunicBlackguardServant> type, Level level) {
        super(type, level);
    }

    private static double damage() { return AttributesConfig.BlackguardServantDamage.get() + 1.0D; }
    private static double armor() { return AttributesConfig.BlackguardServantArmor.get() + 2.0D; }

    public static AttributeSupplier.Builder setCustomAttributes() {
        return BlackguardServant.setCustomAttributes().add(Attributes.ATTACK_DAMAGE, damage()).add(Attributes.ARMOR, armor());
    }

    @Override
    public void setConfigurableAttributes() {
        super.setConfigurableAttributes();
        MobUtil.setBaseAttributes(getAttribute(Attributes.ATTACK_DAMAGE), damage());
        MobUtil.setBaseAttributes(getAttribute(Attributes.ARMOR), armor());
    }

    @Override
    public EntityType<?> getVariant(Level level, BlockPos pos) { return getType(); }

    @Override
    public void attackGoal() {
        super.attackGoal();
        // Below the committed Goety swing and patrol orders, above ordinary pursuit.
        goalSelector.addGoal(3, new BlackguardRushGoal(this));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(RUSHING, false);
        entityData.define(ATTUNEMENT, NONE);
    }

    public boolean isWaterAttuned() { return WATER.equals(entityData.get(ATTUNEMENT)); }
    public boolean isRushing() { return entityData.get(RUSHING); }
    boolean rushReady() { return level().getGameTime() >= nextRush; }
    void beginRush() {
        nextRush = level().getGameTime() + BlackguardRushGoal.COOLDOWN;
        entityData.set(RUSHING, true);
    }
    void endRush() { entityData.set(RUSHING, false); }
    void commitSmash(int targetId) {
        impactTarget = targetId;
        setMeleeAttacking(true); // ShieldedMeleeGoal owns the windup, turning, area and damage.
    }

    @Override
    public void setMeleeAttacking(boolean attacking) {
        super.setMeleeAttacking(attacking);
        if (!attacking) impactTarget = -1;
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        if (target != getTarget() || target.getId() != impactTarget || !isMeleeAttacking() || getAttackTick() != 14)
            return super.doHurtTarget(target);
        impactTarget = -1;
        AttributeInstance knockback = getAttribute(Attributes.ATTACK_KNOCKBACK);
        // Only the original physical hit sees the temporary knockback modifier.
        if (knockback != null) knockback.addTransientModifier(IMPACT);
        boolean hit;
        try { hit = super.doHurtTarget(target); }
        finally { if (knockback != null) knockback.removeModifier(IMPACT); }
        if (hit && isWaterAttuned() && level() instanceof ServerLevel server && target instanceof LivingEntity living && living.isAlive()) {
            RunicBlackguardArsMagic.freeze(server, this, living);
        }
        return hit;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putString(ATTUNEMENT_TAG, entityData.get(ATTUNEMENT));
        tag.putInt("RunicRushCooldown", (int)Math.max(0L, nextRush - level().getGameTime()));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        entityData.set(ATTUNEMENT, WATER.equals(tag.getString(ATTUNEMENT_TAG)) ? WATER : NONE);
        nextRush = level().getGameTime() + Math.max(0, Math.min(BlackguardRushGoal.COOLDOWN, tag.getInt("RunicRushCooldown")));
        impactTarget = -1;
        endRush();
    }
}

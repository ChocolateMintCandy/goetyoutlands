package com.mint.goetyoutlands.common.entity.projectile;

import com.hollingsworth.arsnouveau.common.entity.EntityChimeraProjectile;
import com.mint.goetyoutlands.Register.ModEntities;
import com.mint.goetyoutlands.common.entity.WildenGuardianServant;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;

public final class WildenGuardianSpike extends EntityChimeraProjectile {
    public WildenGuardianSpike(EntityType<EntityChimeraProjectile> type, Level level) { super(type, level); }

    // Ars hard-codes its own type here; both the spawn packet and saved entity must use ours.
    @Override
    public EntityType<?> getType() { return ModEntities.WILDEN_GUARDIAN_SPIKE.get(); }

    @Override
    protected boolean canHitEntity(Entity entity) {
        // Filter during the ray trace, not just after impact: a friendly must not shield a hostile behind it.
        return entity instanceof LivingEntity target && target.canBeHitByProjectile()
                && getOwner() instanceof WildenGuardianServant guardian && !guardian.isPassengerOfSameVehicle(target)
                && guardian.maySpike(target);
    }

    @Override
    protected void onHitEntity(EntityHitResult hit) {
        if (level().isClientSide || !(hit.getEntity() instanceof LivingEntity target) || !canHitEntity(target)) return;
        WildenGuardianServant guardian = (WildenGuardianServant) getOwner();
        if (target.hurt(damageSources().indirectMagic(this, guardian), 7.5F)) {
            guardian.setLastHurtMob(target);
            // Ars removes beneficial effects and applies Slowness III for 100 ticks.
            super.doPostHurtEffects(target);
            playSound(getDefaultHitGroundSoundEvent(), 1.0F, 1.2F / (random.nextFloat() * 0.2F + 0.9F));
        }
        discard();
    }
}

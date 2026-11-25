package com.mint.goetyoutlands.common.entity;

import com.Polarice3.Goety.common.entities.ally.Summoned;
import com.Polarice3.Goety.config.MobsConfig;
import com.Polarice3.Goety.init.ModTags;
import com.Polarice3.Goety.utils.CuriosFinder;
import com.Polarice3.Goety.utils.SEHelper;
import com.mint.goetyoutlands.Register.ModItems;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

abstract class MagicSchoolServant extends Summoned {
    protected MagicSchoolServant(EntityType<? extends MagicSchoolServant> type, Level level) {
        super(type, level);
    }

    private LivingEntity equipmentOwner() {
        LivingEntity owner = getTrueOwner();
        return owner == null || owner instanceof Player || !MobsConfig.ServantOwnedServantPlayerBenefit.get()
                ? owner : getMasterOwner();
    }

    @Override
    public boolean hasLifespan() {
        // Goety persists the remaining ticks separately. Gate the countdown, not the saved lifetime.
        if (!super.hasLifespan()) return false;
        return !(equipmentOwner() instanceof Player player && CuriosFinder.hasCurio(player, ModItems.SPELLWEAVER_CIRCLET.get()));
    }

    @Override
    public void healServant() {
        float before = getHealth();
        super.healServant(); // Keep host crown/tag interactions if a data pack adds another school.
        if (!(level() instanceof ServerLevel level) || getHealth() != before || getHealth() >= getMaxHealth()
                || !MobsConfig.NaturalMinionHeal.get() || isOnFire() || isDeadOrDying()
                || getType().is(ModTags.EntityTypes.NO_HEAL_SERVANTS)) return;
        long interval = 20L * MobsConfig.NaturalMinionHealTime.get() + 1;
        if (tickCount % interval != 0 || (hasLifespan() && getLifespan() <= 20)) return;
        if (!(equipmentOwner() instanceof Player player) || !CuriosFinder.hasCurio(player, ModItems.SPELLWEAVER_ROBE.get())) return;
        int cost = MobsConfig.NaturalMinionHealCost.get();
        if (!SEHelper.getSoulsAmount(player, cost)) return;
        heal(MobsConfig.NaturalMinionHealAmount.get().floatValue());
        SEHelper.decreaseSouls(player, cost);
        Vec3 velocity = getDeltaMovement();
        level.sendParticles(ParticleTypes.SCULK_SOUL, getRandomX(0.5), getRandomY(), getRandomZ(0.5), 0,
                velocity.x * -0.2, 0.1, velocity.z * -0.2, 0.5);
    }
}

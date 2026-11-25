package com.mint.goetyoutlands.common.magic;

import com.Polarice3.Goety.api.magic.SpellType;
import com.Polarice3.Goety.common.enchantments.ModEnchantments;
import com.Polarice3.Goety.common.magic.SpellStat;
import com.Polarice3.Goety.common.magic.SummonSpell;
import com.Polarice3.Goety.init.ModSounds;
import com.Polarice3.Goety.utils.MobUtil;
import com.Polarice3.Goety.utils.SEHelper;
import com.Polarice3.Goety.utils.WandUtil;
import com.mint.goetyoutlands.Register.ModEntities;
import com.mint.goetyoutlands.Register.ModItems;
import com.mint.goetyoutlands.common.data.WildenHuntData;
import com.mint.goetyoutlands.common.entity.WildenGuardianServant;
import com.mint.goetyoutlands.common.entity.WildenHunterServant;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.event.ForgeEventFactory;

import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

public final class WildenGuardianSpell extends SummonSpell {
    @Override
    public SpellType getSpellType() { return SpellType.NONE; }

    @Override
    public SoundEvent CastingSound() { return ModSounds.WILD_PREPARE_SPELL.get(); }

    @Override
    public int defaultSoulCost() { return 20; }

    @Override
    public int defaultCastDuration() { return 80; }

    @Override
    public int defaultSpellCooldown() { return 360; }

    @Override
    public int SummonDownDuration() { return 200; }

    @Override
    public List<Enchantment> acceptedEnchantments() {
        return List.of(ModEnchantments.POTENCY.get(), ModEnchantments.DURATION.get());
    }

    @Override
    public int summonLimit() { return WildenHuntData.GUARDIAN_LIMIT; }

    @Override
    public Predicate<LivingEntity> summonPredicate() { return WildenGuardianServant.class::isInstance; }

    @Override
    public boolean conditionsMet(ServerLevel level, LivingEntity caster) {
        return caster instanceof Player player && (isShifting(player) || WildenHuntData.get(level).guardianSlots(player.getUUID()) > 0);
    }

    @Override
    public boolean conditionsMet(ServerLevel level, LivingEntity caster, SpellStat stats) { return conditionsMet(level, caster); }

    @Override
    public void commonResult(ServerLevel level, LivingEntity caster) {
        if (!(caster instanceof Player player) || !isShifting(player)) return;
        for (UUID id : WildenHuntData.get(level).guardiansOf(player.getUUID())) {
            if (level.getEntity(id) instanceof WildenGuardianServant guardian && guardian.isAlive()
                    && guardian.getTrueOwner() == player && !SEHelper.isGrounded(player, guardian)) {
                guardian.getNavigation().stop();
                guardian.moveTo(player.position());
            }
        }
        commonResultHit(level, player);
    }

    @Override
    public void playSound(ServerLevel level, Entity source, SoundEvent sound) {
        // Preserve Goety's recall particles, replacing only the accompanying sound.
        if (sound == ModSounds.SUMMON_SPELL.get() && source instanceof Player player && isShifting(player)) {
            super.playSound(level, source, SoundEvents.ILLUSIONER_MIRROR_MOVE, 1.0F, 1.0F);
        } else {
            super.playSound(level, source, sound);
        }
    }

    @Override
    public void SpellResult(ServerLevel level, LivingEntity caster, ItemStack staff, SpellStat stats) {
        if (!(caster instanceof Player player)) return;
        if (isShifting(player)) {
            commonResult(level, player);
            return;
        }
        WildenHuntData data = WildenHuntData.get(level);
        UUID owner = player.getUUID();
        int count = Math.min(staff.is(ModItems.SPELLWEAVER_STAFF.get()) ? 2 : 1 + level.random.nextInt(2), data.guardianSlots(owner));
        int potency = stats.getPotency() + WandUtil.getPotencyLevel(player);
        int duration = Math.max(1, stats.getDuration() + WandUtil.getLevels(ModEnchantments.DURATION.get(), player));
        boolean spawned = false;
        for (int i = 0; i < count && data.guardianSlots(owner) > 0; i++) {
            WildenGuardianServant guardian = ModEntities.WILDEN_GUARDIAN_SERVANT.get().create(level);
            if (guardian == null || !WildenHunterServant.placeSummon(level, player, guardian)) continue;
            guardian.setTrueOwner(player);
            guardian.setLimitedLife((int) Math.min(Integer.MAX_VALUE, (long) MobUtil.getSummonLifespan(level) * duration));
            guardian.setPersistenceRequired();
            ForgeEventFactory.onFinalizeSpawn(guardian, level, level.getCurrentDifficultyAt(guardian.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
            buffSummon(player, guardian, potency);
            setTarget(player, guardian);
            if (level.addFreshEntity(guardian)) {
                spawned = true;
                uponSummon(level, player, staff, guardian);
                summonAdvancement(player, guardian);
            }
        }
        if (spawned) {
            SummonDown(player);
            playSound(level, player, SoundEvents.POLAR_BEAR_WARNING, 0.7F, 0.9F);
        }
    }
}

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
import com.mint.goetyoutlands.common.data.WildenHuntData;
import com.mint.goetyoutlands.common.entity.WildenHunterServant;
import com.mint.goetyoutlands.common.entity.WildenStalkerServant;
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

public final class WildenStalkerSpell extends SummonSpell {
    @Override
    public SpellType getSpellType() { return SpellType.NONE; }

    @Override
    public SoundEvent CastingSound() { return ModSounds.WILD_PREPARE_SPELL.get(); }

    @Override
    public float castingPitch() { return 1.1F; }

    @Override
    public void playSound(ServerLevel level, Entity source, SoundEvent sound) {
        // Keep Goety's commonResultHit particles; only substitute its recall sound.
        if (sound == ModSounds.SUMMON_SPELL.get() && source instanceof Player player && isShifting(player)) {
            super.playSound(level, source, SoundEvents.ILLUSIONER_MIRROR_MOVE, 1.0F, 1.0F);
        } else {
            super.playSound(level, source, sound);
        }
    }

    @Override
    public int defaultSoulCost() { return 16; }

    @Override
    public int defaultCastDuration() { return 60; }

    @Override
    public int defaultSpellCooldown() { return 300; }

    @Override
    public int SummonDownDuration() { return 200; }

    @Override
    public List<Enchantment> acceptedEnchantments() {
        return List.of(ModEnchantments.POTENCY.get(), ModEnchantments.DURATION.get());
    }

    @Override
    public int summonLimit() { return WildenHuntData.STALKER_LIMIT; }

    @Override
    public Predicate<LivingEntity> summonPredicate() { return WildenStalkerServant.class::isInstance; }

    @Override
    public boolean conditionsMet(ServerLevel level, LivingEntity caster) {
        // Goety charges even an empty recall, but not a normal cast already at capacity.
        return caster instanceof Player player && (isShifting(player) || WildenHuntData.get(level).stalkerSlots(player.getUUID()) > 0);
    }

    @Override
    public boolean conditionsMet(ServerLevel level, LivingEntity caster, SpellStat stats) {
        return conditionsMet(level, caster);
    }

    @Override
    public void commonResult(ServerLevel level, LivingEntity caster) {
        if (!(caster instanceof Player player) || !isShifting(player)) return;
        for (UUID id : WildenHuntData.get(level).stalkersOf(player.getUUID())) {
            if (level.getEntity(id) instanceof WildenStalkerServant stalker && stalker.isAlive()
                    && stalker.getTrueOwner() == player && !SEHelper.isGrounded(player, stalker)) {
                stalker.getNavigation().stop();
                stalker.moveTo(player.position());
            }
        }
        commonResultHit(level, player);
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
        int count = Math.min(1 + level.random.nextInt(2), data.stalkerSlots(owner));
        int potency = stats.getPotency() + WandUtil.getPotencyLevel(player);
        int duration = Math.max(1, stats.getDuration() + WandUtil.getLevels(ModEnchantments.DURATION.get(), player));
        boolean spawned = false;
        for (int i = 0; i < count && data.stalkerSlots(owner) > 0; i++) {
            WildenStalkerServant stalker = ModEntities.WILDEN_STALKER_SERVANT.get().create(level);
            if (stalker == null || !WildenHunterServant.placeSummon(level, player, stalker)) continue;
            stalker.setTrueOwner(player);
            stalker.setLimitedLife((int) Math.min(Integer.MAX_VALUE, (long) MobUtil.getSummonLifespan(level) * duration));
            stalker.setPersistenceRequired();
            ForgeEventFactory.onFinalizeSpawn(stalker, level, level.getCurrentDifficultyAt(stalker.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
            buffSummon(player, stalker, potency);
            setTarget(player, stalker);
            if (level.addFreshEntity(stalker)) {
                spawned = true;
                uponSummon(level, player, staff, stalker);
                summonAdvancement(player, stalker);
            }
        }
        if (spawned) {
            SummonDown(player);
            playSound(level, player, SoundEvents.ENDER_DRAGON_FLAP, 0.8F, 1.15F);
        }
    }
}

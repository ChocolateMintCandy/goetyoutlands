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

public final class WildenHuntSpell extends SummonSpell {
    @Override
    public SpellType getSpellType() { return SpellType.NONE; }

    @Override
    public SoundEvent CastingSound() { return ModSounds.WILD_PREPARE_SPELL.get(); }

    @Override
    public float castingPitch() { return 0.9F; }

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
    public int summonLimit() { return WildenHuntData.HUNTER_LIMIT; }

    @Override
    public Predicate<LivingEntity> summonPredicate() { return WildenHunterServant.class::isInstance; }

    @Override
    public boolean conditionsMet(ServerLevel level, LivingEntity caster) {
        if (!(caster instanceof Player player)) return false;
        // Like Hunting Focus, a crouching cast may complete (and spend souls) even with no servants to recall.
        return isShifting(player) || WildenHuntData.get(level).hunterSlots(player.getUUID()) > 0;
    }

    @Override
    public boolean conditionsMet(ServerLevel level, LivingEntity caster, SpellStat stats) {
        return conditionsMet(level, caster);
    }

    @Override
    public void commonResult(ServerLevel level, LivingEntity caster) {
        if (!(caster instanceof Player player) || !isShifting(player)) return;
        // Only recall loaded hunters in this dimension. Never fetch a chunk to recall.
        for (UUID id : WildenHuntData.get(level).huntersOf(player.getUUID())) {
            if (level.getEntity(id) instanceof WildenHunterServant hunter && hunter.isAlive()
                    && hunter.getTrueOwner() == player && !SEHelper.isGrounded(player, hunter)) {
                hunter.getNavigation().stop();
                hunter.moveTo(player.position());
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
        int count = Math.min(staff.is(ModItems.SPELLWEAVER_STAFF.get()) ? 4 : 1 + level.random.nextInt(2), data.hunterSlots(player.getUUID()));
        int potency = stats.getPotency() + WandUtil.getPotencyLevel(player);
        int duration = Math.max(1, stats.getDuration() + WandUtil.getLevels(ModEnchantments.DURATION.get(), player));
        boolean spawned = false;
        for (int i = 0; i < count && data.hunterSlots(player.getUUID()) > 0; i++) {
            WildenHunterServant hunter = ModEntities.WILDEN_HUNTER_SERVANT.get().create(level);
            if (hunter == null || !WildenHunterServant.placeSummon(level, caster, hunter)) continue;
            hunter.setTrueOwner(player);
            hunter.setLimitedLife((int)Math.min(Integer.MAX_VALUE, (long)MobUtil.getSummonLifespan(level) * duration));
            hunter.setPersistenceRequired();
            ForgeEventFactory.onFinalizeSpawn(hunter, level, level.getCurrentDifficultyAt(hunter.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
            buffSummon(player, hunter, potency);
            setTarget(player, hunter);
            if (level.addFreshEntity(hunter)) {
                spawned = true;
                uponSummon(level, player, staff, hunter);
                summonAdvancement(player, hunter);
            }
        }
        if (spawned) {
            SummonDown(player);
            playSound(level, player, SoundEvents.WOLF_GROWL, 0.8F, 0.85F);
        }
    }
}

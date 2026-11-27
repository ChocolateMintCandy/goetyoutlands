package com.mint.goetyoutlands.common.magic;

import com.Polarice3.Goety.api.items.magic.IFocus;
import com.Polarice3.Goety.api.magic.ISpell;
import com.Polarice3.Goety.common.enchantments.ModEnchantments;
import com.Polarice3.Goety.common.magic.SpellStat;
import com.Polarice3.Goety.common.magic.spells.necromancy.BlackguardSpell;
import com.Polarice3.Goety.init.ModSounds;
import com.Polarice3.Goety.utils.BlockFinder;
import com.Polarice3.Goety.utils.MobUtil;
import com.Polarice3.Goety.utils.SEHelper;
import com.Polarice3.Goety.utils.WandUtil;
import com.mint.goetyoutlands.Register.ModEntities;
import com.mint.goetyoutlands.Register.ModItems;
import com.mint.goetyoutlands.common.entity.RunicBlackguardServant;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public final class RunicBlackguardSpell extends BlackguardSpell {
    public static final ISpell INSTANCE = new RunicBlackguardSpell();

    private RunicBlackguardSpell() {}

    @Override
    public void SpellResult(ServerLevel level, LivingEntity caster, ItemStack staff, SpellStat stats) {
        if (!(caster instanceof Player) || !ModItems.SPELLWEAVER_STAFF.isPresent()
                || !staff.is(ModItems.SPELLWEAVER_STAFF.get()) || !ModEntities.RUNIC_BLACKGUARD_SERVANT.isPresent()) {
            super.SpellResult(level, caster, staff, stats);
            return;
        }
        commonResult(level, caster);
        if (isShifting(caster)) return;

        int potency = stats.getPotency();
        int duration = stats.getDuration();
        if (WandUtil.enchantedFocus(caster)) {
            potency += WandUtil.getPotencyLevel(caster);
            duration += WandUtil.getLevels(ModEnchantments.DURATION.get(), caster) + 1;
        }
        RunicBlackguardServant guard = ModEntities.RUNIC_BLACKGUARD_SERVANT.get().create(level);
        if (guard == null) return;
        guard.setTrueOwner(caster);
        guard.moveTo(BlockFinder.SummonRadius(caster.blockPosition(), guard, level), 0.0F, 0.0F);
        MobUtil.moveDownToGround(guard);
        guard.setPersistenceRequired();
        guard.setLimitedLife(MobUtil.getSummonLifespan(level) * duration);
        buffSummon(caster, guard, potency);
        guard.finalizeSpawn(level, level.getCurrentDifficultyAt(caster.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
        SummonSap(caster, guard);
        setTarget(caster, guard);
        if (level.addFreshEntity(guard)) {
            uponSummon(level, caster, staff, guard);
        }
        summonAdvancement(caster, guard);
        SummonDown(caster);
        playSound(level, caster, ModSounds.VANGUARD_SUMMON.get());
    }

    @Override
    public boolean hasCustomCooldown(LivingEntity caster, ItemStack staff, ItemStack focus, int initialCooldown) {
        // Goety's spell-to-focus cache is identity based; retain the original focus's shared cooldown.
        if (caster instanceof Player player && focus.getItem() instanceof IFocus holder) {
            ISpell original = holder.getSpell();
            if (original != null && original != this) {
                SEHelper.addSpellCooldown(player, original, initialCooldown);
                return true;
            }
        }
        return false;
    }
}

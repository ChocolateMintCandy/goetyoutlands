package com.mint.goetyoutlands.common.magic;

import com.Polarice3.Goety.api.items.magic.IWand;
import com.Polarice3.Goety.common.enchantments.ModEnchantments;
import com.Polarice3.Goety.common.events.spell.CastMagicEvent;
import com.Polarice3.Goety.common.items.ModItems;
import com.Polarice3.Goety.common.magic.ModSpells;
import com.Polarice3.Goety.common.magic.SpellStat;
import com.Polarice3.Goety.common.magic.spells.SoulBoltSpell;
import com.Polarice3.Goety.utils.SEHelper;
import com.Polarice3.Goety.utils.SoundUtil;
import com.Polarice3.Goety.utils.WandUtil;
import com.mint.goetyoutlands.common.entity.projectile.OminousSoulBolt;
import com.mint.goetyoutlands.goety_outlands;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = goety_outlands.MOD_ID)
public final class OminousSoulBoltSpell extends SoulBoltSpell {
    private static final OminousSoulBoltSpell INSTANCE = new OminousSoulBoltSpell();

    private static boolean isOminousCast(ItemStack stack) {
        return stack.is(ModItems.OMINOUS_STAFF.get()) && IWand.getFocus(stack).is(ModItems.SOUL_BOLT_FOCUS.get());
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onCast(CastMagicEvent event) {
        if (event.getSpell() == ModSpells.SOUL_BOLT && event.getEntity() instanceof Player player
                && !player.level().isClientSide && (isOminousCast(player.getMainHandItem()) || isOminousCast(player.getOffhandItem()))) {
            event.setSpell(INSTANCE);
        }
    }

    @Override
    public void SpellResult(ServerLevel level, LivingEntity caster, ItemStack staff, SpellStat stats) {
        // The cast event has no hand. Decide using the actual casting stack, including for instant casts.
        if (!(caster instanceof Player) || !isOminousCast(staff)) {
            super.SpellResult(level, caster, staff, stats);
            return;
        }
        int potency = stats.getPotency();
        float velocity = stats.getVelocity();
        if (WandUtil.enchantedFocus(caster)) {
            potency += WandUtil.getPotencyLevel(caster);
            velocity += WandUtil.getLevels(ModEnchantments.VELOCITY.get(), caster);
        }
        OminousSoulBolt bolt = new OminousSoulBolt(level, caster, caster.getViewVector(1.0F));
        bolt.setExtraDamage(potency);
        bolt.setBoltSpeed((int) velocity);
        SoundUtil.playSoulBolt(caster);
        level.addFreshEntity(bolt);
    }

    @Override
    public boolean hasCustomCooldown(LivingEntity caster, ItemStack staff, ItemStack focus, int ticks) {
        // Goety indexes focus cooldowns by spell instance identity, not by spell class.
        if (!(caster instanceof Player player)) return false;
        SEHelper.addSpellCooldown(player, ModSpells.SOUL_BOLT, ticks);
        return true;
    }
}

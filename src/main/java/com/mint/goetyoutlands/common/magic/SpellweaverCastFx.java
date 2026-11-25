package com.mint.goetyoutlands.common.magic;

import com.mint.goetyoutlands.Register.ModItems;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;

public final class SpellweaverCastFx {
    public static final String RELEASE = "goety_outlands:staff_release";
    private SpellweaverCastFx() {}

    // Called after Goety returns from an actual server-side spell result. Vanilla syncs the held stack.
    public static void released(ItemStack staff, LivingEntity caster) {
        if (caster != null && !caster.level().isClientSide() && ModItems.SPELLWEAVER_STAFF.isPresent()
                && staff.is(ModItems.SPELLWEAVER_STAFF.get())) {
            staff.getOrCreateTag().putLong(RELEASE, caster.level().getGameTime());
        }
    }

    public static void releasedOnBlock(UseOnContext context) {
        released(context.getItemInHand(), context.getPlayer());
    }
}

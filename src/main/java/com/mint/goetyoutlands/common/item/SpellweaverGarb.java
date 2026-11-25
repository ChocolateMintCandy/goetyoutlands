package com.mint.goetyoutlands.common.item;

import com.Polarice3.Goety.common.items.curios.SingleStackItem;
import com.mint.goetyoutlands.Register.ModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public final class SpellweaverGarb extends SingleStackItem {
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        tooltip.add(Component.translatable(getDescriptionId() + ".info").withStyle(ChatFormatting.DARK_PURPLE));
        if (stack.is(ModItems.SPELLWEAVER_CIRCLET.get())) {
            tooltip.add(Component.translatable(getDescriptionId() + ".cast").withStyle(ChatFormatting.BLUE));
        }
    }
}

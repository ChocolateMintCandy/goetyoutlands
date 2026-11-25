package com.mint.goetyoutlands.common.item;

import com.Polarice3.Goety.api.magic.SpellType;
import com.Polarice3.Goety.common.items.magic.DarkStaff;
import com.mint.goetyoutlands.client.renderer.SpellweaverStaffRenderer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;

public final class SpellweaverStaff extends DarkStaff implements GeoItem {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public SpellweaverStaff() { super(4.0D, SpellType.NONE); }

    @Override public AnimatableInstanceCache getAnimatableInstanceCache() { return cache; }
    @Override public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        // The renderer seeks two bounded clip players; there is no per-stack animation clock.
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new SpellweaverStaffRenderer.Extensions());
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        // Syncing the cast timestamp must not lower the hand at the moment of release.
        return slotChanged || oldStack.getItem() != newStack.getItem();
    }
}

package com.mint.goetyoutlands.client.renderer;

import com.Polarice3.Goety.common.items.magic.DarkWand;
import com.mint.goetyoutlands.client.model.SpellweaverStaffModel;
import com.mint.goetyoutlands.common.item.SpellweaverStaff;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.ItemDisplayContext;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public final class SpellweaverStaffRenderer extends GeoItemRenderer<SpellweaverStaff> {
    private final SpellweaverStaffModel staffModel;
    private SpellweaverStaffRenderer(SpellweaverStaffModel model) { super(model); this.staffModel = model; }

    public static final class Extensions extends DarkWand.DarkWandClient {
        private SpellweaverStaffRenderer renderer;
        @Override public BlockEntityWithoutLevelRenderer getCustomRenderer() {
            if (renderer == null) renderer = new SpellweaverStaffRenderer(new SpellweaverStaffModel());
            return renderer;
        }
    }

    @Override public void actuallyRender(PoseStack pose, SpellweaverStaff staff, BakedGeoModel baked, RenderType type,
            MultiBufferSource buffers, VertexConsumer vertices, boolean rerender, float partial, int light, int overlay,
            float red, float green, float blue, float alpha) {
        var level = Minecraft.getInstance().level;
        long now = level == null ? 0 : level.getGameTime();
        double cast = SpellweaverStaffView.castTick(currentItemStack, renderPerspective, now, partial);
        staffModel.pose(staff, baked, cast >= 0, cast >= 0 ? cast : now % (int)SpellweaverStaffView.LOOP + partial, partial);
        pose.pushPose();
        // Rotate/scale about the grip in hand, not about the heel or the center of the orbit.
        boolean display = renderPerspective == ItemDisplayContext.FIXED || renderPerspective == ItemDisplayContext.GROUND;
        pose.translate(0, -(display ? 14.0 : 9.25) / 16.0 - 0.01, 0);
        super.actuallyRender(pose, staff, baked, type, buffers, vertices, true, partial, light, overlay, red, green, blue, alpha);
        pose.popPose();
    }
}

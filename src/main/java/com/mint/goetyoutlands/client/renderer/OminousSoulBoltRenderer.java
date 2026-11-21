package com.mint.goetyoutlands.client.renderer;

import com.Polarice3.Goety.client.render.ModModelLayer;
import com.Polarice3.Goety.client.render.model.SoulBoltModel;
import com.mint.goetyoutlands.common.entity.projectile.OminousSoulBolt;
import com.mint.goetyoutlands.goety_outlands;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public final class OminousSoulBoltRenderer extends EntityRenderer<OminousSoulBolt> {
    private static final ResourceLocation[] FRAMES = {texture("bolt_1"), texture("bolt_2"), texture("bolt_3")};
    private static final RenderType RIBBON = RenderType.entityTranslucent(texture("trail"));
    private final SoulBoltModel<OminousSoulBolt> model;

    public OminousSoulBoltRenderer(EntityRendererProvider.Context context) {
        super(context);
        model = new SoulBoltModel<>(context.bakeLayer(ModModelLayer.SOUL_BOLT));
    }

    private static ResourceLocation texture(String file) {
        return ResourceLocation.fromNamespaceAndPath(goety_outlands.MOD_ID, "textures/entity/ominous_soul_bolt/" + file + ".png");
    }

    @Override
    public ResourceLocation getTextureLocation(OminousSoulBolt bolt) { return FRAMES[bolt.tickCount % FRAMES.length]; }

    @Override
    protected int getBlockLightLevel(OminousSoulBolt bolt, BlockPos pos) { return 15; }

    @Override
    public void render(OminousSoulBolt bolt, float yaw, float partial, PoseStack poses, MultiBufferSource buffers, int light) {
        poses.pushPose();
        poses.scale(-1.25F, -1.25F, 1.25F);
        model.setupAnim(0.0F, Mth.rotLerp(partial, bolt.yRotO, bolt.getYRot()), Mth.lerp(partial, bolt.xRotO, bolt.getXRot()));
        model.renderToBuffer(poses, buffers.getBuffer(RenderType.eyes(getTextureLocation(bolt))), 15728640,
                OverlayTexture.NO_OVERLAY, 1, 1, 1, 0.5F);
        poses.popPose();
        if (bolt.hasTrail()) {
            poses.pushPose();
            poses.translate(-Mth.lerp(partial, bolt.xOld, bolt.getX()), -Mth.lerp(partial, bolt.yOld, bolt.getY()),
                    -Mth.lerp(partial, bolt.zOld, bolt.getZ()));
            VertexConsumer buf = buffers.getBuffer(RIBBON);
            Vec3 head = bolt.trailPoint(0, partial), tail = bolt.trailPoint(8, partial);
            float u = (bolt.tickCount % 8) / 8.0F;
            vertex(poses.last(), buf, head, -0.25F, u, 1, light);
            vertex(poses.last(), buf, tail, -0.25F, u + 0.5F, 1, light);
            vertex(poses.last(), buf, tail, 0.25F, u + 0.5F, 0, light);
            vertex(poses.last(), buf, head, 0.25F, u, 0, light);
            poses.popPose();
        }
        super.render(bolt, yaw, partial, poses, buffers, light);
    }

    private static void vertex(PoseStack.Pose pose, VertexConsumer buf, Vec3 pos, float dy, float u, float v, int light) {
        buf.vertex(pose.pose(), (float)pos.x, (float)pos.y + dy, (float)pos.z).color(1.0F, 1.0F, 1.0F, 0.6F)
                .uv(u, v).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(pose.normal(), 0, 1, 0).endVertex();
    }
}

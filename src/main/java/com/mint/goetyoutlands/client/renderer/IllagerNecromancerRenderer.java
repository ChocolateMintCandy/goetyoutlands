package com.mint.goetyoutlands.client.renderer;

import com.Polarice3.Goety.client.render.AbstractNecromancerRenderer.NecromancerEyesLayer;
import com.mint.goetyoutlands.client.model.IllagerNecromancerModel;
import com.mint.goetyoutlands.common.entity.IllagerNecromancerServant;
import com.mint.goetyoutlands.goety_outlands;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class IllagerNecromancerRenderer extends MobRenderer<IllagerNecromancerServant, IllagerNecromancerModel> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(
            goety_outlands.MOD_ID, "textures/entity/illager_necromancer.png");
    private static final ResourceLocation GLOW_TEXTURE = ResourceLocation.fromNamespaceAndPath(
            goety_outlands.MOD_ID, "textures/entity/illager_necromancer_glow.png");

    public IllagerNecromancerRenderer(EntityRendererProvider.Context context) {
        super(context, new IllagerNecromancerModel(context.bakeLayer(IllagerNecromancerModel.LAYER_LOCATION)), 0.5F);
        this.addLayer(new NecromancerEyesLayer<>(this, GLOW_TEXTURE));
    }

    @Override
    protected void scale(IllagerNecromancerServant entity, PoseStack poseStack, float partialTick) {
        float size = 1.25F + Math.max(entity.getNecroLevel() * 0.15F, 0.0F);
        poseStack.scale(size, size, size);
    }

    @Override
    public ResourceLocation getTextureLocation(IllagerNecromancerServant entity) {
        return TEXTURE;
    }
}

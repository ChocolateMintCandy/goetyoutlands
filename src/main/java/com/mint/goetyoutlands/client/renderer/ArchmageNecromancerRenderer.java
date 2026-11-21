package com.mint.goetyoutlands.client.renderer;

import com.Polarice3.Goety.client.render.AbstractNecromancerRenderer.NecromancerEyesLayer;
import com.mint.goetyoutlands.client.model.ArchmageNecromancerModel;
import com.mint.goetyoutlands.common.entity.ArchmageNecromancerServant;
import com.mint.goetyoutlands.goety_outlands;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ArchmageNecromancerRenderer extends MobRenderer<ArchmageNecromancerServant, ArchmageNecromancerModel<ArchmageNecromancerServant>> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(
            goety_outlands.MOD_ID, "textures/entity/archmage_necromancer.png");
    private static final ResourceLocation GLOW_TEXTURE = ResourceLocation.fromNamespaceAndPath(
            goety_outlands.MOD_ID, "textures/entity/archmage_necromancer_glow.png");

    public ArchmageNecromancerRenderer(EntityRendererProvider.Context context) {
        super(context, new ArchmageNecromancerModel<>(context.bakeLayer(ArchmageNecromancerModel.LAYER_LOCATION)), 0.5F);
        this.addLayer(new NecromancerEyesLayer<>(this, GLOW_TEXTURE));
    }

    @Override
    protected void scale(ArchmageNecromancerServant entity, PoseStack poseStack, float partialTick) {
        float size = 1.25F + Math.max(entity.getNecroLevel() * 0.15F, 0.0F);
        poseStack.scale(size, size, size);
    }

    @Override
    public ResourceLocation getTextureLocation(ArchmageNecromancerServant entity) {
        return TEXTURE;
    }
}

package com.mint.goetyoutlands.client.model;

import com.hollingsworth.arsnouveau.ArsNouveau;
import com.mint.goetyoutlands.common.entity.WildenHunterServant;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

@OnlyIn(Dist.CLIENT)
public final class WildenHunterServantModel extends GeoModel<WildenHunterServant> {
    private static final ResourceLocation MODEL = ResourceLocation.fromNamespaceAndPath(ArsNouveau.MODID, "geo/wilden_hunter.geo.json");
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(ArsNouveau.MODID, "textures/entity/wilden_hunter.png");
    private static final ResourceLocation ANIMATIONS = ResourceLocation.fromNamespaceAndPath(ArsNouveau.MODID, "animations/wilden_hunter_animations.json");

    @Override
    public ResourceLocation getModelResource(WildenHunterServant hunter) { return MODEL; }

    @Override
    public ResourceLocation getTextureResource(WildenHunterServant hunter) { return TEXTURE; }

    @Override
    public ResourceLocation getAnimationResource(WildenHunterServant hunter) { return ANIMATIONS; }

    @Override
    public void setCustomAnimations(WildenHunterServant hunter, long instanceId, AnimationState<WildenHunterServant> state) {
        super.setCustomAnimations(hunter, instanceId, state);
        CoreGeoBone head = getAnimationProcessor().getBone("head");
        EntityModelData look = (EntityModelData) state.getExtraData().get(DataTickets.ENTITY_MODEL_DATA);
        if (head != null && look != null) {
            head.setRotX((float) Math.toRadians(look.headPitch()));
            head.setRotY((float) Math.toRadians(look.netHeadYaw()));
        }
    }
}

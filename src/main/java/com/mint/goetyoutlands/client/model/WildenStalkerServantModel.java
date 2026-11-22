package com.mint.goetyoutlands.client.model;

import com.hollingsworth.arsnouveau.ArsNouveau;
import com.mint.goetyoutlands.common.entity.WildenStalkerServant;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

@OnlyIn(Dist.CLIENT)
public final class WildenStalkerServantModel extends GeoModel<WildenStalkerServant> {
    private static final ResourceLocation MODEL = ResourceLocation.fromNamespaceAndPath(ArsNouveau.MODID, "geo/wilden_stalker.geo.json");
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(ArsNouveau.MODID, "textures/entity/wilden_stalker.png");
    private static final ResourceLocation ANIMATIONS = ResourceLocation.fromNamespaceAndPath(ArsNouveau.MODID, "animations/wilden_stalker_animations.json");

    @Override
    public ResourceLocation getModelResource(WildenStalkerServant stalker) { return MODEL; }

    @Override
    public ResourceLocation getTextureResource(WildenStalkerServant stalker) { return TEXTURE; }

    @Override
    public ResourceLocation getAnimationResource(WildenStalkerServant stalker) { return ANIMATIONS; }

    @Override
    public void setCustomAnimations(WildenStalkerServant stalker, long instanceId, AnimationState<WildenStalkerServant> state) {
        super.setCustomAnimations(stalker, instanceId, state);
        CoreGeoBone head = getAnimationProcessor().getBone("head");
        EntityModelData look = state.getData(DataTickets.ENTITY_MODEL_DATA);
        if (head != null && look != null) {
            head.setRotX((float) Math.toRadians(look.headPitch()));
            head.setRotY((float) Math.toRadians(look.netHeadYaw()));
        }
        boolean flying = stalker.isDiving();
        hide("wing_fly_left", !flying);
        hide("wing_fly_right", !flying);
        hide("wing_run_left", flying);
        hide("wing_run_right", flying);
    }

    private void hide(String name, boolean hidden) {
        CoreGeoBone bone = getAnimationProcessor().getBone(name);
        if (bone != null) bone.setHidden(hidden);
    }
}

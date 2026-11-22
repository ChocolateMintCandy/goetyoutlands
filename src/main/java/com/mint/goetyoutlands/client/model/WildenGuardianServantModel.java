package com.mint.goetyoutlands.client.model;

import com.hollingsworth.arsnouveau.client.renderer.entity.WildenGuardianModel;
import com.mint.goetyoutlands.common.entity.WildenGuardianServant;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

@OnlyIn(Dist.CLIENT)
public final class WildenGuardianServantModel extends GeoModel<WildenGuardianServant> {
    private static final String[] RETRACTED = {"body_spines_retracted", "left_arm_spines_retracted", "right_arm_spines_retracted", "left_leg_spines_retracted", "right_leg_spines_retracted"};
    private static final String[] EXTENDED = {"body_spines_extended", "left_arm_spines_extended", "right_arm_spines_extended", "left_leg_spines_extended", "right_leg_spines_extended"};

    @Override
    public ResourceLocation getModelResource(WildenGuardianServant guardian) { return WildenGuardianModel.WARDER_NEUTRAL; }

    @Override
    public ResourceLocation getTextureResource(WildenGuardianServant guardian) { return WildenGuardianModel.TEXT; }

    @Override
    public ResourceLocation getAnimationResource(WildenGuardianServant guardian) { return WildenGuardianModel.ANIM; }

    @Override
    public void setCustomAnimations(WildenGuardianServant guardian, long instanceId, AnimationState<WildenGuardianServant> state) {
        super.setCustomAnimations(guardian, instanceId, state);
        CoreGeoBone head = getAnimationProcessor().getBone("head");
        EntityModelData look = state.getData(DataTickets.ENTITY_MODEL_DATA);
        if (head != null && look != null) {
            head.setRotX((float) Math.toRadians(look.headPitch()));
            head.setRotY((float) Math.toRadians(look.netHeadYaw()));
        }
        setHidden(RETRACTED, guardian.isDefending());
        setHidden(EXTENDED, !guardian.isDefending());
    }

    private void setHidden(String[] names, boolean hidden) {
        for (String name : names) {
            CoreGeoBone bone = getAnimationProcessor().getBone(name);
            if (bone != null) bone.setHidden(hidden);
        }
    }
}

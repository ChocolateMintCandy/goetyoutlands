package com.mint.goetyoutlands.client.model;

import com.Polarice3.Goety.client.render.model.BlackguardModel;
import com.mint.goetyoutlands.common.entity.RunicBlackguardServant;
import com.mint.goetyoutlands.goety_outlands;
import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.animation.KeyframeAnimations;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public final class ArcaneBlackguardModel extends BlackguardModel<RunicBlackguardServant> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath(goety_outlands.MOD_ID, "ars_blackguard"), "main");
    private final ModelPart head, shield;

    public ArcaneBlackguardModel(ModelPart root) {
        super(root);
        ModelPart torso = root.getChild("blackguard").getChild("body");
        head = torso.getChild("head");
        shield = torso.getChild("left_arm").getChild("shield");
    }

    @Override
    public void setupAnim(RunicBlackguardServant mob, float stride, float amount, float age, float yaw, float pitch) {
        if (!mob.isRushing() || mob.isDeadOrDying()) {
            super.setupAnim(mob, stride, amount, age, yaw, pitch);
            return;
        }
        root().getAllParts().forEach(ModelPart::resetPose);
        // Use the dedicated Blockbench clip, not Goety's walk at a higher playback speed.
        animateWalk(RUSH, stride, 1.0F, 0.95F, 1.0F);
        head.yRot += yaw * Mth.DEG_TO_RAD;
        head.xRot += pitch * Mth.DEG_TO_RAD;
        shield.visible = mob.hasShield();
    }

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition blackguard = partdefinition.addOrReplaceChild("blackguard", CubeListBuilder.create(), PartPose.offset(0.0F, 12.0F, 0.0F));

		PartDefinition right_leg = blackguard.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(34, 50).addBox(-1.9F, 0.0F, -2.0F, 4.0F, 10.0F, 4.0F, new CubeDeformation(0.3F))
		.texOffs(28, 41).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.5F)).mirror(false), PartPose.offsetAndRotation(-3.5F, 0.0F, 1.5F, 0.0873F, 0.6109F, 0.0436F));

		PartDefinition right_boot = right_leg.addOrReplaceChild("right_boot", CubeListBuilder.create().texOffs(60, 15).addBox(-3.4F, -6.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.5F)), PartPose.offset(1.4F, 12.0F, 0.0F));

		PartDefinition left_leg = blackguard.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-1.9F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(28, 41).addBox(-1.9F, 0.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.5F))
		.texOffs(34, 50).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 10.0F, 4.0F, new CubeDeformation(0.3F)).mirror(false), PartPose.offsetAndRotation(2.9F, 0.0F, -3.0F, -0.0873F, -0.3491F, 0.0F));

		PartDefinition left_boot = left_leg.addOrReplaceChild("left_boot", CubeListBuilder.create().texOffs(60, 15).addBox(0.75F, -6.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.5F)), PartPose.offset(-2.65F, 12.0F, 0.0F));

		PartDefinition body = blackguard.addOrReplaceChild("body", CubeListBuilder.create().texOffs(16, 16).addBox(-4.0F, -12.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(111, 57).addBox(-1.0F, 0.0F, -4.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(110, 59).addBox(-2.0F, -4.0F, -4.0F, 4.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition belt = body.addOrReplaceChild("belt", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, -3.5F));

		PartDefinition cube_r1 = belt.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(120, 56).addBox(-2.0F, 0.0F, 0.0F, 4.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.0436F, 0.0F, 0.0F));

		PartDefinition armor = body.addOrReplaceChild("armor", CubeListBuilder.create().texOffs(0, 46).addBox(-2.5F, 0.0F, 0.0F, 10.0F, 12.0F, 6.0F, new CubeDeformation(0.01F))
		.texOffs(78, 46).addBox(-2.5F, 0.0F, 0.0F, 10.0F, 12.0F, 6.0F, new CubeDeformation(0.5F)), PartPose.offset(-2.5F, -12.0F, -3.0F));

		PartDefinition upperBody_r1 = armor.addOrReplaceChild("upperBody_r1", CubeListBuilder.create().texOffs(56, 55).addBox(-3.0F, 1.0F, -0.5F, 5.0F, 4.0F, 3.0F, new CubeDeformation(0.01F))
		.texOffs(56, 55).mirror().addBox(2.5F, 1.0F, -0.5F, 5.0F, 4.0F, 3.0F, new CubeDeformation(0.01F)).mirror(false), PartPose.offsetAndRotation(0.25F, 0.0F, 0.0F, -0.5236F, 0.0F, 0.0F));

		PartDefinition right_arm = body.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(32, 0).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(112, 13).addBox(-2.75F, 6.0F, -2.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.26F))
		.texOffs(114, 47).addBox(-3.75F, 4.0F, -2.0F, 3.0F, 5.0F, 4.0F, new CubeDeformation(0.5F)), PartPose.offsetAndRotation(-5.25F, -10.0F, 0.5F, 0.48F, 0.6981F, 0.6981F));

		PartDefinition right_gauntlet = right_arm.addOrReplaceChild("right_gauntlet", CubeListBuilder.create().texOffs(112, 19).mirror().addBox(-4.0F, -1.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.4F)).mirror(false)
		.texOffs(112, 27).addBox(-4.0F, 0.0F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.3F)), PartPose.offset(1.0F, -1.0F, 0.0F));

		PartDefinition cube_r2 = right_gauntlet.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(89, 24).mirror().addBox(-0.5F, -3.0F, -3.0F, 5.0F, 4.0F, 5.0F, new CubeDeformation(0.5F)).mirror(false), PartPose.offsetAndRotation(-4.5F, 0.0F, 0.5F, 0.0F, 0.0F, -0.4363F));

		PartDefinition rightItem = right_arm.addOrReplaceChild("rightItem", CubeListBuilder.create(), PartPose.offset(-1.0F, 7.0F, 1.0F));

		PartDefinition mace = rightItem.addOrReplaceChild("mace", CubeListBuilder.create().texOffs(0, 36).addBox(-9.0F, 7.0F, 3.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(5, 32).addBox(-11.0F, -4.0F, -7.0F, 5.0F, 5.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(28, 66).addBox(-9.0F, -6.0F, -5.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(8.0F, 0.0F, -7.0F, 0.8802F, 0.1119F, 0.1343F));

		PartDefinition mace_r1 = mace.addOrReplaceChild("mace_r1", CubeListBuilder.create().texOffs(28, 66).addBox(5.0F, -5.0F, 12.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.0F, 11.0F, 1.0F, 0.0F, 1.5708F, -1.5708F));

		PartDefinition mace_r2 = mace.addOrReplaceChild("mace_r2", CubeListBuilder.create().texOffs(28, 66).addBox(-1.0F, 8.0F, 5.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.0F, 11.0F, 1.0F, 3.1416F, 0.0F, 0.0F));

		PartDefinition mace_r3 = mace.addOrReplaceChild("mace_r3", CubeListBuilder.create().texOffs(28, 66).addBox(-1.0F, -10.0F, 12.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 34).addBox(-1.0F, 0.0F, 5.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 32).addBox(-1.0F, -4.0F, 9.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 32).addBox(-1.0F, -3.0F, 8.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 32).addBox(-1.0F, -2.0F, 7.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 36).addBox(-1.0F, -1.0F, 6.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 34).addBox(-1.0F, 1.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.0F, 11.0F, 1.0F, 1.5708F, 0.0F, 0.0F));

		PartDefinition mace_r4 = mace.addOrReplaceChild("mace_r4", CubeListBuilder.create().texOffs(28, 66).addBox(-1.0F, 1.0F, -13.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.0F, 11.0F, 1.0F, -1.5708F, 0.0F, 0.0F));

		PartDefinition mace_r5 = mace.addOrReplaceChild("mace_r5", CubeListBuilder.create().texOffs(28, 66).addBox(-6.0F, -4.0F, 12.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.0F, 11.0F, 1.0F, 0.0F, -1.5708F, 1.5708F));

		PartDefinition mace_r6 = mace.addOrReplaceChild("mace_r6", CubeListBuilder.create().texOffs(25, 32).addBox(-4.0F, -11.0F, 0.0F, 8.0F, 8.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(25, 32).addBox(-4.0F, -11.0F, -1.0F, 8.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.0F, 11.0F, 1.0F, 0.0F, 1.5708F, 0.0F));

		PartDefinition mace_r7 = mace.addOrReplaceChild("mace_r7", CubeListBuilder.create().texOffs(0, 32).addBox(0.0F, -10.0F, 1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 32).addBox(0.0F, -9.0F, 0.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 32).addBox(0.0F, -8.0F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.0F, 10.0F, 0.0F, -3.1416F, 0.0F, 3.1416F));

		PartDefinition mace_r8 = mace.addOrReplaceChild("mace_r8", CubeListBuilder.create().texOffs(0, 34).addBox(0.0F, -8.0F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 34).addBox(0.0F, -6.0F, -3.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 34).addBox(0.0F, -7.0F, -2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 36).addBox(0.0F, -4.0F, -4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 36).addBox(0.0F, -5.0F, -4.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.0F, 11.0F, 1.0F, -3.1416F, 0.0F, 3.1416F));

		PartDefinition left_arm = body.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(32, 0).mirror().addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(112, 13).mirror().addBox(-1.25F, 6.0F, -2.25F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.26F)).mirror(false)
		.texOffs(114, 47).mirror().addBox(0.75F, 4.0F, -2.25F, 3.0F, 5.0F, 4.0F, new CubeDeformation(0.5F)).mirror(false), PartPose.offsetAndRotation(4.0F, -11.0F, 0.5F, -1.0472F, 0.0F, 0.0F));

		PartDefinition left_gauntlet = left_arm.addOrReplaceChild("left_gauntlet", CubeListBuilder.create().texOffs(112, 19).addBox(0.25F, -1.0F, -2.25F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.4F))
		.texOffs(112, 27).mirror().addBox(0.25F, 0.0F, -2.25F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.3F)).mirror(false), PartPose.offset(-1.25F, -1.0F, 0.0F));

		PartDefinition cube_r3 = left_gauntlet.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(89, 24).addBox(-4.5F, -3.0F, -3.0F, 5.0F, 4.0F, 5.0F, new CubeDeformation(0.5F)), PartPose.offsetAndRotation(4.75F, 0.0F, 0.25F, 0.0F, 0.0F, 0.4363F));

		PartDefinition shield = left_arm.addOrReplaceChild("shield", CubeListBuilder.create().texOffs(80, 0).addBox(-7.0F, -10.5F, -1.0F, 14.0F, 22.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(85, 38).addBox(-7.5F, -11.0F, -1.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.25F))
		.texOffs(91, 38).addBox(5.5F, -11.0F, -1.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.25F))
		.texOffs(85, 41).addBox(-7.5F, 10.0F, -1.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.25F))
		.texOffs(91, 41).addBox(5.5F, 10.0F, -1.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.25F)), PartPose.offsetAndRotation(1.0F, 10.5F, -1.0F, 1.0472F, 0.0F, 0.0F));

		PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -12.0F, 0.0F));

		PartDefinition helmet = head.addOrReplaceChild("helmet", CubeListBuilder.create().texOffs(47, 26).addBox(-5.0F, -34.5F, -4.5F, 10.0F, 9.0F, 9.0F, new CubeDeformation(0.25F))
		.texOffs(116, 9).addBox(-2.0F, -35.85F, -4.65F, 4.0F, 1.0F, 2.0F, new CubeDeformation(0.1F))
		.texOffs(118, 1).mirror().addBox(5.25F, -36.7F, -1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.15F)).mirror(false)
		.texOffs(118, 1).addBox(-7.25F, -36.7F, -1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.15F)), PartPose.offset(0.0F, 25.5F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}


    private static final AnimationDefinition RUSH = AnimationDefinition.Builder.withLength(0.6F).looping()
            .addAnimation("blackguard", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -0.2F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.15F, KeyframeAnimations.posVec(0.0F, 0.25F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.3F, KeyframeAnimations.posVec(0.0F, -0.2F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.45F, KeyframeAnimations.posVec(0.0F, 0.25F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.6F, KeyframeAnimations.posVec(0.0F, -0.2F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("body", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(16.0F, 3.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.15F, KeyframeAnimations.degreeVec(18.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.3F, KeyframeAnimations.degreeVec(16.0F, -3.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.45F, KeyframeAnimations.degreeVec(18.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.6F, KeyframeAnimations.degreeVec(16.0F, 3.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("head", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-10.0F, -3.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.15F, KeyframeAnimations.degreeVec(-12.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.3F, KeyframeAnimations.degreeVec(-10.0F, 3.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.45F, KeyframeAnimations.degreeVec(-12.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.6F, KeyframeAnimations.degreeVec(-10.0F, -3.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(24.0F, -12.0F, -18.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.15F, KeyframeAnimations.degreeVec(18.0F, -12.0F, -18.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.3F, KeyframeAnimations.degreeVec(12.0F, -12.0F, -18.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.45F, KeyframeAnimations.degreeVec(18.0F, -12.0F, -18.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.6F, KeyframeAnimations.degreeVec(24.0F, -12.0F, -18.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-12.0F, -4.0F, -6.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.15F, KeyframeAnimations.degreeVec(-10.0F, -4.0F, -6.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.3F, KeyframeAnimations.degreeVec(-12.0F, -4.0F, -6.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.45F, KeyframeAnimations.degreeVec(-14.0F, -4.0F, -6.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.6F, KeyframeAnimations.degreeVec(-12.0F, -4.0F, -6.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("right_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-47.0F, -20.0F, -2.5F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.15F, KeyframeAnimations.degreeVec(-5.0F, -20.0F, -2.5F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.3F, KeyframeAnimations.degreeVec(37.0F, -20.0F, -2.5F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.45F, KeyframeAnimations.degreeVec(-5.0F, -20.0F, -2.5F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.6F, KeyframeAnimations.degreeVec(-47.0F, -20.0F, -2.5F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("right_leg", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -1.8F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.15F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.3F, KeyframeAnimations.posVec(0.0F, -1.8F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.45F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.6F, KeyframeAnimations.posVec(0.0F, -1.8F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("left_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(47.0F, 12.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.15F, KeyframeAnimations.degreeVec(5.0F, 12.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.3F, KeyframeAnimations.degreeVec(-37.0F, 12.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.45F, KeyframeAnimations.degreeVec(5.0F, 12.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.6F, KeyframeAnimations.degreeVec(47.0F, 12.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("left_leg", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -1.8F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.15F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.3F, KeyframeAnimations.posVec(0.0F, -1.8F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.45F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.6F, KeyframeAnimations.posVec(0.0F, -1.8F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .build();
}

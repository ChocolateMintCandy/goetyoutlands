package com.mint.goetyoutlands.client.model;

import com.Polarice3.Goety.client.render.model.NecromancerModel;
import com.Polarice3.Goety.common.entities.neutral.AbstractNecromancer;
import com.mint.goetyoutlands.goety_outlands;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public final class ArchmageNecromancerModel<T extends AbstractNecromancer> extends NecromancerModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath(goety_outlands.MOD_ID, "archmage_necromancer"), "main");

    public ArchmageNecromancerModel(ModelPart root) {
        super(root);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        PartDefinition skeleton = root.addOrReplaceChild("skeleton", CubeListBuilder.create(), PartPose.offset(0F, 24F, 0F));

        PartDefinition body = skeleton.addOrReplaceChild("body", CubeListBuilder.create()
                .texOffs(32, 45).addBox(-4F, -12F, -2F, 8F, 12F, 4F), PartPose.offsetAndRotation(0F, -12F, 0F, 0F, -0.2618F, 0F));

        PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create()
                .texOffs(0, 45).addBox(-4F, -8F, -4F, 8F, 8F, 8F), PartPose.offsetAndRotation(0F, -12F, 0F, 0F, 0.2618F, 0F));

        PartDefinition hat = head.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.offset(0F, 0F, 0F));

        PartDefinition brim = hat.addOrReplaceChild("brim", CubeListBuilder.create()
                .texOffs(0, 28).addBox(-8F, -1F, -4F, 16F, 1F, 16F)
                .texOffs(24, 76).addBox(-5F, -4F, -1F, 10F, 3F, 10F)
                .texOffs(24, 61).addBox(-5F, -6.25F, -1F, 10F, 5F, 10F, new CubeDeformation(0.25F)), PartPose.offsetAndRotation(0F, -6.5F, -4F, -0.218166156F, 0F, 0F));

        PartDefinition crown = brim.addOrReplaceChild("crown", CubeListBuilder.create()
                .texOffs(0, 77).addBox(-3F, -6F, -6F, 6F, 6F, 6F), PartPose.offsetAndRotation(0F, -4F, 9F, 1.00356432F, 0F, 0F));

        PartDefinition fold = crown.addOrReplaceChild("fold", CubeListBuilder.create()
                .texOffs(44, 89).addBox(-2F, -4F, -2F, 4F, 4F, 4F), PartPose.offsetAndRotation(0F, 0F, 0F, -0.436332313F, 0F, 0F));

        fold.addOrReplaceChild("tip", CubeListBuilder.create()
                .texOffs(0, 100).addBox(-1F, -2F, -2F, 2F, 2F, 4F), PartPose.offsetAndRotation(0F, 0F, 2F, -0.436332313F, 0F, 0F));

        PartDefinition right_arm = body.addOrReplaceChild("right_arm", CubeListBuilder.create()
                .texOffs(44, 0).addBox(-1F, -2F, -1F, 2F, 12F, 2F), PartPose.offsetAndRotation(-5F, -10F, 0F, -1.3963F, 0.2618F, 0F));

        PartDefinition staff = right_arm.addOrReplaceChild("staff", CubeListBuilder.create(), PartPose.offsetAndRotation(0F, 10F, 5.5F, 1.4399F, 0F, 0F));

        staff.addOrReplaceChild("handle", CubeListBuilder.create()
                .texOffs(40, 0).addBox(0.5F, -16F, -19F, 1F, 24F, 1F), PartPose.offset(-1F, 7F, 19F));

        staff.addOrReplaceChild("prongs", CubeListBuilder.create()
                .texOffs(40, 25).addBox(1.5F, -16F, -19F, 1F, 1F, 1F)
                .texOffs(56, 45).addBox(1.5F, -17F, -20F, 1F, 1F, 3F)
                .texOffs(60, 26).addBox(1.5F, -18F, -18F, 1F, 1F, 1F)
                .texOffs(60, 10).addBox(2.5F, -20F, -18F, 1F, 3F, 1F)
                .texOffs(56, 53).addBox(1.5F, -18F, -20F, 1F, 1F, 1F)
                .texOffs(60, 14).addBox(2.5F, -20F, -20F, 1F, 3F, 1F)
                .texOffs(60, 53).addBox(-0.5F, -16F, -19F, 1F, 1F, 1F)
                .texOffs(56, 49).addBox(-0.5F, -17F, -20F, 1F, 1F, 3F)
                .texOffs(56, 55).addBox(-0.5F, -18F, -18F, 1F, 1F, 1F)
                .texOffs(60, 18).addBox(-1.5F, -20F, -18F, 1F, 3F, 1F)
                .texOffs(60, 55).addBox(-0.5F, -18F, -20F, 1F, 1F, 1F)
                .texOffs(60, 22).addBox(-1.5F, -20F, -20F, 1F, 3F, 1F), PartPose.offset(-1F, 7F, 19F));

        PartDefinition staffhead = staff.addOrReplaceChild("staffhead", CubeListBuilder.create(), PartPose.offsetAndRotation(0F, -15.5F, 0.5F, 0F, 0.128231225F, 0F));

        PartDefinition tilt = staffhead.addOrReplaceChild("tilt", CubeListBuilder.create(), PartPose.offsetAndRotation(0F, 0F, 0F, -0.447387449F, 0F, 0F));

        tilt.addOrReplaceChild("roll", CubeListBuilder.create()
                .texOffs(44, 97).addBox(-1.5F, -1.5F, -1.5F, 3F, 3F, 3F), PartPose.offsetAndRotation(0F, 0F, 0F, 0F, 0F, 0.088656062F));

        right_arm.addOrReplaceChild("right_pauldron", CubeListBuilder.create()
                .texOffs(0, 89).addBox(-4F, -4F, -3F, 5F, 5F, 6F), PartPose.offsetAndRotation(0F, 0F, 1F, 1.0472F, -0.0873F, -0.2618F));

        PartDefinition left_arm = body.addOrReplaceChild("left_arm", CubeListBuilder.create()
                .texOffs(52, 0).addBox(-1F, -2F, -1F, 2F, 12F, 2F), PartPose.offsetAndRotation(5F, -10F, 0F, 0F, 0F, -0.0873F));

        left_arm.addOrReplaceChild("leftItem", CubeListBuilder.create(), PartPose.offset(1F, 7F, 1F));

        left_arm.addOrReplaceChild("left_pauldron", CubeListBuilder.create()
                .texOffs(22, 89).addBox(-1F, -4F, -3F, 5F, 5F, 6F), PartPose.offset(0F, 0F, 1F));

        PartDefinition pants = body.addOrReplaceChild("pants", CubeListBuilder.create()
                .texOffs(0, 61).addBox(-4F, 0F, -2F, 8F, 12F, 4F), PartPose.offset(0F, 0F, 0F));

        pants.addOrReplaceChild("middle", CubeListBuilder.create()
                .texOffs(60, 0).addBox(-1F, 0F, 0F, 2F, 10F, 0F), PartPose.offset(0F, 0F, -2F));

        body.addOrReplaceChild("cape", CubeListBuilder.create()
                .texOffs(0, 0).addBox(-8F, 0F, -2F, 16F, 24F, 4F), PartPose.offset(0F, -12F, 1F));

        skeleton.addOrReplaceChild("right_leg", CubeListBuilder.create()
                .texOffs(44, 14).addBox(-1F, 0F, -1F, 2F, 12F, 2F), PartPose.offsetAndRotation(-2F, -12F, 0F, 0F, 0F, 0.0436F));

        skeleton.addOrReplaceChild("left_leg", CubeListBuilder.create()
                .texOffs(52, 14).addBox(-1F, 0F, -1F, 2F, 12F, 2F), PartPose.offsetAndRotation(2F, -12F, 0F, 0F, 0F, -0.1309F));

        return LayerDefinition.create(mesh, 64, 128);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float age, float headYaw, float headPitch) {
        root.getAllParts().forEach(ModelPart::resetPose);
        if (!entity.isDeadOrDying()) {
            if (entity.cantDo > 0) {
                head.zRot += 0.3F * Mth.sin(0.45F * age);
                head.xRot += 0.4F;
            } else {
                head.yRot += headYaw * Mth.DEG_TO_RAD;
                head.xRot += headPitch * Mth.DEG_TO_RAD;
            }
        }

        // Each body clip already animates the crystal. Apply only one full clip per pose.
        if (entity.spellAnimationState.isStarted()) {
            animate(entity.spellAnimationState, ArchmageNecromancerAnimations.SPELL, age);
        } else if (entity.summonAnimationState.isStarted()) {
            animate(entity.summonAnimationState, ArchmageNecromancerAnimations.SUMMON, age);
        } else if (entity.attackAnimationState.isStarted()) {
            animate(entity.attackAnimationState, ArchmageNecromancerAnimations.ATTACK, age, entity.getAttackSpeed());
        } else if (!riding && limbSwingAmount > 0.01F) {
            animateWalk(ArchmageNecromancerAnimations.WALK, limbSwing, 1.0F, 2.5F, 1.0F);
        } else {
            animate(entity.idleAnimationState, ArchmageNecromancerAnimations.ALERT, age);
        }
        if (riding) {
            rightLeg.setRotation(-1.4137167F, Mth.PI / 10, 0.07853982F);
            leftLeg.setRotation(-1.4137167F, -Mth.PI / 10, -0.07853982F);
        }
    }
}

package com.mint.goetyoutlands.client.model;

import com.Polarice3.Goety.client.render.model.NecromancerModel;
import com.mint.goetyoutlands.common.entity.IllagerNecromancerServant;
import com.mint.goetyoutlands.goety_outlands;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Set;

@OnlyIn(Dist.CLIENT)
public class IllagerNecromancerModel extends NecromancerModel<IllagerNecromancerServant> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath(goety_outlands.MOD_ID, "illager_necromancer"), "main");

    public IllagerNecromancerModel(ModelPart root) {
        super(root);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition skeleton = partdefinition.addOrReplaceChild("skeleton", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition body = skeleton.addOrReplaceChild("body", CubeListBuilder.create()
                .texOffs(16, 16).addBox(-4.0F, -12.0F, -2.0F, 8.0F, 12.0F, 4.0F), PartPose.offsetAndRotation(0.0F, -12.0F, 0.0F, 0.0F, -0.26180009F, 0.0F));

        PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create()
                .texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F), PartPose.offsetAndRotation(0.0F, -12.0F, 0.0F, 0.0F, 0.26180009F, 0.0F));

        PartDefinition hat = head.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        hat.addOrReplaceChild("horns", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        // With Java model Y pointing down, DOWN is the visible top edge of each crown strip.
        Set<Direction> crownFaces = Set.of(Direction.NORTH, Direction.DOWN);

        PartDefinition crown = hat.addOrReplaceChild("crown", CubeListBuilder.create()
                .texOffs(14, 70).addBox(-4.5F, -11.0F, -4.5F, 9.0F, 2.0F, 0.16666667F, crownFaces)
                .texOffs(9, 79).addBox(-4.5F, -9.0F, -4.5F, 9.0F, 1.0F, 0.16666667F, crownFaces)
                .texOffs(9, 73).addBox(-4.5F, -8.0F, -4.5F, 9.0F, 2.0F, 0.16666667F, crownFaces)
                .texOffs(9, 76).addBox(-4.5F, -6.0F, -4.5F, 9.0F, 2.0F, 0.16666667F, crownFaces), PartPose.offset(0.0F, 0.0F, 0.0F));

        crown.addOrReplaceChild("crown_back_pivot", CubeListBuilder.create()
                .texOffs(14, 66).addBox(-4.5F, -10.0F, -4.5F, 9.0F, 3.0F, 0.16666667F, crownFaces)
                .texOffs(9, 82).addBox(-4.5F, -7.0F, -4.5F, 9.0F, 1.0F, 0.16666667F, crownFaces)
                .texOffs(0, 82).addBox(-4.5F, -6.0F, -4.5F, 9.0F, 2.0F, 0.16666667F, crownFaces), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -3.14159265F, 0.0F));

        crown.addOrReplaceChild("crown_left_pivot", CubeListBuilder.create()
                .texOffs(0, 85).addBox(-4.5F, -10.0F, -4.5F, 9.0F, 1.0F, 0.16666667F, crownFaces)
                .texOffs(0, 87).addBox(-4.5F, -9.0F, -4.5F, 9.0F, 1.0F, 0.16666667F, crownFaces)
                .texOffs(9, 88).addBox(-4.5F, -8.0F, -4.5F, 9.0F, 1.0F, 0.16666667F, crownFaces)
                .texOffs(0, 89).addBox(-4.5F, -7.0F, -4.5F, 9.0F, 1.0F, 0.16666667F, crownFaces)
                .texOffs(53, 92).addBox(-4.5F, -6.0F, -4.5F, 9.0F, 1.0F, 0.16666667F, crownFaces), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.57079633F, 0.0F));

        crown.addOrReplaceChild("crown_right_pivot", CubeListBuilder.create()
                .texOffs(0, 85).mirror().addBox(-4.5F, -10.0F, -4.5F, 9.0F, 1.0F, 0.16666667F, crownFaces)
                .texOffs(0, 87).addBox(-4.5F, -9.0F, -4.5F, 9.0F, 1.0F, 0.16666667F, crownFaces)
                .texOffs(9, 88).addBox(-4.5F, -8.0F, -4.5F, 9.0F, 1.0F, 0.16666667F, crownFaces)
                .texOffs(0, 89).addBox(-4.5F, -7.0F, -4.5F, 9.0F, 1.0F, 0.16666667F, crownFaces)
                .texOffs(53, 92).addBox(-4.5F, -6.0F, -4.5F, 9.0F, 1.0F, 0.16666667F, crownFaces), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 1.57079633F, 0.0F));

        PartDefinition right_arm = body.addOrReplaceChild("right_arm", CubeListBuilder.create()
                .texOffs(40, 16).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 12.0F, 2.0F), PartPose.offsetAndRotation(-5.0F, -10.0F, 0.0F, -1.39630005F, 0.26180009F, 0.0F));

        right_arm.addOrReplaceChild("right_pauldron", CubeListBuilder.create()
                .texOffs(0, 48).addBox(-4.0F, -4.0F, -3.0F, 5.0F, 5.0F, 6.0F), PartPose.offsetAndRotation(0.0F, 0.0F, 1.0F, 1.04719999F, -0.08729997F, -0.26180009F));

        PartDefinition staff = right_arm.addOrReplaceChild("staff", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 10.0F, 5.5F, 1.43989995F, 0.0F, 0.0F));

        staff.addOrReplaceChild("handle", CubeListBuilder.create()
                .texOffs(8, 16).addBox(0.0F, -16.0F, 0.0F, 1.0F, 24.0F, 1.0F), PartPose.offset(-0.5F, 7.0F, 0.0F));

        staff.addOrReplaceChild("staff_fittings", CubeListBuilder.create()
                .texOffs(48, 16).addBox(-1.0F, -17.0F, -1.0F, 3.0F, 1.0F, 3.0F)
                .texOffs(48, 20).addBox(-0.75F, 7.75F, -0.75F, 2.5F, 0.5F, 2.5F), PartPose.offset(-0.5F, 7.0F, 0.0F));

        staff.addOrReplaceChild("staffhead", CubeListBuilder.create()
                .texOffs(24, 0).addBox(-1.5F, -21.0F, -1.5F, 4.0F, 4.0F, 4.0F), PartPose.offset(-0.5F, 7.0F, 0.0F));

        right_arm.addOrReplaceChild("court_split_sleeve_right", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition left_arm = body.addOrReplaceChild("left_arm", CubeListBuilder.create()
                .texOffs(40, 16).mirror().addBox(-1.0F, -2.0F, -1.0F, 2.0F, 12.0F, 2.0F), PartPose.offsetAndRotation(5.0F, -10.0F, 0.0F, 0.0F, 0.0F, -0.08729997F));

        left_arm.addOrReplaceChild("leftItem", CubeListBuilder.create(), PartPose.offset(1.0F, 7.0F, 1.0F));

        left_arm.addOrReplaceChild("left_pauldron", CubeListBuilder.create()
                .texOffs(0, 48).mirror().addBox(-1.0F, -4.0F, -3.0F, 5.0F, 5.0F, 6.0F), PartPose.offset(0.0F, 0.0F, 1.0F));

        left_arm.addOrReplaceChild("court_split_sleeve_left", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition pants = body.addOrReplaceChild("pants", CubeListBuilder.create()
                .texOffs(16, 32).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F), PartPose.offset(0.0F, 0.0F, 0.0F));

        pants.addOrReplaceChild("middle", CubeListBuilder.create()
                .texOffs(40, 36).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 10.0F, 0.0F), PartPose.offset(0.0F, 0.0F, -2.0F));

        body.addOrReplaceChild("cape", CubeListBuilder.create()
                .texOffs(10, 96).addBox(-8.0F, 0.0F, 1.0F, 16.0F, 24.0F, 1.0F)
                .texOffs(44, 96).addBox(7.0F, 0.0F, -2.0F, 1.0F, 24.0F, 3.0F)
                .texOffs(52, 96).addBox(-8.0F, 0.0F, -2.0F, 1.0F, 24.0F, 3.0F), PartPose.offset(0.0F, -12.0F, 1.5F));

        body.addOrReplaceChild("court_collar", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        body.addOrReplaceChild("court_waistbelt", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        skeleton.addOrReplaceChild("right_leg", CubeListBuilder.create()
                .texOffs(0, 16).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 12.0F, 2.0F), PartPose.offsetAndRotation(-2.0F, -12.0F, 0.0F, 0.0F, 0.0F, 0.03490659F));

        skeleton.addOrReplaceChild("left_leg", CubeListBuilder.create()
                .texOffs(0, 16).mirror().addBox(-1.0F, 0.0F, -1.0F, 2.0F, 12.0F, 2.0F), PartPose.offsetAndRotation(2.0F, -12.0F, 0.0F, 0.0F, 0.0F, -0.03490659F));

        return LayerDefinition.create(meshdefinition, 64, 128);
    }
}

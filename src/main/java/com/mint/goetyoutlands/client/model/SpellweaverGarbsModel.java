package com.mint.goetyoutlands.client.model;

import com.mint.goetyoutlands.goety_outlands;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;

public final class SpellweaverGarbsModel {
    public static final ModelLayerLocation ROBE = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath(goety_outlands.MOD_ID, "spellweaver_robe"), "main");
    public static final ModelLayerLocation CIRCLET = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath(goety_outlands.MOD_ID, "spellweaver_circlet"), "main");

    // WearRenderer needs the humanoid pose anchors, but must not draw another player's skin.
    private static MeshDefinition emptyHumanoid() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        for (String name : new String[]{"head", "hat", "body", "right_arm", "left_arm", "right_leg", "left_leg"}) {
            root.addOrReplaceChild(name, CubeListBuilder.create(), PartPose.ZERO);
        }
        return mesh;
    }

    public static LayerDefinition robeLayer() {
        MeshDefinition mesh = emptyHumanoid();
        PartDefinition root = mesh.getRoot();
        PartDefinition coat = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 16)
                .addBox(-4, 0, -2, 8, 24, 4, new CubeDeformation(0.5F)), PartPose.ZERO);
        coat.addOrReplaceChild("cape", CubeListBuilder.create().texOffs(44, 15)
                .addBox(-4.5F, 0, 0, 9, 20, 1), PartPose.offsetAndRotation(0, 0, 2.68F, (float) Math.toRadians(7), 0, 0));
        root.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(24, 16)
                .addBox(-3, -2, -2, 4, 12, 4, new CubeDeformation(0.3F)), PartPose.offset(-5, 2, 0));
        root.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(24, 16).mirror()
                .addBox(-1, -2, -2, 4, 12, 4, new CubeDeformation(0.3F)), PartPose.offset(5, 2, 0));
        PartDefinition head = root.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0)
                .addBox(-4, -8, -4, 8, 8, 8, new CubeDeformation(0.75F)), PartPose.ZERO);
        head.addOrReplaceChild("fold", CubeListBuilder.create().texOffs(40, 58)
                .addBox(-4, -2.71F, 0.63F, 8, 2, 4, new CubeDeformation(0.75F)),
                PartPose.offsetAndRotation(0, -5.5F, 3.5F, (float) Math.toRadians(-22.5), 0, 0));
        return LayerDefinition.create(mesh, 64, 64);
    }

    public static LayerDefinition circletLayer() {
        MeshDefinition mesh = emptyHumanoid();
        mesh.getRoot().addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0)
                .addBox(-4, -11, -4, 8, 8, 8, new CubeDeformation(0.55F)), PartPose.ZERO);
        return LayerDefinition.create(mesh, 32, 16);
    }

    private SpellweaverGarbsModel() { }
}

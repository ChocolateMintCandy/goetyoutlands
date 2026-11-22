package com.mint.goetyoutlands.client.renderer;

import com.hollingsworth.arsnouveau.client.renderer.entity.GenericRenderer;
import com.mint.goetyoutlands.client.model.WildenGuardianServantModel;
import com.mint.goetyoutlands.common.entity.WildenGuardianServant;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public final class WildenGuardianServantRenderer extends GenericRenderer<WildenGuardianServant> {
    public WildenGuardianServantRenderer(EntityRendererProvider.Context context) {
        super(context, new WildenGuardianServantModel());
    }
}

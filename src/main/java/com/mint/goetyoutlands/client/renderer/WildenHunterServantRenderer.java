package com.mint.goetyoutlands.client.renderer;

import com.hollingsworth.arsnouveau.client.renderer.entity.GenericRenderer;
import com.mint.goetyoutlands.client.model.WildenHunterServantModel;
import com.mint.goetyoutlands.common.entity.WildenHunterServant;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public final class WildenHunterServantRenderer extends GenericRenderer<WildenHunterServant> {
    public WildenHunterServantRenderer(EntityRendererProvider.Context context) {
        super(context, new WildenHunterServantModel());
    }
}

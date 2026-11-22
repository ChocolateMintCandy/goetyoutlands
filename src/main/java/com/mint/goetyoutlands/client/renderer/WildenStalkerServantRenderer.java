package com.mint.goetyoutlands.client.renderer;

import com.hollingsworth.arsnouveau.client.renderer.entity.GenericRenderer;
import com.mint.goetyoutlands.client.model.WildenStalkerServantModel;
import com.mint.goetyoutlands.common.entity.WildenStalkerServant;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public final class WildenStalkerServantRenderer extends GenericRenderer<WildenStalkerServant> {
    public WildenStalkerServantRenderer(EntityRendererProvider.Context context) {
        super(context, new WildenStalkerServantModel());
    }
}

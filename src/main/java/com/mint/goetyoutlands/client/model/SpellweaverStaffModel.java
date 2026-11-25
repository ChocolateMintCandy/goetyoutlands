package com.mint.goetyoutlands.client.model;

import com.mint.goetyoutlands.common.item.SpellweaverStaff;
import com.mint.goetyoutlands.goety_outlands;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.model.GeoModel;

public final class SpellweaverStaffModel extends GeoModel<SpellweaverStaff> {
    private static final ResourceLocation GEO = id("geo/spellweaver_staff.geo.json");
    private static final ResourceLocation SKIN = id("textures/item/spellweaver_staff_model.png");
    private static final ResourceLocation CLIPS = id("animations/spellweaver_staff.animation.json");
    private BakedGeoModel boundModel;
    private AnimatableManager<SpellweaverStaff> idlePlayer, castPlayer;
    private GeoBone fx;

    private static ResourceLocation id(String path) { return ResourceLocation.fromNamespaceAndPath(goety_outlands.MOD_ID, path); }
    @Override public ResourceLocation getModelResource(SpellweaverStaff staff) { return GEO; }
    @Override public ResourceLocation getTextureResource(SpellweaverStaff staff) { return SKIN; }
    @Override public ResourceLocation getAnimationResource(SpellweaverStaff staff) { return CLIPS; }
    // The crystal uses several partial-alpha levels in the Blockbench texture, not cutout holes.
    @Override public RenderType getRenderType(SpellweaverStaff staff, ResourceLocation texture) { return RenderType.entityTranslucent(texture); }

    public void pose(SpellweaverStaff staff, BakedGeoModel baked, boolean casting, double tick, float partial) {
        if (baked != boundModel) {
            idlePlayer = player(staff, "animation.wizard_wand.crystal_spin");
            castPlayer = player(staff, "animation.wizard_wand.cast");
            fx = getBone("cast_fx").orElseThrow();
            boundModel = baked;
        }
        fx.setHidden(!casting);
        getAnimationProcessor().tickAnimation(staff, this, casting ? castPlayer : idlePlayer, tick,
                new AnimationState<>(staff, 0, 0, partial, false), false);
    }

    private AnimatableManager<SpellweaverStaff> player(SpellweaverStaff staff, String name) {
        AnimatableManager<SpellweaverStaff> manager = new AnimatableManager<>(staff);
        manager.addController(new SeekController(staff, getAnimation(staff, name)));
        return manager;
    }

    // A hand render can start halfway through a cast, or immediately follow another player's render.
    // Sample GeckoLib's baked clip at the supplied tick, never advance a shared item clock.
    private static final class SeekController extends AnimationController<SpellweaverStaff> {
        SeekController(SpellweaverStaff staff, Animation clip) {
            super(staff, state -> PlayState.CONTINUE);
            currentAnimation = new AnimationProcessor.QueuedAnimation(clip, Animation.LoopType.PLAY_ONCE);
            animationState = State.RUNNING;
        }
        @Override protected double adjustTick(double tick) { return tick; }
    }
}

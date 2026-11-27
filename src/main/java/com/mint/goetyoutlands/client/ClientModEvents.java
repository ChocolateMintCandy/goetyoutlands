package com.mint.goetyoutlands.client;

import com.Polarice3.Goety.client.particles.AdditiveSpellParticle;
import com.Polarice3.Goety.client.particles.SummonTrailParticle;
import com.Polarice3.Goety.client.render.BlackguardRenderer;
import com.Polarice3.Goety.client.render.model.BlackguardModel;
import com.Polarice3.Goety.client.render.CuriosRenderer;
import com.Polarice3.Goety.client.render.WearRenderer;
import com.mint.goetyoutlands.Register.ModEntities;
import com.mint.goetyoutlands.Register.ModItems;
import com.mint.goetyoutlands.Register.ModParticles;
import com.mint.goetyoutlands.client.model.ArcaneBlackguardModel;
import com.mint.goetyoutlands.client.model.SpellweaverGarbsModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mint.goetyoutlands.client.renderer.OminousSoulBoltRenderer;
import com.mint.goetyoutlands.client.renderer.SpellweaverStaffView;
import com.mint.goetyoutlands.client.model.ArchmageNecromancerModel;
import com.mint.goetyoutlands.client.model.IllagerNecromancerModel;
import com.mint.goetyoutlands.client.renderer.ArchmageNecromancerRenderer;
import com.mint.goetyoutlands.client.renderer.IllagerNecromancerRenderer;
import com.mint.goetyoutlands.client.renderer.WildenHunterServantRenderer;
import com.hollingsworth.arsnouveau.client.renderer.entity.ChimeraProjectileRenderer;
import com.mint.goetyoutlands.client.renderer.WildenGuardianServantRenderer;
import com.mint.goetyoutlands.client.renderer.WildenStalkerServantRenderer;
import com.mint.goetyoutlands.client.screen.NecromancerConfigScreen;
import com.mint.goetyoutlands.common.entity.ArchmageNecromancerServant;
import com.mint.goetyoutlands.common.entity.RunicBlackguardServant;
import com.mint.goetyoutlands.common.entity.IllagerNecromancerServant;
import com.mint.goetyoutlands.goety_outlands;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ConfigScreenHandler.ConfigScreenFactory;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.client.model.BakedModelWrapper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;

import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = goety_outlands.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class ClientModEvents {
    private static final ResourceLocation ILLAGER_JAR_MODEL =
            ResourceLocation.fromNamespaceAndPath(goety_outlands.MOD_ID, "item/soul_jar_illager");
    private static final ResourceLocation ARCHMAGE_JAR_MODEL =
            ResourceLocation.fromNamespaceAndPath(goety_outlands.MOD_ID, "item/soul_jar_archmage");
    private static final ModelResourceLocation GOETY_JAR_MODEL = new ModelResourceLocation("goety", "soul_jar", "inventory");

    @SubscribeEvent
    public static void registerConfigScreen(FMLClientSetupEvent event) {
        event.enqueueWork(() -> ModList.get().getModContainerById(goety_outlands.MOD_ID).orElseThrow()
                .registerExtensionPoint(ConfigScreenFactory.class, () -> new ConfigScreenFactory(NecromancerConfigScreen::new)));
    }

    @SubscribeEvent
    public static void registerGarbs(FMLClientSetupEvent event) {
        if (!goety_outlands.hasArsNouveau()) return;
        event.enqueueWork(() -> {
            CuriosRendererRegistry.register(ModItems.SPELLWEAVER_ROBE.get(), () -> new WearRenderer(
                    ResourceLocation.fromNamespaceAndPath(goety_outlands.MOD_ID, "textures/models/curios/spellweaver_robe.png"),
                    new HumanoidModel<>(CuriosRenderer.bakeLayer(SpellweaverGarbsModel.ROBE))));
            CuriosRendererRegistry.register(ModItems.SPELLWEAVER_CIRCLET.get(), () -> new WearRenderer(
                    ResourceLocation.fromNamespaceAndPath(goety_outlands.MOD_ID, "textures/models/curios/spellweaver_circlet.png"),
                    new HumanoidModel<>(CuriosRenderer.bakeLayer(SpellweaverGarbsModel.CIRCLET))));
        });
    }

    @SubscribeEvent
    public static void registerJarModel(ModelEvent.RegisterAdditional event) {
        event.register(ILLAGER_JAR_MODEL);
        if (goety_outlands.hasArsNouveau()) event.register(ARCHMAGE_JAR_MODEL);
    }

    @SubscribeEvent
    public static void applyStaffModel(ModelEvent.ModifyBakingResult event) {
        if (goety_outlands.hasArsNouveau()) SpellweaverStaffView.wrap(event);
    }

    @SubscribeEvent
    public static void applyJarModel(ModelEvent.ModifyBakingResult event) {
        BakedModel illager = event.getModels().get(ILLAGER_JAR_MODEL);
        BakedModel archmage = goety_outlands.hasArsNouveau() ? event.getModels().get(ARCHMAGE_JAR_MODEL) : null;
        BakedModel original = event.getModels().get(GOETY_JAR_MODEL);
        if (original == null) return;
        ItemOverrides overrides = new ItemOverrides() {
            @Override
            @Nullable
            public BakedModel resolve(BakedModel model, ItemStack stack, @Nullable ClientLevel level,
                                      @Nullable LivingEntity holder, int seed) {
                if (illager != null && IllagerNecromancerServant.isIllagerSoulJar(stack)) return illager;
                if (archmage != null && ArchmageNecromancerServant.isArchmageSoulJar(stack)) return archmage;
                return original.getOverrides().resolve(original, stack, level, holder, seed);
            }
        };
        event.getModels().put(GOETY_JAR_MODEL, new BakedModelWrapper<>(original) {
            @Override
            public ItemOverrides getOverrides() {
                return overrides;
            }
        });
    }

    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(IllagerNecromancerModel.LAYER_LOCATION, IllagerNecromancerModel::createBodyLayer);
        if (goety_outlands.hasArsNouveau()) {
            event.registerLayerDefinition(ArchmageNecromancerModel.LAYER_LOCATION, ArchmageNecromancerModel::createBodyLayer);
            event.registerLayerDefinition(ArcaneBlackguardModel.LAYER_LOCATION, ArcaneBlackguardModel::createBodyLayer);
            event.registerLayerDefinition(SpellweaverGarbsModel.ROBE, SpellweaverGarbsModel::robeLayer);
            event.registerLayerDefinition(SpellweaverGarbsModel.CIRCLET, SpellweaverGarbsModel::circletLayer);
        }
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.ILLAGER_NECROMANCER_SERVANT.get(), IllagerNecromancerRenderer::new);
        if (goety_outlands.hasArsNouveau()) {
            event.registerEntityRenderer(ModEntities.ARCHMAGE_NECROMANCER_SERVANT.get(), ArchmageNecromancerRenderer::new);
            event.registerEntityRenderer(ModEntities.RUNIC_BLACKGUARD_SERVANT.get(), RunicPreviewRenderer::new);
            WildenRendering.register(event);
        }
        event.registerEntityRenderer(ModEntities.OMINOUS_SOUL_BOLT.get(), OminousSoulBoltRenderer::new);
    }

    @SubscribeEvent
    public static void registerParticles(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ModParticles.SOUL_BOLT_TRAIL.get(), SummonTrailParticle.Provider::new);
        event.registerSpriteSet(ModParticles.SOUL_BOLT_IMPACT.get(), AdditiveSpellParticle.AlphaProvider::new);
    }

    // All attunements share the exported rig and rush clip; only their textures differ.
    private static final class RunicPreviewRenderer extends BlackguardRenderer<RunicBlackguardServant> {
        private static final ResourceLocation[] TEXTURES = new ResourceLocation[5];
        private static final RenderType[] GLOWS = new RenderType[5];

        static {
            String[] suffixes = {"", "_water", "_fire", "_earth", "_air"};
            for (int i = 0; i < suffixes.length; i++) {
                String path = "textures/entity/ars_blackguard" + suffixes[i];
                TEXTURES[i] = ResourceLocation.fromNamespaceAndPath(goety_outlands.MOD_ID, path + ".png");
                GLOWS[i] = RenderType.entityTranslucentEmissive(ResourceLocation.fromNamespaceAndPath(
                        goety_outlands.MOD_ID, path + "_glow.png"));
            }
        }

        RunicPreviewRenderer(EntityRendererProvider.Context context) {
            super(context);
            model = new ArcaneBlackguardModel(context.bakeLayer(ArcaneBlackguardModel.LAYER_LOCATION));
            addLayer(new RenderLayer<RunicBlackguardServant, BlackguardModel<RunicBlackguardServant>>(this) {
                @Override
                public void render(PoseStack pose, MultiBufferSource buffers, int light, RunicBlackguardServant mob,
                                   float stride, float amount, float partialTicks, float age, float yaw, float pitch) {
                    if (!isBodyVisible(mob)) return;
                    // Reuse the parent's sampled pose, including broken-shield visibility.
                    getParentModel().renderToBuffer(pose, buffers.getBuffer(GLOWS[attunementIndex(mob)]), LightTexture.FULL_BRIGHT,
                            OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
                }
            });
        }

        private static int attunementIndex(RunicBlackguardServant mob) {
            if (mob.isWaterAttuned()) return 1;
            if (mob.isFireAttuned()) return 2;
            if (mob.isEarthAttuned()) return 3;
            if (mob.isAirAttuned()) return 4;
            return 0;
        }

        @Override
        public ResourceLocation getTextureLocation(RunicBlackguardServant mob) {
            return TEXTURES[attunementIndex(mob)];
        }
    }

    // Keep the Ars/GeckoLib renderer factory out of the always-loaded event subscriber.
    private static final class WildenRendering {
        private static void register(EntityRenderersEvent.RegisterRenderers event) {
            event.registerEntityRenderer(ModEntities.WILDEN_HUNTER_SERVANT.get(), WildenHunterServantRenderer::new);
            event.registerEntityRenderer(ModEntities.WILDEN_STALKER_SERVANT.get(), WildenStalkerServantRenderer::new);
            event.registerEntityRenderer(ModEntities.WILDEN_GUARDIAN_SERVANT.get(), WildenGuardianServantRenderer::new);
            event.registerEntityRenderer(ModEntities.WILDEN_GUARDIAN_SPIKE.get(), ChimeraProjectileRenderer::new);
        }
    }

    private ClientModEvents() { }
}

package com.mint.goetyoutlands.Register;

import com.mint.goetyoutlands.common.entity.ArchmageNecromancerServant;
import com.mint.goetyoutlands.common.entity.IllagerNecromancerServant;
import com.hollingsworth.arsnouveau.common.entity.EntityChimeraProjectile;
import com.mint.goetyoutlands.common.entity.WildenGuardianServant;
import com.mint.goetyoutlands.common.entity.WildenHunterServant;
import com.mint.goetyoutlands.common.entity.WildenStalkerServant;
import com.mint.goetyoutlands.common.entity.projectile.OminousSoulBolt;
import com.mint.goetyoutlands.common.entity.projectile.WildenGuardianSpike;
import com.mint.goetyoutlands.goety_outlands;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, goety_outlands.MOD_ID);
    public static final DeferredRegister<EntityType<?>> ARS_ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, goety_outlands.MOD_ID);

    private static final String ARCHMAGE_PATH = "archmage_necromancer_servant";
    public static final String ARCHMAGE_NECROMANCER_SERVANT_ID = goety_outlands.MOD_ID + ":" + ARCHMAGE_PATH;

    public static final RegistryObject<EntityType<IllagerNecromancerServant>> ILLAGER_NECROMANCER_SERVANT =
            ENTITY_TYPES.register("illager_necromancer_servant", () -> EntityType.Builder
                    .<IllagerNecromancerServant>of(IllagerNecromancerServant::new, MobCategory.MONSTER)
                    .sized(0.75F, 2.4875F)
                    .clientTrackingRange(8)
                    .build(goety_outlands.MOD_ID + ":illager_necromancer_servant"));

    public static final RegistryObject<EntityType<ArchmageNecromancerServant>> ARCHMAGE_NECROMANCER_SERVANT =
            ARS_ENTITY_TYPES.register(ARCHMAGE_PATH, () -> EntityType.Builder
                    .<ArchmageNecromancerServant>of(ArchmageNecromancerServant::new, MobCategory.MONSTER)
                    .sized(0.75F, 2.4875F)
                    .clientTrackingRange(8)
                    .build(ARCHMAGE_NECROMANCER_SERVANT_ID));

    private static final String HUNTER_PATH = "wilden_hunter_servant";
    public static final RegistryObject<EntityType<WildenHunterServant>> WILDEN_HUNTER_SERVANT =
            ARS_ENTITY_TYPES.register(HUNTER_PATH, () -> EntityType.Builder
                    .<WildenHunterServant>of(WildenHunterServant::new, MobCategory.MONSTER)
                    .sized(1.2F, 1.2F).clientTrackingRange(10).setShouldReceiveVelocityUpdates(true)
                    .build(goety_outlands.MOD_ID + ":" + HUNTER_PATH));

    private static final String STALKER_PATH = "wilden_stalker_servant";
    public static final RegistryObject<EntityType<WildenStalkerServant>> WILDEN_STALKER_SERVANT =
            ARS_ENTITY_TYPES.register(STALKER_PATH, () -> EntityType.Builder
                    .<WildenStalkerServant>of(WildenStalkerServant::new, MobCategory.MONSTER)
                    .sized(0.95F, 1.0F).clientTrackingRange(10).setShouldReceiveVelocityUpdates(true)
                    .build(goety_outlands.MOD_ID + ":" + STALKER_PATH));

    private static final String GUARDIAN_PATH = "wilden_guardian_servant";
    public static final RegistryObject<EntityType<WildenGuardianServant>> WILDEN_GUARDIAN_SERVANT =
            ARS_ENTITY_TYPES.register(GUARDIAN_PATH, () -> EntityType.Builder
                    .<WildenGuardianServant>of(WildenGuardianServant::new, MobCategory.MONSTER)
                    .sized(1.15F, 1.15F).clientTrackingRange(10).setShouldReceiveVelocityUpdates(true)
                    .build(goety_outlands.MOD_ID + ":" + GUARDIAN_PATH));

    public static final RegistryObject<EntityType<EntityChimeraProjectile>> WILDEN_GUARDIAN_SPIKE =
            ARS_ENTITY_TYPES.register("wilden_guardian_spike", GuardianSpikeType::create);

    // The upstream arrow constructor takes EntityType<EntityChimeraProjectile>. Resolve its factory only with Ars installed.
    private static final class GuardianSpikeType {
        static EntityType<EntityChimeraProjectile> create() {
            return EntityType.Builder.<EntityChimeraProjectile>of(WildenGuardianSpike::new, MobCategory.MISC)
                    .clientTrackingRange(20).updateInterval(20).setShouldReceiveVelocityUpdates(true)
                    .build(goety_outlands.MOD_ID + ":wilden_guardian_spike");
        }
    }

    public static final RegistryObject<EntityType<OminousSoulBolt>> OMINOUS_SOUL_BOLT =
            ENTITY_TYPES.register("ominous_soul_bolt", () -> EntityType.Builder
                    .<OminousSoulBolt>of(OminousSoulBolt::new, MobCategory.MISC)
                    .sized(0.3125F, 0.3125F).clientTrackingRange(4).updateInterval(1)
                    .build(goety_outlands.MOD_ID + ":ominous_soul_bolt"));

    private ModEntities() { }
}

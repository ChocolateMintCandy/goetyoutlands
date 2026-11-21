package com.mint.goetyoutlands.Register;

import com.mint.goetyoutlands.common.entity.ArchmageNecromancerServant;
import com.mint.goetyoutlands.common.entity.IllagerNecromancerServant;
import com.mint.goetyoutlands.common.entity.projectile.OminousSoulBolt;
import com.mint.goetyoutlands.goety_outlands;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, goety_outlands.MOD_ID);

    public static final RegistryObject<EntityType<IllagerNecromancerServant>> ILLAGER_NECROMANCER_SERVANT =
            ENTITY_TYPES.register("illager_necromancer_servant", () -> EntityType.Builder
                    .<IllagerNecromancerServant>of(IllagerNecromancerServant::new, MobCategory.MONSTER)
                    .sized(0.75F, 2.4875F)
                    .clientTrackingRange(8)
                    .build(goety_outlands.MOD_ID + ":illager_necromancer_servant"));

    public static final RegistryObject<EntityType<ArchmageNecromancerServant>> ARCHMAGE_NECROMANCER_SERVANT =
            ENTITY_TYPES.register("archmage_necromancer_servant", () -> EntityType.Builder
                    .<ArchmageNecromancerServant>of(ArchmageNecromancerServant::new, MobCategory.MONSTER)
                    .sized(0.75F, 2.4875F)
                    .clientTrackingRange(8)
                    .build(goety_outlands.MOD_ID + ":archmage_necromancer_servant"));

    public static final RegistryObject<EntityType<OminousSoulBolt>> OMINOUS_SOUL_BOLT =
            ENTITY_TYPES.register("ominous_soul_bolt", () -> EntityType.Builder
                    .<OminousSoulBolt>of(OminousSoulBolt::new, MobCategory.MISC)
                    .sized(0.3125F, 0.3125F).clientTrackingRange(4).updateInterval(1)
                    .build(goety_outlands.MOD_ID + ":ominous_soul_bolt"));

    private ModEntities() { }
}

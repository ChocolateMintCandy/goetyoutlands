package com.mint.goetyoutlands.Register;

import com.Polarice3.Goety.common.items.ServantSpawnEggItem;
import com.mint.goetyoutlands.goety_outlands;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, goety_outlands.MOD_ID);

    public static final RegistryObject<ServantSpawnEggItem> ILLAGER_NECROMANCER_SERVANT_SPAWN_EGG =
            ITEMS.register("illager_necromancer_servant_spawn_egg", () -> new ServantSpawnEggItem(
                    ModEntities.ILLAGER_NECROMANCER_SERVANT, 0x808080, 0x404040, new Item.Properties()));

    public static final RegistryObject<ServantSpawnEggItem> ARCHMAGE_NECROMANCER_SERVANT_SPAWN_EGG =
            ITEMS.register("archmage_necromancer_servant_spawn_egg", () -> new ServantSpawnEggItem(
                    ModEntities.ARCHMAGE_NECROMANCER_SERVANT, 0x352047, 0xD7AD49, new Item.Properties()));

    private ModItems() { }
}

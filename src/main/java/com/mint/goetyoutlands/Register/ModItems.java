package com.mint.goetyoutlands.Register;

import com.Polarice3.Goety.common.items.ServantSpawnEggItem;
import com.Polarice3.Goety.common.items.magic.MagicFocus;
import com.mint.goetyoutlands.common.magic.WildenHuntSpell;
import com.mint.goetyoutlands.goety_outlands;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, goety_outlands.MOD_ID);
    public static final DeferredRegister<Item> ARS_ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, goety_outlands.MOD_ID);

    public static final RegistryObject<ServantSpawnEggItem> ILLAGER_NECROMANCER_SERVANT_SPAWN_EGG =
            ITEMS.register("illager_necromancer_servant_spawn_egg", () -> new ServantSpawnEggItem(
                    ModEntities.ILLAGER_NECROMANCER_SERVANT, 0x808080, 0x404040, new Item.Properties()));

    public static final RegistryObject<ServantSpawnEggItem> ARCHMAGE_NECROMANCER_SERVANT_SPAWN_EGG =
            ARS_ITEMS.register("archmage_necromancer_servant_spawn_egg", () -> new ServantSpawnEggItem(
                    ModEntities.ARCHMAGE_NECROMANCER_SERVANT, 0x352047, 0xD7AD49, new Item.Properties()));

    public static final RegistryObject<ServantSpawnEggItem> WILDEN_HUNTER_SERVANT_SPAWN_EGG =
            ARS_ITEMS.register("wilden_hunter_servant_spawn_egg", () -> new ServantSpawnEggItem(
                    ModEntities.WILDEN_HUNTER_SERVANT, 0xFDFDFD, 0xCAA97F, new Item.Properties()));

    public static final RegistryObject<MagicFocus> WILDEN_HUNT_FOCUS =
            ARS_ITEMS.register("wilden_hunt_focus", () -> new MagicFocus(new WildenHuntSpell()));

    private ModItems() { }
}

package com.mint.goetyoutlands.Register;

import com.Polarice3.Goety.common.items.ServantSpawnEggItem;
import com.Polarice3.Goety.common.items.curios.SingleStackItem;
import com.Polarice3.Goety.common.items.magic.DarkStaff;
import com.Polarice3.Goety.common.items.magic.MagicFocus;
import com.mint.goetyoutlands.common.item.SpellweaverGarb;
import com.mint.goetyoutlands.common.item.SpellweaverStaff;
import com.mint.goetyoutlands.common.magic.WildenGuardianSpell;
import com.mint.goetyoutlands.common.magic.WildenHuntSpell;
import com.mint.goetyoutlands.common.magic.WildenStalkerSpell;
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

    public static final RegistryObject<ServantSpawnEggItem> WILDEN_STALKER_SERVANT_SPAWN_EGG =
            ARS_ITEMS.register("wilden_stalker_servant_spawn_egg", () -> new ServantSpawnEggItem(
                    ModEntities.WILDEN_STALKER_SERVANT, 0x9B650C, 0xEF1818, new Item.Properties()));

    public static final RegistryObject<ServantSpawnEggItem> WILDEN_GUARDIAN_SERVANT_SPAWN_EGG =
            ARS_ITEMS.register("wilden_guardian_servant_spawn_egg", () -> new ServantSpawnEggItem(
                    ModEntities.WILDEN_GUARDIAN_SERVANT, 0xFFFFFF, 0xFF9E00, new Item.Properties()));

    public static final RegistryObject<DarkStaff> SPELLWEAVER_STAFF =
            ARS_ITEMS.register("spellweaver_staff", () -> ArsStaff.create());
    public static final RegistryObject<SingleStackItem> SPELLWEAVER_ROBE =
            ARS_ITEMS.register("spellweaver_robe", SpellweaverGarb::new);
    public static final RegistryObject<SingleStackItem> SPELLWEAVER_CIRCLET =
            ARS_ITEMS.register("spellweaver_circlet", SpellweaverGarb::new);

    public static final RegistryObject<MagicFocus> WILDEN_HUNT_FOCUS =
            ARS_ITEMS.register("wilden_hunt_focus", () -> new MagicFocus(new WildenHuntSpell()));

    public static final RegistryObject<MagicFocus> WILDEN_STALKER_FOCUS =
            ARS_ITEMS.register("wilden_stalker_focus", () -> new MagicFocus(new WildenStalkerSpell()));

    public static final RegistryObject<MagicFocus> WILDEN_GUARDIAN_FOCUS =
            ARS_ITEMS.register("wilden_guardian_focus", () -> new MagicFocus(new WildenGuardianSpell()));

    // Do not resolve the GeckoLib-backed item when Ars is absent.
    private static final class ArsStaff {
        static DarkStaff create() { return new SpellweaverStaff(); }
    }

    private ModItems() { }
}

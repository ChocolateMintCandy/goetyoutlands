package com.mint.goetyoutlands.Register;

import com.mint.goetyoutlands.goety_outlands;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public final class goety_outlandsTab {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, goety_outlands.MOD_ID);

    public static final RegistryObject<CreativeModeTab> ITEMS = CREATIVE_MODE_TABS.register("goety_outlands_tab_items",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup." + goety_outlands.MOD_ID + ".goety_outlands_tab_items"))
                    .icon(() -> new ItemStack(Items.SOUL_SAND))
                    .displayItems((params, output) -> {
                        output.accept(soulJar(ModEntities.ILLAGER_NECROMANCER_SERVANT.getId()));
                        if (goety_outlands.hasArsNouveau()) {
                            output.accept(soulJar(ModEntities.ARCHMAGE_NECROMANCER_SERVANT.getId()));
                        }
                    })
                    .build());

    public static final RegistryObject<CreativeModeTab> SPAWN_EGGS = CREATIVE_MODE_TABS.register("goety_outlands_tab_spawn_eggs",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup." + goety_outlands.MOD_ID + ".goety_outlands_tab_spawn_eggs"))
                    .icon(() -> soulJar(goety_outlands.hasArsNouveau()
                            ? ModEntities.ARCHMAGE_NECROMANCER_SERVANT.getId() : ModEntities.ILLAGER_NECROMANCER_SERVANT.getId()))
                    .displayItems((params, output) -> {
                        output.accept(ModItems.ILLAGER_NECROMANCER_SERVANT_SPAWN_EGG.get());
                        if (goety_outlands.hasArsNouveau()) {
                            output.accept(ModItems.ARCHMAGE_NECROMANCER_SERVANT_SPAWN_EGG.get());
                            output.accept(ModItems.WILDEN_HUNTER_SERVANT_SPAWN_EGG.get());
                        }
                    })
                    .build());

    static {
        if (goety_outlands.hasArsNouveau()) {
            CREATIVE_MODE_TABS.register("goety_outlands_tab_focuses", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup." + goety_outlands.MOD_ID + ".goety_outlands_tab_focuses"))
                    .icon(() -> ModItems.WILDEN_HUNT_FOCUS.get().getDefaultInstance())
                    .displayItems((params, output) -> output.accept(ModItems.WILDEN_HUNT_FOCUS.get()))
                    .build());
        }
    }

    private static ItemStack soulJar(ResourceLocation type) {
        ItemStack jar = com.Polarice3.Goety.common.items.ModItems.SOUL_JAR.get().getDefaultInstance();
        jar.getOrCreateTagElement("entity").putString("entity", type.toString());
        return jar;
    }

    private goety_outlandsTab() { }
}

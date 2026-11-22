package com.mint.goetyoutlands.event;

import com.hollingsworth.arsnouveau.common.entity.WildenGuardian;
import com.hollingsworth.arsnouveau.common.entity.WildenHunter;
import com.hollingsworth.arsnouveau.common.entity.WildenStalker;
import com.mint.goetyoutlands.Register.ModEntities;
import com.mint.goetyoutlands.common.entity.ArchmageNecromancerServant;
import com.mint.goetyoutlands.common.entity.IllagerNecromancerServant;
import com.mint.goetyoutlands.goety_outlands;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = goety_outlands.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ModEntityEvents {
    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.ILLAGER_NECROMANCER_SERVANT.get(), IllagerNecromancerServant.setCustomAttributes().build());
        if (goety_outlands.hasArsNouveau()) {
            event.put(ModEntities.ARCHMAGE_NECROMANCER_SERVANT.get(), ArchmageNecromancerServant.setCustomAttributes().build());
            event.put(ModEntities.WILDEN_HUNTER_SERVANT.get(), WildenHunter.getModdedAttributes().build());
            event.put(ModEntities.WILDEN_STALKER_SERVANT.get(), WildenStalker.getModdedAttributes().build());
            event.put(ModEntities.WILDEN_GUARDIAN_SERVANT.get(), WildenGuardian.getModdedAttributes().build());
        }
    }

    private ModEntityEvents() { }
}

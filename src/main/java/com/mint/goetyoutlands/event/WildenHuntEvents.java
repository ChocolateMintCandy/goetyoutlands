package com.mint.goetyoutlands.event;

import com.Polarice3.Goety.common.entities.ally.BlackWolf;
import com.mint.goetyoutlands.common.data.WildenHuntData;
import com.mint.goetyoutlands.goety_outlands;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.EntityLeaveLevelEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = goety_outlands.MOD_ID)
public final class WildenHuntEvents {
    private static final String CHECK_PACK = "goety_outlands:check_hunt_pack";

    @SubscribeEvent
    public static void onJoin(EntityJoinLevelEvent event) {
        if (event.getLevel() instanceof ServerLevel && event.getEntity() instanceof BlackWolf wolf
                && wolf.getPersistentData().hasUUID(WildenHuntData.PACK_TAG)) {
            // Join is cancellable and may run while a chunk is loading. Consume the handover on a real tick.
            wolf.getPersistentData().putBoolean(CHECK_PACK, true);
        }
    }

    @SubscribeEvent
    public static void onWolfTick(LivingEvent.LivingTickEvent event) {
        if (event.getEntity() instanceof BlackWolf wolf && wolf.level() instanceof ServerLevel level) {
            CompoundTag tag = wolf.getPersistentData();
            if (!tag.getBoolean(CHECK_PACK)) return;
            tag.remove(CHECK_PACK);
            if (tag.hasUUID(WildenHuntData.PACK_TAG)) {
                WildenHuntData.get(level).reconcileWolf(wolf, tag.getUUID(WildenHuntData.PACK_TAG));
            }
        }
    }

    @SubscribeEvent
    public static void onWolfRemoved(EntityLeaveLevelEvent event) {
        if (event.getEntity() instanceof BlackWolf wolf && event.getLevel() instanceof ServerLevel level) {
            Entity.RemovalReason reason = wolf.getRemovalReason();
            if (reason != null && reason.shouldDestroy() && wolf.getPersistentData().hasUUID(WildenHuntData.PACK_TAG)) {
                WildenHuntData.get(level).forgetWolf(wolf.getPersistentData().getUUID(WildenHuntData.PACK_TAG), wolf.getUUID());
            }
        }
    }

    private WildenHuntEvents() { }
}

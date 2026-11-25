package com.mint.goetyoutlands.common.data;

import com.Polarice3.Goety.common.entities.ally.BlackWolf;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.saveddata.SavedData;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/** World-wide membership, not a cache of loaded entities. Unloading must not free a summon slot. */
public final class WildenHuntData extends SavedData {
    public static final int HUNTER_LIMIT = 8, STALKER_LIMIT = 12, GUARDIAN_LIMIT = 8, WOLF_LIMIT = 4;
    public static final String PACK_TAG = "goety_outlands:hunt_pack";
    private final Roster hunters = new Roster();
    private final Roster stalkers = new Roster();
    private final Roster guardians = new Roster();
    private final SetMultimap<UUID, UUID> packs = HashMultimap.create();
    // Only retained while some of this hunter's wolves still need to receive the handover.
    private final Map<UUID, UUID> successors = new HashMap<>();

    public static WildenHuntData get(ServerLevel level) {
        return level.getServer().overworld().getDataStorage().computeIfAbsent(WildenHuntData::load,
                WildenHuntData::new, "goety_outlands_wilden_hunt");
    }

    public Set<UUID> huntersOf(UUID owner) { return Collections.unmodifiableSet(hunters.byOwner.get(owner)); }
    public int hunterSlots(UUID owner) { return Math.max(0, HUNTER_LIMIT - hunters.byOwner.get(owner).size()); }
    public Set<UUID> stalkersOf(UUID owner) { return Collections.unmodifiableSet(stalkers.byOwner.get(owner)); }
    public int stalkerSlots(UUID owner) { return Math.max(0, STALKER_LIMIT - stalkers.byOwner.get(owner).size()); }
    public Set<UUID> guardiansOf(UUID owner) { return Collections.unmodifiableSet(guardians.byOwner.get(owner)); }
    public int guardianSlots(UUID owner) { return Math.max(0, GUARDIAN_LIMIT - guardians.byOwner.get(owner).size()); }
    public int wolfSlots(UUID hunter) { return Math.max(0, WOLF_LIMIT - packs.get(hunter).size()); }

    public void setHunterOwner(UUID hunter, @Nullable UUID owner) {
        if (hunters.set(hunter, owner)) setDirty();
    }

    public void setStalkerOwner(UUID stalker, @Nullable UUID owner) {
        if (stalkers.set(stalker, owner)) setDirty();
    }

    public void setGuardianOwner(UUID guardian, @Nullable UUID owner) {
        if (guardians.set(guardian, owner)) setDirty();
    }

    public void recordWolf(UUID hunter, UUID wolf) {
        if (packs.put(hunter, wolf)) setDirty();
    }

    public void forgetWolf(UUID hunter, UUID wolf) {
        if (packs.remove(hunter, wolf)) {
            if (!packs.containsKey(hunter)) successors.remove(hunter);
            setDirty();
        }
    }

    public void retireHunter(ServerLevel level, UUID hunter, @Nullable UUID successor) {
        setHunterOwner(hunter, null);
        if (!packs.containsKey(hunter) || successors.containsKey(hunter)) return;
        successors.put(hunter, successor);
        setDirty();
        for (UUID id : List.copyOf(packs.get(hunter))) {
            for (ServerLevel dimension : level.getServer().getAllLevels()) {
                if (dimension.getEntity(id) instanceof BlackWolf wolf) {
                    reconcileWolf(wolf, hunter);
                    break;
                }
            }
        }
    }

    public void reconcileWolf(BlackWolf wolf, UUID hunter) {
        if (hunter.equals(wolf.getOwnerId())) {
            if (!successors.containsKey(hunter)) {
                recordWolf(hunter, wolf.getUUID());
                return;
            }
            UUID next = successors.get(hunter);
            // The player may be offline. Preserve the UUID without retaining a stale client entity ID.
            Entity loaded = next == null ? null : ((ServerLevel)wolf.level()).getEntity(next);
            wolf.setOwnerId(next);
            wolf.setOwnerClientId(loaded == null ? -1 : loaded.getId());
        }
        wolf.getPersistentData().remove(PACK_TAG);
        forgetWolf(hunter, wolf.getUUID());
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        tag.put("Hunters", writePairs(hunters.byOwner.entries()));
        tag.put("Stalkers", writePairs(stalkers.byOwner.entries()));
        tag.put("Guardians", writePairs(guardians.byOwner.entries()));
        tag.put("Wolves", writePairs(packs.entries()));
        tag.put("Handovers", writePairs(successors.entrySet()));
        return tag;
    }

    private static ListTag writePairs(Iterable<Map.Entry<UUID, UUID>> entries) {
        ListTag list = new ListTag();
        for (var entry : entries) {
            CompoundTag pair = new CompoundTag();
            pair.putUUID("From", entry.getKey());
            if (entry.getValue() != null) pair.putUUID("To", entry.getValue());
            list.add(pair);
        }
        return list;
    }

    private static WildenHuntData load(CompoundTag tag) {
        WildenHuntData data = new WildenHuntData();
        data.hunters.load(tag.getList("Hunters", Tag.TAG_COMPOUND));
        data.stalkers.load(tag.getList("Stalkers", Tag.TAG_COMPOUND));
        data.guardians.load(tag.getList("Guardians", Tag.TAG_COMPOUND));
        for (Tag entry : tag.getList("Wolves", Tag.TAG_COMPOUND)) {
            CompoundTag pair = (CompoundTag)entry;
            if (pair.hasUUID("From") && pair.hasUUID("To")) data.packs.put(pair.getUUID("From"), pair.getUUID("To"));
        }
        for (Tag entry : tag.getList("Handovers", Tag.TAG_COMPOUND)) {
            CompoundTag pair = (CompoundTag)entry;
            if (pair.hasUUID("From") && data.packs.containsKey(pair.getUUID("From"))) {
                data.successors.put(pair.getUUID("From"), pair.hasUUID("To") ? pair.getUUID("To") : null);
            }
        }
        return data;
    }

    private static final class Roster {
        final Map<UUID, UUID> ownerByServant = new HashMap<>();
        final SetMultimap<UUID, UUID> byOwner = HashMultimap.create();

        void load(ListTag list) {
            for (Tag entry : list) {
                CompoundTag pair = (CompoundTag)entry;
                if (pair.hasUUID("From") && pair.hasUUID("To")) set(pair.getUUID("To"), pair.getUUID("From"));
            }
        }

        boolean set(UUID servant, @Nullable UUID owner) {
            UUID old = ownerByServant.get(servant);
            if (Objects.equals(old, owner)) return false;
            if (old != null) byOwner.remove(old, servant);
            if (owner == null) ownerByServant.remove(servant);
            else {
                ownerByServant.put(servant, owner);
                byOwner.put(owner, servant);
            }
            return true;
        }
    }
}

package com.mint.goetyoutlands.common.entity;

import com.Polarice3.Goety.api.entities.IOwned;
import com.Polarice3.Goety.common.enchantments.ModEnchantments;
import com.Polarice3.Goety.common.entities.ModEntityType;
import com.Polarice3.Goety.common.entities.ally.Summoned;
import com.Polarice3.Goety.common.entities.ally.undead.skeleton.NecromancerServant;
import com.Polarice3.Goety.common.entities.ally.undead.skeleton.RogueguardServant;
import com.Polarice3.Goety.common.entities.ally.undead.skeleton.SkeletonPillagerServant;
import com.Polarice3.Goety.common.entities.ally.undead.zombie.ZombieRoyalGuardServant;
import com.Polarice3.Goety.common.entities.ally.undead.zombie.ZombieVillagerServant;
import com.Polarice3.Goety.common.entities.projectiles.ViciousTooth;
import com.Polarice3.Goety.common.items.ModItems;
import com.Polarice3.Goety.common.items.revive.SoulJar;
import com.Polarice3.Goety.common.magic.ModSpells;
import com.Polarice3.Goety.common.magic.SpellStat;
import com.Polarice3.Goety.config.AttributesConfig;
import com.Polarice3.Goety.config.ItemConfig;
import com.Polarice3.Goety.config.MobsConfig;
import com.Polarice3.Goety.init.ModSounds;
import com.Polarice3.Goety.utils.MathHelper;
import com.Polarice3.Goety.utils.MobUtil;
import com.Polarice3.Goety.utils.SEHelper;
import com.Polarice3.Goety.utils.SoundUtil;
import com.Polarice3.Goety.utils.WandUtil;
import com.mint.goetyoutlands.Register.ModEntities;
import com.mint.goetyoutlands.common.entity.projectile.OminousSoulBolt;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;
import java.util.function.Predicate;

public class IllagerNecromancerServant extends NecromancerServant {
    private static final String STORED_ENTITY_TYPE = ModEntities.ILLAGER_NECROMANCER_SERVANT.getId().toString();

    private boolean fixedSummonVariant;
    private long nextTeethCastTime;

    public IllagerNecromancerServant(EntityType<? extends IllagerNecromancerServant> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder setCustomAttributes() {
        return NecromancerServant.setCustomAttributes().add(Attributes.MAX_HEALTH, maxHealthForLevel(0));
    }

    private static double maxHealthForLevel(int rank) {
        return AttributesConfig.DrownedNecromancerHealth.get() * Math.max(1.0D, rank * 1.25D);
    }

    @Override
    public void setConfigurableAttributes() {
        super.setConfigurableAttributes();
        MobUtil.setBaseAttributes(getAttribute(Attributes.MAX_HEALTH), maxHealthForLevel(getNecroLevel()));
    }

    @Override
    public void setNecroLevel(int rank) {
        super.setNecroLevel(rank);
        MobUtil.setBaseAttributes(getAttribute(Attributes.MAX_HEALTH), maxHealthForLevel(getNecroLevel()));
    }

    @Override
    public Predicate<Entity> summonPredicate() {
        return e -> e instanceof IllagerNecromancerServant && e.isAlive();
    }

    @Override
    public int getSummonLimit(LivingEntity owner) {
        return 2;
    }

    public static boolean isIllagerSoulJar(ItemStack stack) {
        if (!stack.is(ModItems.SOUL_JAR.get())) return false;
        CompoundTag saved = stack.getTagElement("entity");
        return saved != null && STORED_ENTITY_TYPE.equals(saved.getString("entity"));
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack held = player.getItemInHand(hand);
        // Goety's base class accepts every filled jar; only that feeding branch needs a restriction.
        if (held.is(ModItems.SOUL_JAR.get()) && !isIllagerSoulJar(held)) return InteractionResult.PASS;
        return super.mobInteract(player, hand);
    }

    @Override
    public void soulJar() {
        if (level().isClientSide || !MobsConfig.NecromancerSoulJar.get() || !(getTrueOwner() instanceof Player owner)) return;

        Inventory inventory = owner.getInventory();
        for (ItemStack empty : inventory.items) {
            if (!empty.is(ModItems.EMPTY_SOUL_JAR.get())) continue;
            ItemStack filled = new ItemStack(ModItems.SOUL_JAR.get());
            SoulJar.setOwnerName(owner, filled);
            SoulJar.setSummon(this, filled);
            CompoundTag saved = filled.getTagElement("entity");
            if (saved == null || !saved.hasUUID("UUID") || !getUUID().equals(saved.getUUID("UUID"))) return;

            if (!owner.isCreative()) empty.shrink(1);
            // Filled soul jars do not stack. Inventory.add would silently discard one in a full creative inventory.
            boolean delivered = inventory.getFreeSlot() >= 0 && inventory.add(filled.copy());
            if (!delivered) {
                ItemEntity drop = owner.drop(filled, false, true);
                delivered = drop != null && (drop.isAddedToWorld()
                        || (owner.captureDrops() != null && owner.captureDrops().contains(drop)));
            }
            if (delivered) {
                SEHelper.addSpecificCooldown(owner, filled, MathHelper.secondsToTicks(ItemConfig.ReviveSecondsCool.get()));
            } else if (!owner.isCreative()) {
                empty.grow(1);
            }
            return;
        }
    }

    @Override
    public void avoidGoal(int priority) {
        // Like the wither necromancer, do not register the close-range retreat goal.
    }

    @Override
    public void summonSpells(int priority) {
        goalSelector.addGoal(priority, new TeethSpellGoal());
        super.summonSpells(priority);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putLong("NextTeethCastTime", nextTeethCastTime);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        nextTeethCastTime = tag.getLong("NextTeethCastTime");
    }

    @Override
    public Summoned getSummon() {
        // Keep Goety's focus unlocks, saved pool entries and roll order; only replace the winning entry.
        Summoned rolled = super.getSummon();
        EntityType<?> type = rolled.getType();
        Summoned result;
        if (type == ModEntityType.ZOMBIE_SERVANT.get()) {
            result = new ZombieVillagerServant(ModEntityType.ZOMBIE_VILLAGER_SERVANT.get(), level());
        } else if (type == ModEntityType.SKELETON_SERVANT.get()) {
            result = new SkeletonPillagerServant(ModEntityType.SKELETON_PILLAGER_SERVANT.get(), level());
        } else if (type == ModEntityType.VANGUARD_SERVANT.get()) {
            result = new RogueguardServant(ModEntityType.ROGUEGUARD_SERVANT.get(), level());
        } else if (type == ModEntityType.BLACKGUARD_SERVANT.get()) {
            result = new ZombieRoyalGuardServant(ModEntityType.BLACKGUARD_VARIANT_SERVANT.get(), level());
        } else {
            result = rolled;
        }
        fixedSummonVariant = result != rolled;
        return result;
    }

    @Override
    public boolean summonVariants() {
        // The owner-requested summon checks this immediately after each roll, before applying terrain variants.
        return !fixedSummonVariant && super.summonVariants();
    }

    private final class TeethSpellGoal extends Goal {
        private static final int WINDUP = 20, CAST_DURATION = 30;
        private int castTicks;

        private TeethSpellGoal() {
            setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            LivingEntity target = getTarget();
            return level() instanceof ServerLevel && !isSpellCasting() && target != null && target.isAlive()
                    && level().getGameTime() >= nextTeethCastTime;
        }

        @Override
        public void start() {
            boolean hasServants = !level().getEntitiesOfClass(LivingEntity.class, getBoundingBox().inflate(32, 16, 32),
                    e -> e.isAlive() && e instanceof IOwned owned && owned.getTrueOwner() == IllagerNecromancerServant.this).isEmpty();
            nextTeethCastTime = level().getGameTime() + (hasServants ? 360 : 120);
            castTicks = 0;
            getNavigation().stop();
            setSpellCasting(true);
            setAnimationState(SPELL_ANIM);
            playSound(ModSounds.NECROMANCER_LAUGH.get(), 1.0F, 1.0F);
        }

        @Override
        public boolean canContinueToUse() {
            LivingEntity target = getTarget();
            return castTicks < CAST_DURATION && isSpellCasting() && target != null && target.isAlive();
        }

        @Override
        public boolean requiresUpdateEveryTick() { return true; }

        @Override
        public void tick() {
            LivingEntity target = getTarget();
            ++castTicks;
            if (target == null || !target.isAlive()) return;
            getLookControl().setLookAt(target, 30.0F, 30.0F);
            if (castTicks == WINDUP && level() instanceof ServerLevel server) {
                castTeeth(server, target);
                playSound(ModSounds.SUMMON_SPELL.get(), 1.0F, 1.0F);
            }
        }

        @Override
        public void stop() {
            setSpellCasting(false);
            setAnimationState(IDLE);
        }
    }

    private void castTeeth(ServerLevel server, LivingEntity target) {
        if (distanceToSqr(target) > 16.0D) {
            BlockPos cursor = target.blockPosition();
            boolean spawned = false;
            for (int i = 0; i < 16; ++i) {
                // Minister's offsets accumulate from the previous point rather than restarting at the target.
                cursor = cursor.offset(random.nextInt(8) - 4, 0, random.nextInt(8) - 4);
                if (trySpawnTooth(server, cursor.getX() + 0.5D, cursor.getY(), cursor.getZ() + 0.5D, false)) spawned = true;
            }
            if (spawned) return;
        }
        double angle = Math.atan2(target.getZ() - getZ(), target.getX() - getX());
        spawnToothRing(server, 5, 1.5D, angle);
        spawnToothRing(server, 8, 2.5D, angle + 2 * Math.PI / 5);
    }

    private void spawnToothRing(ServerLevel server, int count, double radius, double startAngle) {
        for (int i = 0; i < count; ++i) {
            double angle = startAngle + 2 * Math.PI * i / count;
            trySpawnTooth(server, getX() + radius * Math.cos(angle), getBlockY(), getZ() + radius * Math.sin(angle), true);
        }
    }

    private boolean trySpawnTooth(ServerLevel server, double x, int baseY, double z, boolean ring) {
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos().set(x, baseY, z);
        if (!server.isLoaded(pos)) return false;
        while (pos.getY() < baseY + 8 && !server.getBlockState(pos).blocksMotion()) pos.move(Direction.UP);

        EntityType<ViciousTooth> type = ModEntityType.VICIOUS_TOOTH.get();
        double y = pos.getY() + (ring ? 0.0D : 0.5D);
        // Under a low ceiling the defensive ring can drop lower; a blocked scatter point is skipped.
        while (y >= baseY && (y + type.getHeight() > server.getMaxBuildHeight()
                || !server.noCollision(type.getDimensions().makeBoundingBox(x, y, z)))) {
            if (!ring) return false;
            --y;
        }
        if (y < baseY) return false;
        ViciousTooth tooth = new ViciousTooth(type, server);
        tooth.setPos(x, y, z);
        tooth.setOwner(this);
        tooth.setExtraDamage(getNecroLevel());
        if (!server.addFreshEntity(tooth)) return false;
        tooth.playSound(ModSounds.TOOTH_SPAWN.get());
        return true;
    }

    @Override
    public void performRangedAttack(LivingEntity target, float distanceFactor) {
        if (!(level() instanceof ServerLevel server)) return;
        int rank = getNecroLevel();
        Vec3 forward = getViewVector(1.0F);
        for (int i = -rank; i <= rank; i++) {
            OminousSoulBolt bolt = new OminousSoulBolt(server, this, forward, i / 10.0F);
            if (rank == 0) {
                // Goety applies spell stats to the single cast, not to its higher-level volleys.
                SpellStat stats = WandUtil.getStats(this, ModSpells.SOUL_BOLT);
                int potency = stats.getPotency();
                float velocity = stats.getVelocity();
                if (WandUtil.enchantedFocus(this)) {
                    potency += WandUtil.getPotencyLevel(this);
                    velocity += WandUtil.getLevels(ModEnchantments.VELOCITY.get(), this);
                }
                bolt.setExtraDamage(potency);
                bolt.setBoltSpeed((int) velocity);
            } else {
                bolt.setExtraDamage(rank);
            }
            if (server.addFreshEntity(bolt)) {
                SoundUtil.playSoulBolt(this);
                if (rank > 0) swing(InteractionHand.MAIN_HAND);
            }
        }
    }
}

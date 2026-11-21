package com.mint.goetyoutlands.common.entity;

import com.Polarice3.Goety.common.entities.ally.undead.skeleton.NecromancerServant;
import com.Polarice3.Goety.common.items.ModItems;
import com.Polarice3.Goety.config.AttributesConfig;
import com.Polarice3.Goety.utils.MobUtil;
import com.mint.goetyoutlands.Register.ModEntities;
import com.mint.goetyoutlands.compat.ars.ArchmageArsCombat;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.ModList;

import java.util.function.Predicate;

public class ArchmageNecromancerServant extends NecromancerServant {
    private static final String STORED_ENTITY_TYPE = ModEntities.ARCHMAGE_NECROMANCER_SERVANT.getId().toString();

    public ArchmageNecromancerServant(EntityType<? extends ArchmageNecromancerServant> type, Level level) {
        super(type, level);
    }

    private static double maxHealthForLevel(int rank) {
        return AttributesConfig.DrownedNecromancerHealth.get() * Math.max(1.0D, rank * 1.25D);
    }

    public static AttributeSupplier.Builder setCustomAttributes() {
        return NecromancerServant.setCustomAttributes().add(Attributes.MAX_HEALTH, maxHealthForLevel(0));
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
        return e -> e instanceof ArchmageNecromancerServant && e.isAlive();
    }

    @Override
    public int getSummonLimit(LivingEntity owner) {
        return 2;
    }

    public static boolean isArchmageSoulJar(ItemStack stack) {
        if (!stack.is(ModItems.SOUL_JAR.get())) return false;
        CompoundTag saved = stack.getTagElement("entity");
        return saved != null && STORED_ENTITY_TYPE.equals(saved.getString("entity"));
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack held = player.getItemInHand(hand);
        if (held.is(ModItems.SOUL_JAR.get()) && !isArchmageSoulJar(held)) return InteractionResult.PASS;
        return super.mobInteract(player, hand);
    }

    @Override
    public void avoidGoal(int priority) {
        // Keep ranged attacks and summoning active at close range instead of registering a retreat goal.
    }

    @Override
    public void performRangedAttack(LivingEntity target, float distanceFactor) {
        if (!(level() instanceof ServerLevel server)) return;
        if (ModList.get().isLoaded("ars_nouveau")) {
            ArchmageArsCombat.fire(server, this, target);
        } else {
            super.performRangedAttack(target, distanceFactor);
        }
    }
}

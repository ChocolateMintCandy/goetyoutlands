package com.mint.goetyoutlands.common.entity;

import com.Polarice3.Goety.common.entities.ally.undead.skeleton.NecromancerServant;
import com.Polarice3.Goety.common.items.ModItems;
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

import java.util.function.Predicate;

import static com.mint.goetyoutlands.Config.ARCHMAGE_NECROMANCER;

public class ArchmageNecromancerServant extends NecromancerServant {
    public ArchmageNecromancerServant(EntityType<? extends ArchmageNecromancerServant> type, Level level) {
        super(type, level);
        if (!level.isClientSide) {
            applyCombatAttributes();
            setHealth(getMaxHealth());
        }
    }

    public static AttributeSupplier.Builder setCustomAttributes() {
        return NecromancerServant.setCustomAttributes()
                .add(Attributes.MAX_HEALTH, ARCHMAGE_NECROMANCER.baseHealth.getDefault())
                .add(Attributes.ATTACK_DAMAGE, ARCHMAGE_NECROMANCER.baseAttackDamage.getDefault());
    }

    private void applyCombatAttributes() {
        MobUtil.setBaseAttributes(getAttribute(Attributes.MAX_HEALTH), ARCHMAGE_NECROMANCER.maxHealthForLevel(getNecroLevel()));
        MobUtil.setBaseAttributes(getAttribute(Attributes.ATTACK_DAMAGE), ARCHMAGE_NECROMANCER.baseAttackDamage.get());
    }

    @Override
    public void setConfigurableAttributes() {
        super.setConfigurableAttributes();
        applyCombatAttributes();
    }

    @Override
    public void setNecroLevel(int rank) {
        super.setNecroLevel(rank);
        applyCombatAttributes();
        if (getHealth() > getMaxHealth()) setHealth(getMaxHealth());
    }

    @Override
    public Predicate<Entity> summonPredicate() {
        return e -> e instanceof ArchmageNecromancerServant && e.isAlive();
    }

    @Override
    public int getSummonLimit(LivingEntity owner) {
        return ARCHMAGE_NECROMANCER.summonLimit.get();
    }

    public static boolean isArchmageSoulJar(ItemStack stack) {
        if (!stack.is(ModItems.SOUL_JAR.get())) return false;
        CompoundTag saved = stack.getTagElement("entity");
        return saved != null && ModEntities.ARCHMAGE_NECROMANCER_SERVANT_ID.equals(saved.getString("entity"));
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
        if (level() instanceof ServerLevel server) {
            ArchmageArsCombat.fire(server, this, target);
        }
    }
}

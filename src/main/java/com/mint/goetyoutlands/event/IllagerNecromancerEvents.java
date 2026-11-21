package com.mint.goetyoutlands.event;

import com.Polarice3.Goety.common.entities.ally.undead.skeleton.NecromancerServant;
import com.Polarice3.Goety.common.entities.ally.undead.skeleton.SkeletonServant;
import com.Polarice3.Goety.common.entities.projectiles.ViciousPike;
import com.Polarice3.Goety.common.items.ServantSpawnEggItem;
import com.Polarice3.Goety.common.items.revive.SoulJar;
import com.Polarice3.Goety.common.ritual.RitualRequirements;
import com.Polarice3.Goety.init.ModSounds;
import com.Polarice3.Goety.init.ModTags;
import com.Polarice3.Goety.utils.SEHelper;
import com.Polarice3.Goety.utils.ServerParticleUtil;
import com.mint.goetyoutlands.Register.ModEntities;
import com.mint.goetyoutlands.common.entity.IllagerNecromancerServant;
import com.mint.goetyoutlands.goety_outlands;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = goety_outlands.MOD_ID)
public final class IllagerNecromancerEvents {
    private static final String PIKE_BONUS = "goety_outlands:necro_pike_bonus";

    private IllagerNecromancerEvents() { }

    @SubscribeEvent
    public static void onPikeSpawn(EntityJoinLevelEvent event) {
        if (!(event.getEntity() instanceof ViciousPike pike) || event.getLevel().isClientSide || event.loadedFromDisk()) return;
        if (pike.getOwner() instanceof IllagerNecromancerServant caster) {
            // Goety gives a tooth's pikes the caster, but does not pass along its damage bonus or save the owner's UUID.
            pike.setOwner(caster);
            pike.getPersistentData().putInt(PIKE_BONUS, caster.getNecroLevel());
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onPikeDamage(LivingHurtEvent event) {
        if (event.getSource().getDirectEntity() instanceof ViciousPike pike && !pike.level().isClientSide
                && pike.getOwner() instanceof IllagerNecromancerServant) {
            event.setAmount(event.getAmount() + pike.getPersistentData().getInt(PIKE_BONUS));
        }
    }

    private static EntityType<?> bindingType(PlayerInteractEvent event) {
        ItemStack stack = event.getItemStack();
        if (event.getLevel().isClientSide || !event.getEntity().isCrouching()
                || !(stack.getItem() instanceof ServantSpawnEggItem egg)) return null;
        EntityType<?> type = egg.getType(stack.getTag());
        if (type == ModEntities.ILLAGER_NECROMANCER_SERVANT.get()) return type;
        return goety_outlands.hasArsNouveau() && type == ModEntities.ARCHMAGE_NECROMANCER_SERVANT.get() ? type : null;
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onEggUseOnBlock(PlayerInteractEvent.RightClickBlock event) {
        EntityType<?> type = bindingType(event);
        if (type != null && !event.getLevel().getBlockState(event.getPos()).is(Blocks.SPAWNER)
                && !RitualRequirements.canSummon(event.getLevel(), event.getEntity(), type)) {
            event.setUseItem(Event.Result.DENY);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onEggUseOnFluid(PlayerInteractEvent.RightClickItem event) {
        EntityType<?> type = bindingType(event);
        if (type == null) return;
        Player player = event.getEntity();
        Vec3 eye = player.getEyePosition();
        double reach = player.getAttributeValue(ForgeMod.BLOCK_REACH.get()) + 0.5D;
        BlockHitResult hit = event.getLevel().clip(new ClipContext(eye, eye.add(player.getLookAngle().scale(reach)),
                ClipContext.Block.OUTLINE, ClipContext.Fluid.SOURCE_ONLY, player));
        if (hit.getType() == HitResult.Type.BLOCK && event.getLevel().getBlockState(hit.getBlockPos()).getBlock() instanceof LiquidBlock
                && !RitualRequirements.canSummon(event.getLevel(), player, type)) {
            finish(event, InteractionResult.FAIL);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onJarRevival(PlayerInteractEvent.EntityInteract event) {
        ItemStack jar = event.getItemStack();
        if (!(jar.getItem() instanceof SoulJar)) return;
        CompoundTag tag = jar.getTag();
        if (tag == null) return;
        String id = tag.getCompound("entity").getString("entity");
        boolean archmage = ModEntities.ARCHMAGE_NECROMANCER_SERVANT_ID.equals(id);
        if (archmage && !goety_outlands.hasArsNouveau()) {
            finish(event, useAsOrdinaryJar(event, jar));
            return;
        }
        if (!(event.getLevel() instanceof ServerLevel level)) return;
        if (!archmage && !ModEntities.ILLAGER_NECROMANCER_SERVANT.getId().toString().equals(id)) return;
        Entity target = event.getTarget();
        if (!isRegularRevivalTarget(target)) return;
        Player player = event.getEntity();
        if (!(SoulJar.getSummon(jar, level) instanceof NecromancerServant servant) || servant.getTrueOwner() != player) return;

        // SoulJar hard-codes the ordinary necromancer's cap; only replace its stored Outlands revival branch.
        finish(event, InteractionResult.FAIL);
        if (!RitualRequirements.canSummon(level, player, servant.getType())) return;
        servant.setHealth(servant.getMaxHealth());
        servant.setPos(target.position());
        servant.lookAt(EntityAnchorArgument.Anchor.EYES, player.position());
        if (!level.addFreshEntity(servant)) return;
        servant.spawnAnim();
        for (int i = 0; i < 8; ++i) {
            ServerParticleUtil.addParticlesAroundSelf(level, ParticleTypes.SCULK_SOUL, servant);
            ServerParticleUtil.addParticlesAroundSelf(level, ParticleTypes.POOF, servant);
        }
        servant.playSound(SoundEvents.GENERIC_EXPLODE, 1.0F, 0.5F);
        servant.playSound(ModSounds.NECROMANCER_LAUGH.get(), 2.0F, 0.5F);
        target.discard();
        player.swing(event.getHand());
        SEHelper.addCooldown(player, jar.getItem(), 100);
        if (!player.getAbilities().instabuild) jar.shrink(1);
        event.setCancellationResult(InteractionResult.SUCCESS);
    }

    private static boolean isRegularRevivalTarget(Entity target) {
        return target instanceof Skeleton || target instanceof SkeletonServant || target.getType().is(ModTags.EntityTypes.REGULAR_CONVERT);
    }

    private static InteractionResult useAsOrdinaryJar(PlayerInteractEvent.EntityInteract event, ItemStack legacyJar) {
        if (!(event.getTarget() instanceof LivingEntity target) || !isRegularRevivalTarget(target)) return InteractionResult.FAIL;
        if (event.getLevel().isClientSide) return InteractionResult.CONSUME;

        Player player = event.getEntity();
        ItemStack plainJar = new ItemStack(legacyJar.getItem());
        plainJar.interactLivingEntity(player, target, event.getHand());
        // Goety returns PASS even on success; it consumes the plain jar only after the servant spawns.
        if (!plainJar.isEmpty()) return InteractionResult.FAIL;
        if (!player.getAbilities().instabuild) legacyJar.shrink(1);
        return InteractionResult.SUCCESS;
    }

    private static void finish(PlayerInteractEvent event, InteractionResult result) {
        event.setCancellationResult(result);
        event.setCanceled(true);
    }
}

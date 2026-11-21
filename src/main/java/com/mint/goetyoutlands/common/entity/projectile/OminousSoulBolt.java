package com.mint.goetyoutlands.common.entity.projectile;

import com.Polarice3.Goety.common.effects.GoetyEffects;
import com.Polarice3.Goety.common.entities.ModEntityType;
import com.Polarice3.Goety.common.entities.ally.Summoned;
import com.Polarice3.Goety.common.entities.projectiles.SpellHurtingProjectile;
import com.Polarice3.Goety.client.particles.ModParticleTypes;
import com.Polarice3.Goety.config.SpellConfig;
import com.Polarice3.Goety.init.ModSounds;
import com.Polarice3.Goety.utils.ModDamageSource;
import com.Polarice3.Goety.utils.ServantUtil;
import com.Polarice3.Goety.utils.WandUtil;
import com.mint.goetyoutlands.Register.ModEntities;
import com.mint.goetyoutlands.Register.ModParticles;
import com.mint.goetyoutlands.common.entity.IllagerNecromancerServant;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.ZombieVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.network.NetworkHooks;

import java.util.Arrays;

public final class OminousSoulBolt extends SpellHurtingProjectile implements IEntityAdditionalSpawnData {
    private final Vec3[] trail;
    private int cursor = -1;
    private boolean convertsOnKill;

    public OminousSoulBolt(EntityType<? extends OminousSoulBolt> type, Level level) {
        super(type, level);
        trail = level.isClientSide ? new Vec3[16] : null;
    }

    public OminousSoulBolt(ServerLevel level, LivingEntity caster, Vec3 forward) {
        this(level, caster, forward, 0.0F);
    }

    public OminousSoulBolt(ServerLevel level, LivingEntity caster, Vec3 forward, float spread) {
        super(ModEntities.OMINOUS_SOUL_BOLT.get(), caster.getX() + forward.x / 2, caster.getEyeY() - 0.2,
                caster.getZ() + forward.z / 2, forward.x + spread, forward.y, forward.z + spread, level);
        setOwner(caster);
        // The caster may level up before this bolt lands.
        convertsOnKill = caster instanceof IllagerNecromancerServant servant && servant.getNecroLevel() == 0;
        trail = null;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("ConvertsOnKill", convertsOnKill);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        convertsOnKill = tag.getBoolean("ConvertsOnKill");
    }

    @Override
    public void tick() {
        super.tick();
        if (tickCount >= 200) discard();
        if (trail == null || isRemoved()) return;
        Vec3 motion = getDeltaMovement();
        level().addParticle(ModParticles.SOUL_BOLT_TRAIL.get(), getX() - motion.x + jitter(),
                getY() - motion.y + 0.15, getZ() - motion.z + jitter(), 0, 0, 0);
        Vec3 center = position().add(0, getBbHeight() / 2.0, 0);
        if (cursor < 0) Arrays.fill(trail, center);
        cursor = (cursor + 1) & 15;
        trail[cursor] = center;
    }

    private double jitter() {
        return random.nextDouble() * 0.25 * random.nextIntBetweenInclusive(-1, 1);
    }

    public boolean hasTrail() { return cursor >= 0; }

    public Vec3 trailPoint(int ticksAgo, float partialTick) {
        return trail[(cursor - ticksAgo - 1) & 15].lerp(trail[(cursor - ticksAgo) & 15], partialTick);
    }

    @Override
    protected ParticleOptions getTrailParticle() { return ModParticleTypes.NONE.get(); }

    @Override
    protected boolean shouldBurn() { return false; }

    @Override
    public boolean isOnFire() { return false; }

    @Override
    public boolean isPickable() { return false; }

    @Override
    public boolean hurt(net.minecraft.world.damagesource.DamageSource source, float amount) { return false; }

    @Override
    protected void onHitEntity(EntityHitResult hit) {
        super.onHitEntity(hit);
        if (!(level() instanceof ServerLevel server)) return;
        Entity target = hit.getEntity();
        Entity owner = getOwner();
        float base = SpellConfig.SoulBoltDamage.get().floatValue() * WandUtil.damageMultiply();
        boolean hurt;
        if (owner instanceof LivingEntity caster) {
            if (caster instanceof IllagerNecromancerServant servant) {
                AttributeInstance attack = servant.getAttribute(Attributes.ATTACK_DAMAGE);
                if (attack != null && attack.getValue() > 0) base = (float) attack.getValue();
            }
            hurt = target.hurt(ModDamageSource.indirectSoul(this, caster), base + getExtraDamage());
            if (hurt) {
                if (target.isAlive()) {
                    doEnchantDamageEffects(caster, target);
                } else if (convertsOnKill && caster instanceof IllagerNecromancerServant servant && target instanceof Mob mob) {
                    convertOnKill(server, mob, servant);
                }
            }
        } else {
            hurt = target.hurt(target.damageSources().magic(), base);
        }
        if (hurt && target instanceof LivingEntity living) {
            Entity origin = owner != null ? owner : this;
            living.knockback(1.0, origin.getX() - target.getX(), origin.getZ() - target.getZ());
            if (living.isAlive() && (owner instanceof Player || owner instanceof IllagerNecromancerServant)) {
                living.addEffect(new MobEffectInstance(GoetyEffects.CURSED.get(), 200, 0), owner);
            }
        }
    }

    private static void convertOnKill(ServerLevel level, Mob victim, IllagerNecromancerServant owner) {
        EntityType<? extends Summoned> resultType;
        if (victim instanceof ZombieVillager) {
            // Goety already produces the desired servant and preserves the villager's trades, gossip and XP.
            ServantUtil.convertZombies(victim, owner, false);
            return;
        } else if (victim instanceof Zombie) {
            resultType = ModEntityType.ZOMBIE_VILLAGER_SERVANT.get();
        } else if (victim instanceof AbstractSkeleton) {
            resultType = ModEntityType.SKELETON_PILLAGER_SERVANT.get();
        } else {
            ServantUtil.convertUndead(victim, owner, false, true);
            return;
        }

        if (!ForgeEventFactory.canLivingConvert(victim, resultType, timer -> {})) return;
        Summoned servant = victim.convertTo(resultType, true);
        if (servant == null) return;
        servant.setTrueOwner(owner);
        ForgeEventFactory.onFinalizeSpawn(servant, level, level.getCurrentDifficultyAt(servant.blockPosition()),
                MobSpawnType.CONVERSION, null, null);
        if (victim instanceof Zombie) servant.setBaby(victim.isBaby());
        servant.setLimitedLife(10 * (15 + level.random.nextInt(45)));
        ForgeEventFactory.onLivingConvert(victim, servant);
        if (!servant.isSilent()) level.levelEvent(null, LevelEvent.SOUND_ZOMBIE_INFECTED, servant.blockPosition(), 0);
    }

    @Override
    protected void onHit(HitResult hit) {
        super.onHit(hit);
        if (!(level() instanceof ServerLevel server)) return;
        playSound(ModSounds.BOLT_IMPACT.get());
        for (int i = 0; i < 32; i++) {
            server.sendParticles(ModParticles.SOUL_BOLT_IMPACT.get(), getX() + random.nextDouble(),
                    getY() + random.nextDouble(), getZ() + random.nextDouble(), 0, 0.45, 0.45, 0.45, 0.5);
        }
        discard();
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() { return NetworkHooks.getEntitySpawningPacket(this); }

    @Override
    public void writeSpawnData(FriendlyByteBuf buf) {
        buf.writeDouble(xPower).writeDouble(yPower).writeDouble(zPower).writeFloat(boltSpeed);
        buf.writeVarInt(getOwner() == null ? 0 : getOwner().getId());
    }

    @Override
    public void readSpawnData(FriendlyByteBuf buf) {
        xPower = buf.readDouble();
        yPower = buf.readDouble();
        zPower = buf.readDouble();
        boltSpeed = buf.readFloat();
        int ownerId = buf.readVarInt();
        if (ownerId != 0) setOwner(level().getEntity(ownerId));
    }
}

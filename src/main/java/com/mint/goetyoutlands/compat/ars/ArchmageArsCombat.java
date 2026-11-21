package com.mint.goetyoutlands.compat.ars;

import com.Polarice3.Goety.api.entities.IOwned;
import com.Polarice3.Goety.utils.MobUtil;
import com.hollingsworth.arsnouveau.api.spell.EntitySpellResolver;
import com.hollingsworth.arsnouveau.api.spell.Spell;
import com.hollingsworth.arsnouveau.api.spell.SpellContext;
import com.hollingsworth.arsnouveau.api.spell.wrapped_caster.LivingCaster;
import com.hollingsworth.arsnouveau.common.entity.EntityProjectileSpell;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentAmplify;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentPierce;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectHarm;
import com.hollingsworth.arsnouveau.common.spell.method.MethodProjectile;
import com.mint.goetyoutlands.common.entity.ArchmageNecromancerServant;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

import java.util.UUID;

public final class ArchmageArsCombat {
    private ArchmageArsCombat() { }

    public static void fire(ServerLevel level, ArchmageNecromancerServant caster, LivingEntity target) {
        int rank = caster.getNecroLevel();
        Spell spell = new Spell(MethodProjectile.INSTANCE);
        if (rank > 0) spell.add(AugmentPierce.INSTANCE);
        spell.add(EffectHarm.INSTANCE).add(AugmentAmplify.INSTANCE, rank);
        Vec3 aim = new Vec3(target.getX() - caster.getX(), target.getY(0.5D) - (caster.getEyeY() - 0.1D),
                target.getZ() - caster.getZ()).normalize();
        for (int i = -rank; i <= rank; ++i) {
            // Each projectile resolves independently, even when two hits occur in the same tick.
            EntitySpellResolver resolver = new EntitySpellResolver(new SpellContext(level, spell, caster, new LivingCaster(caster)));
            var shot = new AlliedPassThroughProjectile(level, resolver);
            Vec3 direction = aim.yRot((float) Math.toRadians(10.0D * i));
            shot.shoot(direction.x, direction.y, direction.z, 0.75F, 0.0F);
            level.addFreshEntity(shot);
        }
    }

    // Keep Ars' entity type, client visuals, glyph resolution and piercing. Only the server hit filter differs.
    private static final class AlliedPassThroughProjectile extends EntityProjectileSpell {
        AlliedPassThroughProjectile(ServerLevel level, EntitySpellResolver resolver) {
            super(level, resolver);
        }

        @Override
        protected boolean canHitEntity(Entity other) {
            if (!super.canHitEntity(other)) return false;
            if (!(getOwner() instanceof ArchmageNecromancerServant caster)) return true;
            UUID ownerId = caster.getOwnerId();
            if (other == caster || other.getUUID().equals(ownerId) || MobUtil.areAllies(caster, other)) return false;
            if (other instanceof IOwned owned) {
                UUID otherOwner = owned.getOwnerId();
                if (caster.getUUID().equals(otherOwner) || (ownerId != null && ownerId.equals(otherOwner))) return false;
                LivingEntity master = owned.getMasterOwner();
                if (master != null && (master == caster || master.getUUID().equals(ownerId))) return false;
            }
            return true;
        }
    }
}

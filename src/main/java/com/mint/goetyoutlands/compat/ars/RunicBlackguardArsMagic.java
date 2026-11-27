package com.mint.goetyoutlands.compat.ars;

import com.hollingsworth.arsnouveau.api.spell.EntitySpellResolver;
import com.hollingsworth.arsnouveau.api.spell.Spell;
import com.hollingsworth.arsnouveau.api.spell.SpellContext;
import com.hollingsworth.arsnouveau.api.spell.wrapped_caster.LivingCaster;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectCrush;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectDelay;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectFlare;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectFreeze;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectIgnite;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectLaunch;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectWindshear;
import com.hollingsworth.arsnouveau.common.spell.method.MethodTouch;
import com.mint.goetyoutlands.common.entity.RunicBlackguardServant;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;

public final class RunicBlackguardArsMagic {
    private RunicBlackguardArsMagic() { }

    public static void freeze(ServerLevel level, RunicBlackguardServant caster, LivingEntity target) {
        Spell spell = new Spell(MethodTouch.INSTANCE, EffectFreeze.INSTANCE);
        SpellContext context = new SpellContext(level, spell, caster, new LivingCaster(caster));
        new EntitySpellResolver(context).onCastOnEntity(target);
    }

    public static void delayedCrush(ServerLevel level, RunicBlackguardServant caster, LivingEntity target) {
        // Ars schedules the follow-up so it is separate from the physical hit.
        Spell spell = new Spell(MethodTouch.INSTANCE, EffectDelay.INSTANCE, EffectCrush.INSTANCE);
        SpellContext context = new SpellContext(level, spell, caster, new LivingCaster(caster));
        new EntitySpellResolver(context).onCastOnEntity(target);
    }

    public static void launchAndWindshear(ServerLevel level, RunicBlackguardServant caster, LivingEntity target) {
        // Delay lets the target leave the ground before Ars checks Wind Shear's airborne condition.
        Spell spell = new Spell(MethodTouch.INSTANCE, EffectLaunch.INSTANCE, EffectDelay.INSTANCE, EffectWindshear.INSTANCE);
        SpellContext context = new SpellContext(level, spell, caster, new LivingCaster(caster));
        new EntitySpellResolver(context).onCastOnEntity(target);
    }

    public static void igniteAndFlare(ServerLevel level, RunicBlackguardServant caster, LivingEntity target) {
        // Ars resolves Ignite first so Flare can use its native burning-target condition and cinders.
        Spell spell = new Spell(MethodTouch.INSTANCE, EffectIgnite.INSTANCE, EffectFlare.INSTANCE);
        SpellContext context = new SpellContext(level, spell, caster, new LivingCaster(caster));
        new EntitySpellResolver(context).onCastOnEntity(target);
    }
}

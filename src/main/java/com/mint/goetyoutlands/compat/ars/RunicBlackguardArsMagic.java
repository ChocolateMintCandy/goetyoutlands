package com.mint.goetyoutlands.compat.ars;

import com.hollingsworth.arsnouveau.api.spell.EntitySpellResolver;
import com.hollingsworth.arsnouveau.api.spell.Spell;
import com.hollingsworth.arsnouveau.api.spell.SpellContext;
import com.hollingsworth.arsnouveau.api.spell.wrapped_caster.LivingCaster;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectFreeze;
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
}

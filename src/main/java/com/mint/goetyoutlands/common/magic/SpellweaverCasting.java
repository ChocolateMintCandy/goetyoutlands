package com.mint.goetyoutlands.common.magic;

import com.Polarice3.Goety.api.magic.ISpell;
import com.Polarice3.Goety.api.magic.SpellType;
import com.Polarice3.Goety.utils.CuriosFinder;
import com.mint.goetyoutlands.Register.ModItems;
import net.minecraft.world.entity.LivingEntity;

public final class SpellweaverCasting {
    private SpellweaverCasting() {}

    public static boolean reduceCastTime(boolean alreadyReduced, ISpell spell, LivingEntity caster) {
        return alreadyReduced || (ModItems.SPELLWEAVER_CIRCLET.isPresent() && spell.getSpellType() == SpellType.NONE
                && CuriosFinder.hasCurio(caster, ModItems.SPELLWEAVER_CIRCLET.get()));
    }
}

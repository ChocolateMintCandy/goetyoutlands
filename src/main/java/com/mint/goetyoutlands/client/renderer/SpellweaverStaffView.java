package com.mint.goetyoutlands.client.renderer;

import com.Polarice3.Goety.api.magic.IChargingSpell;
import com.Polarice3.Goety.common.items.magic.DarkStaff;
import com.mint.goetyoutlands.common.magic.SpellweaverCastFx;
import com.mint.goetyoutlands.goety_outlands;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.model.BakedModelWrapper;

import javax.annotation.Nullable;

public final class SpellweaverStaffView {
    private static final ModelResourceLocation KEY = new ModelResourceLocation(goety_outlands.MOD_ID, "spellweaver_staff", "inventory");
    static final double RELEASE = 47, END = 76, LOOP = 160; // Blockbench seconds × 20
    private static HumanoidArm activeArm;
    private static int elapsed, duration;
    private SpellweaverStaffView() {}

    public static void wrap(ModelEvent.ModifyBakingResult event) {
        BakedModel base = event.getModels().get(KEY);
        if (base == null) return;
        // ItemOverrides sees the actual holder just before renderByItem, which only receives a stack.
        // Retain only the scalar input for this draw, never the player or their world.
        ItemOverrides overrides = new ItemOverrides() {
            @Override public BakedModel resolve(BakedModel model, ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity holder, int seed) {
                activeArm = null;
                if (holder != null && holder.isUsingItem() && holder.getUseItem().getItem() == stack.getItem()) {
                    ItemStack used = holder.getUseItem();
                    activeArm = holder.getUsedItemHand() == InteractionHand.MAIN_HAND ? holder.getMainArm() : holder.getMainArm().getOpposite();
                    elapsed = holder.getTicksUsingItem();
                    duration = ((DarkStaff)used.getItem()).getSpell(used) instanceof IChargingSpell charging
                            ? charging.castUp(holder, used) : used.getUseDuration();
                }
                return base.getOverrides().resolve(base, stack, level, holder, seed);
            }
        };
        event.getModels().put(KEY, new BakedModelWrapper<>(base) {
            @Override public ItemOverrides getOverrides() { return overrides; }
        });
    }

    // Negative means idle. Partial ticks interpolate within the host's actual wind-up.
    static double castTick(ItemStack stack, ItemDisplayContext context, long now, float partial) {
        HumanoidArm arm = switch (context) {
            case FIRST_PERSON_LEFT_HAND, THIRD_PERSON_LEFT_HAND -> HumanoidArm.LEFT;
            case FIRST_PERSON_RIGHT_HAND, THIRD_PERSON_RIGHT_HAND -> HumanoidArm.RIGHT;
            default -> null;
        };
        if (arm == null) return -1;
        boolean using = activeArm == arm;
        var tag = stack.getTag();
        if (tag != null && tag.contains(SpellweaverCastFx.RELEASE)) {
            long age = now - tag.getLong(SpellweaverCastFx.RELEASE);
            if (age >= -10 && age + partial < END - RELEASE && (!using || age <= elapsed))
                return RELEASE + Math.max(0, age + partial);
        }
        return using ? RELEASE * Math.min(1, (elapsed + partial) / Math.max(1, duration)) : -1;
    }
}

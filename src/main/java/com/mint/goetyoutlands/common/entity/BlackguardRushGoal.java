package com.mint.goetyoutlands.common.entity;

import com.Polarice3.Goety.utils.MobUtil;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.pathfinder.Path;

import java.util.EnumSet;

final class BlackguardRushGoal extends Goal {
    static final int COOLDOWN = 160;
    private static final int DURATION = 20;
    private static final double SPEED = 1.6D;
    private final RunicBlackguardServant mob;
    private Path path;
    private int targetId, ticks;
    private long nextPathCheck;

    BlackguardRushGoal(RunicBlackguardServant mob) {
        this.mob = mob;
        setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    private boolean eligible(LivingEntity target) {
        return target != null && target.isAlive() && mob.canAttack(target) && !MobUtil.areAllies(mob, target)
                && !mob.isStaying() && !mob.isCommanded() && !mob.isPassenger() && !mob.isInWaterOrBubble()
                && (!mob.isGuardingArea() || mob.isWithinGuard(target.blockPosition())) && mob.hasLineOfSight(target);
    }

    @Override
    public boolean canUse() {
        LivingEntity target = mob.getTarget();
        if (!mob.rushReady() || !mob.onGround() || mob.isMeleeAttacking() || target == null) return false;
        double distance = mob.distanceToSqr(target);
        if (distance < 25 || distance > 81 || !eligible(target) || mob.level().getGameTime() < nextPathCheck) return false;
        nextPathCheck = mob.level().getGameTime() + 10;
        path = mob.getNavigation().createPath(target, 0);
        return path != null && path.canReach();
    }

    @Override
    public void start() {
        LivingEntity target = mob.getTarget();
        if (target != null && mob.getNavigation().moveTo(path, SPEED)) {
            targetId = target.getId();
            ticks = 0;
            mob.beginRush();
            mob.playSound(SoundEvents.ARMOR_EQUIP_IRON, 0.8F, 0.85F);
        }
        path = null;
    }

    @Override
    public boolean canContinueToUse() {
        LivingEntity target = mob.getTarget();
        return mob.isRushing() && ticks < DURATION && !mob.horizontalCollision && !mob.isMeleeAttacking()
                && target != null && target.getId() == targetId && eligible(target)
                && (!mob.getNavigation().isDone() || mob.targetClose(target, mob.distanceToSqr(target)));
    }

    @Override
    public boolean requiresUpdateEveryTick() { return true; }

    @Override
    public void tick() {
        if (!canContinueToUse()) { stop(); return; }
        LivingEntity target = mob.getTarget();
        mob.getLookControl().setLookAt(target, 30.0F, 30.0F);
        if (mob.targetClose(target, mob.distanceToSqr(target))) {
            stop();
            mob.commitSmash(targetId);
        } else if (++ticks >= DURATION) {
            stop();
        } else if (ticks % 5 == 0) {
            Path update = mob.getNavigation().createPath(target, 0);
            if (update == null || !update.canReach() || !mob.getNavigation().moveTo(update, SPEED)) stop();
        }
    }

    @Override
    public void stop() {
        if (mob.isRushing()) {
            mob.getNavigation().stop();
            mob.endRush();
        }
        path = null;
    }
}

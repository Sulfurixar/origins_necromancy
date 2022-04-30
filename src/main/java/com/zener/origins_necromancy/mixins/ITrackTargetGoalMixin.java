package com.zener.origins_necromancy.mixins;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.entity.ai.goal.TrackTargetGoal;
import net.minecraft.entity.mob.MobEntity;

@Mixin(TrackTargetGoal.class)
public class ITrackTargetGoalMixin {
    @Shadow
    @Final
    protected MobEntity mob;

    public MobEntity getMob() {
        return mob;
    }
}

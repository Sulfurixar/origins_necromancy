package com.zener.origins_necromancy.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.entity.ai.goal.SkeletonHorseTrapTriggerGoal;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.entity.passive.HorseBaseEntity;
import net.minecraft.world.LocalDifficulty;

@Mixin(SkeletonHorseTrapTriggerGoal.class)
public interface ISkeletonHorseTrapTriggerGoalMixin {

    @Invoker("getSkeleton")
    public SkeletonEntity $getSkeleton(LocalDifficulty localDifficulty, HorseBaseEntity vehicle);

    @Invoker("getHorse")
    public HorseBaseEntity $getHorse(LocalDifficulty localDifficulty);
}

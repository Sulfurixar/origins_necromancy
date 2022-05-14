package com.zener.origins_necromancy.mixins;

import java.util.UUID;

import com.zener.origins_necromancy.components.ComponentHandler;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.entity.ai.goal.SkeletonHorseTrapTriggerGoal;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.entity.mob.SkeletonHorseEntity;
import net.minecraft.entity.passive.HorseBaseEntity;
import net.minecraft.world.LocalDifficulty;

@Mixin(SkeletonHorseTrapTriggerGoal.class)
public class SkeletonHorseTrapTriggerGoalMixin {
    
    @Redirect(method = "tick()V", at = @At(value = "INVOKE", 
    target = "Lnet/minecraft/entity/ai/goal/SkeletonHorseTrapTriggerGoal;getSkeleton(Lnet/minecraft/world/LocalDifficulty;Lnet/minecraft/entity/passive/HorseBaseEntity;)Lnet/minecraft/entity/mob/SkeletonEntity;"))
    private SkeletonEntity setSkeletonOwner(SkeletonHorseTrapTriggerGoal goal, LocalDifficulty difficulty, HorseBaseEntity horse) {

        SkeletonEntity skeleton = ((ISkeletonHorseTrapTriggerGoalMixin)goal).$getSkeleton(difficulty, horse);
        UUID owner = ComponentHandler.OWNER_KEY.get(horse).OwnerUUID();
        ComponentHandler.OWNER_KEY.get(skeleton).setOwner(owner);
        ComponentHandler.OWNER_KEY.sync(skeleton);
        return skeleton;
    }

    @Shadow
    @Final
    private SkeletonHorseEntity skeletonHorse;

    @Redirect(method = "tick()V", at = @At(value = "INVOKE", 
    target = "Lnet/minecraft/entity/ai/goal/SkeletonHorseTrapTriggerGoal;getHorse(Lnet/minecraft/world/LocalDifficulty;)Lnet/minecraft/entity/passive/HorseBaseEntity;"))
    private HorseBaseEntity setHorseOwner(SkeletonHorseTrapTriggerGoal goal, LocalDifficulty difficulty) {

        HorseBaseEntity horse = ((ISkeletonHorseTrapTriggerGoalMixin)goal).$getHorse(difficulty);
        UUID owner = ComponentHandler.OWNER_KEY.get(skeletonHorse).OwnerUUID();
        ComponentHandler.OWNER_KEY.get(horse).setOwner(owner);
        ComponentHandler.OWNER_KEY.sync(horse);
        return horse;
        
    }
}

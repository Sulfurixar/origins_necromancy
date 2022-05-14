package com.zener.origins_necromancy.mixins;

import com.zener.origins_necromancy.targeting.TargetingHandler;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.RevengeGoal;
import net.minecraft.entity.mob.MobEntity;

@Mixin(RevengeGoal.class)
public class RevengeGoalMixin {
    
    @Inject(method = "canStart()Z", at = @At("RETURN"), cancellable = true)
    public void start(CallbackInfoReturnable<Boolean> cir) {
        MobEntity mob = ((ITrackTargetGoalMixin)((Object) this)).getMob();
        if (TargetingHandler.target_override((LivingEntity)mob, null, cir)) {
            cir.cancel();
        }
        if (mob.getAttacker() == null) {
            cir.setReturnValue(false);
            cir.cancel();
        }
    }

}

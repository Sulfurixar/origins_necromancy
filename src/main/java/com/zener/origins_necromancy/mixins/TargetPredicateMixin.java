package com.zener.origins_necromancy.mixins;

import javax.annotation.Nullable;

import com.zener.origins_necromancy.targeting.TargetingHandler;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;

@Mixin(TargetPredicate.class)
public class TargetPredicateMixin {
    
    @Inject(method = "test(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/entity/LivingEntity;)Z", at = @At("TAIL"), cancellable = true)
    public void test(@Nullable LivingEntity baseEntity, LivingEntity targetEntity, CallbackInfoReturnable<Boolean> cir) {
        if (TargetingHandler.target_override(baseEntity, targetEntity, cir)) {
            cir.cancel();
        }
    }
}

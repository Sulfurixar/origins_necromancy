package com.zener.origins_necromancy.mixins;

import com.zener.origins_necromancy.targeting.TargetingHandler;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;

@Mixin(HostileEntity.class)
public class HostileEntityMixin {
    
    @Inject(method = "isAngryAt(Lnet/minecraft/entity/player/PlayerEntity;)Z", cancellable = true, at = @At("HEAD"))
    public void isAngryAt(PlayerEntity player, CallbackInfoReturnable<Boolean> cir) {
        if (TargetingHandler.target_override((LivingEntity)((Object)this), (LivingEntity)player, cir)) {
            cir.cancel();
        }

    }
}

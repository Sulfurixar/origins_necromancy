package com.zener.origins_necromancy.mixins;

import com.zener.origins_necromancy.components.ComponentHandler;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;

@Mixin(HostileEntity.class)
public class HostileEntityMixin {
    
    @Inject(method = "isAngryAt(Lnet/minecraft/entity/player/PlayerEntity;)Z", cancellable = true, at = @At("HEAD"))
    public void isAngryAt(PlayerEntity player, CallbackInfoReturnable<Boolean> cir) {
        if (ComponentHandler.OWNER_KEY.get((Entity)((Object)this)).OwnerUUID().equals(player.getUuid())) {
            cir.setReturnValue(false);
            cir.cancel();
        }
    }
}

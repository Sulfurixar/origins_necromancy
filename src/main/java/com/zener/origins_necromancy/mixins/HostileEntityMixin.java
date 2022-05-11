package com.zener.origins_necromancy.mixins;

import com.zener.origins_necromancy.components.ComponentHandler;
import com.zener.origins_necromancy.components.OwnerUUIDComponent;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;

@Mixin(HostileEntity.class)
public class HostileEntityMixin {
    
    @Inject(method = "isAngryAt(Lnet/minecraft/entity/player/PlayerEntity;)Z", cancellable = true, at = @At("HEAD"))
    public void isAngryAt(PlayerEntity player, CallbackInfoReturnable<Boolean> cir) {
        OwnerUUIDComponent ownerUUIDComponent = ComponentHandler.OWNER_KEY.get((Entity)((Object)this));
        if (ownerUUIDComponent.OwnerUUID().equals(player.getUuid())) {
            cir.setReturnValue(false);
            cir.cancel();
        }

        if (player.getEntityWorld().isClient()) return;
        ServerPlayerEntity entity_target = (ServerPlayerEntity)player;

        ServerPlayerEntity owner = entity_target.getServer().getPlayerManager().getPlayer(ownerUUIDComponent.OwnerUUID());
        
        if (owner != null) {
            Entity target = ComponentHandler.TARGET_COMPONENT.get(owner).getTarget();
            if (target != null && target.getUuid().equals(player.getUuid())) {
                cir.setReturnValue(true);
                cir.cancel();
            }
        }
    }
}

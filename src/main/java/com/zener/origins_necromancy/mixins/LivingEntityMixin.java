package com.zener.origins_necromancy.mixins;

import java.util.UUID;

import com.zener.origins_necromancy.components.ComponentHandler;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Inject(method = "canTarget(Lnet/minecraft/entity/LivingEntity;)Z", at = @At("TAIL"), cancellable = true)
    public void canTarget(LivingEntity target, CallbackInfoReturnable<Boolean> cir) {
        UUID owner_uuid = ComponentHandler.OWNER_KEY.get((Entity)((Object)this)).OwnerUUID();
        if (owner_uuid != null && owner_uuid.equals(target.getUuid())) {
            cir.setReturnValue(false);
            cir.cancel();
        }
    }
}

package com.zener.origins_necromancy.mixins;

import java.util.UUID;

import javax.annotation.Nullable;

import com.zener.origins_necromancy.components.ComponentHandler;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;

@Mixin(TargetPredicate.class)
public class TargetPredicateMixin {
    
    @Inject(method = "test(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/entity/LivingEntity;)Z", at = @At("TAIL"), cancellable = true)
    public void test(@Nullable LivingEntity baseEntity, LivingEntity targetEntity, CallbackInfoReturnable<Boolean> cir) {
        UUID owner_uuid = ComponentHandler.OWNER_KEY.get(baseEntity).OwnerUUID();
        if (owner_uuid != null && owner_uuid.equals(targetEntity.getUuid())) {
            if (baseEntity.getAttacking() != null && baseEntity.getAttacking().getUuid().equals(owner_uuid)) {
                baseEntity.setAttacking(null);
            }
            if (baseEntity.getAttacker() != null && baseEntity.getAttacker().getUuid().equals(owner_uuid)) {
                baseEntity.setAttacker(null);
            }
            if (baseEntity instanceof PathAwareEntity) {
                MobEntity mob = (MobEntity)baseEntity;
                if (mob.getTarget() != null && mob.getTarget().getUuid().equals(owner_uuid)) {
                    mob.setTarget(null);
                }
            }
            cir.setReturnValue(false);
            cir.cancel();
        }
    }
}

package com.zener.origins_necromancy.mixins;

import java.util.UUID;

import com.zener.origins_necromancy.components.ComponentHandler;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.ai.goal.RevengeGoal;
import net.minecraft.entity.mob.MobEntity;

@Mixin(RevengeGoal.class)
public class RevengeGoalMixin {
    
    @Inject(method = "start()V", at = @At("HEAD"), cancellable = true)
    public void start(CallbackInfo ci) {
        MobEntity mob = ((ITrackTargetGoalMixin)((Object) this)).getMob();
        UUID owner_uuid = ComponentHandler.OWNER_KEY.get(mob).OwnerUUID();
        if (owner_uuid == null) return;
        boolean stop = false;
        if (mob.getAttacker() != null && owner_uuid.equals(mob.getAttacker().getUuid())) {
            mob.setAttacker(null);
            stop = true;
        }
        if (mob.getTarget() != null && owner_uuid.equals(mob.getTarget().getUuid())) {
            mob.setTarget(null);
            stop = true;
        }
        if (mob.getAttacking() != null && owner_uuid.equals(mob.getAttacking().getUuid())) {
            mob.setAttacking(null);
            stop = true;
        }
        if (stop) {
            ci.cancel();
        }
    }

}

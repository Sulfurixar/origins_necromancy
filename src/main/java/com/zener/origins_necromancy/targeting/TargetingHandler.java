package com.zener.origins_necromancy.targeting;

import java.util.UUID;

import javax.annotation.Nullable;

import com.zener.origins_necromancy.components.ComponentHandler;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.server.network.ServerPlayerEntity;

public class TargetingHandler {
    
    // returns boolean based on whether cir was set or not
    public static boolean target_override(LivingEntity attacker, @Nullable LivingEntity target, CallbackInfoReturnable<Boolean> cir) {
        if (attacker == null) return false;
        if (attacker.getEntityWorld().isClient()) return false;
        UUID owner_uuid = ComponentHandler.OWNER_KEY.get(attacker).OwnerUUID();
        // we don't want to attack an owner
        boolean modified = false;
        if (isOwner(attacker, target, owner_uuid)) {
            cir.setReturnValue(false);
            modified = true;
        }
        if (isOwnerTarget(attacker, target, owner_uuid)) {
            cir.setReturnValue(true);
            modified = true;
        }
        return modified;
    }

    public static boolean target_override(LivingEntity attacker, @Nullable LivingEntity target, CallbackInfo ci) {
        if (attacker.getEntityWorld().isClient()) return false;
        UUID owner_uuid = ComponentHandler.OWNER_KEY.get(attacker).OwnerUUID();
        // we don't want to attack an owner
        boolean modified = false;
        if (isOwner(attacker, target, owner_uuid)) {
            modified = true;
        }
        if (isOwnerTarget(attacker, target, owner_uuid)) {
            modified = true;
        }
        return modified;
    }

    public static boolean isOwner(LivingEntity attacker, @Nullable LivingEntity target, @Nullable UUID owner_uuid) {
        if (owner_uuid == null) return false;
        boolean result = false;
        if (target != null && target.getUuid().equals(owner_uuid)) {
            result = true;
        } 
        if (attacker.getAttacking() != null && attacker.getAttacking().getUuid().equals(owner_uuid)) {
            attacker.setAttacking(null);
            result = true;
        }
        if (attacker.getAttacker() != null && attacker.getAttacker().getUuid().equals(owner_uuid)) {
            attacker.setAttacker(null);
            result = true;
        }
        if (attacker instanceof PathAwareEntity) {
            MobEntity mob = (MobEntity)attacker;
            if (mob.getTarget() != null && mob.getTarget().getUuid().equals(owner_uuid)) {
                mob.setTarget(null);
                result = true;
            }
        }
        return result;
    }

    public static boolean isOwnerTarget(LivingEntity attacker, @Nullable LivingEntity target, @Nullable UUID owner_uuid) {
        if (owner_uuid == null) return false;
        ServerPlayerEntity owner = attacker.getServer().getPlayerManager().getPlayer(owner_uuid);
        if (owner == null) return false;
        LivingEntity owner_target = ComponentHandler.TARGET_COMPONENT.get(owner).getTarget();
        if (owner_target != null) {
            if (target != null && owner_target.getUuid().equals(target.getUuid())) {
                attacker.setAttacker(target);
                if (attacker instanceof PathAwareEntity) {
                    MobEntity mob = (MobEntity)attacker;
                    mob.setTarget(target);
                    mob.setAttacking(true);
                }
                return true;
            } else {
                attacker.setAttacker(owner_target);
                if (attacker instanceof PathAwareEntity) {
                    MobEntity mob = (MobEntity)attacker;
                    mob.setTarget(owner_target);
                }
                return true;
            }
        }
        return false;
    }
}

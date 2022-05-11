package com.zener.origins_necromancy.mixins;

import java.util.Iterator;
import java.util.UUID;

import com.zener.origins_necromancy.ILivingEntityMixin;
import com.zener.origins_necromancy.components.ComponentHandler;
import com.zener.origins_necromancy.components.PhylacteryComponent;
import com.zener.origins_necromancy.phylactery.PhylacteryCrystalBlock;
import com.zener.origins_necromancy.phylactery.PhylacteryEntity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

@Mixin(LivingEntity.class)
public class LivingEntityMixin implements ILivingEntityMixin {

    @Inject(method = "canTarget(Lnet/minecraft/entity/LivingEntity;)Z", at = @At("TAIL"), cancellable = true)
    public void canTarget(LivingEntity target, CallbackInfoReturnable<Boolean> cir) {
        UUID owner_uuid = ComponentHandler.OWNER_KEY.get((Entity)((Object)this)).OwnerUUID();
        if (owner_uuid != null && owner_uuid.equals(target.getUuid())) {
            cir.setReturnValue(false);
            cir.cancel();
        }
    }

    @Inject(method = "tryUseTotem(Lnet/minecraft/entity/damage/DamageSource;)Z", 
    at = @At("RETURN"))
    public void tryUseTotem(DamageSource source, CallbackInfoReturnable<Boolean> cir) {
        if ((LivingEntity)(Object)this instanceof PlayerEntity) {
            ILivingEntityMixin e = ((ILivingEntityMixin)(Object)this);
            if (e.getVarItemStack().isEmpty()) {    // Didn't find an item at saveItemStack or in tryUseTotem
                teleportPlayer((LivingEntity)(Object)this);
            }
            e.setVarItemStack(ItemStack.EMPTY);
        }
    }

    // SET varItemStack for later use
    @Redirect(method = "tryUseTotem(Lnet/minecraft/entity/damage/DamageSource;)Z", 
    at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;copy()Lnet/minecraft/item/ItemStack;"))
    private ItemStack saveItemStack(ItemStack stack, DamageSource source) {
        ILivingEntityMixin e = (ILivingEntityMixin)(Object)this;
        e.setVarItemStack(stack);
        return stack;
    }

    private void teleportPlayer(LivingEntity entity) {
        if (entity instanceof PlayerEntity) {
            if (entity.getEntityWorld().isClient()) { return; }
            ServerPlayerEntity player = (ServerPlayerEntity)entity;
            PhylacteryEntity phylacteryEntity = findPhylactery(player);
            PhylacteryComponent component = ComponentHandler.PHYLACTERY_KEY.get(player);
            if (PhylacteryCrystalBlock.discharge(phylacteryEntity.getCachedState(), phylacteryEntity.getWorld(), phylacteryEntity.getPos())) {
                player.teleport((ServerWorld)phylacteryEntity.getWorld(), component.playerX(), component.playerY(), component.playerZ(), 0, 0);
                player.setHealth(1.0f);
                player.clearStatusEffects();
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 900, 1));
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 100, 1));
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 800, 0));
                player.world.sendEntityStatus(player, (byte)35);
            }
        }
    }

    private PhylacteryEntity findPhylactery(ServerPlayerEntity player) {
        PhylacteryComponent component = ComponentHandler.PHYLACTERY_KEY.get(player);
        BlockPos phylacteryPos = new BlockPos(component.phylacteryX(), component.phylacteryY(), component.phylacteryZ());
        Iterator<ServerWorld> iterator = ((ServerPlayerEntity)player).server.getWorlds().iterator();
        while (iterator.hasNext()) {
            ServerWorld world = iterator.next();
            if (world.getDimension().getSuffix().equals(component.world())) {
                BlockEntity blockEntity = world.getBlockEntity(phylacteryPos);
                if (!(blockEntity instanceof PhylacteryEntity)) {
                    return null;
                }
                return ((PhylacteryEntity) blockEntity);
            }
        }
        return null;
    }

    @Getter @Setter private ItemStack varItemStack = ItemStack.EMPTY;

    /*
    private boolean tryUseTotem(DamageSource source) {
        if (source.isOutOfWorld()) {
            return false;
        }
        ItemStack itemStack = null;
        for (Hand hand : Hand.values()) {
            ItemStack itemStack2 = this.getStackInHand(hand);
            if (!itemStack2.isOf(Items.TOTEM_OF_UNDYING)) continue;
            itemStack = itemStack2.copy();                              // SET varItemStack for later use
            itemStack2.decrement(1);
            break;
        }

        // INJECT CODE HERE

        if (itemStack != null) {
            if (this instanceof ServerPlayerEntity) {
                ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity)this;
                serverPlayerEntity.incrementStat(Stats.USED.getOrCreateStat(Items.TOTEM_OF_UNDYING));
                Criteria.USED_TOTEM.trigger(serverPlayerEntity, itemStack);
            }
            this.setHealth(1.0f);
            this.clearStatusEffects();
            this.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 900, 1));
            this.addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 100, 1));
            this.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 800, 0));
            this.world.sendEntityStatus(this, (byte)35);
        }
        return itemStack != null;
    }
    */
}

package com.zener.origins_necromancy.mixins;

import java.util.Iterator;
import java.util.Optional;
import java.util.UUID;

import com.zener.origins_necromancy.BlockGen;
import com.zener.origins_necromancy.ILivingEntityMixin;
import com.zener.origins_necromancy.OriginsNecromancy;
import com.zener.origins_necromancy.components.ComponentHandler;
import com.zener.origins_necromancy.components.OwnerUUIDComponent;
import com.zener.origins_necromancy.components.PhylacteryComponent;
import com.zener.origins_necromancy.components.IWorldPhylacteryComponent.PhylacteryData;
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
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

@Mixin(LivingEntity.class)
public class LivingEntityMixin implements ILivingEntityMixin {

    @Inject(method = "canTarget(Lnet/minecraft/entity/LivingEntity;)Z", at = @At("TAIL"), cancellable = true)
    public void canTarget(LivingEntity target, CallbackInfoReturnable<Boolean> cir) {
        OwnerUUIDComponent ownerUUIDComponent = ComponentHandler.OWNER_KEY.get((Entity)((Object)this));
        UUID owner_uuid = ownerUUIDComponent.OwnerUUID();
        boolean stop = false;
        if (owner_uuid != null && owner_uuid.equals(target.getUuid())) {
            cir.setReturnValue(false);
            stop = true;
        }

        if (!target.getEntityWorld().isClient()) {
            ServerPlayerEntity owner = ((Entity)((Object)this)).getServer().getPlayerManager().getPlayer(ownerUUIDComponent.OwnerUUID());
        
            if (owner != null) {
                Entity _target = ComponentHandler.TARGET_COMPONENT.get(owner).getTarget();
                if (_target != null && _target.getUuid().equals(target.getUuid())) {
                    cir.setReturnValue(true);
                    stop = true;
                }
            }
        }

        if (stop) { cir.cancel(); }

    }

    @Inject(method = "tryUseTotem(Lnet/minecraft/entity/damage/DamageSource;)Z", 
    at = @At("RETURN"), cancellable = true)
    public void tryUseTotem(DamageSource source, CallbackInfoReturnable<Boolean> cir) {
        if ((LivingEntity)(Object)this instanceof PlayerEntity) {
            ILivingEntityMixin e = ((ILivingEntityMixin)(Object)this);
            if (e.getVarItemStack().isEmpty()) {    // Didn't find an item at saveItemStack or in tryUseTotem
                if (teleportPlayer2((LivingEntity)(Object)this)) {
                    cir.setReturnValue(true);
                    cir.cancel();
                } else if (teleportPlayer((LivingEntity)(Object)this)) {
                    cir.setReturnValue(true);
                    cir.cancel();
                }
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

    @Deprecated
    private boolean teleportPlayer(LivingEntity entity) {
        if (entity instanceof PlayerEntity) {
            if (entity.getEntityWorld().isClient()) { return false; }
            ServerPlayerEntity player = (ServerPlayerEntity)entity;
            PhylacteryComponent component = ComponentHandler.PHYLACTERY_KEY.get(player);
            PhylacteryEntity phylacteryEntity = findPhylactery2(player, component);
            OriginsNecromancy.LOGGER.info("Attempting Player Teleport:");
            OriginsNecromancy.LOGGER.info("\t"+player+":"+phylacteryEntity+":"+component);
            return PhylacteryEntity.playerRespawn(player, phylacteryEntity, component);
        }
        return false;
    }

    private boolean teleportPlayer2(LivingEntity entity) {
        if (entity instanceof PlayerEntity) {
            if (entity.getEntityWorld().isClient()) { return false; }
            ServerPlayerEntity player = (ServerPlayerEntity)entity;
            PhylacteryComponent component = ComponentHandler.PHYLACTERY_KEY.get(player);
            PhylacteryData data = findWorldPhylacteryComponent(player, component);
            OriginsNecromancy.LOGGER.info("Attempting Player Teleport:");
            OriginsNecromancy.LOGGER.info("\t"+player+":"+data+":"+component);
            return PhylacteryEntity.playerRespawn(player, data, component);
        }
        return false;
    }

    private PhylacteryData findWorldPhylacteryComponent(ServerPlayerEntity player, PhylacteryComponent component) {
        OriginsNecromancy.LOGGER.info("\tFinding Phylactery...");
        BlockPos blockPos = new BlockPos(component.phylacteryX(), component.phylacteryY(), component.phylacteryZ());
        Iterator<ServerWorld> iterator = ((ServerPlayerEntity)player).server.getWorlds().iterator();
        while (iterator.hasNext()) {
            ServerWorld world = iterator.next();
            OriginsNecromancy.LOGGER.info("\t\tComparing worlds: '"+world.getDimension().toString()+"'=='"+component.world()+"'");
            if (world.getDimension().toString().equals(component.world())) {
                PhylacteryData data = ComponentHandler.PHYLACTERY_COMPONENT.get(world).getPhylacteries().get(blockPos);
                return data;
            }
        }
        return null;
    }

    @Deprecated
    private PhylacteryEntity findPhylactery(ServerPlayerEntity player, PhylacteryComponent component) {
        OriginsNecromancy.LOGGER.info("\tFinding Phylactery...");
        BlockPos phylacteryPos = new BlockPos(component.phylacteryX(), component.phylacteryY(), component.phylacteryZ());
        Iterator<ServerWorld> iterator = ((ServerPlayerEntity)player).server.getWorlds().iterator();
        while (iterator.hasNext()) {
            ServerWorld world = iterator.next();
            OriginsNecromancy.LOGGER.info("\t\tComparing worlds: '"+world.getDimension().toString()+"'=='"+component.world()+"'");
            if (world.getDimension().toString().equals(component.world())) {
                Optional<PhylacteryEntity> opt = world.getBlockEntity(phylacteryPos, BlockGen.PHYLACTERY_ENTITY);
                if (!opt.isPresent()) {
                    return null;
                }
                BlockEntity blockEntity = opt.get();
                OriginsNecromancy.LOGGER.info("\t\tBlockEntity: "+blockEntity);
                if (!(blockEntity instanceof PhylacteryEntity)) {
                    return null;
                }
                OriginsNecromancy.LOGGER.info("Allegedly found Phylactery...");
                return ((PhylacteryEntity) blockEntity);
            }
        }
        return null;
    }

    @Deprecated
    private PhylacteryEntity findPhylactery2(ServerPlayerEntity player, PhylacteryComponent component) {
        OriginsNecromancy.LOGGER.info("\tFinding Phylactery...");
        PhylacteryEntity phylacteryEntity = component.phylacteryEntity();
        if (phylacteryEntity == null) {
            return findPhylactery(player, component);
        }
        OriginsNecromancy.LOGGER.info("Allegedly found Phylactery...");
        return phylacteryEntity;
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

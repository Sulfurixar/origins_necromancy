package com.zener.origins_necromancy.phylactery;

import java.util.UUID;

import com.zener.origins_necromancy.BlockGen;
import com.zener.origins_necromancy.OriginsNecromancy;
import com.zener.origins_necromancy.components.PhylacteryComponent;
import com.zener.origins_necromancy.components.IWorldPhylacteryComponent.PhylacteryData;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PhylacteryEntity extends BlockEntity {

    public boolean has_base = false;
    public UUID uuid;

    public PhylacteryEntity(BlockPos pos, BlockState state) {
        super(BlockGen.PHYLACTERY_ENTITY, pos, state);
    }
    
    @Override
    public NbtCompound writeNbt(NbtCompound tag) {
        super.writeNbt(tag);
        if (uuid != null) {
            tag.putUuid("UUID", uuid);
        }
        tag.putBoolean("b", this.getCachedState().get(PhylacteryCrystalBlock.ON_BASE));
        tag.putBoolean("c", this.getCachedState().get(PhylacteryCrystalBlock.CHARGED));
        return tag;
    }

    @Override
    public void readNbt(NbtCompound tag) {
        super.readNbt(tag);
        if (tag.contains("UUID")) {
            uuid = tag.getUuid("UUID");
        }
    }

    public static void serverTick(World world, BlockPos pos, BlockState state, PhylacteryEntity blockEntity) {
        blockEntity.has_base = world.getBlockState(pos.down()).isOf(BlockGen.PHYLACTERY_BASE);

        // clear data
        if (blockEntity.has_base == false) {
            world.setBlockState(pos, state.with(PhylacteryCrystalBlock.ON_BASE, blockEntity.has_base).with(PhylacteryCrystalBlock.CHARGED, false));
            blockEntity.uuid = null;
        } else {
            world.setBlockState(pos, state.with(PhylacteryCrystalBlock.ON_BASE, blockEntity.has_base));
        }
    }

    public static boolean playerRespawn(ServerPlayerEntity player, PhylacteryEntity phylacteryEntity, PhylacteryComponent component) {
        OriginsNecromancy.LOGGER.info("\tAttempting death prevention...");
        if (phylacteryEntity != null && PhylacteryCrystalBlock.discharge(phylacteryEntity.getCachedState(), phylacteryEntity.getWorld(), phylacteryEntity.getPos())) {
            player.teleport((ServerWorld)phylacteryEntity.getWorld(), component.playerX(), component.playerY(), component.playerZ(), 0, 0);
            player.setHealth(player.getMaxHealth());
            player.clearStatusEffects();
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 900, 1));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 100, 1));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 800, 0));
            player.world.sendEntityStatus(player, (byte)35);
            OriginsNecromancy.LOGGER.info("\tRespawn Success!");
            return true;
        }
        OriginsNecromancy.LOGGER.info("\tRespawn Failed!");
        return false;
    }

    public static boolean playerRespawn(ServerPlayerEntity player, PhylacteryData data, PhylacteryComponent component) {
        OriginsNecromancy.LOGGER.info("\tAttempting death prevention...");
        if (data != null && PhylacteryCrystalBlock.discharge(data.getBlockState(), data.getWorld(), data.getBlockPos())) {
            player.teleport((ServerWorld)data.getWorld(), component.playerX(), component.playerY(), component.playerZ(), 0, 0);
            player.setHealth(player.getMaxHealth());
            player.clearStatusEffects();
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 900, 1));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 100, 1));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 800, 0));
            player.world.sendEntityStatus(player, (byte)35);
            OriginsNecromancy.LOGGER.info("\tRespawn Success!");
            return true;
        }
        OriginsNecromancy.LOGGER.info("\tRespawn Failed!");
        return false;
    }

}

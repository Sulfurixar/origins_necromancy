package com.zener.origins_necromancy.phylactery;

import java.util.UUID;

import com.zener.origins_necromancy.BlockGen;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
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

}

package com.zener.origins_necromancy.components;

import java.util.Map;
import java.util.UUID;

import javax.annotation.Nullable;

import dev.onyxstudios.cca.api.v3.component.ComponentV3;
import lombok.Data;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IWorldPhylacteryComponent extends ComponentV3 {
    public Map<BlockPos, PhylacteryData> getPhylacteries();

    @Data
    public class PhylacteryData {
        private BlockState blockState;
        private final BlockPos blockPos;
        private UUID uuid;
        private final World world;
        PhylacteryData(@Nullable BlockState blockState, BlockPos blockPos, @Nullable UUID uuid, World world) {
            this.blockState = blockState;
            this.blockPos = blockPos;
            this.uuid = uuid;
            this.world = world;
        }
    }
}

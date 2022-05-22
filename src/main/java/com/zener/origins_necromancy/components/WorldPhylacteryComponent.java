package com.zener.origins_necromancy.components;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Nullable;

import com.zener.origins_necromancy.phylactery.PhylacteryEntity;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import lombok.Getter;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldPhylacteryComponent implements IWorldPhylacteryComponent, AutoSyncedComponent {
    @Getter private Map<BlockPos, PhylacteryData> Phylacteries = new HashMap<BlockPos, PhylacteryData>();

    private final World world;

    WorldPhylacteryComponent(World world) {
        this.world = world;
    }

    public void addPhylactery(PhylacteryEntity phylacteryEntity) {
        BlockState state = phylacteryEntity.getCachedState();
        BlockPos pos = phylacteryEntity.getPos();
        UUID uuid = phylacteryEntity.uuid;
        List<BlockPos> conflicts = Phylacteries.keySet().stream().filter(data -> data.equals(pos)).toList();
        if (!conflicts.isEmpty()) return;
        PhylacteryData data = new PhylacteryData(state, pos, uuid, world);
        Phylacteries.put(pos, data);
    }

    public void addPhylactery(BlockPos blockPos, @Nullable BlockState blockState, @Nullable UUID uuid) {
        List<BlockPos> conflicts = Phylacteries.keySet().stream().filter(data -> data.equals(blockPos)).toList();
        if (!conflicts.isEmpty()) return;
        PhylacteryData data = new PhylacteryData(blockState, blockPos, uuid, world);
        Phylacteries.put(blockPos, data);
    }

    public void updatePhylactery(PhylacteryEntity phylacteryEntity) {
        BlockState state = phylacteryEntity.getCachedState();
        BlockPos pos = phylacteryEntity.getPos();
        UUID uuid = phylacteryEntity.uuid;
        List<BlockPos> conflicts = Phylacteries.keySet().stream().filter(data -> data.equals(pos)).toList();
        if (conflicts.isEmpty()) { addPhylactery(phylacteryEntity); return; }
        PhylacteryData data = Phylacteries.get(conflicts.get(0));
        data.setBlockState(state);
        data.setUuid(uuid);
    }

    public void updatePhylactery(BlockPos blockPos, @Nullable BlockState blockState, @Nullable UUID uuid) {
        List<BlockPos> conflicts = Phylacteries.keySet().stream().filter(data -> data.equals(blockPos)).toList();
        if (conflicts.isEmpty()) { addPhylactery(blockPos, blockState, uuid); return; }
        PhylacteryData data = Phylacteries.get(conflicts.get(0));
        data.setBlockState(blockState);
        data.setUuid(uuid);
    }

    public void removePhylactery(PhylacteryEntity phylacteryEntity) {
        BlockPos pos = phylacteryEntity.getPos();
        List<BlockPos> conflicts = Phylacteries.keySet().stream().filter(data -> data.equals(pos)).toList();
        if (conflicts.isEmpty()) return;
        Phylacteries.remove(pos);
    }

    public void removePhylactery(BlockPos blockPos) {
        List<BlockPos> conflicts = Phylacteries.keySet().stream().filter(data -> data.equals(blockPos)).toList();
        if (conflicts.isEmpty()) return;
        Phylacteries.remove(blockPos);
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        Map <BlockPos, PhylacteryData> phylacteries = new HashMap<BlockPos, PhylacteryData>();
        if (tag.contains("list")) {
            NbtList list = tag.getList("list", NbtType.COMPOUND);
            list.forEach(entry -> {
                if (entry.getType() == NbtType.COMPOUND) {
                    NbtCompound e = (NbtCompound)entry;
                    BlockPos pos = NbtHelper.toBlockPos(e.getCompound("pos"));
                    PhylacteryData data = new PhylacteryData(
                        NbtHelper.toBlockState(e.getCompound("state")),
                        pos,
                        NbtHelper.toUuid(e.getCompound("uuid")),
                        world
                    );
                    phylacteries.put(pos, data);
                }
            });
        }
        Phylacteries = phylacteries;
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        NbtList list = new NbtList();
        Phylacteries.values().forEach(entry -> {
            NbtCompound c = new NbtCompound();
            c.put("state", NbtHelper.fromBlockState(entry.getBlockState()));
            c.put("pos", NbtHelper.fromBlockPos(entry.getBlockPos()));
            if (entry.getUuid() != null) {
                c.put("uuid", NbtHelper.fromUuid(entry.getUuid()));
            }
        });
        tag.put("list", list);
    }
}

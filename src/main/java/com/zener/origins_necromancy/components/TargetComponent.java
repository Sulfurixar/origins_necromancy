package com.zener.origins_necromancy.components;

import java.util.Iterator;
import java.util.UUID;

import lombok.Data;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

@Data
public class TargetComponent implements ITargetComponent {
    private LivingEntity target;
    private World world;
    private final PlayerEntity player;

    TargetComponent(PlayerEntity player) {
        this.player = player;
    }

    public void setTarget(LivingEntity target) {
        this.target = target;
        setWorld(target.getEntityWorld());
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        if (tag.contains("world") && tag.get("world").getType() == NbtType.STRING) {
            String world = tag.getString("world");
            if (tag.contains("target")) {
                UUID uuid = tag.getUuid("target");
                if (uuid == null) return;
                Iterator<ServerWorld> iterator = ((ServerPlayerEntity)player).server.getWorlds().iterator();
                while (iterator.hasNext()) {
                    ServerWorld _world = iterator.next();
                    if (_world.getDimension().getSuffix().equals(world)) {
                        target = (LivingEntity)_world.getEntity(uuid);
                        this.world = _world;
                        return;
                    }
                }
            }
        }
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        if (target == null) return;
        UUID uuid = target.getUuid();
        String world = this.world.getDimension().getSuffix();
        tag.putUuid("target", uuid);
        tag.putString("world", world);
    }
}

package com.zener.origins_necromancy.components;

import java.util.UUID;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;

public class OwnerUUIDComponent implements IOwnerUUIDComponent, AutoSyncedComponent {

    private UUID ownerUUID;
    private final Entity provider;

    public OwnerUUIDComponent(Entity provider) {
        this.provider = provider;
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        if (tag.get("owner") != null && tag.getLongArray("owner") instanceof long[]) {
            long[] owner = tag.getLongArray("owner");
            if (owner == null || owner.length != 2) { return; }

            ownerUUID = new UUID(owner[0], owner[1]);
        }
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        if (ownerUUID != null) {
            tag.putLongArray("owner", new long[] { ownerUUID.getMostSignificantBits(), ownerUUID.getLeastSignificantBits() });
        } else {
            tag.putLongArray("owner", new long[0]);
        }
    }

    public void setOwner(PlayerEntity player) {
        ownerUUID = player.getUuid();
    }

    @Override
    public UUID OwnerUUID() {
        return ownerUUID;
    }
    
    @Override
    public boolean shouldSyncWith(ServerPlayerEntity player) {
        return true; // sync with everyone
    }
}

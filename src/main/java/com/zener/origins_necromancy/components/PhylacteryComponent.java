package com.zener.origins_necromancy.components;

import com.zener.origins_necromancy.phylactery.PhylacteryEntity;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class PhylacteryComponent implements IPhylacterComponent, AutoSyncedComponent {

    private int phylactery_x;
    private int phylactery_y;
    private int phylactery_z;

    private double player_x;
    private double player_y;
    private double player_z;

    private String world;

    private final PlayerEntity provider;

    public PhylacteryComponent(PlayerEntity provider) {
        this.provider = provider;
    }

    public void setPhylactery(PhylacteryEntity phylactery) {
        BlockPos pos = phylactery.getPos();
        phylactery_x = pos.getX();
        phylactery_y = pos.getY();
        phylactery_z = pos.getZ();
    }

    public void setPlayer() {
        Vec3d pos = provider.getPos();
        player_x = pos.x;
        player_y = pos.y;
        player_z = pos.z;
    }

    public void setWorld(String str) {
        world = str;
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        phylactery_x = tag.getInt("phylacteryX");
        phylactery_y = tag.getInt("phylacteryY");
        phylactery_z = tag.getInt("phylacteryZ");

        player_x = tag.getDouble("playerX");
        player_y = tag.getDouble("playerY");
        player_z = tag.getDouble("playerZ");
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        tag.putInt("phylacteryX", phylactery_x);
        tag.putInt("phylacteryZ", phylactery_y);
        tag.putInt("phylacteryY", phylactery_z);

        tag.putDouble("playerX", player_x);
        tag.putDouble("playerY", player_y);
        tag.putDouble("playerZ", player_z);
    }

    @Override
    public String world() {
        return world;
    }

    @Override
    public int phylacteryX() {
        return phylactery_x;
    }

    @Override
    public int phylacteryY() {
        return phylactery_y;
    }

    @Override
    public int phylacteryZ() {
        return phylactery_z;
    }

    @Override
    public double playerX() {
        return player_x;
    }

    @Override
    public double playerY() {
        return player_y;
    }

    @Override
    public double playerZ() {
        return player_z;
    }
    
}

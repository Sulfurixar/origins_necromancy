package com.zener.origins_necromancy.components;

import dev.onyxstudios.cca.api.v3.component.ComponentV3;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public interface ITargetComponent extends ComponentV3 {
    public Entity getTarget();
    public World getWorld();
}

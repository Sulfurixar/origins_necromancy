package com.zener.origins_necromancy.components;

import com.zener.origins_necromancy.phylactery.PhylacteryEntity;

import dev.onyxstudios.cca.api.v3.component.ComponentV3;

public interface IPhylacterComponent extends ComponentV3 {
    int phylacteryX();
    int phylacteryY();
    int phylacteryZ();

    double playerX();
    double playerY();
    double playerZ();

    String world();

    PhylacteryEntity phylacteryEntity();
}

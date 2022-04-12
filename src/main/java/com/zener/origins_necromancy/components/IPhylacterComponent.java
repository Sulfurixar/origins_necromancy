package com.zener.origins_necromancy.components;

import dev.onyxstudios.cca.api.v3.component.ComponentV3;

public interface IPhylacterComponent extends ComponentV3 {
    int phylacteryX();
    int phylacteryY();
    int phylacteryZ();

    double playerX();
    double playerY();
    double playerZ();

    String world();
}

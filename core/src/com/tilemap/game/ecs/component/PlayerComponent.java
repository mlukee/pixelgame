package com.tilemap.game.ecs.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class PlayerComponent implements Component, Pool.Poolable {

    public boolean isInGrass = false;
    @Override
    public void reset() {
        isInGrass = false;
    }
}

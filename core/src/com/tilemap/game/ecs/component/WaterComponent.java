package com.tilemap.game.ecs.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class WaterComponent implements Component, Pool.Poolable {
    public boolean hit;

    @Override
    public void reset() {
        hit = false;
    }
}

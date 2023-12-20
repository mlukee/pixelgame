package com.tilemap.game.ecs.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class DimensionComponent implements Component, Pool.Poolable {

    public float width = 1f;
    public float height = 1f;

    @Override
    public void reset() {
        width = 1f;
        height = 1f;
    }
}

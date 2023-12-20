package com.tilemap.game.ecs.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class ZOrderComponent implements Component, Pool.Poolable {

    public int z = 0;

    @Override
    public void reset() {
        z = 0;
    }
}

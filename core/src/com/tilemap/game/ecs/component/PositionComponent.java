package com.tilemap.game.ecs.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class PositionComponent implements Component, Pool.Poolable {

    public float x;
    public float y;
    public float r; // rotation

    @Override
    public void reset() {
        x = 0f;
        y = 0f;
        r = 0f;
    }

    @Override
    public String toString() {
        return "PositionComponent{" +
                "x=" + x +
                ", y=" + y +
                ", r=" + r +
                '}';
    }
}

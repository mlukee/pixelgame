package com.tilemap.game.ecs.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class MovementComponent implements Component, Pool.Poolable {

    public float speedX;
    public float speedY;

    @Override
    public void reset() {
        speedX = 0f;
        speedY = 0f;
    }

    @Override
    public String toString() {
        return "MovementComponent{" +
                "speed=" + speedX +
                ", speed=" + speedY +
                '}';
    }
}

package com.tilemap.game.ecs.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class MovementComponent implements Component, Pool.Poolable {

    public float speed;
    public float rSpeed;    // rotation speed

    @Override
    public void reset() {
        speed = 0f;
        rSpeed = 0f;
    }

    @Override
    public String toString() {
        return "MovementComponent{" +
                "speed=" + speed +
                ", rSpeed=" + rSpeed +
                '}';
    }
}

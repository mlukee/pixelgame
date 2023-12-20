package com.tilemap.game.ecs.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;
import com.tilemap.game.ecs.component.MovementComponent;
import com.tilemap.game.ecs.component.PositionComponent;
import com.tilemap.game.util.Mappers;


public class MovementSystem extends IteratingSystem {

    private static final Family FAMILY = Family.all(
            PositionComponent.class,
            MovementComponent.class
    ).get();

    public MovementSystem() {
        super(FAMILY);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PositionComponent position = Mappers.POSITION.get(entity);
        MovementComponent movement = Mappers.MOVEMENT.get(entity);

        position.r += movement.rSpeed;
//        if (position.r < 0) position.r = position.r + 360;
//        if (position.r > 360) position.r = position.r - 360;
//
//        float tmpR = position.r;
//        position.x += MathUtils.cosDeg(tmpR) * movement.speed * 10;
//        position.y += MathUtils.sinDeg(tmpR) * movement.speed * 10;

            position.x += MathUtils.cosDeg(position.r) * movement.speed * 10;
            position.y += MathUtils.sinDeg(position.r) * movement.speed * 10;
    }
}

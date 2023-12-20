package com.tilemap.game.ecs.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.MathUtils;
import com.tilemap.game.config.GameConfig;
import com.tilemap.game.ecs.component.MovementComponent;
import com.tilemap.game.ecs.component.PlayerComponent;
import com.tilemap.game.util.Mappers;


public class PlayerInputSystem extends IteratingSystem {

    private static final Family FAMILY = Family.all(PlayerComponent.class, MovementComponent.class).get();

    public PlayerInputSystem() {
        super(FAMILY);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        MovementComponent movement = Mappers.MOVEMENT.get(entity);
        movement.rSpeed = 0;

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            movement.rSpeed = MathUtils.clamp(movement.rSpeed + GameConfig.MAX_PLAYER_R_SPEED * deltaTime, -GameConfig.MAX_PLAYER_R_SPEED, GameConfig.MAX_PLAYER_R_SPEED);
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            movement.rSpeed = MathUtils.clamp(movement.rSpeed - GameConfig.MAX_PLAYER_R_SPEED * deltaTime, -GameConfig.MAX_PLAYER_R_SPEED, GameConfig.MAX_PLAYER_R_SPEED);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            movement.speed = MathUtils.clamp(movement.speed + GameConfig.MAX_PLAYER_SPEED * deltaTime, 0, GameConfig.MAX_PLAYER_SPEED);
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            movement.speed = MathUtils.clamp(movement.speed - GameConfig.MAX_PLAYER_SPEED * deltaTime, 0, GameConfig.MAX_PLAYER_SPEED);
        } else { // friction slows down item
            movement.speed -= movement.speed * deltaTime;
        }

    }
}

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

        // Reset movement each frame
        movement.speedX = 0;
        movement.speedY = 0;

        // Set movement distance per frame
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            movement.speedX = -GameConfig.MAX_PLAYER_SPEED;
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            movement.speedX = GameConfig.MAX_PLAYER_SPEED;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            movement.speedY = GameConfig.MAX_PLAYER_SPEED;
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            movement.speedY = -GameConfig.MAX_PLAYER_SPEED;
        }
    }

}

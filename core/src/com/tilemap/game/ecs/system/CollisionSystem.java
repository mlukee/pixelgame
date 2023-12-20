package com.tilemap.game.ecs.system;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.tilemap.game.common.GameManager;
import com.tilemap.game.ecs.component.BoundsComponent;
import com.tilemap.game.ecs.component.MovementComponent;
import com.tilemap.game.ecs.component.ObstacleComponent;
import com.tilemap.game.ecs.component.PlayerComponent;
import com.tilemap.game.ecs.component.WaterComponent;
import com.tilemap.game.ecs.system.passive.SoundSystem;
import com.tilemap.game.ecs.system.passive.TiledSystem;
import com.tilemap.game.util.Mappers;


public class CollisionSystem extends EntitySystem {

    private static final Family FAMILY_MOWER = Family.all(PlayerComponent.class, BoundsComponent.class).get();
    private static final Family FAMILY_OBSTACLE = Family.all(ObstacleComponent.class, BoundsComponent.class).get();
    private static final Family FAMILY_WATER = Family.all(WaterComponent.class, BoundsComponent.class).get();

    private SoundSystem soundSystem;
    private TiledSystem tiledSystem;

    public CollisionSystem() {
    }

    @Override
    public void addedToEngine(Engine engine) {
        soundSystem = engine.getSystem(SoundSystem.class);
        tiledSystem = engine.getSystem(TiledSystem.class);
    }

    @Override
    public void update(float deltaTime) {
        if (GameManager.INSTANCE.isGameOver()) return;

        ImmutableArray<Entity> players = getEngine().getEntitiesFor(FAMILY_MOWER);
        ImmutableArray<Entity> obstacles = getEngine().getEntitiesFor(FAMILY_OBSTACLE);
        ImmutableArray<Entity> waters = getEngine().getEntitiesFor(FAMILY_WATER);

        for (Entity player : players) { //pick collision by tile
            BoundsComponent firstBounds = Mappers.BOUNDS.get(player);
            MovementComponent movement = Mappers.MOVEMENT.get(player);


            if (tiledSystem.collideWith(firstBounds.rectangle)) {
                soundSystem.pick();
            }

            for (Entity water : waters) {
                WaterComponent waterComponent = Mappers.WATER.get(water);
                if (waterComponent.hit) {
                    continue;
                }

                BoundsComponent secondBounds = Mappers.BOUNDS.get(water);
                if (Intersector.overlaps(firstBounds.rectangle, secondBounds.rectangle)) {
                    // obstacleComponent.hit = true;
                    GameManager.INSTANCE.damage();
                    GameManager.INSTANCE.update(deltaTime);
                    soundSystem.obstacle();
                }
            }

            for (Entity obstacle : obstacles) {
                BoundsComponent obstacleBounds = Mappers.BOUNDS.get(obstacle);
                if (Intersector.overlaps(firstBounds.rectangle, obstacleBounds.rectangle)) {
                    // Determine collision direction and stop movement in that direction
                    handleCollisionDirection(firstBounds.rectangle, obstacleBounds.rectangle, movement);
                    break; // Assume one collision at a time for simplicity
                }
            }
        }
    }

    private void handleCollisionDirection(Rectangle player, Rectangle obstacle, MovementComponent movement) {
        // Simple collision direction logic
        // You may need to adjust this based on your game's specifics
        if (player.x < obstacle.x && player.x + player.width > obstacle.x) {
            movement.speedX = Math.min(movement.speedX, 0); // Colliding from left
        } else if (player.x > obstacle.x && player.x < obstacle.x + obstacle.width) {
            movement.speedX = Math.max(movement.speedX, 0); // Colliding from right
        }

        if (player.y < obstacle.y && player.y + player.height > obstacle.y) {
            movement.speedY = Math.min(movement.speedY, 0); // Colliding from bottom
        } else if (player.y > obstacle.y && player.y < obstacle.y + obstacle.height) {
            movement.speedY = Math.max(movement.speedY, 0); // Colliding from top
        }
    }
}

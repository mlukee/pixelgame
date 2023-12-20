package com.tilemap.game.ecs.system;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Intersector;
import com.tilemap.game.common.GameManager;
import com.tilemap.game.ecs.component.BoundsComponent;
import com.tilemap.game.ecs.component.ObstacleComponent;
import com.tilemap.game.ecs.component.PlayerComponent;
import com.tilemap.game.ecs.system.passive.SoundSystem;
import com.tilemap.game.ecs.system.passive.TiledSystem;
import com.tilemap.game.util.Mappers;


public class CollisionSystem extends EntitySystem {

    private static final Family FAMILY_MOWER = Family.all(PlayerComponent.class, BoundsComponent.class).get();
    private static final Family FAMILY_OBSTACLE = Family.all(ObstacleComponent.class, BoundsComponent.class).get();

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

        ImmutableArray<Entity> mowers = getEngine().getEntitiesFor(FAMILY_MOWER);
        ImmutableArray<Entity> obstacles = getEngine().getEntitiesFor(FAMILY_OBSTACLE);

        for (Entity mower : mowers) { //pick collision by tile
            BoundsComponent firstBounds = Mappers.BOUNDS.get(mower);

            if (tiledSystem.collideWith(firstBounds.rectangle)) {
                soundSystem.pick();
            }

            for (Entity obstacle : obstacles) {
                ObstacleComponent obstacleComponent = Mappers.OBSTACLE.get(obstacle);
                if (obstacleComponent.hit) {
                    continue;
                }

                BoundsComponent secondBounds = Mappers.BOUNDS.get(obstacle);
                if (Intersector.overlaps(firstBounds.rectangle, secondBounds.rectangle)) {
                    // obstacleComponent.hit = true;
                    GameManager.INSTANCE.damage();
                    soundSystem.obstacle();
                }
            }
        }
    }
}

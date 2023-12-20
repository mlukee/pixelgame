package com.tilemap.game.ecs.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.tilemap.game.config.GameConfig;
import com.tilemap.game.ecs.component.DimensionComponent;
import com.tilemap.game.ecs.component.MovementComponent;
import com.tilemap.game.ecs.component.PositionComponent;
import com.tilemap.game.ecs.component.WorldWrapComponent;
import com.tilemap.game.util.Mappers;


public class WorldWrapSystem extends IteratingSystem {
    private static final Family FAMILY = Family.all(
            PositionComponent.class,
            DimensionComponent.class,
            MovementComponent.class,
            WorldWrapComponent.class
    ).get();

    public WorldWrapSystem() {
        super(FAMILY, 10);
    }

    // http://www.3dkingdoms.com/weekly/weekly.php?a=2
    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PositionComponent position = Mappers.POSITION.get(entity);
        DimensionComponent dimensionComponent = Mappers.DIMENSION.get(entity);

        // Check and adjust the x-coordinate
        if (position.x >= GameConfig.W_WIDTH - dimensionComponent.width) {
            position.x = GameConfig.W_WIDTH - dimensionComponent.width;
        } else if (position.x < 0) {
            position.x = 0;
        }

        // Check and adjust the y-coordinate
        if (position.y >= GameConfig.W_HEIGHT - dimensionComponent.height) {
            position.y = GameConfig.W_HEIGHT - dimensionComponent.height;
        } else if (position.y < 0) {
            position.y = 0;
        }
    }
}

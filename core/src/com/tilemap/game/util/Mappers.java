package com.tilemap.game.util;

import com.badlogic.ashley.core.ComponentMapper;
import com.tilemap.game.ecs.component.BoundsComponent;
import com.tilemap.game.ecs.component.DimensionComponent;
import com.tilemap.game.ecs.component.MovementComponent;
import com.tilemap.game.ecs.component.ObstacleComponent;
import com.tilemap.game.ecs.component.PositionComponent;
import com.tilemap.game.ecs.component.TextureComponent;
import com.tilemap.game.ecs.component.WaterComponent;
import com.tilemap.game.ecs.component.ZOrderComponent;


public final class Mappers {

    public static final ComponentMapper<BoundsComponent> BOUNDS =
            ComponentMapper.getFor(BoundsComponent.class);

    public static final ComponentMapper<DimensionComponent> DIMENSION =
            ComponentMapper.getFor(DimensionComponent.class);

    public static final ComponentMapper<PositionComponent> POSITION =
            ComponentMapper.getFor(PositionComponent.class);

    public static final ComponentMapper<MovementComponent> MOVEMENT =
            ComponentMapper.getFor(MovementComponent.class);


    public static final ComponentMapper<TextureComponent> TEXTURE =
            ComponentMapper.getFor(TextureComponent.class);

    public static final ComponentMapper<ZOrderComponent> Z_ORDER =
            ComponentMapper.getFor(ZOrderComponent.class);

    public static final ComponentMapper<ObstacleComponent> OBSTACLE =
            ComponentMapper.getFor(ObstacleComponent.class);

    public static final ComponentMapper<WaterComponent> WATER =
            ComponentMapper.getFor(WaterComponent.class);

    private Mappers() {
    }
}

package com.tilemap.game.ecs.system.passive;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.tilemap.game.assets.AssetDescriptors;
import com.tilemap.game.assets.RegionNames;
import com.tilemap.game.config.GameConfig;
import com.tilemap.game.ecs.component.BoundsComponent;
import com.tilemap.game.ecs.component.DimensionComponent;
import com.tilemap.game.ecs.component.MovementComponent;
import com.tilemap.game.ecs.component.PlayerComponent;
import com.tilemap.game.ecs.component.PositionComponent;
import com.tilemap.game.ecs.component.TextureComponent;
import com.tilemap.game.ecs.component.WorldWrapComponent;
import com.tilemap.game.ecs.component.ZOrderComponent;


public class EntityFactorySystem extends EntitySystem {

    private static final int PLAYER_Z_ORDER = 3;

    private final AssetManager assetManager;

    private PooledEngine engine;
    private TextureAtlas gamePlayAtlas;

    public EntityFactorySystem(AssetManager assetManager) {
        this.assetManager = assetManager;
        setProcessing(false);   // passive
        init();
    }

    private void init() {
        gamePlayAtlas = assetManager.get(AssetDescriptors.GAME_PLAY);
    }

    @Override
    public void addedToEngine(Engine engine) {
        this.engine = (PooledEngine) engine;
    }


    public void createPlayer() {
        PositionComponent position = engine.createComponent(PositionComponent.class);
        position.x = (GameConfig.W_WIDTH - GameConfig.PLAYER_HEIGHT) / 2;
        position.r = 90;

        DimensionComponent dimension = engine.createComponent(DimensionComponent.class);
        dimension.width = GameConfig.PLAYER_WIDTH;
        dimension.height = GameConfig.PLAYER_HEIGHT;

        BoundsComponent bounds = engine.createComponent(BoundsComponent.class);
        bounds.rectangle.setPosition(position.x, position.y);
        bounds.rectangle.setSize(dimension.width, dimension.height);

        MovementComponent movement = engine.createComponent(MovementComponent.class);

        PlayerComponent mowerComponent = engine.createComponent(PlayerComponent.class);

        WorldWrapComponent worldWrap = engine.createComponent(WorldWrapComponent.class);

        TextureComponent texture = engine.createComponent(TextureComponent.class);
        texture.region = gamePlayAtlas.findRegion(RegionNames.PLAYER);

        ZOrderComponent zOrder = engine.createComponent(ZOrderComponent.class);
        zOrder.z = PLAYER_Z_ORDER;

        Entity entity = engine.createEntity();
        entity.add(position);
        entity.add(dimension);
        entity.add(bounds);
        entity.add(movement);
        entity.add(mowerComponent);
        entity.add(worldWrap);
        entity.add(texture);
        entity.add(zOrder);

        engine.addEntity(entity);
    }
}

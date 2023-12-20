package com.tilemap.game.ecs.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.assets.AssetManager;
import com.tilemap.game.assets.AssetDescriptors;
import com.tilemap.game.ecs.component.MovementComponent;
import com.tilemap.game.ecs.component.PlayerComponent;
import com.tilemap.game.util.Mappers;


public class PlayerMusicSystem extends IteratingSystem {

    private static final Family FAMILY = Family.all(
            PlayerComponent.class,
            MovementComponent.class
    ).get();

    private final AssetManager assetManager;
    private int state = 0;

    public PlayerMusicSystem(AssetManager assetManager) {
        super(FAMILY);
        this.assetManager = assetManager;
        state = 0;
        assetManager.get(AssetDescriptors.BG_MUSIC).setLooping(true);
        assetManager.get(AssetDescriptors.BG_MUSIC).play();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        MovementComponent movement = Mappers.MOVEMENT.get(entity);
    }
}

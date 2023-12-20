package com.tilemap.game.ecs.system.passive;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;

public class StartUpSystem extends EntitySystem {

    private EntityFactorySystem factory;

    public StartUpSystem() {
        setProcessing(false);
    }

    @Override
    public void addedToEngine(Engine engine) {
        factory = engine.getSystem(EntityFactorySystem.class);
        startUp();
    }

    private void startUp() {
        factory.createPlayer();
    }
}

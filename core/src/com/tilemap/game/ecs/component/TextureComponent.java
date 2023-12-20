package com.tilemap.game.ecs.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Pool;

public class TextureComponent implements Component, Pool.Poolable {

    public TextureRegion region;
    public float opacity = 1.0f;

    @Override
    public void reset() {
        region = null;
    }
}

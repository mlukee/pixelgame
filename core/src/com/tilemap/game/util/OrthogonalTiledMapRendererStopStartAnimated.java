package com.tilemap.game.util;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile;

public class OrthogonalTiledMapRendererStopStartAnimated extends OrthogonalTiledMapRenderer {
    private boolean animate = true;

    public OrthogonalTiledMapRendererStopStartAnimated(TiledMap map) {
        super(map);
    }

    public OrthogonalTiledMapRendererStopStartAnimated(TiledMap map, Batch batch) {
        super(map, batch);
    }

    public OrthogonalTiledMapRendererStopStartAnimated(TiledMap map, float unitScale) {
        super(map, unitScale);
    }

    public OrthogonalTiledMapRendererStopStartAnimated(TiledMap map, float unitScale, Batch batch) {
        super(map, unitScale, batch);
    }

    @Override
    protected void beginRender() {
        if (animate)
            AnimatedTiledMapTile.updateAnimationBaseTime();
        batch.begin();
    }

    public void setAnimate(boolean animate) {
        this.animate = animate;
    }
}

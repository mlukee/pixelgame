package com.tilemap.game.ecs.system.passive;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.tilemap.game.common.GameManager;
import com.tilemap.game.config.GameConfig;
import com.tilemap.game.ecs.component.BoundsComponent;
import com.tilemap.game.ecs.component.ObstacleComponent;
import com.tilemap.game.util.OrthogonalTiledMapRendererStopStartAnimated;

public class TiledSystem extends EntitySystem {

    public static float UNIT_SCALE = 1f;
    private final TiledMap tiledMap;
    private float tileWidth;
    private float tileHeight;
    private int widthInt;
    private int heightInt;
    private float widthMapInPx;
    private float heightMapInPx;
    private TiledMapTileLayer collideTileLayer;
    private MapLayer collideObjectsLayer;
    private final Rectangle tmp;
    private final Rectangle tmpArea;
    private Array<BoundsComponent> debug;

    private OrthogonalTiledMapRendererStopStartAnimated mapRenderer;

    public TiledSystem(TiledMap tiledMap) {
        setProcessing(false);   // passive

        this.tiledMap = tiledMap;
        tmp = new Rectangle();
        tmpArea = new Rectangle();

        if (GameConfig.debug) {
            debug = new Array<BoundsComponent>();
            for (int i = 0; i < 30; i++) debug.add(new BoundsComponent());
        }

        init();
    }

    private void init() {
        mapRenderer = new OrthogonalTiledMapRendererStopStartAnimated(tiledMap, UNIT_SCALE);
        mapRenderer.setAnimate(true);

        TiledMapTileLayer tiledLayer = (TiledMapTileLayer) tiledMap.getLayers().get("background");
        collideTileLayer = (TiledMapTileLayer) tiledMap.getLayers().get("ores");
        collideObjectsLayer = tiledMap.getLayers().get("l_waterObject");
        widthInt = tiledLayer.getWidth();
        heightInt = tiledLayer.getHeight();
        tileWidth = tiledLayer.getTileWidth();
        tileHeight = tiledLayer.getTileHeight();
        widthMapInPx = tileWidth * widthInt;
        heightMapInPx = tileHeight * heightInt;
        GameConfig.W_WIDTH = widthMapInPx;
        GameConfig.W_HEIGHT = heightMapInPx;
    }

    private void addObstacle(MapLayer layer, Engine engine) {
        MapObjects objects = layer.getObjects();
        for (MapObject object : objects) {
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
            Entity entity = engine.createEntity();
            BoundsComponent bc = engine.createComponent(BoundsComponent.class);
            bc.rectangle.set(rectangle);
            ObstacleComponent oc = engine.createComponent(ObstacleComponent.class);
            entity.add(bc).add(oc);
            engine.addEntity(entity);
        }
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        addObstacle(collideObjectsLayer, engine);

        // add debug bounds
        if (GameConfig.debug) {
            for (BoundsComponent bc : debug) {
                Entity entity = engine.createEntity();
                entity.add(bc);
                engine.addEntity(entity);
            }
        }
    }

    public void renderTiledView(OrthographicCamera camera, float x, float y) {
        camera.position.x = MathUtils.clamp(x, camera.viewportWidth / 2, widthMapInPx - camera.viewportWidth / 2);
        camera.position.y = MathUtils.clamp(y, camera.viewportHeight / 2, heightMapInPx - camera.viewportHeight / 2);
        // camera.position.x = MathUtils.lerp(camera.position.x,MathUtils.clamp(x, camera.viewportWidth / 2, widthMapInPx - camera.viewportWidth / 2),1f);
        // camera.position.y = MathUtils.lerp(camera.position.y, MathUtils.clamp(y, camera.viewportHeight / 2, heightMapInPx - camera.viewportHeight / 2),1f);
        // if (Math.abs(x1-camera.position.x)>0.1)  Gdx.app.log("lerp ", x1+" "+(x1-camera.position.x));
        // if (Math.abs(y1-camera.position.y)>0.1)  Gdx.app.log("lerp y", y1+" "+(y1-camera.position.y));
        camera.update();
        mapRenderer.setView(camera);
        mapRenderer.render();
    }

    public boolean collideTile(float x, float y) {
        int ix = (int) (x / tileWidth);
        int iy = (int) (y / tileHeight);
        if (collideTileLayer.getCell(ix, iy) != null) {
            collideTileLayer.setCell(ix, iy, null);
            return true;
        }
        return false;
    }

    public boolean collideWith(Rectangle rectangle) {
        tmpArea.set(rectangle.x - tileWidth, rectangle.y - tileHeight, rectangle.width + tileWidth * 2, rectangle.height + tileHeight * 2);
        int i = 0;  // init tiled checked
        if (GameConfig.debug) { // area that will be searched
            for (BoundsComponent bc : debug) {
                bc.rectangle.set(0, 0, tileWidth, tileHeight);
            }
            debug.get(i++).rectangle.set(tmpArea);  // show area
        }
        boolean result = false;
        float dx = 0;
        float dy = 0;
        int iy = MathUtils.clamp((int) (tmpArea.y / tileHeight) + 1, 0, heightInt);
        int ix;
        do {
            ix = MathUtils.clamp((int) (tmpArea.x / tileWidth), 0, widthInt);
            dx = 0;
            do {
                if (GameConfig.debug) {
                    tmp.set(ix * tileWidth, iy * tileHeight, tileWidth, tileHeight);
                    debug.get(i++).rectangle.set(tmp);
                }
                if (collideTileLayer.getCell(ix, iy) != null) {
                    tmp.set(ix * tileWidth, iy * tileHeight, tileWidth, tileHeight);
                    if (tmp.overlaps(rectangle)) {
                        collideTileLayer.setCell(ix, iy, null);
                        GameManager.INSTANCE.incResult();
                        result = true;
                    }
                }
                ix++;
                dx += tileWidth;
            } while (dx < tmpArea.width);
            iy++;
            dy += tileHeight;
        } while (dy < tmpArea.height);
        return result;
    }
}

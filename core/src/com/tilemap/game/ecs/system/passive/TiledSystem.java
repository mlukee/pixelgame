package com.tilemap.game.ecs.system.passive;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
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
import com.tilemap.game.ecs.component.MovementComponent;
import com.tilemap.game.ecs.component.ObstacleComponent;
import com.tilemap.game.ecs.component.PlayerComponent;
import com.tilemap.game.ecs.component.TextureComponent;
import com.tilemap.game.ecs.component.WaterComponent;
import com.tilemap.game.util.Mappers;
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
    private TiledMapTileLayer hideInGrassLayer;
    private MapLayer collideObjectsLayer;
    private MapLayer collideHousesAndTreesLayer;
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
        hideInGrassLayer = (TiledMapTileLayer) tiledMap.getLayers().get("grass");
        collideObjectsLayer = tiledMap.getLayers().get("l_waterObject");
        collideHousesAndTreesLayer = tiledMap.getLayers().get("l_houses_trees");
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

    private void addWater(MapLayer layer, Engine engine) {
        MapObjects objects = layer.getObjects();
        for (MapObject object : objects) {
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
            Entity entity = engine.createEntity();
            BoundsComponent bc = engine.createComponent(BoundsComponent.class);
            bc.rectangle.set(rectangle);
            WaterComponent oc = engine.createComponent(WaterComponent.class);
            entity.add(bc).add(oc);
            engine.addEntity(entity);
        }
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        addWater(collideObjectsLayer, engine);
        addObstacle(collideHousesAndTreesLayer, engine);

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

    public boolean collideWith(Rectangle rectangle) {
        tmpArea.set(rectangle.x - tileWidth, rectangle.y - tileHeight, rectangle.width + tileWidth, rectangle.height + tileHeight);
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
                if (hideInGrassLayer.getCell(ix, iy) != null) {
                    tmp.set(ix * tileWidth, iy * tileHeight, tileWidth, tileHeight);
                    if (tmp.overlaps(rectangle)) {
                        hideAndSlowPlayer();
                    }
                }
//                } else {
//                    Entity player = getEngine().getEntitiesFor(Family.all(PlayerComponent.class, MovementComponent.class).get()).first();
//
//                    PlayerComponent playerComp = Mappers.PLAYER.get(player);
//                    TextureComponent texture = Mappers.TEXTURE.get(player);
//                    texture.opacity = 1f;
//                    playerComp.isInGrass = false;
//                }
                ix++;
                dx += tileWidth;
            } while (dx < tmpArea.width);
            iy++;
            dy += tileHeight;
        } while (dy < tmpArea.height);

        if (!result && !isInGrassArea(rectangle)) {
            resetPlayerFromGrass();
        }

        return result;
    }

    private boolean isInGrassArea(Rectangle rectangle) {
        int startX = MathUtils.floor(rectangle.x / tileWidth);
        int startY = MathUtils.floor(rectangle.y / tileHeight);
        int endX = MathUtils.ceil((rectangle.x + rectangle.width) / tileWidth);
        int endY = MathUtils.ceil((rectangle.y + rectangle.height) / tileHeight);

        for (int x = startX; x < endX; x++) {
            for (int y = startY; y < endY; y++) {
                if (x >= 0 && y >= 0 && x < widthInt && y < heightInt) {
                    if (hideInGrassLayer.getCell(x, y) != null) {
                        return true; // The player is still in a grass area
                    }
                }
            }
        }
        return false; // The player is not in a grass area
    }

    private void resetPlayerFromGrass() {
        Entity player = getEngine().getEntitiesFor(Family.all(PlayerComponent.class, MovementComponent.class, TextureComponent.class).get()).first();
        if (player != null) {
            PlayerComponent playerComp = Mappers.PLAYER.get(player);
            MovementComponent movement = Mappers.MOVEMENT.get(player);
            TextureComponent texture = Mappers.TEXTURE.get(player);

            if (playerComp.isInGrass) {
                // Reset the flag
                playerComp.isInGrass = false;

                // Reset movement speed
                movement.speedX = GameConfig.MAX_PLAYER_SPEED; // or another default speed value
                movement.speedY = GameConfig.MAX_PLAYER_SPEED; // or another default speed value

                // Reset opacity
                texture.opacity = 1.0f; // Fully opaque
            }
        }
    }



    private void hideAndSlowPlayer() {
        Entity player = getEngine().getEntitiesFor(Family.all(PlayerComponent.class, MovementComponent.class).get()).first();
        TextureComponent texture = Mappers.TEXTURE.get(player);

        PlayerComponent playerComp = Mappers.PLAYER.get(player);

        if (!playerComp.isInGrass) {
            playerComp.isInGrass = true;
            texture.opacity = 0.7f;
        }
    }
}

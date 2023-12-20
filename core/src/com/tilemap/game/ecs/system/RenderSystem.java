package com.tilemap.game.ecs.system;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.SortedIteratingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.tilemap.game.config.GameConfig;
import com.tilemap.game.ecs.component.DimensionComponent;
import com.tilemap.game.ecs.component.PositionComponent;
import com.tilemap.game.ecs.component.TextureComponent;
import com.tilemap.game.ecs.component.ZOrderComponent;
import com.tilemap.game.ecs.system.passive.TiledSystem;
import com.tilemap.game.util.Mappers;
import com.tilemap.game.util.ZOrderComparator;

public class RenderSystem extends SortedIteratingSystem {

    public static int PRIORITY = 2;
    private static final Family FAMILY = Family.all(
            PositionComponent.class,
            DimensionComponent.class,
            TextureComponent.class,
            ZOrderComponent.class
    ).get();

    private final SpriteBatch batch;
    private final Viewport viewport;
    private TiledSystem tiledSystem;

    public RenderSystem(SpriteBatch batch, Viewport viewport) {
        super(FAMILY, ZOrderComparator.INSTANCE, PRIORITY);
        this.batch = batch;
        this.viewport = viewport;
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        tiledSystem = engine.getSystem(TiledSystem.class);
    }

    @Override
    public void update(float deltaTime) {
        viewport.apply();
        tiledSystem.renderTiledView((OrthographicCamera) viewport.getCamera(), GameConfig.POSITION_X, GameConfig.POSITION_Y);
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();

        super.update(deltaTime);

        batch.end();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PositionComponent position = Mappers.POSITION.get(entity);
        DimensionComponent dimension = Mappers.DIMENSION.get(entity);
        TextureComponent texture = Mappers.TEXTURE.get(entity);

        batch.draw(texture.region,
                position.x, position.y,
                dimension.width / 2, dimension.height / 2,
                dimension.width, dimension.height,
                1, 1,
                0);
    }
}

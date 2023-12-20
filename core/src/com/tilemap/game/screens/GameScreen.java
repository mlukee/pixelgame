package com.tilemap.game.screens;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.tilemap.game.Tilemap;
import com.tilemap.game.assets.AssetPaths;
import com.tilemap.game.common.GameManager;
import com.tilemap.game.config.GameConfig;
import com.tilemap.game.ecs.system.BoundsSystem;
import com.tilemap.game.ecs.system.CameraMovementSystem;
import com.tilemap.game.ecs.system.CleanUpSystem;
import com.tilemap.game.ecs.system.CollisionSystem;
import com.tilemap.game.ecs.system.HudRenderSystem;
import com.tilemap.game.ecs.system.MovementSystem;
import com.tilemap.game.ecs.system.PlayerInputSystem;
import com.tilemap.game.ecs.system.PlayerMusicSystem;
import com.tilemap.game.ecs.system.RenderSystem;
import com.tilemap.game.ecs.system.WorldWrapSystem;
import com.tilemap.game.ecs.system.debug.DebugCameraSystem;
import com.tilemap.game.ecs.system.debug.DebugGridRenderSystem;
import com.tilemap.game.ecs.system.debug.DebugInputSystem;
import com.tilemap.game.ecs.system.debug.DebugRenderSystem;
import com.tilemap.game.ecs.system.passive.EntityFactorySystem;
import com.tilemap.game.ecs.system.passive.SoundSystem;
import com.tilemap.game.ecs.system.passive.StartUpSystem;
import com.tilemap.game.ecs.system.passive.TiledSystem;

/**
 * Artwork from https://goodstuffnononsense.com/about/
 * https://goodstuffnononsense.com/hand-drawn-icons/space-icons/
 */

public class GameScreen extends ScreenAdapter {

    private static final Logger log = new Logger(GameScreen.class.getSimpleName(), Logger.DEBUG);

    private final AssetManager assetManager;
    private final SpriteBatch batch;
    private final Tilemap game;

    private OrthographicCamera camera;
    private Viewport viewport;
    private Viewport hudViewport;
    private ShapeRenderer renderer;
    private PooledEngine engine;
    private BitmapFont font;
    private TiledMap map;

    //OrthoCachedTiledMapRenderer mapRenderer;
    public GameScreen(Tilemap game) {
        this.game = game;
        assetManager = game.getAssetManager();
        batch = game.getBatch();
        //https://gamedev.stackexchange.com/questions/127733/libgdx-how-to-handle-touchpad-input
    /*
    Stage stage = new Stage();
    Table table = new Table(skin); // You can skip table and add it directly to the stage as well.
    Touchpad touchpad = new Touchpad(GameConfig.TOUCH_RADIUS, skin);
    Gdx.input.setInputProcessor(stage);
    touchpad.addListener(new ChangeListener() {
      @Override
      public void changed(ChangeEvent event, Actor actor) {
        // This is run when anything is changed on this actor.
        float deltaX = ((Touchpad) actor).getKnobPercentX();
        float deltaY = ((Touchpad) actor).getKnobPercentY();
        //...
      }
    });
    */
    }

    @Override
    public void show() {
        map = assetManager.get(AssetPaths.TILES); //Rethink add with manager?

        camera = new OrthographicCamera();
        viewport = new FitViewport(GameConfig.WIDTH, GameConfig.HEIGHT, camera);
        hudViewport = new FitViewport(GameConfig.WIDTH, GameConfig.HEIGHT);
        renderer = new ShapeRenderer();
        engine = new PooledEngine();
        //passive systems
        engine.addSystem(new EntityFactorySystem(assetManager));
        engine.addSystem(new SoundSystem(assetManager));
        engine.addSystem(new TiledSystem(map));
        if (GameConfig.debug) {
            engine.addSystem(new DebugGridRenderSystem(viewport, renderer));
            engine.addSystem(new DebugCameraSystem(
                    GameConfig.WIDTH / 2, GameConfig.HEIGHT / 2, //center
                    camera
            ));
            engine.addSystem(new DebugRenderSystem(viewport, renderer));
            engine.addSystem(new DebugInputSystem());
        }
        engine.addSystem(new BoundsSystem());
        //engine.addSystem(new PlayerControlSystem());
        engine.addSystem(new WorldWrapSystem());
        engine.addSystem(new MovementSystem());
        engine.addSystem(new PlayerInputSystem());
        engine.addSystem(new RenderSystem(batch, viewport));
        engine.addSystem(new StartUpSystem());
        engine.addSystem(new CleanUpSystem());
        engine.addSystem(new CollisionSystem());
        engine.addSystem(new CameraMovementSystem());
        engine.addSystem(new PlayerMusicSystem(assetManager));

        engine.addSystem(new HudRenderSystem(batch, hudViewport, font));
        GameManager.INSTANCE.resetResult();

        //mapRenderer = new OrthoCachedTiledMapRenderer(map);
    }

    @Override
    public void render(float delta) {
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) Gdx.app.exit();
//    if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) GameManager.INSTANCE.resetResult();


        ScreenUtils.clear(0f, 0f, 0f, 0f);
        engine.update(delta);

        if (GameManager.INSTANCE.isGameOver()) {
            //Gdx.app.exit();
        }
        //     game.setScreen(new MenuScreen(game));
        // }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        hudViewport.update(width, height, true);
    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        renderer.dispose();
        engine.removeAllEntities();
    }

    public void printEngine() {
        ImmutableArray<EntitySystem> systems = engine.getSystems();
        for (EntitySystem system : systems) {
            System.out.println(system.getClass().getSimpleName());
        }
    }
}

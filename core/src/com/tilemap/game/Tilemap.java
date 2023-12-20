package com.tilemap.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.ScreenUtils;
import com.tilemap.game.assets.AssetDescriptors;
import com.tilemap.game.screens.GameScreen;
import com.tilemap.game.util.ViewportUtils;

public class Tilemap extends Game {

	private AssetManager assetManager;
	private SpriteBatch batch;
	@Override
	public void create () {
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		ViewportUtils.DEFAULT_CELL_SIZE = 32;

		assetManager = new AssetManager();
		assetManager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
		assetManager.getLogger().setLevel(Logger.DEBUG);

		batch = new SpriteBatch();
		assetManager.load(AssetDescriptors.BG_MUSIC);
		assetManager.load(AssetDescriptors.GAME_PLAY);
		assetManager.load(AssetDescriptors.PICK_SOUND);
		assetManager.load(AssetDescriptors.HIT_SOUND);
		assetManager.load(AssetDescriptors.TILES);
		assetManager.finishLoading();

		setScreen(new GameScreen(this));
	}


	@Override
	public void dispose () {
		batch.dispose();
		assetManager.dispose();
	}

	public AssetManager getAssetManager() {
		return assetManager;
	}

	public SpriteBatch getBatch() {
		return batch;
	}
}

package com.tilemap.game.assets;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;

public class AssetDescriptors {

    public static final AssetDescriptor<TextureAtlas> GAME_PLAY =
            new AssetDescriptor<TextureAtlas>(AssetPaths.GAME_PLAY, TextureAtlas.class);

    public static final AssetDescriptor<Sound> HIT_SOUND =
            new AssetDescriptor<Sound>(AssetPaths.HIT_SOUND, Sound.class);

    public static final AssetDescriptor<Sound> PICK_SOUND =
            new AssetDescriptor<Sound>(AssetPaths.PICK_SOUND, Sound.class);

    public static final AssetDescriptor<Music> BG_MUSIC =
            new AssetDescriptor<Music>(AssetPaths.BG_MUSIC, Music.class);

    public static final AssetDescriptor<TiledMap> TILES =
            new AssetDescriptor<TiledMap>(AssetPaths.TILES, TiledMap.class);

    private AssetDescriptors() {
    }
}

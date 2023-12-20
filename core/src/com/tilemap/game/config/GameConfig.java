package com.tilemap.game.config;

public class GameConfig {

    public static final float PLAYER_WIDTH = 28f;
    public static final float PLAYER_HEIGHT = 28f;
    public static final float MAX_PLAYER_R_SPEED = 190; //1s 360
    public static final float MAX_PLAYER_SPEED = 0.2f;
    public static final float TOUCH_RADIUS = 20;

    public static float WIDTH = 32 * 14;
    public static float HEIGHT = 32 * 14f;
    public static float W_WIDTH = 32 * 20;
    public static float W_HEIGHT = 32 * 20;

    public static float POSITION_X = (W_WIDTH - PLAYER_WIDTH) / 2;
    public static float POSITION_Y = 0;
    
    public static boolean debug = false;

    private GameConfig() {
    }
}

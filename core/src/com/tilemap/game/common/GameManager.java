package com.tilemap.game.common;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class GameManager {

    public static final GameManager INSTANCE = new GameManager();

    private final Preferences PREFS;

    private static final String RESULT_BEST = "BEST_RESULT";

    private int result;
    private int health;
    private float lastObstacleTime;

    private GameManager() {
        PREFS = Gdx.app.getPreferences(GameManager.class.getSimpleName());
    }

    public int getHealth() {
        return health;
    }

    public void damage() {
        if(lastObstacleTime > 0) return;

        health--;
        lastObstacleTime = 0.2f;

        if (health == 0) {
            if (result > getBestResult()) setBestResult(result);
        }
    }

    public int getResult() {
        return result;
    }

    public void incResult() {
        result++;
    }

    public void resetResult() {
        result = 0;
        health = 100;
    }

    public boolean isGameOver() {
        return getHealth() <= 0;
    }

    public int getBestResult() {
        return PREFS.getInteger(RESULT_BEST, 0);
    }

    public void setBestResult(int result) {
        PREFS.putInteger(RESULT_BEST, result);
        PREFS.flush();
    }

    public void update(float deltaTime) {
        lastObstacleTime -= deltaTime;
    }
}

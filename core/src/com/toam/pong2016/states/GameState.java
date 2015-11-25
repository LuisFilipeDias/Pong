package com.toam.pong2016.states;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.toam.pong2016.handlers.GameStateManager;
import com.toam.pong2016.main.Pong2016;

/**
 * Created by luisfilipedias on 15-11-2015.
 */
public abstract class GameState {

    protected GameStateManager gsm;
    protected Pong2016 game;

    protected SpriteBatch sb ;

    // main camera;
    protected OrthographicCamera cam;
    protected OrthographicCamera hudCam;

    protected GameState(GameStateManager gsm) {
        this.gsm = gsm;
        game = gsm.game();
        sb = game.getSpriteBath();
        cam = game.getCamera();
        hudCam = game.getHUDCamera();
    }

    public abstract void create();
    public abstract void handleInput();
    public abstract void update(float dt);
    public abstract void render ();
    public abstract void dispose ();

}

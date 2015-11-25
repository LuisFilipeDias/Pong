package com.toam.pong2016.main;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.toam.pong2016.handlers.GameStateManager;

public class Pong2016 extends ApplicationAdapter {


    private float accum;
    private SpriteBatch sb;
    private OrthographicCamera cam;
    private OrthographicCamera hudCam;

    public SpriteBatch getSpriteBath() {
        return sb;
    }

    public OrthographicCamera getCamera() {
        return cam;
    }

    public OrthographicCamera getHUDCamera() {
        return hudCam;
    }

    public static final float STEP = 1 / 60f;
    public static final float PPM = 100;
    public static final String TITLE = "Pong 2016";
    public static final int V_WIDTH = 640;
    public static final int V_HEIGHT = 480;
    public static final int SCALE = 2;

    private GameStateManager gsm;

    @Override
    public void create() {

        sb = new SpriteBatch();
        cam = new OrthographicCamera();
        cam.setToOrtho(false, V_WIDTH, V_HEIGHT);
        hudCam = new OrthographicCamera();
        hudCam.setToOrtho(false, V_WIDTH, V_HEIGHT);

        gsm = new GameStateManager(this);

        gsm.create();

    }

    @Override
    public void render() {
        // camera.update();
        // Step the physics simulation forward at a rate of 60hz
        accum += Gdx.graphics.getDeltaTime();

        while(accum >= STEP) {
            accum -= STEP;
            gsm.update(STEP);
            gsm.render();
        }
    }

    @Override
    public void dispose() {
    }

}

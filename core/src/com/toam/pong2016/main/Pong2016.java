package com.toam.pong2016.main;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.toam.pong2016.handlers.Common;
import com.toam.pong2016.handlers.GameStateManager;

public class Pong2016 extends ApplicationAdapter{

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

    private GameStateManager gsm;

    @Override
    public void create() {

        sb = new SpriteBatch();
        cam = new OrthographicCamera();
        cam.setToOrtho(false, Common.V_WIDTH/Common.PPM, Common.V_HEIGHT/Common.PPM);
        hudCam = new OrthographicCamera();
        hudCam.setToOrtho(false, Common.V_WIDTH/Common.PPM, Common.V_HEIGHT/ Common.PPM);

        gsm = new GameStateManager(this);

        gsm.create();

    }

    @Override
    public void render() {
        // camera.update();
        // Step the physics simulation forward at a rate of 60hz
        accum += Gdx.graphics.getDeltaTime();

        while(accum >= Common.STEP) {
            accum -= Common.STEP;
            gsm.update(Common.STEP);
            gsm.render();
        }
    }

    @Override
    public void dispose() {
    }

}

package com.toam.pong2016.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.toam.pong2016.handlers.GameStateManager;
import com.toam.pong2016.handlers.Common;

/**
 * Created by luisfilipedias on 15-11-2015.
 */
public class Play extends GameState implements InputProcessor{

    Sprite s_ball, s_paddle;
    Texture img_ball, img_paddle;
    World world;
    Body b_ball, b_paddle;
    Body b_EdgeScreen;
    Box2DDebugRenderer debugRenderer;

    Matrix4 debugMatrix;
    BitmapFont font;
    boolean go_right = false, go_left = false;

    float speed_paddle = 0;
    float torque = 0.0f;
    boolean drawSprite = true;

    float screenWidth;
    float screenHeight;

    public Play(GameStateManager gsm) {
        super(gsm);
    }


    public void create() {

        // width and height dimensions are using body dimensions instead of window dimensions
        screenWidth = Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();

        createSprites();

        world = new World(new Vector2(0, Common.GRAVITY),true);
        debugRenderer = new Box2DDebugRenderer();

        createBodies();

        Gdx.input.setInputProcessor(this);

        font = new BitmapFont();
        font.setColor(Color.BLACK);

        setWorldContactListener();

        b_ball.setLinearVelocity(0, -5);
        b_paddle.setGravityScale(0);

        // set up box2d cam
       // b2dCam = new OrthographicCamera();
       // b2dCam.setToOrtho(false, Pong2016.V_WIDTH / 100, Pong2016.V_HEIGHT / 100);
    }

    public void handleInput() {

    }

    public void update(float dt) {
        world.step(dt, 6, 2);
    }

    public void render() {
        sb.setProjectionMatrix(cam.combined);

        /* clear screen */
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        b_ball.applyTorque(torque, true);
        s_ball.setPosition((b_ball.getPosition().x * Common.PPM) - s_ball.getWidth() / 2,
                (b_ball.getPosition().y * Common.PPM) - s_ball.getHeight() / 2);
        s_paddle.setPosition((b_paddle.getPosition().x * Common.PPM) - s_paddle.getWidth()/2,
                (b_paddle.getPosition().y * Common.PPM) - s_paddle.getHeight()/2);
        s_ball.setRotation((float) Math.toDegrees(b_ball.getAngle()));

        /*debugMatrix = sb.getProjectionMatrix().cpy().scale(Common.PPM,
                Common.PPM, 0);
*/
        sb.begin();

        if(drawSprite) {
            sb.draw(s_paddle, s_paddle.getX()/Common.PPM, s_paddle.getY()/Common.PPM, s_paddle.getOriginX(), s_paddle.getOriginY(),
                    s_paddle.getWidth()/Common.PPM, s_paddle.getHeight()/Common.PPM, s_paddle.getScaleX(), s_paddle.getScaleY(), s_paddle.getRotation());
            sb.draw(s_ball, s_ball.getX()/Common.PPM, s_ball.getY()/Common.PPM, s_ball.getOriginX(), s_ball.getOriginY(),
                    s_ball.getWidth()/Common.PPM, s_ball.getHeight()/Common.PPM, s_ball.getScaleX(), s_ball.getScaleY(), s_ball.getRotation());
        }

        sb.end();

        b_paddle.setLinearVelocity(speed_paddle, 0f);

        /* draw box2d world */
        debugRenderer.render(world, cam.combined);
    }

    public void dispose() {

    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.RIGHT) {
            if (go_left)
                speed_paddle += Common.SPEED_PADDLE/Common.PPM * 2;
            else
                speed_paddle += Common.SPEED_PADDLE/Common.PPM;
            go_right= true;
            go_left = false;
        }
        if(keycode == Input.Keys.LEFT) {
            if (go_right)
                speed_paddle -= Common.SPEED_PADDLE/Common.PPM * 2;
            else
                speed_paddle -= Common.SPEED_PADDLE/Common.PPM;
            go_right= false;
            go_left = true;
        }
        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    private void createSprites() {

        img_ball = new Texture("/home/luisfilipedias/Android/Apps/Pong/android/assets/ball.png");
        img_paddle = new Texture("/home/luisfilipedias/Android/Apps/Pong/android/assets/paddle.png");

        s_ball = new Sprite(img_ball);
        s_paddle = new Sprite(img_paddle);

        s_ball.setPosition((screenWidth/2)/Common.PPM, (screenHeight/2)/Common.PPM);
        s_paddle.setPosition((screenWidth/2)/Common.PPM, (screenHeight/2)/Common.PPM - Common.EDGE_OFFSET/Common.PPM);
    }

    private void createBodies() {
        BodyDef s_ballDef = new BodyDef();
        s_ballDef.type = BodyDef.BodyType.DynamicBody;
        s_ballDef.position.set(s_ball.getX(),
                s_ball.getY());

        BodyDef s_paddleDef = new BodyDef();
        s_paddleDef.type = BodyDef.BodyType.KinematicBody;
        s_paddleDef.position.set(s_paddle.getX(),
                s_paddle.getY());

/*        BodyDef s_edgeDef = new BodyDef();
        s_edgeDef.type = BodyDef.BodyType.StaticBody;
        s_edgeDef.position.set(0,0);
*/
        b_ball = world.createBody(s_ballDef);
        b_paddle = world.createBody(s_paddleDef);
    //    b_EdgeScreen = world.createBody(s_edgeDef);

        PolygonShape ballShape = new PolygonShape();
        ballShape.setAsBox((s_ball.getWidth()/2) / Common.PPM, (s_ball.getHeight()/2) / Common.PPM);

        PolygonShape paddleShape = new PolygonShape();
        paddleShape.setAsBox((s_paddle.getWidth() / 2) / Common.PPM, (s_paddle.getHeight() / 2) / Common.PPM);

  //      EdgeShape edgeShape = new EdgeShape();
//        edgeShape.set(-w/2,-h/2,w/2,-h/2);

        FixtureDef fixtureBallDef = new FixtureDef();
        fixtureBallDef.shape = ballShape;
        fixtureBallDef.density = 0.1f;
        fixtureBallDef.restitution = 1f;

        FixtureDef fixturePaddleDef = new FixtureDef();
        fixturePaddleDef.shape = paddleShape;
        fixturePaddleDef.density = 0.1f;
        fixturePaddleDef.restitution = 0f;
/*
        FixtureDef fixtureEdgeDef = new FixtureDef();
        fixtureEdgeDef.shape = edgeShape;
*/
        b_ball.createFixture(fixtureBallDef);
        b_paddle.createFixture(fixturePaddleDef);
    //    b_EdgeScreen.createFixture(fixtureEdgeDef);

        ballShape.dispose();
        paddleShape.dispose();
     //   edgeShape.dispose();
    }

    private void setWorldContactListener() {world.setContactListener(new ContactListener() {
        @Override
        public void beginContact(Contact contact) {
            // Check to see if the collision is between the second sprite and the bottom of the screen
            // If so apply a random amount of upward force to both objects... just because
            if ((contact.getFixtureA().getBody() == b_paddle &&
                    contact.getFixtureB().getBody() == b_ball)
                    ||
                    (contact.getFixtureA().getBody() == b_ball &&
                            contact.getFixtureB().getBody() == b_paddle)) {

//                    b_ball.applyForceToCenter(0, 1, true);
            }

            if ((contact.getFixtureA().getBody() == b_EdgeScreen &&
                    contact.getFixtureB().getBody() == b_ball)
                    ||
                    (contact.getFixtureA().getBody() == b_ball &&
                            contact.getFixtureB().getBody() == b_EdgeScreen)) {

                font.draw(sb,
                        "YOU LOST! HAHA",
                        -screenWidth / 2,
                        screenHeight / 2);
            }
        }

        @Override
        public void endContact(Contact contact) {
        }

        @Override
        public void preSolve(Contact contact, Manifold oldManifold) {
        }

        @Override
        public void postSolve(Contact contact, ContactImpulse impulse) {
        }
    });

    }

}

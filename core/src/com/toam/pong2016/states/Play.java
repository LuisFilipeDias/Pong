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

/**
 * Created by luisfilipedias on 15-11-2015.
 */
public class Play extends GameState implements InputProcessor{

//    final float PIXELS_TO_METERS = 100f;
    final float PIXELS_TO_METERS = 1f;
    final float EDGE_OFFSET = 250;
    final float GRAVITY = -30f;

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


    public Play(GameStateManager gsm) {
        super(gsm);
    }

    public void create() {

        // width and height dimensions are using body dimensions instead of window dimensions
        float w = Gdx.graphics.getWidth()/PIXELS_TO_METERS;

        // Set the height to just 150 pixels above the bottom of the screen so we can see the edge in the
        // debug renderer
        float h = Gdx.graphics.getHeight()/PIXELS_TO_METERS;// - EDGE_OFFSET/PIXELS_TO_METERS;

        img_ball = new Texture("/home/luisfilipedias/Android/Apps/Pong/android/assets/ball.png");
        img_paddle = new Texture("/home/luisfilipedias/Android/Apps/Pong/android/assets/paddle.png");

        s_ball = new Sprite(img_ball);
        s_paddle = new Sprite(img_paddle);

        //s_ball.setPosition(-s_ball.getWidth()/2,-s_ball.getHeight()/2);
        s_ball.setPosition(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
        //s_paddle.setPosition( -s_paddle.getWidth()/2, -Gdx.graphics.getHeight()/2 - s_paddle.getHeight()/2);
        s_paddle.setPosition( Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2 - EDGE_OFFSET);

        world = new World(new Vector2(0, GRAVITY),true);

        BodyDef s_ballDef = new BodyDef();
        s_ballDef.type = BodyDef.BodyType.DynamicBody;
        s_ballDef.position.set((s_ball.getX() + s_ball.getWidth()/2) /
                        PIXELS_TO_METERS,
                (s_ball.getY() + s_ball.getHeight()/2) / PIXELS_TO_METERS);

        BodyDef s_paddleDef = new BodyDef();
        s_paddleDef.type = BodyDef.BodyType.KinematicBody;
        s_paddleDef.position.set((s_paddle.getX() + s_paddle.getWidth() / 2) /
                        PIXELS_TO_METERS,
                (s_paddle.getY() + s_paddle.getHeight() / 2 + EDGE_OFFSET / 2) / PIXELS_TO_METERS);

        BodyDef s_edgeDef = new BodyDef();
        s_edgeDef.type = BodyDef.BodyType.StaticBody;
        s_edgeDef.position.set(0,0);

        b_ball = world.createBody(s_ballDef);
        b_paddle = world.createBody(s_paddleDef);
        b_EdgeScreen = world.createBody(s_edgeDef);

        PolygonShape ballShape = new PolygonShape();
        ballShape.setAsBox(s_ball.getWidth() / 2 / PIXELS_TO_METERS, s_ball.getHeight()
                / 2 / PIXELS_TO_METERS);

        PolygonShape paddleShape = new PolygonShape();
        paddleShape.setAsBox(s_paddle.getWidth() / 2 / PIXELS_TO_METERS, s_paddle.getHeight()
                / 2 / PIXELS_TO_METERS);

        EdgeShape edgeShape = new EdgeShape();
        edgeShape.set(-w/2,-h/2,w/2,-h/2);

        FixtureDef fixtureBallDef = new FixtureDef();
        fixtureBallDef.shape = ballShape;
        fixtureBallDef.density = 0.1f;
        fixtureBallDef.restitution = 1f;

        FixtureDef fixturePaddleDef = new FixtureDef();
        fixturePaddleDef.shape = paddleShape;
        fixturePaddleDef.density = 0.1f;
        fixturePaddleDef.restitution = 0f;

        FixtureDef fixtureEdgeDef = new FixtureDef();
        fixtureEdgeDef.shape = edgeShape;

        b_ball.createFixture(fixtureBallDef);
        b_paddle.createFixture(fixturePaddleDef);
        b_EdgeScreen.createFixture(fixtureEdgeDef);

        ballShape.dispose();
        paddleShape.dispose();
        edgeShape.dispose();

        Gdx.input.setInputProcessor(this);

        debugRenderer = new Box2DDebugRenderer();
        font = new BitmapFont();
        font.setColor(Color.BLACK);

        world.setContactListener(new ContactListener() {
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
                            -Gdx.graphics.getWidth() / 2,
                            Gdx.graphics.getHeight() / 2);
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
        b_ball.setLinearVelocity(0, -100);
        b_paddle.setGravityScale(0);
    }

    public void handleInput() {

    }

    public void update(float dt) {
        world.step(dt, 6, 2);
    }

    public void render() {
        sb.setProjectionMatrix(cam.combined);

        b_ball.applyTorque(torque, true);

        s_ball.setPosition((b_ball.getPosition().x * PIXELS_TO_METERS) - s_ball.
                        getWidth() / 2,
                (b_ball.getPosition().y * PIXELS_TO_METERS) - s_ball.getHeight() / 2);

        s_paddle.setPosition((b_paddle.getPosition().x * PIXELS_TO_METERS) - s_paddle.
                        getWidth() / 2,
                (b_paddle.getPosition().y * PIXELS_TO_METERS) - s_paddle.getHeight() / 2);

        s_ball.setRotation((float) Math.toDegrees(b_ball.getAngle()));

        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        debugMatrix = sb.getProjectionMatrix().cpy().scale(PIXELS_TO_METERS,
                PIXELS_TO_METERS, 0);
        sb.begin();

        if(drawSprite) {
            sb.draw(s_paddle, s_paddle.getX(), s_paddle.getY(), s_paddle.getOriginX(),
                    s_paddle.getOriginY(),
                    s_paddle.getWidth(), s_paddle.getHeight(), s_paddle.getScaleX(), s_paddle.
                            getScaleY(), s_paddle.getRotation());
            sb.draw(s_ball, s_ball.getX(), s_ball.getY(), s_ball.getOriginX(),
                    s_ball.getOriginY(),
                    s_ball.getWidth(), s_ball.getHeight(), s_ball.getScaleX(), s_ball.
                            getScaleY(), s_ball.getRotation());
        }

        sb.end();

        b_paddle.setLinearVelocity(speed_paddle, 0f);

        debugRenderer.render(world, debugMatrix);
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
                speed_paddle += 200;
            else
                speed_paddle += 100;
            go_right= true;
            go_left = false;
        }
        if(keycode == Input.Keys.LEFT) {
            if (go_right)
                speed_paddle -= 200;
            else
                speed_paddle -= 100;
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
}

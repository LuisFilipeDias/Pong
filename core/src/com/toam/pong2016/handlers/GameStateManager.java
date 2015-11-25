package com.toam.pong2016.handlers;

import com.toam.pong2016.main.Pong2016;
import com.toam.pong2016.states.GameState;
import com.toam.pong2016.states.Play;

import java.util.Stack;

/**
 * Created by luisfilipedias on 15-11-2015.
 */
public class GameStateManager {
    private Pong2016 game;

    private Stack<GameState> gameStates;

    public static final int PLAY = 0x01;

    public GameStateManager(Pong2016 game){
        this.game = game;
        gameStates = new Stack<GameState>();
        pushState(PLAY);
    }

    public void create() {
        gameStates.peek().create();
    }

    public void update(float dt) {
        gameStates.peek().update(dt);
    }

    public void render() {
        gameStates.peek().render();
    }

    public Pong2016 game() {
        return game;
    }

    private GameState getState(int state) {
        if (state == PLAY) return new Play(this);
        return null;
    }

    public void setState(int state) {
        popState();
        pushState(state);
    }

    public void pushState(int state) {
        gameStates.push(getState(state));
    }

    public void popState() {
        GameState g = gameStates.pop();
        g.dispose();
    }

}

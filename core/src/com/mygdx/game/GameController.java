package com.mygdx.game;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector3;

public class GameController implements InputProcessor {
    public boolean flagA, flagS, flagD, flagW;

    @Override
    public boolean keyDown(int keycode) {
        if (Input.Keys.W == keycode) {
            flagW = true;
        }
        if (Input.Keys.S == keycode) {
            flagS = true;
        }
        if (Input.Keys.D == keycode) {
            flagD = true;
        }
        if (Input.Keys.A == keycode) {
            flagA = true;
        }

        if (Input.Keys.SPACE == keycode) {
            World.pers.attack();
        }

        if (Input.Keys.ESCAPE == keycode) {
            MyGame.staticSetScreen(new PauseMenuScreen());
        }
        if (Input.Keys.SHIFT_LEFT == keycode) {
            World.pers.setSpeedBoost(200);
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (Input.Keys.W == keycode) {
            flagW = false;
        }
        if (Input.Keys.S == keycode) {
            flagS = false;
        }
        if (Input.Keys.D == keycode) {
            flagD = false;
        }
        if (Input.Keys.A == keycode) {
            flagA = false;
        }
        if (Input.Keys.SHIFT_LEFT == keycode) {
            World.pers.setSpeedBoost(0);
        }
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        if (character == 'e') {
            World.take();
        }

        if (character == 'i') {
            World.pers.y += 50;
        }
        if (character == 'l') {
            World.pers.x += 50;
        }
        if (character == 'k') {
            World.pers.y -= 50;
        }
        if (character == 'j') {
            World.pers.x -= 50;
        }
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

    public Vector3 speedVector() {
        Vector3 v = new Vector3(0, 0, 0);
        if (flagA) {
            v.x = -5;
        }
        if (flagD) {
            v.x = 5;
        }
        if (flagW) {
            v.y = 5;
        }
        if (flagS) {
            v.y = -5;
        }
        return Helper.norm(v);
    }
}

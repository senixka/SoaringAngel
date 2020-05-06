package com.mygdx.game;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector3;

public class PauseMenuController implements InputProcessor {
    public Rectangle back;

    public PauseMenuController() {
        back = new Rectangle(740, 420, 60, 60);
    }

    @Override
    public boolean keyDown(int keycode) {
        if (Input.Keys.ESCAPE == keycode) {
            MyGame.gameScreen.contin();
        }
        if (Input.Keys.BACK == keycode) {
            MyGame.gameScreen.contin();
        }

        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector3 v = new Vector3(screenX, screenY, 0);
        MyGame.camera.unproject(v);
        //System.out.println(v.x + " " + v.y);
        if (back.contains(v)) {
            MyGame.gameScreen.contin();
        }
        Inventory.touchDown(v);
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

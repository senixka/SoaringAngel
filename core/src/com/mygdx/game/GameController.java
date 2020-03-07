package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;

public class GameController implements InputProcessor {
    public boolean flagA, flagS, flagD, flagW;
    public static Texture hpAndEnergy = new Texture(Gdx.files.internal("HpAndEnergy.psd"));
    public static Texture hp = new Texture(Gdx.files.internal("red.png"));
    public static Texture energy = new Texture(Gdx.files.internal("blue.png"));

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

    public void draw() {
        float sizeX = MyGame.camera.viewportWidth;
        float sizeY = MyGame.camera.viewportHeight;
        float x = MyGame.camera.position.x - sizeX / 2;
        float y = MyGame.camera.position.y + sizeY / 2;
        MyGame.batch.draw(hpAndEnergy, x, y - sizeY / 480 * 50, sizeX / 800 * 150, sizeY / 480 * 50);
        MyGame.batch.draw(hp, x + sizeX / 800 * 40, y - sizeY / 480 * 20, sizeX / 800 * 100 * ((float) World.pers.hp / World.pers.maxHp), sizeY / 480 * 10);
        MyGame.batch.draw(energy, x + sizeX / 800 * 40, y - sizeY / 480 * 40, sizeX / 800 * 100 * ((float) World.pers.energy / World.pers.maxEnergy), sizeY / 480 * 10);

    }
}

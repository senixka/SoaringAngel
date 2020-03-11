package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.MapGenerator.GameMapController;
import com.mygdx.game.MapGenerator.GameMapGenerator;
import com.mygdx.game.MapGenerator.Pair;

public class GameController implements InputProcessor {
    public boolean flagA, flagS, flagD, flagW;
    public static Texture hpAndEnergy = new Texture(Gdx.files.internal("HpAndEnergy.psd"));
    public static Texture hp = new Texture(Gdx.files.internal("red.png"));
    public static Texture energy = new Texture(Gdx.files.internal("blue.png"));
    public static Texture desert = new Texture(Gdx.files.internal("desert.png"));
    public static Texture persColor = new Texture(Gdx.files.internal("RedCircleBullet.psd"));
    public static Texture contr1 = new Texture(Gdx.files.internal("Controller1.psd"));
    public static Texture contr2 = new Texture(Gdx.files.internal("Controller2.psd"));
    public static Texture attackButton1 = new Texture(Gdx.files.internal("AttackButton1.psd"));
    public static Texture attackButton2 = new Texture(Gdx.files.internal("AttackButton2.psd"));
    public static final float zoom = 1.5f;
    public static final int contrSize = 100;
    public Vector3 speedVector;


    public int contrFinger = -1;
    public Vector3 contrStart;

    public int attackFinger = -1;
    public static final Vector3 attackButton = new Vector3(650, 50, 0);
    public static final int attackSize = 100;

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
            if (World.pers.weapon != null) {
                World.pers.weapon.attackDown();
            }
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
        if (Input.Keys.SPACE == keycode) {
            if (World.pers.weapon != null) {
                World.pers.weapon.attackUp();
            }
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
        if (character == '1') {
            World.pers.hp = World.pers.maxHp;
        }
        if (character == '2') {
            World.pers.energy = World.pers.maxEnergy;
        }
        if (character == '0') {
            Pair temp = World.mapController.teleportPersInMaze();
            Vector3 tmp = GameMapController.mapCordsToGame(new Vector3(temp.first, temp.second, 0));
            World.pers.setPosition(tmp.x, tmp.y);
        }
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        float sizeX = MyGame.camera.viewportWidth;
        float sizeY = MyGame.camera.viewportHeight;
        Vector3 touch = new Vector3(screenX, screenY, 0);
        MyGame.camera.unproject(touch);
        float x = MyGame.camera.position.x - MyGame.camera.viewportWidth / 2;
        float y = MyGame.camera.position.y - MyGame.camera.viewportHeight / 2;
        if (touch.x < x + 400 && contrFinger == -1) {
            contrFinger = pointer;
            contrStart = new Vector3(touch.x - x, touch.y - y, 0);
        }

        if (Helper.dist(new Vector3(touch.x - x, touch.y - y, 0), new Vector3((attackButton.x + attackSize / 2) * sizeX / 800, (attackButton.y + attackSize / 2) * sizeY / 480, 0)) < attackSize * sizeX / 800) {
            attackFinger = pointer;
            World.take();
            if (World.pers.weapon != null) {
                World.pers.weapon.attackDown();
            }
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (pointer == contrFinger) {
            contrFinger = -1;
            speedVector = null;
            contrStart = null;
        }
        if (pointer == attackFinger) {
            attackFinger = -1;
            if (World.pers.weapon != null) {
                World.pers.weapon.attackUp();
            }
        }
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        Vector3 drag = new Vector3(screenX, screenY, 0);
        MyGame.camera.unproject(drag);
        float x = MyGame.camera.position.x - MyGame.camera.viewportWidth / 2;
        float y = MyGame.camera.position.y - MyGame.camera.viewportHeight / 2;
        if (pointer == contrFinger) {
            speedVector = new Vector3(drag.x - x - contrStart.x, drag.y - y - contrStart.y, 0);
        }
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
        if (speedVector != null) {
            return speedVector.cpy().nor();
        }
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
        MyGame.batch.draw(hp, x + sizeX - World.map.length * zoom, y - World.map.length * zoom, World.map.length * zoom, World.map.length * zoom);
        for (int i = 0; i < World.miniMap.length; i++) {
            for (int j = 0; j < World.miniMap[i].length; j++) {
                if (World.miniMap[i][j] == GameMapController.wallCode) {
                    MyGame.batch.draw(energy, x + sizeX - World.miniMap[j].length * zoom + i * zoom, y - World.miniMap.length * zoom + j * zoom, zoom, zoom);
                }
                if (World.miniMap[i][j] == GameMapController.roomPassedCode) {
                    MyGame.batch.draw(desert, x + sizeX - World.miniMap[j].length * zoom + i * zoom, y - World.miniMap.length * zoom + j * zoom, zoom, zoom);
                }
            }
        }
        Vector3 p = GameMapController.gameCordsToMap(World.pers.getCenter());
        MyGame.batch.draw(persColor, x + sizeX - World.map.length * zoom + p.x * zoom, y - World.map.length * zoom + p.y * zoom, 15 * zoom, 15 * zoom);

        y -= sizeY;
        if (contrStart != null) {
            MyGame.batch.draw(contr1, contrStart.x + x - contrSize * sizeX / 800 / 2, contrStart.y - contrSize * sizeY / 480 / 2 + y, contrSize * sizeX / 800, contrSize * sizeY / 480);
            Vector3 v = speedVector.cpy().nor();
            MyGame.batch.draw(contr2, contrStart.x - contrSize * sizeX / 800 / 2 + x + (v.x * 50) * sizeX / 800, contrStart.y - contrSize * sizeY / 480 / 2 + y + v.y * 50 * sizeY / 480, contrSize * sizeX / 800, contrSize * sizeY / 480);
        }
        if (attackFinger == -1) {
            MyGame.batch.draw(attackButton1, attackButton.x * sizeX / 800 + x, attackButton.y * sizeY / 480 + y, attackSize * sizeX / 800, attackSize * sizeY / 480);
        } else {
            MyGame.batch.draw(attackButton2, attackButton.x * sizeX / 800 + x, attackButton.y * sizeY / 480 + y, attackSize * sizeX / 800, attackSize * sizeY / 480);
        }
    }
}

package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.MapGenerator.GameMapController;

public class GameController implements InputProcessor {
    public static final float zoom = 1.0f;
    public static final int contrSize = 100;
    public static final Vector3 attackButton = new Vector3(650, 50, 0);
    public static final int attackSize = 100;
    public static final Vector3 speedButton = new Vector3(600, 50, 0);
    public static final int speedSize = 40;
    public static final int menuSize = 40;
    public static final Vector3 menuButton = new Vector3(750, 300, 0);
    public static final Texture deathMenuIMG = new Texture(Gdx.files.internal("DeathMenu.psd"));
    public static final Vector3 deathMenu = new Vector3(300, 200, 0);
    public static final int deathMenuSizeX = 200, deathMenuSizeY = 100;
    public static final Texture moneyIMG = new Texture(Gdx.files.internal("Money.psd"));
    public static Texture hpAndEnergy = new Texture(Gdx.files.internal("HpAndEnergy.psd"));
    public static Texture hp = new Texture(Gdx.files.internal("red.png"));
    public static Texture energy = new Texture(Gdx.files.internal("blue.png"));
    public static Texture desert = new Texture(Gdx.files.internal("desert.png"));
    public static Texture persColor = new Texture(Gdx.files.internal("RedCircleBullet.psd"));
    public static Texture contr1 = new Texture(Gdx.files.internal("Controller1.psd"));
    public static Texture contr2 = new Texture(Gdx.files.internal("Controller2.psd"));
    public static Texture attackButton1 = new Texture(Gdx.files.internal("AttackButton1.psd"));
    public static Texture attackButton2 = new Texture(Gdx.files.internal("AttackButton2.psd"));
    public static Texture speedButton1 = new Texture(Gdx.files.internal("Dodge1.psd"));
    public static Texture speedButton2 = new Texture(Gdx.files.internal("Dodge2.psd"));
    public static Texture menuButtonIMG = new Texture(Gdx.files.internal("Inventory.psd"));
    public static boolean flag = true;
    public static Button deathBackToMenu;
    public boolean flagA, flagS, flagD, flagW;
    public Vector3 speedVector;
    public int contrFinger = -1;
    public Vector3 contrStart;
    public int attackFinger = -1;
    public int speedFinger = -1;
    public int deadTime;
    public boolean isDialogue = false;
    public Dialogue dialogue = null;


    public GameController() {
        Gdx.input.setCatchBackKey(true);
        deadTime = 200;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (World.pers.isDead()) {
            return false;
        }
        if (isDialogue) {
            if (Input.Keys.SPACE == keycode) {
                dialogue.click();
            }
            return false;
        }
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
            globalUp();
            MyGame.staticSetScreen(new PauseMenuScreen());
        }
        if (Input.Keys.SHIFT_LEFT == keycode) {
            World.pers.setSpeedBoost(600);
        }
        if (Input.Keys.BACK == keycode) {
            globalUp();
            MyGame.staticSetScreen(new PauseMenuScreen());
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
        if (World.pers.isDead()) {
            return false;
        }
        if (isDialogue) {
            return false;
        }
        if (character == 'e') {
            World.take();
            if (World.talk()) {
                globalUp();
            }
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
        if (character == '3') {
            World.pers.money += 5;
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
        if (World.pers.isDead() && deadTime <= 0) {
            if (deathBackToMenu.isPressed(touch)) {
                MyGame.staticSetScreen(new MainMenuScreen());
            }
            return false;
        }
        if (World.pers.isDead()) {
            return false;
        }
        if (isDialogue) {
            dialogue.click();
            return false;
        }
        if (touch.x < x + 400 * sizeX / 800 && contrFinger == -1) {
            contrFinger = pointer;
            contrStart = new Vector3(touch.x - x, touch.y - y, 0);
        }


        if (Helper.dist(new Vector3(touch.x - x, touch.y - y, 0), new Vector3((attackButton.x + attackSize / 2) * sizeX / 800, (attackButton.y + attackSize / 2) * sizeY / 480, 0)) < attackSize * sizeX / 2 / 800) {
            attackFinger = pointer;
            if (World.take()) {
                attackFinger = -1;
                return false;
            }
            if (World.talk()) {
                attackFinger = -1;
                return false;
            }
            if (World.pers.weapon != null) {
                World.pers.weapon.attackDown();
            }
        }

        if (Helper.dist(new Vector3(touch.x - x, touch.y - y, 0), new Vector3((speedButton.x + speedSize / 2) * sizeX / 800, (speedButton.y + speedSize / 2) * sizeY / 480, 0)) < speedSize * sizeX / 2 / 800) {
            speedFinger = pointer;
            World.pers.setSpeedBoost(600);
        }

        if (Helper.dist(new Vector3(touch.x - x, touch.y - y, 0), new Vector3((menuButton.x + menuSize / 2) * sizeX / 800, (menuButton.y + menuSize / 2) * sizeY / 480, 0)) < menuSize * sizeX / 800) {
            MyGame.staticSetScreen(new PauseMenuScreen());
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
        if (pointer == speedFinger) {
            speedFinger = -1;
            World.pers.setSpeedBoost(0);
        }
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (World.pers.isDead()) {
            return false;
        }
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
        if (World.pers.isDead() && deadTime > 0) {
            deadTime--;
            if (deadTime == 0) {
                deathBackToMenu = new Button((int) (x + (deathMenu.x + 50) * sizeX / 800), (int) (y - sizeY + (deathMenu.y + 20) * sizeY / 480), (int) (deathMenuSizeX / 2 * sizeX / 800), (int) (20 * sizeY / 480));
                deathBackToMenu.setText("Main Menu");
            }
            return;
        } else if (World.pers.isDead() && deadTime <= 0) {
            MyGame.batch.draw(deathMenuIMG, x + deathMenu.x * sizeX / 800, y - sizeY + deathMenu.y * sizeY / 480, deathMenuSizeX * sizeX / 800, deathMenuSizeY * sizeY / 480);
            MyGame.font.getData().setScale(1.5f);
            deathBackToMenu.draw(MyGame.batch, MyGame.font);
            return;
        }

        if (isDialogue) {
            dialogue.draw(x, y - sizeY, sizeX / 800);
            return;
        }

        MyGame.batch.draw(hpAndEnergy, x, y - sizeY / 480 * 50, sizeX / 800 * 150, sizeY / 480 * 50);
        MyGame.batch.draw(hp, x + sizeX / 800 * 40, y - sizeY / 480 * 20, sizeX / 800 * 100 * ((float) World.pers.hp / World.pers.maxHp), sizeY / 480 * 10);
        MyGame.batch.draw(energy, x + sizeX / 800 * 40, y - sizeY / 480 * 40, sizeX / 800 * 100 * ((float) World.pers.energy / World.pers.maxEnergy), sizeY / 480 * 10);
        MyGame.batch.draw(moneyIMG, x + sizeX / 800 * 150, y - sizeY / 480 * 20, sizeX / 800 * 20, sizeY / 480 * 20);
        MyGame.font.getData().setScale(1f * sizeX / 800);
        MyGame.font.draw(MyGame.batch, Integer.toString(World.pers.money), x + sizeX / 800 * 150 + sizeX / 800 * 20, y - sizeY / 480 * 5);

        if (World.miniMap != null) {
            MyGame.batch.draw(World.miniMap, x + sizeX - World.map.length * zoom, y - World.map[0].length * zoom, World.map.length * zoom, World.map[0].length * zoom);
            Vector3 p = GameMapController.gameCordsToMap(World.pers.getCenter());
            MyGame.batch.draw(persColor, x + sizeX - World.map.length * zoom + p.x * zoom - 15 * zoom / 2, y - World.map[0].length * zoom + p.y * zoom - 15 * zoom / 2, 15 * zoom, 15 * zoom);
        }
        y -= sizeY;
        if (contrStart != null) {
            MyGame.batch.draw(contr1, contrStart.x + x - contrSize * sizeX / 800 / 2, contrStart.y - contrSize * sizeY / 480 / 2 + y, contrSize * sizeX / 800, contrSize * sizeY / 480);
            Vector3 v = Vector3.Zero;
            if (speedVector != null) {
                v = speedVector.cpy().nor();
            }
            MyGame.batch.draw(contr2, contrStart.x - contrSize * sizeX / 800 / 2 + x + (v.x * 50) * sizeX / 800, contrStart.y - contrSize * sizeY / 480 / 2 + y + v.y * 50 * sizeY / 480, contrSize * sizeX / 800, contrSize * sizeY / 480);
        }
        if (attackFinger == -1) {
            MyGame.batch.draw(attackButton1, attackButton.x * sizeX / 800 + x, attackButton.y * sizeY / 480 + y, attackSize * sizeX / 800, attackSize * sizeY / 480);
        } else {
            MyGame.batch.draw(attackButton2, attackButton.x * sizeX / 800 + x, attackButton.y * sizeY / 480 + y, attackSize * sizeX / 800, attackSize * sizeY / 480);
        }

        if (speedFinger == -1) {
            MyGame.batch.draw(speedButton1, speedButton.x * sizeX / 800 + x, speedButton.y * sizeY / 480 + y, speedSize * sizeX / 800, speedSize * sizeY / 480);
        } else {
            MyGame.batch.draw(speedButton2, speedButton.x * sizeX / 800 + x, speedButton.y * sizeY / 480 + y, speedSize * sizeX / 800, speedSize * sizeY / 480);
        }
        MyGame.batch.draw(menuButtonIMG, menuButton.x * sizeX / 800 + x, menuButton.y * sizeY / 480 + y, menuSize * sizeX / 800, menuSize * sizeY / 480);

    }

    public void globalUp() {
        for (int i = 0; i < 10; i++) {
            touchUp(0, 0, i, 0);
        }
        for (int i = 0; i < 250; i++) {
            keyUp(i);
        }
    }

    public void setDialogue(Dialogue dialogue) {
        this.dialogue = dialogue;
        this.isDialogue = true;
    }

    public void removeDialogue() {
        this.isDialogue = false;
        dialogue = null;
    }
}

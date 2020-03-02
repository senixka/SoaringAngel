package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.Weapons.FirstGun;
import com.mygdx.game.Weapons.Shotgun;

public class Pers {
    float x = 0, y = 0;
    int sizeX = 8 * 5;
    int sizeY = 11 * 5;
    int speed = 200;
    int speedBoost = 0;
    Texture texture;
    Mob target;
    Weapon weapon;
    Vector3 lastVect;

    boolean flip = false;

    public Pers() {
        texture = new Texture(Gdx.files.internal("Human.psd"));
        lastVect = new Vector3(5, 0, 0);
        weapon = new Shotgun();
    }

    public void setSpeedBoost(int speedBoost) {
        this.speedBoost = speedBoost;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void move(Vector3 vector3, float delta) {
        if (!vector3.epsilonEquals(Vector3.Zero)) {
            lastVect = vector3;
        }

        if (target != null) {
            flip = (target.x < x);
        } else if (vector3.x != 0) {
            flip = (vector3.x < 0);
        }

        int tempX = (int) ((getCenter().x + vector3.x * speed * delta) / World.pixSize);
        int tempY = (int) ((getCenter().y) / World.pixSize);
        boolean tempFlagX = true;

        //Rectangle r1 = new Rectangle(tempX * World.pixSize, tempY * World.pixSize, World.pixSize, World.pixSize);
        try {
            if (World.map[tempX][tempY] == 0) {
                tempFlagX = false;
            }
        } catch (IndexOutOfBoundsException e) {

        }


        if (tempFlagX) {
            x += vector3.x * (speed + speedBoost) * delta;
        }

        tempX = (int) ((getCenter().x) / World.pixSize);
        tempY = (int) ((getCenter().y + vector3.y * speed * delta) / World.pixSize);
        boolean tempFlagY = true;

        try {
            if (World.map[tempX][tempY] == 0) {
                tempFlagY = false;
            }

        } catch (IndexOutOfBoundsException e) {

        }

        if (tempFlagY) {
            y += vector3.y * (speed + speedBoost) * delta;
        }
    }

    public Vector3 getCenter() {
        return new Vector3(x + sizeX / 2, y + sizeY /2, 0);
    }

    public void draw() {
        MyGame.batch.draw(texture, x, y, sizeX, sizeY, 0, 0, texture.getWidth(), texture.getHeight(), flip, false);
        weapon.draw();
    }

    public boolean getFlip() {
        return flip;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void attack() {
        if (weapon != null) {
            weapon.attack();
        }
    }
}

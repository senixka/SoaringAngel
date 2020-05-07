package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.Animations.DeathAnimation;

public class Pers {
    public float x = 0, y = 0;
    public int sizeX = 8 * 5, sizeY = 11 * 5;
    public int speed = 400, speedBoost = 0;
    public Texture texture;
    public Mob target;
    public Weapon weapon;
    public Vector3 lastVect;
    public boolean flip = false;
    public int hp, energy;
    public int maxHp = 50, maxEnergy = 250;
    //public int maxHp = 500000, maxEnergy = 2500000;
    public int money = 0;

    public Pers() {
        texture = new Texture(Gdx.files.internal("Human.psd"));
        lastVect = new Vector3(5, 0, 0);
        weapon = new Weapon(null, "", 0);
        hp = maxHp;
        energy = maxEnergy;
    }

    public void setSpeedBoost(int speedBoost) {
        this.speedBoost = speedBoost;
    }

    public void setPosition(float x, float y) {
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

        Rectangle tempR = new Rectangle(x, y, sizeX, sizeY / 2);

        tempR.x += vector3.x * (speed + speedBoost) * delta;
        boolean tempFlagX = !Helper.intersectWall(tempR);

        if (tempFlagX) {
            x += vector3.x * (speed + speedBoost) * delta;
        }


        tempR.x = x;
        tempR.y += vector3.y * (speedBoost + speed) * delta;
        boolean tempFlagY = !Helper.intersectWall(tempR);

        if (tempFlagY) {
            y += vector3.y * (speed + speedBoost) * delta;
        }
    }

    public Vector3 getCenter() {
        return new Vector3(x + sizeX / 2, y + sizeY / 2, 0);
    }

    public void draw() {
        MyGame.batch.draw(texture, x, y, sizeX, sizeY, 0, 0, texture.getWidth(), texture.getHeight(), flip, false);
        if (weapon != null) {
            weapon.draw();
        }
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

    public void update(float delta) {
        if (weapon != null) {
            weapon.update(delta);
        }
        //energy = maxEnergy;
    }

    public Rectangle getBody() {
        return new Rectangle(x, y, sizeX, sizeY);
    }

//    public void attack() {
//        if (weapon != null) {
//            weapon.attack();
//        }
//    }

    public void hit(int dmg) {
        if (hp == 0) {
            return;
        }
        hp -= dmg;
        if (hp < 0) {
            hp = 0;
        }
        if (hp == 0) {
            World.myAnimations.add(new DeathAnimation(getCenter().x, getCenter().y));
            World.controller.globalUp();
        }
    }

    public void addEnergy(int en) {
        if (en + energy > maxEnergy) {
            energy = maxEnergy;
        } else {
            energy += en;
        }
    }

    public boolean isDead() {
        return hp == 0;
    }
}

package com.mygdx.game.Weapons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.Bullet;
import com.mygdx.game.Bullets.DNKBullet;
import com.mygdx.game.Bullets.FirstBullet;
import com.mygdx.game.MyGame;
import com.mygdx.game.Weapon;

public class Relstron extends Weapon {
    public static final Texture IMG = new Texture(Gdx.files.internal("FirstGun.png"));
    public static final int rapid = 20;
    public static final int charging = 20;
    public float time = 0;
    public boolean isFire = false;
    public Bullet b;

    public Relstron() {
        super(IMG, "relstron");
    }

    @Override
    public void draw() {
        if (getPers() == null) {
            System.out.println("AAAAAAAAA");
        }
        float x = getPers().getX();
        float y = getPers().getY();
        MyGame.batch.draw(texture, x - 5, y + 5, 10 * 5, 25, 0, 0, texture.getWidth(), texture.getHeight(), getPers().getFlip(), false);
    }

    @Override
    public void update(float delta) {
        if (!isFire) {
            return;
        }
        b.x = getPers().x;
        b.y = getPers().getCenter().y - b.sizeY / 2;
        if (!getPers().flip) {
            b.x += getPers().sizeX;
        } else {
            b.x -= b.sizeX;
        }
        b.dead = 500;
        time += charging * delta;
        if (time < 50) {
            b.sizeX += charging * delta;
            b.sizeY += charging * delta;
        }
    }

    @Override
    public void attackDown() {
        time = 0;
        isFire = true;
        b = new FirstBullet(getVector(), getPers().x, getPers().y);
        b.speed = 0;
    }

    @Override
    public void attackUp() {
        isFire = false;
        b.speed = 600;
        b.vect = getVector();
        b = null;
    }
}

package com.mygdx.game.Weapons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.Bullets.Shotgun2Bullet;
import com.mygdx.game.MyGame;
import com.mygdx.game.Weapon;

public class Shotgun2 extends Weapon {
    public static final Texture texture = new Texture(Gdx.files.internal("Shotgun2.psd"));
    public static final int rapid = 50;
    public float time = 0;
    public boolean isFire = false;

    public Shotgun2() {
        super(texture, "First gun", 2);
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
        time += rapid * delta;
        if (time > 10) {
            if (!isFire) {
                time = 10;
                return;
            }
            time = 0;
            if (!energyEnough()) {
                return;
            }
            new Shotgun2Bullet(getVector(), getPers().getX(), getPers().getY());
        }
    }

    @Override
    public void attackDown() {
        isFire = true;
    }

    @Override
    public void attackUp() {
        isFire = false;
    }
}

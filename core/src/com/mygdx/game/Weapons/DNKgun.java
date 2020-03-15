package com.mygdx.game.Weapons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.Bullets.DNKBullet;
import com.mygdx.game.MyGame;
import com.mygdx.game.Weapon;

public class DNKgun extends Weapon {
    public static final Texture texture = new Texture(Gdx.files.internal("DNKgun.psd"));
    public static final int rapid = 100;
    public float time = 0;
    public boolean isFire = false;

    public DNKgun() {
        super(texture, "First gun", 1);
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
        time += rapid * delta;
        if (time > 10) {
            time = 0;
            if (!energyEnough()) {
                return;
            }
            new DNKBullet(getVector(), getPers().getX(), getPers().getY(), 1);
            new DNKBullet(getVector(), getPers().getX(), getPers().getY(), 0);
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

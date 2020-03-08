package com.mygdx.game.Weapons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.Bullets.DNKBullet;
import com.mygdx.game.Bullets.FirstBullet;
import com.mygdx.game.MyGame;
import com.mygdx.game.Weapon;

public class FirstGun extends Weapon {
    public Texture texture;
    public static final int rapid = 100;
    public float time = 0;
    public boolean isFire = false;

    public FirstGun() {
        super(new Texture(Gdx.files.internal("FirstGun.png")), "First gun");
        texture = new Texture(Gdx.files.internal("FirstGun.png"));
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
            new FirstBullet(getVector(), getPers().getX(), getPers().getY());
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

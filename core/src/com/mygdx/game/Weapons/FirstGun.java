package com.mygdx.game.Weapons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.Bullets.FirstBullet;
import com.mygdx.game.MyGame;
import com.mygdx.game.Weapon;

public class FirstGun extends Weapon {
    public Texture texture;

    public FirstGun() {
        super();
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
    public void attack() {
        new FirstBullet(getVector(), getPers().getX(), getPers().getY());
    }
}

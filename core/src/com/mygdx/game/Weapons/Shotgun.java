package com.mygdx.game.Weapons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.Bullets.FirstBullet;
import com.mygdx.game.Helper;
import com.mygdx.game.MyGame;
import com.mygdx.game.Subject;
import com.mygdx.game.Weapon;

public class Shotgun extends Weapon {
    Texture texture;

    public Shotgun() {
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
        MyGame.batch.draw(texture, x - 5, y + 5, 10 * 5, 25, 0,0, texture.getWidth(), texture.getHeight(), getPers().getFlip(), false);
    }

    @Override
    public void attack() {
        new FirstBullet(getVector(), getPers().getX(), getPers().getY());
        Vector3 v = getVector().cpy();
        System.out.println("pau");
        new FirstBullet(v.rotate(new Vector3(0, 0, 1), 45), getPers().getX(), getPers().getY());
        v = getVector().cpy();
        new FirstBullet(v.rotate(new Vector3(0, 0, 1), -45), getPers().getX(), getPers().getY());
    }

    @Override
    public Subject getSubject() {
        return super.getSubject();
    }

    public static Subject getSub() {
        return new Subject(new Texture(Gdx.files.internal("FirstGun.png")), "Shotgun");
    }
}

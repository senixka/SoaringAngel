package com.mygdx.game.Weapons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.Bullets.FirstBullet;
import com.mygdx.game.MyGame;
import com.mygdx.game.Subject;
import com.mygdx.game.Weapon;

public class Shotgun extends Weapon {
    public Texture texture;
    public static final int rapid = 20;
    public float time = 0;
    public boolean isFire = false;

    public Shotgun() {
        super(new Texture(Gdx.files.internal("FirstGun.png")), "Shotgun");
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
    public Subject getSubject() {
        return super.getSubject();
    }

    public static Subject getSub() {
        return new Subject(new Texture(Gdx.files.internal("FirstGun.png")), "Shotgun");
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
            Vector3 v = getVector().cpy();
            new FirstBullet(v.rotate(new Vector3(0, 0, 1), 45), getPers().getX(), getPers().getY());
            v = getVector().cpy();
            new FirstBullet(v.rotate(new Vector3(0, 0, 1), -45), getPers().getX(), getPers().getY());
            v = getVector().cpy();
            new FirstBullet(v.rotate(new Vector3(0, 0, 1), -15), getPers().getX(), getPers().getY());
            v = getVector().cpy();
            new FirstBullet(v.rotate(new Vector3(0, 0, 1), 15), getPers().getX(), getPers().getY());
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

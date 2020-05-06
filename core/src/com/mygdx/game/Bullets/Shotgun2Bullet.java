package com.mygdx.game.Bullets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.Bullet;
import com.mygdx.game.Helper;

public class Shotgun2Bullet extends Bullet {

    public static final Texture IMG = new Texture(Gdx.files.internal("RedCircleBullet.psd"));
    public static final Texture IMG2 = new Texture(Gdx.files.internal("BlueCircleBullet.psd"));

    public Shotgun2Bullet(Vector3 vect, float x, float y) {
        super(IMG2, 400, 40, 40, 11, vect, x, y);
    }

    public Shotgun2Bullet(Vector3 vect, float x, float y, boolean isEn) {
        super(IMG2, 400, 40, 40, 11, vect, x, y);
        if (isEn) {
            isEnemy = true;
            texture = IMG;
        }
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        if (dead == 480) {
            dead = 0;
            Vector3 v = vect.cpy();
            Helper.rotate(v, 15);
            Bullet b = new Shotgun2Bullet(v, x, y);
            b.damage -= 5;
            b.dead = 480 - 1;
            b.sizeX /= 2;
            b.sizeY /= 2;
            v = vect.cpy();
            Helper.rotate(v, -15);
            b = new Shotgun2Bullet(v, x, y);
            b.damage -= 5;
            b.dead = 480 - 1;
            b.sizeX /= 2;
            b.sizeY /= 2;
            v = vect.cpy();
            b = new Shotgun2Bullet(v, x, y);
            b.damage -= 5;
            b.dead = 480 - 1;
            b.sizeX /= 2;
            b.sizeY /= 2;
        }
        if (dead == 450) {
            dead = 0;
            Vector3 v = vect.cpy();
            Helper.rotate(v, 15);
            Bullet b = new Shotgun2Bullet(v, x, y);
            b.damage -= 5;
            b.dead = 450 - 1;
            b.sizeX /= 2;
            b.sizeY /= 2;
            v = vect.cpy();
            Helper.rotate(v, -15);
            b = new Shotgun2Bullet(v, x, y);
            b.damage -= 5;
            b.dead = 450 - 1;
            b.sizeX /= 2;
            b.sizeY /= 2;
            v = vect.cpy();
            b = new Shotgun2Bullet(v, x, y);
            b.damage -= 5;
            b.dead = 450 - 1;
            b.sizeX /= 2;
            b.sizeY /= 2;
        }
    }
}

package com.mygdx.game.Bullets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.Bullet;

public class FirstBullet extends Bullet {
    public static final Texture IMG = new Texture(Gdx.files.internal("RedCircleBullet.psd"));
    public static final Texture IMG2 = new Texture(Gdx.files.internal("BlueCircleBullet.psd"));

    public FirstBullet(Vector3 vect, float x, float y) {
        super(IMG2, 600, 20, 20, 5, vect, x, y);
    }

    public FirstBullet(Vector3 vect, float x, float y, boolean isEn) {
        super(IMG2, 600, 20, 20, 5, vect, x, y);
        if (isEn) {
            isEnemy = true;
            texture = IMG;
        }
    }
}

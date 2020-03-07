package com.mygdx.game.Bullets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.Bullet;

public class DNKBullet extends Bullet {
    public static final Texture IMG = new Texture(Gdx.files.internal("RedCircleBullet.psd"));
    Vector3 center;
    double alp;
    double angSpeed = 180;

    public DNKBullet(Vector3 vect, float x, float y, int num) {
        super(IMG, 600, 20, 20, 5, vect, x, y);
        center = new Vector3(x, y, 0);
        if (num == 1) {
            alp = 0;
        } else {
            alp = 180;
        }
    }

    @Override
    public void update(float delta) {
        center.x += speed * delta * vect.x;
        center.y += speed * delta * vect.y;
        alp += angSpeed * delta;
        x = (float) Math.cos(Math.toRadians(alp)) * 20 + center.x;
        y = (float) Math.sin(Math.toRadians(alp)) * 20 + center.y;
        dead--;
    }
}

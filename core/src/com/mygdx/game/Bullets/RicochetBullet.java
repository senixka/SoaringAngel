package com.mygdx.game.Bullets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.Bullet;
import com.mygdx.game.Helper;
import com.mygdx.game.Rectangle;

public class RicochetBullet extends Bullet {
    public static final Texture IMG = new Texture(Gdx.files.internal("RedCircleBullet.psd"));
    public int live = 2;

    public RicochetBullet(Vector3 vect, float x, float y) {
        super(IMG, 600, 20, 20, 5, vect, x, y);
        dead = 1000;
    }

    @Override
    public void update(float delta) {
        x += speed * delta * vect.x;
        if (Helper.intersectWall(new Rectangle(x, y, sizeX, sizeY))) {
            x -= speed * delta * vect.x;
            vect.x = -vect.x;
            live -= 1;
        }
        y += speed * delta * vect.y;
        if (Helper.intersectWall(new Rectangle(x, y, sizeX, sizeY))) {
            y -= speed * delta * vect.y;
            vect.y = -vect.y;
            live -= 1;
        }
        if (live <= -1) {
            dead = -1;
        }
    }
}

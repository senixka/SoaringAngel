package com.mygdx.game.Bullets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.Bullet;

public class FirstBullet extends Bullet {
    public FirstBullet(Vector3 vect, float x, float y) {
        super(new Texture(Gdx.files.internal("red.png")), 300, 20, 20, 5, vect, x, y);
    }
}

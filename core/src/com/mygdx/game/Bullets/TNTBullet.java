package com.mygdx.game.Bullets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.Animations.Explosion;
import com.mygdx.game.Bullet;
import com.mygdx.game.Helper;
import com.mygdx.game.Mob;
import com.mygdx.game.World;

public class TNTBullet extends Bullet {
    public static final Texture IMG = new Texture(Gdx.files.internal("TNT.png"));

    public TNTBullet(Vector3 vect, float x, float y) {
        super(IMG, 0, 30, 30, 15, vect, x, y);
        dead = 50;
    }

    @Override
    public boolean isDead() {
        if (dead < 0) {
            explode();
        }
        return dead < 0;
    }

    public void explode() {
        World.myAnimations.add(new Explosion(x + sizeX / 2, y + sizeY / 2));
        for (Mob mob : World.mobs) {
            if (Helper.dist(mob.getCenter(), new Vector3(x + sizeX / 2, y + sizeY / 2, 0)) < 150) {
                mob.hit(damage);
            }
        }
    }
}

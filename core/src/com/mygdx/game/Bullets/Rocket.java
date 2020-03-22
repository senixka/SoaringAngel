package com.mygdx.game.Bullets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.Animations.Explosion;
import com.mygdx.game.Bullet;
import com.mygdx.game.Helper;
import com.mygdx.game.Mob;
import com.mygdx.game.Rectangle;
import com.mygdx.game.World;

public class Rocket extends Bullet {
    public static final Texture IMG = new Texture(Gdx.files.internal("RedCircleBullet.psd"));
    public static final Texture IMG2 = new Texture(Gdx.files.internal("BlueCircleBullet.psd"));

    public Rocket(Vector3 vect, float x, float y) {
        super(IMG2, 400, 40, 40, 15, vect, x, y);
    }

    public Rocket(Vector3 vect, float x, float y, boolean isEn) {
        super(IMG2, 400, 40, 40, 15, vect, x, y);
        if (isEn) {
            isEnemy = true;
            texture = IMG;
        }
    }

    @Override
    public boolean isDead() {
        if (!isEnemy) {
            for (Mob mob : World.mobs) {
                if (Helper.intersect(mob.x, mob.y, mob.sizeX, mob.sizeY, x, y, sizeX, sizeY)) {
                    mob.hit(damage);
                    explode();
                    return true;
                }
            }
        } else {
            if (Helper.intersect(World.pers.getBody(), new Rectangle(x, y, sizeX, sizeY))) {
                World.pers.hit(damage);
                return true;
            }
        }
        if (Helper.intersectWall(new Rectangle(x, y, sizeX, sizeY))) {
            explode();
            return true;
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

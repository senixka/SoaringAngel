package com.mygdx.game.Bullets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.Bullet;
import com.mygdx.game.Helper;
import com.mygdx.game.Mob;
import com.mygdx.game.Rectangle;
import com.mygdx.game.World;

import java.util.Random;

public class Fire extends Bullet {
    public static final Texture IMG1 = new Texture(Gdx.files.internal("Fire1.psd"));
    public static final Texture IMG2 = new Texture(Gdx.files.internal("Fire2.psd"));
    public static final Random rnd = new Random();

    public Fire(Vector3 vect, float x, float y) {
        super(null, 700, 30, 30, 0, vect, x, y);
        if (rnd.nextInt() % 2 == 0) {
            texture = IMG1;
        } else {
            texture = IMG2;
        }
        Helper.rotate(vect, rnd.nextInt() % 20);
        dead = 50;

    }

    public boolean isDead() {
        if (!isEnemy) {
            for (Mob mob : World.mobs) {
                if (Helper.intersect(mob.x, mob.y, mob.sizeX, mob.sizeY, x, y, sizeX, sizeY)) {
                    mob.hit(damage);
                    mob.fireCondition = true;
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
            return true;
        }
        return dead < 0;
    }
}

package com.mygdx.game.Bullets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.Bullet;
import com.mygdx.game.Helper;
import com.mygdx.game.Mob;
import com.mygdx.game.World;

public class WeaponGunBullet extends Bullet {
    public static final Texture IMG = new Texture(Gdx.files.internal("RedCircleBullet.psd"));
    public static final Texture IMG2 = new Texture(Gdx.files.internal("BlueCircleBullet.psd"));

    public WeaponGunBullet(Vector3 vect, float x, float y) {
        super(IMG2, 200, 50, 50, 10, vect, x, y);
    }

    public WeaponGunBullet(Vector3 vect, float x, float y, boolean isEn) {
        super(IMG2, 200, 50, 50, 10, vect, x, y);
        if (isEn) {
            isEnemy = true;
            texture = IMG;
        }
    }



    @Override
    public void update(float delta) {
        super.update(delta);
        if (dead % 50 == 0) {
            Mob target = null;
            float dist = 10000;
            System.out.println("Pau");
            for (Mob mob : World.mobs) {
                if (dist > Helper.dist(new Vector3(x, y, 0), mob.getCenter())) {
                    System.out.println("Yes target");
                    dist = Helper.dist(new Vector3(x, y, 0), mob.getCenter());
                    target = mob;
                }
            }
            if (target != null) {
                new FirstBullet(new Vector3(target.getCenter().x - (x + sizeX / 2), target.getCenter().y - (y + sizeY / 2), 0).nor(), x + sizeX / 2, y + sizeY / 2);
            }
        }
    }
}

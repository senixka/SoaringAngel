package com.mygdx.game.Weapons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.Bullets.FirstBullet;
import com.mygdx.game.Bullets.TNTBullet;
import com.mygdx.game.MyGame;
import com.mygdx.game.Pers;
import com.mygdx.game.Weapon;
import com.mygdx.game.World;

public class TNTGun extends Weapon {
    public static final Texture texture = new Texture(Gdx.files.internal("TNT.png"));
    public static final int rapid = 20;
    public float time = 0;
    public boolean isFire = false;

    public TNTGun() {
        super(texture, "TNT", 3);
    }

    @Override
    public void draw() {
        float x = getPers().getX();
        float y = getPers().getY();
        if (!World.pers.flip) {
            MyGame.batch.draw(texture, x + World.pers.sizeX - 10, y + 5, 30, 30, 0, 0, texture.getWidth(), texture.getHeight(), getPers().getFlip(), false);
        } else {
            MyGame.batch.draw(texture, x - 30 + 10, y + 5, 30, 30, 0, 0, texture.getWidth(), texture.getHeight(), getPers().getFlip(), false);
        }
    }

    @Override
    public void update(float delta) {
        time += rapid * delta;
        if (time > 10) {
            if (!isFire) {
                time = 10;
                return;
            }
            time = 0;
            if (!energyEnough()) {
                return;
            }
            new TNTBullet(getVector(), getPers().getX(), getPers().getY());
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

package com.mygdx.game.Weapons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.Bullets.FirstBullet;
import com.mygdx.game.MyGame;
import com.mygdx.game.Weapon;

public class SpeedGun extends Weapon {
    public static final Texture texture = new Texture(Gdx.files.internal("SpeedGun.psd"));
    public static final int rapid = 100;
    public float time = 0;
    public boolean isFire = false;
    public float rapidBonus = -90;

    public SpeedGun() {
        super(texture, "Speed Gun", 1);
    }

    @Override
    public void draw() {
        float x = getPers().getX();
        float y = getPers().getY();
        MyGame.batch.draw(texture, x - 5, y + 5, 10 * 5, 25, 0, 0, texture.getWidth(), texture.getHeight(), getPers().getFlip(), false);
    }

    @Override
    public void update(float delta) {
        time += (rapid + rapidBonus) * delta;
        if (rapidBonus < 100) {
            rapidBonus += delta * 10;
        }
        if (rapidBonus > 50) {
            energy = 0;
        }
        if (time > 10) {
            if (!isFire) {
                time = 10;
                return;
            }
            time = 0;
            if (!energyEnough()) {
                return;
            }
            new FirstBullet(getVector(), getPers().getX(), getPers().getY());
        }
    }

    @Override
    public void attackDown() {
        isFire = true;
    }

    @Override
    public void attackUp() {
        isFire = false;
        rapidBonus = -90;
        energy = 1;
    }
}

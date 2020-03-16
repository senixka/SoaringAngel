package com.mygdx.game.Weapons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.Bullets.WeaponGunBullet;
import com.mygdx.game.MyGame;
import com.mygdx.game.Weapon;

public class WeaponGun extends Weapon {
    public static final Texture texture = new Texture(Gdx.files.internal("WeaponGun.png"));
    public static final int rapid = 20;
    public float time = 0;
    public boolean isFire = false;

    public WeaponGun() {
        super(texture, "Weapon Gun", 5);
    }

    @Override
    public void draw() {
        float x = getPers().getX();
        float y = getPers().getY();
        MyGame.batch.draw(texture, x - 5, y + 5, 10 * 5, 25, 0, 0, texture.getWidth(), texture.getHeight(), getPers().getFlip(), false);
    }

    @Override
    public void update(float delta) {
        time += rapid * delta;
        if (time > 10) {
            if (!isFire) {
                time = 10;
                return;
            }
            if (!energyEnough()) {
                return;
            }
            time = 0;
            new WeaponGunBullet(getVector(), getPers().getX(), getPers().getY());
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

    @Override
    public String getDamage() {
        return "5";
    }

    @Override
    public String getRapid() {
        return Integer.toString(rapid);
    }

    @Override
    public String addInf() {
        return "Bullets shoot! (damage: 5)";
    }
}

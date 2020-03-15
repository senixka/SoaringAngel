package com.mygdx.game.Weapons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.Bullets.Fire;
import com.mygdx.game.Bullets.FirstBullet;
import com.mygdx.game.MyGame;
import com.mygdx.game.Weapon;

public class Flamethrower extends Weapon {
    public static final Texture texture = new Texture(Gdx.files.internal("FlameThrower.psd"));
    public static final int rapid = 100;
    public float time = 0;
    public boolean isFire = false;
    public boolean isRealyFire = false;

    public Flamethrower() {
        super(texture, "Flamethrower", 1);
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
            time = 0;
            if (!energyEnough()) {
                isRealyFire = false;
                return;
            }
            isRealyFire = true;
        }
        if (!isRealyFire) {
            return;
        }
        if (time > 9 && time < 10) {
            new Fire(getVector(), getPers().x, getPers().y);
            new Fire(getVector(), getPers().x, getPers().y);
            new Fire(getVector(), getPers().x, getPers().y);
        } else if (time > 7 && time < 9) {
            new Fire(getVector(), getPers().x, getPers().y);
            new Fire(getVector(), getPers().x, getPers().y);
            new Fire(getVector(), getPers().x, getPers().y);
        } else if (time > 5 && time < 7) {
            new Fire(getVector(), getPers().x, getPers().y);
            new Fire(getVector(), getPers().x, getPers().y);
            new Fire(getVector(), getPers().x, getPers().y);
        } else if (time > 3 && time < 5) {
            new Fire(getVector(), getPers().x, getPers().y);
            new Fire(getVector(), getPers().x, getPers().y);
            new Fire(getVector(), getPers().x, getPers().y);
        } else if (time > 1 && time < 3) {
            new Fire(getVector(), getPers().x, getPers().y);
            new Fire(getVector(), getPers().x, getPers().y);
            new Fire(getVector(), getPers().x, getPers().y);
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

package com.mygdx.game.Dots;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.Dot;
import com.mygdx.game.World;

public class MoneyDot extends Dot {
    public static final Texture IMG = new Texture(Gdx.files.internal("Money.psd"));

    public MoneyDot(float x, float y) {
        super(x, y, 20, 20);
        img = IMG;
    }

    @Override
    public boolean take() {
        if (super.take()) {
            World.pers.money += 1;
            return true;
        }
        return false;
    }
}

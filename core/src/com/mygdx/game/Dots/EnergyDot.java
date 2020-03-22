package com.mygdx.game.Dots;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.Dot;
import com.mygdx.game.World;

public class EnergyDot extends Dot {
    public static final Texture IMG = new Texture(Gdx.files.internal("EnergyPoint.psd"));

    public EnergyDot(float x, float y) {
        super(x, y, 20, 20);
        img = IMG;
    }

    @Override
    public boolean take() {
        if (super.take()) {
            World.pers.addEnergy(5);
            return true;
        }
        return false;
    }
}

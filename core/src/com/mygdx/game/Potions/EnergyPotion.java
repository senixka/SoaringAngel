package com.mygdx.game.Potions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.Potion;
import com.mygdx.game.World;

public class EnergyPotion extends Potion {
    public static final Texture IMG = new Texture(Gdx.files.internal("EnergyPotion.psd"));

    public EnergyPotion() {
        super(IMG, "EnergyPotion");
        setText("Restore energy");
    }

    @Override
    public void use() {
        World.pers.energy = World.pers.maxEnergy;
    }
}

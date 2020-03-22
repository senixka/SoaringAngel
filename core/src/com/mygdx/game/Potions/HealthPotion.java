package com.mygdx.game.Potions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.Potion;
import com.mygdx.game.World;

public class HealthPotion extends Potion {
    public static final Texture IMG = new Texture(Gdx.files.internal("HealthPotion.psd"));

    public HealthPotion() {
        super(IMG, "HealthPotion");
        setText("Restore hp");
    }

    @Override
    public void use() {
        World.pers.hp = World.pers.maxHp;
    }
}

package com.mygdx.game.Mobs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.Mob;

public class Slime extends Mob {

    public Slime(int x, int y) {
        super(x, y, 50, 50, new Texture(Gdx.files.internal("desert.png")), 20);
    }
}

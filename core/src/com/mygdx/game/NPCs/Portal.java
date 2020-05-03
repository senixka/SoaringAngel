package com.mygdx.game.NPCs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.Helper;
import com.mygdx.game.MyGame;
import com.mygdx.game.NPC;
import com.mygdx.game.World;

public class Portal extends NPC {
    public static final Texture IMG = new Texture(Gdx.files.internal("Portal.psd"));
    public static final int sizeX = World.pixSize, sizeY = World.pixSize;

    public Portal(float x, float y) {
        super(IMG, x, y, sizeX, sizeY);
    }


    public void draw1() {
        MyGame.batch.draw(texture, x, y, sizeX, sizeY);
    }

    public void draw2() {

    }

    @Override
    public boolean talk() {
        if (Helper.dist(World.pers.getCenter(), getCenter()) < 100) {
            World.nextLevel();
            return true;
        }
        return false;
    }
}

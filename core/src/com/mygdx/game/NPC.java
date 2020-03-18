package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;

public class NPC {
    float x, y, sizeX, sizeY;
    Texture texture;

    public NPC(Texture texture, float x, float y, float sizeX, float sizeY) {
        this.x = x;
        this.y = y;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.texture = texture;
    }

    public void draw1() {
        if (y > World.pers.y) {
            boolean flip = World.pers.getCenter().x < (x + sizeX / 2);
            MyGame.batch.draw(texture, x, y, sizeX, sizeY, 0, 0, texture.getWidth(), texture.getHeight(), flip, false);
        }
    }

    public void draw2() {
        if (y < World.pers.y) {
            boolean flip = World.pers.getCenter().x < (x + sizeX / 2);
            MyGame.batch.draw(texture, x, y, sizeX, sizeY, 0, 0, texture.getWidth(), texture.getHeight(), flip, false);
        }
    }

    public Vector3 getCenter() {
        return new Vector3(x + sizeX / 2, y + sizeY / 2, 0);
    }

    public boolean talk() {
        return false;
    }
}

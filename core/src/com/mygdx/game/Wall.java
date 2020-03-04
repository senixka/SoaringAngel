package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class Wall {
    public int x, y, sizeX, sizeY;
    public Texture texture;

    public Wall(int x, int y, int sizeX, int sizeY) {
        this.x = x;
        this.y = y;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        texture = new Texture(Gdx.files.internal("blue.png"));
    }

    public void draw() {
        MyGame.batch.draw(texture, x, y, sizeX, sizeY);
    }
}

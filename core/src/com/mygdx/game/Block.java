package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by iliamikado on 03.10.2019.
 */

public class Block {
    Rectangle rect;
    Texture img;

    Block(int x, int y, int width, int height) {
        rect = new Rectangle(x, y, width, height);
        img = new Texture(Gdx.files.internal("blue.png"));
    }

    public void draw() {
        MyGame.batch.draw(img, rect.x, rect.y, rect.width, rect.height);
    }
}

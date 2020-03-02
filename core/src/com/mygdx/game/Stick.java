package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by iliamikado on 04.10.2019.
 */

public class Stick {
    Texture img1, img2;
    int x1, y1;
    int x2, y2;
    int size = 100;
    Rectangle rect;

    public Stick() {
        img1 = new Texture(Gdx.files.internal("Controller1.psd"));
        img2 = new Texture(Gdx.files.internal("Controller2.psd"));
    }

    public void draw(SpriteBatch batch, BitmapFont font) {
        batch.draw(img1, x1, y1, size, size);
        batch.draw(img2, x2, y2, size, size);
    }

    public void setPosition(int x, int y) {
        x1 = x;
        y1 = y;
        x2 = x;
        y2 = y;
        rect = new Rectangle(x1, y1, size, size);
    }
}

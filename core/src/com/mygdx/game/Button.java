package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

public class Button {
    public Texture img;
    public Sprite sprite;
    public String text;
    public boolean invis = false;

    public Button(int x, int y, int width, int height) {
        img = new Texture(Gdx.files.internal("blue.png"));
        sprite = new Sprite(img, x, y, width, height);
        sprite.setPosition(x, y);
    }

    public Button(int x, int y, int width, int height, Texture img) {
        sprite = new Sprite(img, x, y, width, height);
    }

    public void setInvis(boolean invis) {
        this.invis = invis;
    }

    public void setText(String str) {
        text = str;
    }

    public void draw(SpriteBatch batch, BitmapFont font) {
        if (invis) {
            return;
        }
        sprite.draw(batch);
        font.draw(batch, text, sprite.getX(), sprite.getY() + sprite.getHeight());
    }

    public boolean isPressed(float x, float y) {
        if (sprite.getX() < x && sprite.getY() < y && sprite.getX() + sprite.getWidth() > x
                && sprite.getY() + sprite.getHeight() > y) {
            return true;
        }
        return false;
    }

    public boolean isPressed(Vector3 vector3) {
        float x = vector3.x;
        float y = vector3.y;
        if (sprite.getX() < x && sprite.getY() < y && sprite.getX() + sprite.getWidth() > x
                && sprite.getY() + sprite.getHeight() > y) {
            return true;
        }
        return false;
    }
}

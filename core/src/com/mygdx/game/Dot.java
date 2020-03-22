package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;

public class Dot {
    float x, y, sizeX, sizeY;
    public Texture img;
    private float timer, hesitate = 5;

    public Dot(float x, float y, float sizeX, float sizeY) {
        this.x = x;
        this.y = y;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
    }

    public void update(float delta) {
        if (timer > 1) {
            hesitate = -hesitate;
            timer = 0;
        }
        timer += delta;
        y += delta * hesitate;
    }

    public void draw() {
        MyGame.batch.draw(img, x, y, sizeX, sizeY);
    }

    public boolean take() {
        if (Helper.dist(World.pers.getCenter(), new Vector3(x, y, 0)) < 30) {
            return true;
        }
        return false;
    }
}

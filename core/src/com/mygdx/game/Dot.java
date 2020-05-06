package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;

public class Dot {
    public Texture img;
    float x, y, sizeX, sizeY;
    private float timer, hesitate = 10, speed = 100;

    public Dot(float x, float y, float sizeX, float sizeY) {
        this.x = x;
        this.y = y;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
    }

    public void update(float delta) {
        if (Helper.dist(World.pers.getCenter(), new Vector3(x, y, 0)) < 200) {
            Vector3 v = Helper.norm(new Vector3(World.pers.getCenter().x - x, World.pers.getCenter().y - y, 0));
            x += v.x * speed * delta;
            y += v.y * speed * delta;
            return;
        }
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
        return Helper.dist(World.pers.getCenter(), new Vector3(x, y, 0)) < 30;
    }
}

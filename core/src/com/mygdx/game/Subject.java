package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;

public class Subject {
    public static final int sizeX = 30, sizeY = 25;
    public float x, y;
    public boolean free;
    public Texture texture;
    public String name;
    public String text;

    public Subject(Texture texture, String name) {
        this.texture = texture;
        this.name = name;
        free = true;
    }

    public void setText(String text) {
        this.text = name + "\n"+ text;
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void drawSubject() {
        MyGame.batch.draw(texture, x, y, sizeX, sizeY);
    }

    public boolean take() {
        //System.out.println("take " + x + " " + y);
        if (Helper.dist(World.pers.getCenter(), new Vector3(x, y, 0)) > 50) {
            return false;
        }
        free = false;
        return true;
    }
}

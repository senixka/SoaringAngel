package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;

public class Mob {
    int x, y;
    int sizeX, sizeY;
    Texture texture;
    boolean target;
    Texture mark;
    int hp;


    public Mob(int x, int y, int sizeX, int sizeY, Texture texture, int hp) {
        this.x = x;
        this.y = y;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.texture = texture;
        mark = new Texture(Gdx.files.internal("red.png"));
        this.hp = hp;
    }

    public Mob() {

    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void hit(int damage) {
        hp -= damage;
    }

    public void move(Vector3 vector3) {
        x += vector3.x;
        y += vector3.y;
    }

    public Vector3 getCenter() {
        return new Vector3(x + sizeX / 2, y + sizeY /2, 0);
    }

    public void draw() {
        MyGame.batch.draw(texture, x, y, sizeX, sizeY);
        if (target) {
            MyGame.batch.draw(mark, x, y - 10, sizeX, 10);
        }
    }

    public void setTarget(boolean target) {
        this.target = target;
    }

    public void update(float delta) {

    }

    public boolean isDead() {
        return (hp <= 0);
    }
}

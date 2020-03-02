package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.MapGenerator.GameMapGenerator;
import com.mygdx.game.MapGenerator.Room;

public class Mob {
    public float x, y, speed = 150;
    public int intX, intY, sizeX, sizeY, hp;
    public Texture texture, mark;
    public Room room;
    boolean target;

    public Mob(float x, float y, int sizeX, int sizeY, int hp, Room room, Texture texture) {
        this.x = x;
        this.y = y;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.texture = texture;
        this.room = room;
        this.hp = hp;
        this.mark = new Texture(Gdx.files.internal("red.png"));
        intX = (int) (x / (float) World.pixSize);
        intY = (int) (y / (float) World.pixSize);
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void hit(int damage) {
        hp -= damage;
    }

    public boolean isDead() {
        return (hp <= 0);
    }

    public Vector3 getCenter() {
        return new Vector3(x + sizeX / 2, y + sizeY / 2, 0);
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

    public void move(Vector3 vec) { }

    public void update(float delta) { }
}

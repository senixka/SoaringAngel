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
    public boolean target, isDead;

    public Mob(float x, float y, int sizeX, int sizeY, int hp, Room room, Texture texture) {
        this.x = x;
        this.y = y;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.texture = texture;
        this.room = room;
        this.hp = hp;
        this.isDead = false;
        this.mark = new Texture(Gdx.files.internal("red.png"));
        this.intX = (int) GameMapGenerator.gameCordsToMap(new Vector3(x + (float) sizeX / 2f, y + (float) sizeY / 2f, 0)).x;
        this.intY = (int) GameMapGenerator.gameCordsToMap(new Vector3(x + (float) sizeX / 2f, y + (float) sizeY / 2f, 0)).y;
    }

    public void hit(int damage) {
        hp -= damage;
    }

    public boolean isDead() {
        if (hp <= 0 && !isDead) {
            isDead = true;
            room.removeMob(this);
        }
        return isDead;
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

    public void move(float delta) {
    }

    public void update(float delta) {
    }
}

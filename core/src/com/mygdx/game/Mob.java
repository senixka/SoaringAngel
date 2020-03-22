package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.Animations.MonsterAppear;
import com.mygdx.game.Dots.EnergyDot;
import com.mygdx.game.MapGenerator.GameMapGenerator;
import com.mygdx.game.MapGenerator.Room;

public class Mob {
    public float x, y, speed = 150;
    public int intX, intY, sizeX, sizeY, hp, maxHP;
    public Texture texture, mark;
    public Room room;
    public boolean target, isDead;
    public boolean fireCondition = false, iceCondition = false;
    private float timerFire = 0, timerIce = 0;
    public static final Texture fireConditionIMG = new Texture(Gdx.files.internal("FireCondition.psd"));
    public static final Texture iceConditionIMG = new Texture(Gdx.files.internal("IceCondition.psd"));
    public static final Texture preMobIMG = new Texture(Gdx.files.internal("PreMob.psd"));
    public float startTime = 1;
    public boolean isStarted = false;


    public Mob() {

    }

    public Mob(float x, float y, int sizeX, int sizeY, int hp, Room room, Texture texture) {
        this.x = x;
        this.y = y;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.texture = texture;
        this.room = room;
        this.hp = hp;
        this.maxHP = hp;
        this.isDead = false;
        this.mark = new Texture(Gdx.files.internal("red.png"));
        this.intX = (int) GameMapGenerator.gameCordsToMap(new Vector3(x + (float) sizeX / 2f, y + (float) sizeY / 2f, 0)).x;
        this.intY = (int) GameMapGenerator.gameCordsToMap(new Vector3(x + (float) sizeX / 2f, y + (float) sizeY / 2f, 0)).y;
    }

    public void hit(int damage) {
        if (!isStarted) {
            return;
        }
        hp -= damage;
    }

    public boolean isDead() {
        if (hp <= 0 && !isDead) {
            isDead = true;
            room.removeMob(this);
            World.dots.add(new EnergyDot(x, y));
        }
        return isDead;
    }

    public Vector3 getCenter() {
        return new Vector3(x + sizeX / 2, y + sizeY / 2, 0);
    }

    public void draw() {
        if (startTime > 0) {
            MyGame.batch.draw(preMobIMG, x, y, sizeX, sizeX);
            return;
        }
        MyGame.batch.draw(texture, x, y, sizeX, sizeY);
        if (target) {
            MyGame.batch.draw(mark, x + (sizeX - ((float) sizeX / maxHP * hp)) / 2, y - 10, (float) sizeX / maxHP * hp, 10);
        }
        if (fireCondition) {
            MyGame.batch.draw(fireConditionIMG, x, y + sizeY - 10, 50, 50);
        }
        if (iceCondition) {
            MyGame.batch.draw(iceConditionIMG, x, y, sizeX, sizeX);
        }
    }

    public void setTarget(boolean target) {
        this.target = target;
    }

    public void move(float delta) {
    }

    public void update(float delta) {
        if (startTime > 0) {
            startTime -= delta;
            return;
        }
        if (!isStarted) {
            World.myAnimations.add(new MonsterAppear(x, y, sizeX, sizeY));
            isStarted = true;
        }

        if (fireCondition && ((int) (timerFire * 100)) % 5 == 0) {
            hit(1);
        }
        if (fireCondition && timerFire < 10) {
            timerFire += delta * 10;
        } else {
            fireCondition = false;
            timerFire = 0;
        }
        if (iceCondition && timerIce < 10) {
            timerIce += delta * 2;
        } else {
            iceCondition = false;
            timerIce = 0;
        }

        if (!iceCondition) {
            realUpdate(delta);
        }
    }

    public void realUpdate(float delta) {

    }
}

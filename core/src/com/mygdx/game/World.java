package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.MapGenerator.GameMapGenerator;
import com.mygdx.game.Weapons.DNKgun;
import com.mygdx.game.Weapons.FirstGun;
import com.mygdx.game.Weapons.Shotgun;

import java.util.LinkedList;
import java.util.List;

public class World {
    public static Pers pers;
    public static GameController controller;
    public static List<Mob> mobs;
    public static List<Bullet> bullets;
    public static List<Subject> subjects;
    public static int[][] map;
    public static final int pixSize = 50;
    public static Texture pix;
    public static Texture pix2;

    public static void start(GameController controller2) {
        System.out.println((int) -51 / 100);
        System.out.println((int) -1.34);
        pers = new Pers();
        pers.setPosition(-100, -100);
        controller = controller2;

        mobs = new LinkedList<Mob>();

        subjects = new LinkedList<>();
        Subject s = new Shotgun();
        s.setPosition(-100, -300);
        subjects.add(s);
        Subject s2 = new DNKgun();
        s2.setPosition(0, -300);
        subjects.add(s2);

        bullets = new LinkedList<Bullet>();

        pix = new Texture(Gdx.files.internal("StonePix.psd"));
        pix2 = new Texture(Gdx.files.internal("StonePixDown.psd"));

        createMap();
        Inventory.create();
    }

    public static void update(float delta) {
        if (!Helper.globalCheck()) {
            return;
        }
        for (Mob mob : mobs) {
            mob.update(delta);
        }
        for (Bullet bullet : bullets) {
            bullet.update(delta);
        }
        delete();

        pers.move(controller.speedVector(), delta);
        setTarget();
    }

    public static void delete() {
        List<Mob> temp = new LinkedList<>();
        for (Mob mob : mobs) {
            if (!mob.isDead()) {
                temp.add(mob);
            }
        }
        mobs = temp;

        List<Bullet> temp2 = new LinkedList<>();
        for (Bullet bullet : bullets) {
            if (!bullet.isDead()) {
                temp2.add(bullet);
            }
        }
        bullets = temp2;


    }

    public static void take() {
        if (Inventory.isFull()) {
            return;
        }
        List<Subject> temp3 = new LinkedList<>();
        for (Subject subject : subjects) {
            if (!subject.take()) {
                temp3.add(subject);
            } else {
                Inventory.add(subject);
            }
        }
        subjects = temp3;
    }

    public static void draw() {
        if (!Helper.globalCheck()) {
            return;
        }

        if (World.pers == null) {
            System.out.println("Whaat");
            return;
        }


        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map.length; j++) {
                if (map[i][j] == 0) {
                    MyGame.batch.draw(pix2, i * pixSize, j * pixSize + pixSize / 2, pixSize, pixSize / 2);
                }
            }
        }

        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map.length; j++) {
                if (map[i][j] == 0) {
                    MyGame.batch.draw(pix, i * pixSize, j * pixSize, pixSize, pixSize);
                }
            }
        }

        for (Mob mob : mobs) {
            mob.draw();
        }
        for (Bullet bullet : bullets) {
            bullet.draw();
        }
        for (Subject subject : subjects) {
            subject.drawSubject();
        }

        pers.draw();
    }

    public static Vector3 getFocus() {
        return pers.getCenter();
    }

    public static void setTarget() {
        float dist = 300;
        Mob target = null;
        for (Mob mob : mobs) {
            if (Helper.dist(mob.getCenter(), pers.getCenter()) < dist) {
                dist = Helper.dist(mob.getCenter(), pers.getCenter());
                target = mob;
            }
        }
        if (pers.target != null) {
            pers.target.setTarget(false);
            pers.target = null;
        }
        if (target != null && target != pers.target) {
            target.setTarget(true);
            pers.target = target;
        }
    }

    public static void setController(GameController controller2) {
        controller = controller2;
    }

    public static void createMap() {
        map = new GameMapGenerator(300, 300, 3, 10, 10, 0, 0).generate();
    }
}

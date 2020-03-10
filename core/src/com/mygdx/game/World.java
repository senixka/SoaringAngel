package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.MapGenerator.BossMapGenerator;
import com.mygdx.game.MapGenerator.GameMapGenerator;
import com.mygdx.game.Weapons.DNKgun;
import com.mygdx.game.Weapons.FirstGun;
import com.mygdx.game.Weapons.Relstron;
import com.mygdx.game.Weapons.Shotgun;
import com.mygdx.game.Weapons.Shotgun2;

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
    public static Texture pix, pix2, pix3;

    public static void start(GameController controller2) {
        System.out.println("Yeeeee shit here we go");
        pers = new Pers();
        pers.setPosition(-100, -100);
        controller = controller2;

        mobs = new LinkedList<Mob>();

        subjects = new LinkedList<>();
        Subject s = new Shotgun();
        s.setPosition(-100, -300);
        subjects.add(s);
        s = new DNKgun();
        s.setPosition(0, -300);
        subjects.add(s);
        s = new Shotgun2();
        s.setPosition(100, -300);
        subjects.add(s);
        s = new Relstron();
        s.setPosition(-200, -300);
        subjects.add(s);
        s = new FirstGun();
        s.setPosition(200, -300);
        subjects.add(s);

        bullets = new LinkedList<Bullet>();

        pix = new Texture(Gdx.files.internal("StonePix.psd"));
        pix2 = new Texture(Gdx.files.internal("StonePixDown.psd"));
        pix3 = new Texture(Gdx.files.internal("DoorPix.png"));

        createMap();
        Inventory.create();

    }

    public static void update(float delta) {
        if (!Helper.globalCheck()) {
            return;
        }
        for (int i = 0; i < mobs.size(); i++) {
            Mob mob = mobs.get(i);
            mob.update(delta);
        }
        for (int i = 0; i < bullets.size(); i++) {
            bullets.get(i).update(delta);
        }
        delete();

        pers.move(controller.speedVector(), delta);
        pers.update(delta);
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
                if (map[i][j] == GameMapGenerator.wallCode) {
                    MyGame.batch.draw(pix2, i * pixSize, j * pixSize + pixSize / 2, pixSize, pixSize / 2);
                }
            }
        }

        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map.length; j++) {
                if (map[i][j] == GameMapGenerator.wallCode) {
                    MyGame.batch.draw(pix, i * pixSize, j * pixSize, pixSize, pixSize);
                }
            }
        }


        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map.length; j++) {
                if (map[i][j] == GameMapGenerator.doorCode) {
                    MyGame.batch.draw(pix3, i * pixSize, j * pixSize, pixSize, pixSize);
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

        controller.draw();
    }

    public static Vector3 getFocus() {
        return pers.getCenter();
    }

    public static void setTarget() {
        float dist = 1000;
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
        long start = System.currentTimeMillis();
        //GameMapGenerator tempGenerator = new GameMapGenerator(300, 300, 3, 10, 10, 0, 0);
        //map = tempGenerator.getMap();
        BossMapGenerator tempGenerator = new BossMapGenerator(100, 100, 20);
        map = tempGenerator.getMap();
        System.out.println("Generation time: " + (double) (System.currentTimeMillis() - start) + " millis");
    }
}

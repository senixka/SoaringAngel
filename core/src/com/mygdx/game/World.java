package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.Animations.Explosion;
import com.mygdx.game.MapGenerator.BossMapGenerator;
import com.mygdx.game.MapGenerator.GameMapController;
import com.mygdx.game.MapGenerator.GameMapGenerator;
import com.mygdx.game.MapGenerator.Pair;
import com.mygdx.game.Weapons.Bazook;
import com.mygdx.game.Weapons.DNKgun;
import com.mygdx.game.Weapons.FirstGun;
import com.mygdx.game.Weapons.Relstron;
import com.mygdx.game.Weapons.Shotgun;
import com.mygdx.game.Weapons.Shotgun2;
import com.mygdx.game.Weapons.SpeedGun;
import com.mygdx.game.Weapons.TNTGun;
import com.mygdx.game.Weapons.WeaponGun;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import javax.xml.soap.Text;

import javafx.animation.Animation;

public class World {
    public static Pers pers;
    public static GameController controller;
    public static ArrayList<Mob> mobs;
    public static ArrayList<Bullet> bullets;
    public static ArrayList<Subject> subjects;
    public static ArrayList<MyAnimation> myAnimations;
    public static int[][] map;
    public static Texture miniMap;
    public static final int pixSize = 50;
    public static Texture pix, pix2, pix3;
    public static GameMapController mapController;

    public static void start(GameController controller2) {
        pers = new Pers();
        pers.setPosition(-100, -100);
        controller = controller2;

        mobs = new ArrayList<Mob>();
        myAnimations = new ArrayList<>();
        subjects = new ArrayList<>();
        bullets = new ArrayList<Bullet>();

        pix = new Texture(Gdx.files.internal("StonePix.psd"));
        pix2 = new Texture(Gdx.files.internal("StonePixDown.psd"));
        pix3 = new Texture(Gdx.files.internal("DoorPix.png"));

        createMap();
        Inventory.create();

        Inventory.add(new FirstGun());
        Inventory.add(new Bazook());
        Inventory.add(new TNTGun());
        Inventory.add(new Shotgun2());
        Inventory.add(new Shotgun());
        Inventory.add(new DNKgun());
        Inventory.add(new Relstron());
        Inventory.add(new SpeedGun());
        Inventory.add(new WeaponGun());

        Pair temp = World.mapController.teleportPersInMaze();
        Vector3 tmp = GameMapController.mapCordsToGame(new Vector3(temp.first, temp.second, 0));
        World.pers.setPosition(tmp.x, tmp.y);

    }

    public static void update(float delta) {
        if (!Helper.globalCheck()) {
            return;
        }
        for (MyAnimation animation : myAnimations) {
            animation.update(delta);
        }

        // Надо думать как ускорить этот кусок кода,
        // Осимптотика тут O(n^2)
        //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
        for (int i = 0; i < mobs.size(); i++) {
            mobs.get(i).update(delta);
        }
        for (int i = 0; i < bullets.size(); i++) {
            bullets.get(i).update(delta);
        }
        //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

        delete();

        pers.move(controller.speedVector(), delta);
        pers.update(delta);
        setTarget();

        mapController.update();
    }

    public static void delete() {
        Iterator<Mob> itMob = mobs.iterator();
        while (itMob.hasNext()) {
            Mob mob = itMob.next();
            if (mob.isDead()) {
                itMob.remove();
            }
        }
//        for (int i = 0; i < mobs.size(); ++i) {
//            if (mobs.get(i).isDead()) {
//                mobs.remove(i);
//                --i;
//            }
//        }

        Iterator<Bullet> itBullet = bullets.iterator();
        while (itBullet.hasNext()) {
            Bullet bullet = itBullet.next();
            if (bullet.isDead()) {
                itBullet.remove();
            }
        }
//        for (int i = 0; i < bullets.size(); ++i) {
//            if (bullets.get(i).isDead()) {
//                bullets.remove(i);
//                --i;
//            }
//        }

        Iterator<MyAnimation> itAnim = myAnimations.iterator();
        while (itAnim.hasNext()) {
            MyAnimation myAnim = itAnim.next();
            if (myAnim.isDead()) {
                itAnim.remove();
            }
        }

//        for (int i = 0; i < myAnimations.size(); ++i) {
//            if (myAnimations.get(i).isDead()) {
//                myAnimations.remove(i);
//                --i;
//            }
//        }
    }

    public static void take() {
        if (Inventory.isFull()) {
            return;
        }

        Iterator<Subject> itSub = subjects.iterator();
        while (itSub.hasNext()) {
            Subject sub = itSub.next();
            if (sub.take()) {
                Inventory.add(sub);
                itSub.remove();
            }
        }

//        for (int i = 0; i < subjects.size(); ++i) {
//            if (subjects.get(i).take()) {
//                Inventory.add(subjects.get(i));
//                subjects.remove(i);
//                --i;
//            }
//        }
    }

    public static void draw() {
        if (!Helper.globalCheck()) {
            return;
        }

        if (World.pers == null) {
            System.out.println("Whaat");
            return;
        }

        Vector3 temp = GameMapController.gameCordsToMap(pers.getCenter());
        int persX = (int) temp.x, persY = (int) temp.y, drawDist = 25;

        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if (map[i][j] == GameMapGenerator.wallCode) {
                    if (Math.abs(persX - i) < drawDist && Math.abs(persY - j) < drawDist) {
                        MyGame.batch.draw(pix2, i * pixSize, j * pixSize - pixSize / 2, pixSize, pixSize / 2);
                    }
                }
            }
        }

        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if (map[i][j] == GameMapGenerator.wallCode) {
                    if (Math.abs(persX - i) < drawDist && Math.abs(persY - j) < drawDist) {
                        MyGame.batch.draw(pix, i * pixSize, j * pixSize, pixSize, pixSize);
                    }
                }
            }
        }

        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if (Math.abs(map[i][j]) == GameMapGenerator.openDoorCode) {
                    if (Math.abs(persX - i) < drawDist && Math.abs(persY - j) < drawDist) {
                        MyGame.batch.draw(pix3, i * pixSize, j * pixSize, pixSize, pixSize);
                    }
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
        for (MyAnimation animation : myAnimations) {
            animation.draw();
        }

        pers.draw();

        controller.draw();
    }

    public static Vector3 getFocus() {
        return pers.getCenter();
    }

    public static void setTarget() {
        float dist = 100000;
        Mob target = null;
        for (Mob mob : mobs) {
            Vector3 tempV = new Vector3(mob.getCenter().x - pers.getCenter().x, mob.getCenter().y - pers.getCenter().y, 0).nor();
            float delt = 10;
            float tempX = pers.getCenter().x, tempY = pers.getCenter().y;
            boolean ret = false;
            while (Helper.dist(mob.getCenter(), pers.getCenter()) > Helper.dist(pers.getCenter(), new Vector3(tempX, tempY, 0))) {
                tempX += tempV.x * delt;
                tempY += tempV.y * delt;
                if (Helper.intersectWall(new Rectangle(tempX, tempY, 1, 1))) {
                    ret = true;
                    break;
                }
            }
            if (ret) {
                continue;
            }
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
        GameMapGenerator tempGenerator = new GameMapGenerator(300, 300, 8, 10, 10, 30, "prt", 30, 30);
        map = tempGenerator.getMap();
        miniMap = tempGenerator.getMiniMap();
        mapController = new GameMapController(tempGenerator);
        //BossMapGenerator tempGenerator = new BossMapGenerator(100, 100, 20);
        //map = tempGenerator.getMap();
        System.out.println("Generation time: " + (double) (System.currentTimeMillis() - start) + " millis");
    }
}

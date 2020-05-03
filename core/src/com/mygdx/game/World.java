package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.MapGenerator.BossMapController;
import com.mygdx.game.MapGenerator.BossMapGenerator;
import com.mygdx.game.MapGenerator.ConstantMapController;
import com.mygdx.game.MapGenerator.ConstantMapGenerator;
import com.mygdx.game.MapGenerator.GameMapController;
import com.mygdx.game.MapGenerator.GameMapGenerator;
import com.mygdx.game.MapGenerator.MapController;
import com.mygdx.game.MapGenerator.MapGenerator;
import com.mygdx.game.MapGenerator.Pair;
import com.mygdx.game.NPCs.Portal;
import com.mygdx.game.NPCs.Seller;
import com.mygdx.game.Potions.EnergyPotion;
import com.mygdx.game.Potions.HealthPotion;
import com.mygdx.game.Weapons.Bazook;
import com.mygdx.game.Weapons.DNKgun;
import com.mygdx.game.Weapons.FirstGun;
import com.mygdx.game.Weapons.Flamethrower;
import com.mygdx.game.Weapons.Icethrower;
import com.mygdx.game.Weapons.Relstron;
import com.mygdx.game.Weapons.RicochetGun;
import com.mygdx.game.Weapons.Shotgun;
import com.mygdx.game.Weapons.Shotgun2;
import com.mygdx.game.Weapons.SpeedGun;
import com.mygdx.game.Weapons.TNTGun;
import com.mygdx.game.Weapons.WeaponGun;

import java.util.ArrayList;
import java.util.Iterator;

public class World {
    public static final int pixSize = 50;
    public static Pers pers;
    public static GameController controller;
    public static ArrayList<Mob> mobs;
    public static ArrayList<Bullet> bullets;
    public static ArrayList<Subject> subjects;
    public static ArrayList<MyAnimation> myAnimations;
    public static ArrayList<NPC> npcs;
    public static ArrayList<Dot> dots;
    public static int[][] map;
    public static Texture miniMap;
    public static Texture pix, pix2, pix3, pix4;

//    public static GameMapController gameMapController;
//    public static BossMapController bossMapController;

    public static MapController mapController;

    public static Texture CD1, CD2, CD3;
    public static boolean CDFlag = false;
    public static int CDCnt = 150;
    public static int mapLevel;

    public static void start(GameController controller2) {
        pers = new Pers();
        pers.setPosition(-100, -100);
        controller = controller2;

        mobs = new ArrayList<Mob>();
        myAnimations = new ArrayList<>();
        subjects = new ArrayList<>();
        bullets = new ArrayList<Bullet>();

        npcs = new ArrayList<>();
        npcs.add(new Seller(-100, -100));

        dots = new ArrayList<>();

        pix = new Texture(Gdx.files.internal("StoneUp.psd"));
        pix2 = new Texture(Gdx.files.internal("StoneDown.psd"));
        pix3 = new Texture(Gdx.files.internal("DoorPix.png"));
        pix4 = new Texture(Gdx.files.internal("StoneFloor.psd"));
        CD1 = new Texture(Gdx.files.internal("CountDown1.png"));
        CD2 = new Texture(Gdx.files.internal("CountDown2.png"));
        CD3 = new Texture(Gdx.files.internal("CountDown3.png"));

        mapLevel = 0;
        createMap();
        Inventory.create();
        Shop.create();

        Inventory.add(Helper.getRandomWeapon());
        Inventory.add(Helper.getRandomWeapon());
        Inventory.add(new HealthPotion());
        Inventory.add(new EnergyPotion());

//        Inventory.add(new Bazook());
//        Inventory.add(new FirstGun());
//        Inventory.add(new TNTGun());
//        Inventory.add(new Shotgun2());
//        Inventory.add(new Shotgun());
//        Inventory.add(new DNKgun());
        //Inventory.add(new Relstron());
        //Inventory.add(new SpeedGun());
        //Inventory.add(new WeaponGun());
//        Inventory.add(new Flamethrower());
//        Inventory.add(new Icethrower());
//        Inventory.add(new RicochetGun());

        //Pair temp = World.gameMapController.teleportPersInMaze();
//        Pair temp = mapController.teleportPersInMaze();
//        Vector3 tmp = GameMapController.mapCordsToGame(new Vector3(temp.first, temp.second, 0));
//        World.pers.setPosition(tmp.x, tmp.y);
//        npcs.add(new Portal(tmp.x + 200, tmp.y));

    }

    public static void update(float delta) {
        if (!Helper.globalCheck()) {
            return;
        }

        if (CDFlag) {
            return;
        }

        for (MyAnimation animation : myAnimations) {
            animation.update(delta);
        }

        for (Dot dot : dots) {
            dot.update(delta);
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


        if (mapController != null) {
            mapController.update();
        }

//        if (gameMapController != null) {
//            if (gameMapController.goToNextLevel()) {
//                BossMapGenerator bossGen = new BossMapGenerator(100, 100, 35);
//                bossMapController = new BossMapController(bossGen);
//                map = bossMapController.getMap();
//                miniMap = bossMapController.getMiniMap();
//                Pair temp = World.bossMapController.teleportPersInMaze();
//                Vector3 tmp = BossMapController.mapCordsToGame(new Vector3(temp.first, temp.second, 0));
//                World.pers.setPosition(tmp.x, tmp.y);
//                subjects.clear();
//                gameMapController = null;
//                CDFlag = true;
//            } else {
//                gameMapController.update();
//            }
//        } else if (bossMapController != null) {
//            if (bossMapController.goToNextLevel()) {
//                GameMapGenerator gameGen = new GameMapGenerator(250, 250, 13, 15, 15, 30, "prt", 30, 30);
//                gameMapController = new GameMapController(gameGen);
//                map = gameMapController.getMap();
//                miniMap = gameMapController.getMiniMap();
//                Pair temp = World.gameMapController.teleportPersInMaze();
//                Vector3 tmp = GameMapController.mapCordsToGame(new Vector3(temp.first, temp.second, 0));
//                World.pers.setPosition(tmp.x, tmp.y);
//                subjects.clear();
//                bossMapController = null;
//                CDFlag = true;
//            } else {
//                bossMapController.update();
//            }
//        }
    }

    public static void delete() {
        Iterator<Mob> itMob = mobs.iterator();
        while (itMob.hasNext()) {
            Mob mob = itMob.next();
            if (mob.isDead()) {
                itMob.remove();
            }
        }

        Iterator<Dot> itDot = dots.iterator();
        while (itDot.hasNext()) {
            Dot dot = itDot.next();
            if (dot.take()) {
                itDot.remove();
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

    public static boolean take() {
        if (Inventory.isFull()) {
            return false;
        }

        Iterator<Subject> itSub = subjects.iterator();
        while (itSub.hasNext()) {
            Subject sub = itSub.next();
            if (sub.take()) {
                Inventory.add(sub);
                itSub.remove();
                return true;
            }
        }
        return false;

//        for (int i = 0; i < subjects.size(); ++i) {
//            if (subjects.get(i).take()) {
//                Inventory.add(subjects.get(i));
//                subjects.remove(i);
//                --i;
//            }
//        }
    }

    public static boolean talk() {
        for (NPC npc : npcs) {
            if (npc.talk()) {
                return true;
            }
        }
        return false;
    }

    public static void draw() {
        if (!Helper.globalCheck()) {
            return;
        }

        if (World.pers == null) {
            System.out.println("Whaat");
            return;
        }

        if (CDFlag) {
            float sizeX = MyGame.camera.viewportWidth;
            float sizeY = MyGame.camera.viewportHeight;
            float x = MyGame.camera.position.x - sizeX / 2;
            float y = MyGame.camera.position.y - sizeY / 2;

            if (CDCnt > 100) {
                MyGame.batch.draw(CD3, x, y, sizeX, sizeY);
            } else if (CDCnt > 50) {
                MyGame.batch.draw(CD2, x, y, sizeX, sizeY);
            } else if (CDCnt > 0) {
                MyGame.batch.draw(CD1, x, y, sizeX, sizeY);
            } else {
                CDCnt = 151;
                CDFlag = false;
            }
            --CDCnt;
            return;
        }

        Vector3 temp = GameMapController.gameCordsToMap(pers.getCenter());
        int persX = (int) temp.x, persY = (int) temp.y, drawDist = 25;

        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if (Math.abs(persX - i) < drawDist && Math.abs(persY - j) < drawDist) {
                    if (map[i][j] == GameMapGenerator.wallCode) {
                        MyGame.batch.draw(pix2, i * pixSize, j * pixSize - pixSize, pixSize, pixSize);
                    }
                    if (map[i][j] == GameMapGenerator.spaceCode) {
                        MyGame.batch.draw(pix4, i * pixSize, j * pixSize, pixSize, pixSize);
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
        for (Dot dot : dots) {
            dot.draw();
        }

        for (NPC npc : npcs) {
            npc.draw1();
        }
        if (!pers.isDead()) {
            pers.draw();
        }
        for (NPC npc : npcs) {
            npc.draw2();
        }

        controller.draw();
    }

    public static Vector3 getFocus() {
        return pers.getCenter();
    }

    public static void setTarget() {
        float dist = 100000;
        Mob target = null;
        for (Mob mob : mobs) {
            if (!mob.isStarted) {
                continue;
            }
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
        MapGenerator mapGenerator;
        if (mapLevel == 0) {
            mapGenerator = new ConstantMapGenerator("Maps/StartRoom.txt");
            mapController = new ConstantMapController(mapGenerator);
        } else if (mapLevel % 2 == 0) {
            mapGenerator = new BossMapGenerator(100, 100, 35);
            mapController = new BossMapController((BossMapGenerator) mapGenerator);
        } else {
            mapGenerator = new GameMapGenerator(250, 250, 13, 15, 15, 30, "prt", 30, 30);
            mapController = new GameMapController((GameMapGenerator) mapGenerator);
        }
        map = mapController.getMap();
        miniMap = mapController.getMiniMap();
        Pair temp = mapController.teleportPersInMaze();
        Vector3 tmp = BossMapController.mapCordsToGame(new Vector3(temp.first, temp.second, 0));
        World.pers.setPosition(tmp.x, tmp.y);
        npcs.add(new Portal(tmp.x + 200, tmp.y));
        CDFlag = true;
//        long start = System.currentTimeMillis();
//        GameMapGenerator tempGenerator = new GameMapGenerator(250, 250, 13, 15, 15, 30, "prt", 30, 30);
//        map = tempGenerator.getMap();
//        miniMap = tempGenerator.getMiniMap();
//        //gameMapController = new GameMapController(tempGenerator);
//        mapController = new GameMapController(tempGenerator);
//        System.out.println("Generation time: " + (double) (System.currentTimeMillis() - start) + " millis");
    }

    public static void nextLevel() {
        clear(); // удаляем всё что осталось с предыдущего левела
        mapLevel++;
        createMap();
    }

    public static void clear() {
        mobs.clear();
        npcs.clear();
        bullets.clear();
        dots.clear();
        myAnimations.clear();
        subjects.clear();
    }
}

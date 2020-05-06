package com.mygdx.game;

import com.badlogic.gdx.math.Vector3;
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

import java.util.Random;

public class Helper {
    public static final Random rnd = new Random();

    public static float dist(Vector3 v, Vector3 v2) {
        float a = v.x - v2.x;
        float b = v.y - v2.y;
        return (float) Math.pow(a * a + b * b, 0.5);
    }

    public static Vector3 norm(Vector3 v) {
        Vector3 ans = v.cpy();
        if (v.equals(Vector3.Zero)) {
            return ans;
        }
        ans.x = ans.x / dist(Vector3.Zero, v);
        ans.y = ans.y / dist(Vector3.Zero, v);
        return ans;
    }

    public static void rotate(Vector3 v, float degree) {
        v.rotate(degree, 0, 0, 1);
    }

    public static boolean globalCheck() {
        return (World.pers != null);
    }

    public static boolean intersect(float x1, float y1, float sizeX1, float sizeY1, float x2, float y2, float sizeX2, float sizeY2) {
        Rectangle r1 = new Rectangle(x1, y1, sizeX1, sizeY1);
        Rectangle r2 = new Rectangle(x2, y2, sizeX2, sizeY2);
        return r1.intersect(r2);
    }

    public static boolean intersect(Rectangle r1, Rectangle r2) {
        return r1.intersect(r2);
    }

    public static boolean intersectWall(Rectangle r) {

        int tempX = (int) r.x / World.pixSize;
        int tempY = (int) r.y / World.pixSize;
        Rectangle r1 = new Rectangle(tempX * World.pixSize, tempY * World.pixSize, World.pixSize, World.pixSize);
        try {
            if (World.map[tempX][tempY] == 0 && r1.contains(r.x, r.y)) {
                return true;
            }
            //r1.x += World.pixSize;
            tempX = (int) (r.x + r.width) / World.pixSize;
            tempY = (int) (r.y) / World.pixSize;
            r1 = new Rectangle(tempX * World.pixSize, tempY * World.pixSize, World.pixSize, World.pixSize);

            if (World.map[tempX][tempY] == 0 && r1.contains(r.x + r.width, r.y)) {
                return true;
            }

            tempX = (int) (r.x) / World.pixSize;
            tempY = (int) (r.y + r.height) / World.pixSize;
            r1 = new Rectangle(tempX * World.pixSize, tempY * World.pixSize, World.pixSize, World.pixSize);

            if (World.map[tempX][tempY] == 0 && r1.contains(r.x, r.y + r.height)) {
                return true;
            }

            tempX = (int) (r.x + r.width) / World.pixSize;
            tempY = (int) (r.y + r.height) / World.pixSize;
            r1 = new Rectangle(tempX * World.pixSize, tempY * World.pixSize, World.pixSize, World.pixSize);

            if (World.map[tempX][tempY] == 0 && r1.contains(r.x + r.width, r.y + r.height)) {
                return true;
            }

        } catch (IndexOutOfBoundsException e) {

        }

        return false;
    }

    public static Weapon getRandomWeapon() {
        int r = Math.abs(rnd.nextInt()) % 12;
        if (r == 0) {
            return new Bazook();
        } else if (r == 1) {
            return new DNKgun();
        } else if (r == 2) {
            return new FirstGun();
        } else if (r == 3) {
            return new Flamethrower();
        } else if (r == 4) {
            return new Icethrower();
        } else if (r == 5) {
            return new Relstron();
        } else if (r == 6) {
            return new RicochetGun();
        } else if (r == 7) {
            return new Shotgun();
        } else if (r == 8) {
            return new Shotgun2();
        } else if (r == 9) {
            return new SpeedGun();
        } else if (r == 10) {
            return new TNTGun();
        } else if (r == 11) {
            return new WeaponGun();
        }

        return null;
    }

    public static Vector3 getRndVector() {
        return norm(new Vector3(rnd.nextInt(), rnd.nextInt(), 0));
    }
}

package com.mygdx.game;

import com.badlogic.gdx.math.Vector3;

public class Helper {
    public static float dist(Vector3 v, Vector3 v2) {
        float a = v.x - v2.x;
        float b = v.y - v2.y;
        return (float) Math.pow(a * a + b * b, 0.5);
    }

    public static Vector3 norm(Vector3 v) {
        Vector3 ans = v.cpy();
        if (v.equals(v.Zero)) {
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
            r1.x += World.pixSize;
            if (World.map[tempX + 1][tempY] == 0 && r1.contains(r.x + r.width, r.y)) {
                return true;
            }
            r1.x -= World.pixSize;
            r1.y += World.pixSize;
            if (World.map[tempX][tempY + 1] == 0 && r1.contains(r.x, r.y + r.height)) {
                return true;
            }
            r1.x += World.pixSize;
            if (World.map[tempX + 1][tempY + 1] == 0 && r1.contains(r.x + r.width, r.y + r.height)) {
                return true;
            }

        } catch (IndexOutOfBoundsException e) {

        }

        return false;
    }
}

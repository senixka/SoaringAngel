package com.mygdx.game;

import com.badlogic.gdx.math.Vector3;

/**
 * Created by iliamikado on 03.10.2019.
 */

public class Rectangle {
    float x, y, width, height;

    public Rectangle(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public boolean contains(float x, float y) {
        return (this.x <= x && x <= this.x + width && this.y <= y && y <= this.y + height);
    }

    public boolean contains(Vector3 vector3) {
        int x = (int) vector3.x;
        int y = (int) vector3.y;
        return (this.x <= x && x <= this.x + width && this.y <= y && y <= this.y + height);
    }

    public boolean intersect(Rectangle rect) {
        if (contains(rect.x, rect.y) || contains(rect.x + rect.width, rect.y) ||
                contains(rect.x, rect.y + rect.height) || contains(rect.x + rect.width, rect.y + rect.height)) {
            return true;
        }

        if (rect.contains(x, y) || rect.contains(x + width, y) || rect.contains(x, y + height) ||
                rect.contains(x + width, y + height)) {
            return true;
        }

//        if (x < rect.x && rect.x + rect.width < x + width && rect.y < y && y + height < rect.y + rect.height) {
//            return true;
//        }

        return false;
    }
}

package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;

public class Bullet {
    Texture texture;
    Vector3 vect;
    int speed;
    int damage;
    float x, y;
    float sizeX, sizeY;
    int dead = 500;

    public Bullet(Texture texture, int speed, float sizeX, float sizeY, int damage, Vector3 vect, float x, float y) {
        this.texture = texture;
        this.vect = vect;
        //this.speed = speed;
        this.speed = 600;
        this.x = x;
        this.y = y;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.damage = damage;
        World.bullets.add(this);
    }

    public void update(float delta) {
        x += speed * delta * vect.x;
        y += speed * delta * vect.y;
        dead --;
    }

    public  boolean isDead (){
        for (Mob mob : World.mobs) {
            if (Helper.intersect(mob.x, mob.y, mob.sizeX, mob.sizeY, x, y, sizeX, sizeY)) {
                mob.hit(damage);
                System.out.println("Shoooot");
                return true;
            }
        }

//        for (Wall wall : World.walls) {
//            if (Helper.intersect(wall.x, wall.y, wall.sizeX, wall.sizeY, x, y, sizeX, sizeY)) {
//                System.out.println(wall.x + " " + wall.y + " " +  x + " " + sizeX + " " + y);
//                return true;
//            }
//        }
        int tempX = (int) x / World.pixSize;
        int tempY = (int) y / World.pixSize;
        Rectangle r1 = new Rectangle(tempX * World.pixSize, tempY * World.pixSize, World.pixSize, World.pixSize);
        try {
            if (World.map[tempX][tempY] == 0 && r1.contains(x, y)) {
                return true;
            }
            r1.x += World.pixSize;
            if (World.map[tempX + 1][tempY] == 0 && r1.contains(x + sizeX, y)) {
                return true;
            }
            r1.x -= World.pixSize;
            r1.y += World.pixSize;
            if (World.map[tempX][tempY + 1] == 0 && r1.contains(x, y + sizeY)) {
                return true;
            }
            r1.x += World.pixSize;
            if (World.map[tempX + 1][tempY + 1] == 0 && r1.contains(x + sizeX, y + sizeY)) {
                return true;
            }

        } catch (IndexOutOfBoundsException e) {

        }
        return dead<0;
    }
    public void draw() {
        MyGame.batch.draw(texture, x, y, sizeX, sizeY);
    }
}


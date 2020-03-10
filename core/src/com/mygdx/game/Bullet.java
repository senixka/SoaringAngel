package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;

public class Bullet {
    public float x, y, sizeX, sizeY;
    public int speed, damage, dead = 500;
    public Texture texture;
    public Vector3 vect;
    public boolean isEnemy = false;

    public Bullet(Texture texture, int speed, float sizeX, float sizeY, int damage, Vector3 vect, float x, float y) {
        this.texture = texture;
        this.vect = vect;
        this.speed = speed;

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
        dead--;
    }

    public boolean isDead() {
        if (!isEnemy) {
            for (Mob mob : World.mobs) {
                if (Helper.intersect(mob.x, mob.y, mob.sizeX, mob.sizeY, x, y, sizeX, sizeY)) {
                    mob.hit(damage);
                    return true;
                }
            }
        } else {
            if (Helper.intersect(World.pers.getBody(), new Rectangle(x, y, sizeX, sizeY))) {
                World.pers.hit(damage);
                return true;
            }
        }
        if (Helper.intersectWall(new Rectangle(x, y, sizeX, sizeY))) {
            return true;
        }
        return dead < 0;
    }

    public void draw() {
        MyGame.batch.draw(texture, x, y, sizeX, sizeY);
    }
}

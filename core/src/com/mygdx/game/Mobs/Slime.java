package com.mygdx.game.Mobs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.Bullet;
import com.mygdx.game.Bullets.FirstBullet;
import com.mygdx.game.Helper;
import com.mygdx.game.MapGenerator.GameMapGenerator;
import com.mygdx.game.MapGenerator.Pair;
import com.mygdx.game.MapGenerator.Room;
import com.mygdx.game.Mob;
import com.mygdx.game.Rectangle;
import com.mygdx.game.World;

import java.util.ArrayList;
import java.util.Vector;

public class Slime extends Mob {

    public int timer = 0;
    private static final Texture zombie = new Texture(Gdx.files.internal("Zombie.psd"));

    public Slime() {

    }

    public Slime(float x, float y, Room room) {
        super(x, y, 40, 40, 20, room, zombie);
    }

    @Override
    public void move(float delta) {
        Vector3 newVec = new Vector3(World.pers.getCenter().x, World.pers.getCenter().y, 0);
        int targetX = (int) GameMapGenerator.gameCordsToMap(newVec).x;
        int targetY = (int) GameMapGenerator.gameCordsToMap(newVec).y;
        if (!room.isPointInRoom(targetX, targetY)) {
            return;
        }

        ArrayList<Pair> path = room.findMobPath(this, targetX, targetY);
        if (path.size() <= 1) {
            return;
        }

        Vector3 tmp = GameMapGenerator.mapCordsToGame(new Vector3(path.get(1).first, path.get(1).second, 0));
        Vector3 moveVec = new Vector3(tmp.x - (x + (float) sizeX / (float) 2), tmp.y - (y + (float) sizeY / (float) 2), 0).nor();
        moveVec.x *= delta * speed;
        moveVec.y *= delta * speed;

        if (!Helper.intersectWall(new Rectangle(x + moveVec.x, y + moveVec.y, sizeX, sizeY / 2)) || true) {
            boolean flag = false;
            for (int i = 0; i < room.mobs.size(); ++i) {
                Mob mob = room.mobs.get(i);
                if (super.equals(mob)) {
                    break;
                }
                if (Helper.intersect(new Rectangle(x + moveVec.x, y + moveVec.y, sizeX, sizeY),
                        new Rectangle(mob.x, mob.y, mob.sizeX, mob.sizeY))) {
                    flag = true;
                    break;
                }
            }
            flag = false;
            if (!flag) {
                x += moveVec.x;
                y += moveVec.y;
                intX = (int) GameMapGenerator.gameCordsToMap(new Vector3(x + (float) sizeX / 2f, y + (float) sizeY / 2f, 0)).x;
                intY = (int) GameMapGenerator.gameCordsToMap(new Vector3(x + (float) sizeX / 2f, y + (float) sizeY / 2f, 0)).y;
            }
        }
    }

    private void shoot(Vector3 vec) {
        Vector3 newVec = new Vector3(World.pers.getCenter().x, World.pers.getCenter().y, 0);
        int targetX = (int) GameMapGenerator.gameCordsToMap(newVec).x;
        int targetY = (int) GameMapGenerator.gameCordsToMap(newVec).y;
        if (!room.isPointInRoom(targetX, targetY)) {
            return;
        }
        if (timer == 0) {
            Bullet b = new FirstBullet(Helper.norm(vec), x + sizeX / 2, y + sizeY / 2);
            b.isEnemy = true;
            timer = 20;
        } else {
            --timer;
        }
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        if (iceCondition) {
            return;
        }
        float perX = World.pers.getCenter().x, perY = World.pers.getCenter().y;
        Vector3 vec = new Vector3(perX - (x + (float) sizeX / 2),
                perY - (y + (float) sizeY / 2), 0).nor();
        vec.x *= delta * speed;
        vec.y *= delta * speed;
        move(delta);
        shoot(vec);
    }
}

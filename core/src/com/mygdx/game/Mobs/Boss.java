package com.mygdx.game.Mobs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.Bullets.FirstBullet;
import com.mygdx.game.Helper;
import com.mygdx.game.MapGenerator.GameMapGenerator;
import com.mygdx.game.MapGenerator.Pair;
import com.mygdx.game.MapGenerator.Room;
import com.mygdx.game.Mob;
import com.mygdx.game.Rectangle;
import com.mygdx.game.World;

import java.util.ArrayList;

public class Boss extends Mob {
    private static final Texture boss = new Texture(Gdx.files.internal("Boss.png"));
    public float timer = 0, mobTimer = 0;

    public Boss() {

    }

    public Boss(float x, float y, Room room) {
        super(x, y, 150, 150, 2000, room, boss);
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

    private void shoot(Vector3 vec, float delta) {
        Vector3 newVec = new Vector3(World.pers.getCenter().x, World.pers.getCenter().y, 0);
        int targetX = (int) GameMapGenerator.gameCordsToMap(newVec).x;
        int targetY = (int) GameMapGenerator.gameCordsToMap(newVec).y;
        if (!room.isPointInRoom(targetX, targetY)) {
            return;
        }

        if (hp < maxHP / 2) {
            if (timer <= 0) {
                timer = 1.5f;
                Vector3 tempVec;
                for (int i = 0; i < 360; i += 20) {
                    tempVec = new Vector3((float) Math.sin((double) System.currentTimeMillis() / 10d), (float) Math.cos((double) System.currentTimeMillis() / 10d), 0);
                    FirstBullet b = new FirstBullet(tempVec.rotate(new Vector3(0, 0, 1), i), x + sizeX / 2, y + sizeY / 2, true);
                }
            } else {
                timer -= delta;
            }
            if (mobTimer <= 0) {
                mobTimer = 15;
                for (int i = 0; i < 5; ++i) {
                    room.createMob(new Slime());
                }
            } else {
                mobTimer -= delta;
            }
        } else {
            if (timer <= 0) {
                timer = 1.5f;
                Vector3 tempVec;
                for (int i = -20; i <= 20; i += 6) {
                    tempVec = vec.cpy().nor();
                    FirstBullet b = new FirstBullet(tempVec.rotate(new Vector3(0, 0, 1), i), x + sizeX / 2, y + sizeY / 2, true);
                }
            } else {
                timer -= delta;
            }
        }
    }

    @Override
    public void realUpdate(float delta) {
        float perX = World.pers.getCenter().x, perY = World.pers.getCenter().y;
        Vector3 vec = new Vector3(perX - (x + (float) sizeX / 2), perY - (y + (float) sizeY / 2), 0).nor();
        vec.x *= delta * speed;
        vec.y *= delta * speed;
        move(delta);
        shoot(vec, delta);
    }
}

package com.mygdx.game.Mobs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.Bullet;
import com.mygdx.game.Bullets.FirstBullet;
import com.mygdx.game.Helper;
import com.mygdx.game.MapGenerator.GameMapGenerator;
import com.mygdx.game.MapGenerator.Room;
import com.mygdx.game.Mob;
import com.mygdx.game.Rectangle;
import com.mygdx.game.World;

public class Slime extends Mob {

    public int timer = 0;

    public Slime(float x, float y, Room room) {
        super(x, y, 50, 50, 20, room, new Texture(Gdx.files.internal("Zombie.psd")));
    }

    @Override
    public void move(Vector3 vec) {
        Vector3 newVec = GameMapGenerator.gameCordsToMap(
                new Vector3(vec.x + x + (float) sizeX / 2, vec.y + y + (float) sizeY / 2, 0));
//        if (!Helper.intersectWall(new Rectangle(x + vec.x, y + vec.y, sizeX, sizeY / 2))) {
//            x += vec.x;
//            y += vec.y;
//        }
        if (!Helper.intersectWall(new Rectangle(x + vec.x, y + vec.y, sizeX, sizeY / 2))) {
            if (room.isPointAccessible((int) newVec.x, (int) newVec.y)) {
                x += vec.x;
                y += vec.y;
            }
        }
    }

    private void shoot(Vector3 vec) {
        if (timer == 0) {
            //new FirstBullet(Helper.norm(vec), x + sizeX / 2 + 20 * vec.x, y + sizeY / 2 + 20 * vec.y);
            timer = 20;
        } else {
            --timer;
        }
    }

    @Override
    public void update(float delta) {
        float perX = World.pers.getCenter().x, perY = World.pers.getCenter().y;
        Vector3 vec = new Vector3(perX - (x + (float) sizeX / 2),
                perY - (y + (float) sizeY / 2), 0).nor();
        vec.x *= delta * speed;
        vec.y *= delta * speed;
        move(vec);
        shoot(vec);
    }
}

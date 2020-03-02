package com.mygdx.game.MapGenerator;

import com.mygdx.game.Mobs.Slime;
import com.mygdx.game.Mob;
import com.mygdx.game.World;

import java.security.SecureRandom;
import java.util.ArrayList;

public class Room {
    public int x, y, width, height;
    public boolean isRoomVisible;
    public ArrayList<ArrayList<Pair>> ways;
    public ArrayList<Mob> mobs;
    private SecureRandom secureRandom;

    public Room(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.isRoomVisible = true;
        this.ways = new ArrayList<>();
        this.mobs = new ArrayList<>();
        this.secureRandom = new SecureRandom();
    }

    public Pair getRandomPointInRoom() {
        int newX = (Math.abs(secureRandom.nextInt()) % height);
        int newY = (Math.abs(secureRandom.nextInt()) % width);
        return (new Pair(x + newX, y + newY));
    }

    public void addWay(ArrayList<Pair> way) {
        ways.add(way);
    }

    public void addMob(int mobX, int mobY) {
        mobs.add(new Slime(mobX, mobY, this));
        World.mobs.add(mobs.get(mobs.size() - 1));
    }

    public boolean isPointInRoom(int pointX, int pointY) {
        if (x <= pointX && pointX < x + height && y <= pointY && pointY < y + width) {
            return true;
        }
        return false;
    }
}

package com.mygdx.game.MapGenerator;

import com.mygdx.game.Mobs.Slime;
import com.mygdx.game.Mob;
import com.mygdx.game.World;

import java.util.ArrayList;

public class Room {
    public int x, y, width, height;
    public boolean isRoomVisible;
    public ArrayList<ArrayList<Pair>> ways;
    public ArrayList<Mob> mobs;
    public Shelter shelter;
    public Node node;
    public ArrayList<Room> connectedRooms;
    private int[][] localGameMap;

    public Room(Node node, int x, int y, int width, int height, int[][] localGameMap) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.isRoomVisible = true;
        this.node = node;
        this.ways = new ArrayList<>();
        this.mobs = new ArrayList<>();
        this.shelter = new Shelter();
        this.localGameMap = localGameMap;
    }

    public Pair getRandomPointInRoom() {
        int newX = Rand.AbsModInt(height);
        int newY = Rand.AbsModInt(width);
        return (new Pair(x + newX, y + newY));
    }

    public Pair getCenterPointInRoom() {
        return (new Pair(x + height / 2, y + width / 2));
    }

    public void addConnectedRoom(Room room) {
        connectedRooms.add(room);
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

    public boolean isPointAccessible(int pointX, int pointY) {
        if (isPointInRoom(pointX, pointY)) {
            return true;
        }
        return false;
    }

    public void removeMob(Mob mob) {
        mobs.remove(mob);
    }

    public void createMob() {
        Pair cords = getRandomPointInRoom();
        while (localGameMap[cords.first][cords.second] != 1) {
            cords = getRandomPointInRoom();
        }
        addMob(cords.first * World.pixSize, cords.second * World.pixSize);
    }
}

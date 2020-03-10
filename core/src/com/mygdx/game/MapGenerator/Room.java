package com.mygdx.game.MapGenerator;

import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.Mobs.Boss;
import com.mygdx.game.Mobs.Slime;
import com.mygdx.game.Mob;
import com.mygdx.game.World;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;

public class Room {
    public int x, y, width, height;
    public boolean isRoomVisible, isPassed, isActivated;
    public ArrayList<ArrayList<Pair>> ways;
    public ArrayList<Mob> mobs;
    public Shelter shelter;
    public Node node;
    public ArrayList<Room> connectedRooms;
    public ArrayList<Door> doors;

    private ArrayList<Integer> prt;
    private int cnt = 0;
    private int[][] localGameMap;

    public Room(Node node, int x, int y, int width, int height, int[][] localGameMap) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.isRoomVisible = true;
        this.isPassed = false;
        this.isActivated = false;
        this.node = node;
        this.ways = new ArrayList<>();
        this.mobs = new ArrayList<>();
        this.doors = new ArrayList<>();
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

    public void addDoor(Door door) {
        doors.add(door);
    }

    public void addMob(Mob mob, int mobX, int mobY) {
        if (mob instanceof Slime) {
            mobs.add(new Slime(mobX, mobY, this));
            World.mobs.add(mobs.get(mobs.size() - 1));
        } else if (mob instanceof Boss) {
            mobs.add(new Boss(mobX, mobY, this));
            World.mobs.add(mobs.get(mobs.size() - 1));
        }
    }

    public boolean isPointInRoom(int pointX, int pointY) {
        if (x <= pointX && pointX < x + height && y <= pointY && pointY < y + width) {
            return true;
        }
        return false;
    }

    public void removeMob(Mob mob) {
        mobs.remove(mob);
    }

    public void createMob(Mob mob) {
        Pair cords = getRandomPointInRoom();
        while (localGameMap[cords.first][cords.second] != GameMapGenerator.spaceCode) {
            cords = getRandomPointInRoom();
        }
        addMob(mob,cords.first * World.pixSize, cords.second * World.pixSize);
    }

    public boolean isMobInRoom(Mob mob) {
        return mobs.contains(mob);
    }

    public ArrayList<Pair> findMobPath(Mob mob, int targetX, int targetY) {
        int intX = mob.intX;
        int intY = mob.intY;

        ArrayList<Pair> path = new ArrayList<>();
        if (!isMobInRoom(mob) || !isPointInRoom(targetX, targetY)) {
            return path;
        }

        int[][] used = new int[height][width];
        GameMapGenerator.fillMatrix(used, -1);
        ArrayDeque<Pair> q = new ArrayDeque<>();
        int[] cordX = {0, 0, 1, -1}, cordY = {1, -1, 0, 0};

        used[intX - x][intY - y] = 0;
        q.addLast(new Pair(intX, intY));

        while (!q.isEmpty()) {
            Pair temp = q.getFirst();
            q.removeFirst();
            for (int i = 0; i < 4; ++i) {
                int newX = temp.first + cordX[i];
                int newY = temp.second + cordY[i];
                if (!isPointInRoom(newX, newY)) {
                    continue;
                }
                if (used[newX - x][newY - y] != -1) {
                    continue;
                }

//                if (newX == targetX && newY == targetY) {
//                    used[newX - x][newY - y] = used[temp.first - x][temp.second - y] + 1;
//                    q.addLast(new Pair(newX, newY));
//                }

                if (isPointInRoom(newX, newY) && localGameMap[newX][newY] != GameMapGenerator.wallCode) {
                    boolean flag = false;
                    for (Mob it : mobs) {
                        if (it == mob || (newX == targetX && newY == targetY)) {
                            continue;
                        }
                        if (it.intX == newX && it.intY == newY) {
                            flag = true;
                            break;
                        }
                    }
                    if (!flag) {
                        used[newX - x][newY - y] = used[temp.first - x][temp.second - y] + 1;
                        q.addLast(new Pair(newX, newY));
                    }
                }
            }
        }

        if (used[targetX - x][targetY - y] == -1) {
            return path;
        }

        int dist = used[targetX - x][targetY - y];
        path.add(new Pair(targetX, targetY));
        int tmpX = targetX, tmpY = targetY;

        if (cnt == 0) {
            cnt = 70;
            prt = new ArrayList<Integer>();
            prt.add(0);
            prt.add(1);
            prt.add(2);
            prt.add(3);
            java.util.Collections.shuffle(prt);
        } else {
            --cnt;
        }

        while (dist > 0) {
            for (int i = 0; i < 4; ++i) {
                int newX = tmpX + cordX[prt.get(i)], newY = tmpY + cordY[prt.get(i)];
                if (isPointInRoom(newX, newY)) {
                    if (used[newX - x][newY - y] == dist - 1) {
                        tmpX = newX;
                        tmpY = newY;
                        --dist;
                        path.add(new Pair(tmpX, tmpY));
                        break;
                    }
                }
            }
        }

        Collections.reverse(path);
        return path;
    }
}

package com.mygdx.game.MapGenerator;

import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.Mob;
import com.mygdx.game.Mobs.Boss;
import com.mygdx.game.Mobs.Slime;
import com.mygdx.game.NPCs.Portal;
import com.mygdx.game.NPCs.Seller;
import com.mygdx.game.Subject;
import com.mygdx.game.World;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;

import static com.mygdx.game.World.npcs;

public class Room {
    public int x, y, width, height;
    public boolean isRoomVisible, isPassed, isActivated, isBonus, isEnter, isPortal;
    public ArrayList<ArrayList<Pair>> ways;
    public ArrayList<Mob> mobs;
    public ArrayList<Door> doors;
    public Shelter shelter;
    public Node node;
    public ArrayList<Subject> subjects;

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
        this.isBonus = false;
        this.isEnter = false;
        this.isPortal = false;
        this.node = node;
        this.ways = new ArrayList<>();
        this.mobs = new ArrayList<>();
        this.doors = new ArrayList<>();
        this.subjects = new ArrayList<>();
        this.shelter = new Shelter();
        this.localGameMap = localGameMap;
    }

    public void configureBonusEnvironment() {
        isBonus = true;
        isEnter = false;
        isPortal = false;
        shelter.index = Shelter.shelters.indexOf(Shelter.tmpEmpty);
        Pair point = getCenterPointInRoom();
        Vector3 temp = GameMapController.mapCordsToGame(new Vector3(point.first, point.second, 0));
        Seller seller = new Seller(temp.x, temp.y);
        npcs.add(seller);
    }

    public void configureEnterEnvironment() {
        isEnter = true;
        isBonus = false;
        isPortal = false;
        shelter.index = Shelter.shelters.indexOf(Shelter.tmpEmpty);
    }

    public void configurePortalEnvironment() {
        isPortal = true;
        isBonus = false;
        isEnter = false;
        shelter.index = Shelter.shelters.indexOf(Shelter.tmpEmpty);
        Pair point = getCenterPointInRoom();
        Vector3 temp = GameMapController.mapCordsToGame(new Vector3(point.first, point.second, 0));
        Portal portal = new Portal(temp.x, temp.y);
        npcs.add(portal);
    }

    public void markRoomBonus() {
        if (!isEnter && !isPortal) {
            isBonus = true;
        }
    }

    public void markRoomEnter() {
        isEnter = true;
        isBonus = false;
        isPortal = false;
    }

    public void markRoomPortal() {
        isPortal = true;
        isEnter = false;
        isBonus = false;
    }

    //######################### HELPER #########################

    private Pair getRandomPointInRoom() {
        int newX = Rand.AbsModInt(height);
        int newY = Rand.AbsModInt(width);
        return (new Pair(x + newX, y + newY));
    }

    public Pair getCenterPointInRoom() {
        Pair temp = new Pair(x + height / 2, y + width / 2);
        return temp;
    }

    public boolean isPointInRoom(int pointX, int pointY) {
        return x <= pointX && pointX < x + height && y <= pointY && pointY < y + width;
    }

    //######################### WAY & DOOR #########################

    public void addWay(ArrayList<Pair> way) {
        ways.add(way);
    }

    public void extractDoorsFromWays() {
        for (int i = 0; i < ways.size(); ++i) {
            addDoor(new Door(ways.get(i).get(0).first, ways.get(i).get(0).second, true, this));
        }
    }

    private void addDoor(Door door) {
        doors.add(door);
    }

    //######################### MOB #########################

    public void createMob(Mob mob) {
        Pair cords = getRandomPointInRoom();
        while (localGameMap[cords.first][cords.second] != GameMapGenerator.spaceCode) {
            cords = getRandomPointInRoom();
        }
        addMob(mob, cords.first * World.pixSize, cords.second * World.pixSize);
    }

    public void removeMob(Mob mob) {
        mobs.remove(mob);
    }

    public ArrayList<Pair> findMobPath(Mob mob, int targetX, int targetY) {
        int intX = mob.intX;
        int intY = mob.intY;

        ArrayList<Pair> path = new ArrayList<>();
        if (!isMobInRoom(mob) || !isPointInRoom(targetX, targetY)) {
            return path;
        }

        int[][] used = new int[height][width];
        GameMapController.fillMatrix(used, -1);
        ArrayDeque<Pair> q = new ArrayDeque<>();
        int[] cordX = {0, 0, 1, -1}, cordY = {1, -1, 0, 0};

        try {
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        return path;
    }

    private void addMob(Mob mob, int mobX, int mobY) {
        if (mob instanceof Slime) {
            mobs.add(new Slime(mobX, mobY, this));
            World.mobs.add(mobs.get(mobs.size() - 1));
        } else if (mob instanceof Boss) {
            mobs.add(new Boss(mobX, mobY, this));
            World.mobs.add(mobs.get(mobs.size() - 1));
        }
    }

    private boolean isMobInRoom(Mob mob) {
        return mobs.contains(mob);
    }
}

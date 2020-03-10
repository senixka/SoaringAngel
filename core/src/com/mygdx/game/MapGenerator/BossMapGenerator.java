package com.mygdx.game.MapGenerator;

import com.mygdx.game.Mobs.Boss;
import com.mygdx.game.World;

import java.util.ArrayList;

public class BossMapGenerator {
    public int WIDTH, HEIGHT, BORDER;
    public static final int wallCode = 0, spaceCode = 1;
    public int[][] localGameMap;

    private Node bossNode;
    private Room bossRoom;

    public BossMapGenerator(int HEIGHT, int WIDTH, int BORDER) {
        this.HEIGHT = HEIGHT;
        this.WIDTH = WIDTH;
        this.BORDER = BORDER;
        this.localGameMap = new int[HEIGHT][WIDTH];
        fillMatrix(localGameMap, wallCode);
        this.bossNode = new Node(0, 0, HEIGHT, WIDTH, BORDER, HEIGHT, WIDTH, localGameMap);
        bossNode.createRoom();
        this.bossRoom = bossNode.room;

        initBossMap();

    }

    public int[][] getMap() {
        int[][] bossMap = new int[HEIGHT][WIDTH];
        copyMatrix(localGameMap, bossMap);
        return bossMap;
    }

    private void initBossMap() {
        printRoomToMap(localGameMap, bossRoom);
        createBossMob();
    }

    private void printRoomToMap(int[][] gameMap, Room room) {
        for (int i = room.x; i < room.x + room.height; ++i) {
            for (int j = room.y; j < room.y + room.width; ++j) {
                gameMap[i][j] = spaceCode;
            }
        }
    }

    private void createBossMob() {
        bossRoom.createMob(new Boss());
    }

    public static void copyMatrix(int[][] from, int[][] to) {
        for (int i = 0; i < from.length; ++i) {
            for (int j = 0; j < from[i].length; ++j) {
                to[i][j] = from[i][j];
            }
        }
    }

    public static void fillMatrix(int[][] matrix, int val) {
        for (int i = 0; i < matrix.length; ++i) {
            for (int j = 0; j < matrix[i].length; ++j) {
                matrix[i][j] = val;
            }
        }
    }

    public static void fillMatrix(boolean[][] matrix, boolean val) {
        for (int i = 0; i < matrix.length; ++i) {
            for (int j = 0; j < matrix[i].length; ++j) {
                matrix[i][j] = val;
            }
        }
    }
}

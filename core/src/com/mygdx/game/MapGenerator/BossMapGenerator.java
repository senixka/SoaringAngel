package com.mygdx.game.MapGenerator;

import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.Mobs.Boss;

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
        bossRoom.shelter.index = 0;
        printRoomToMap(localGameMap, bossRoom);
        printRoomShelterToMap(localGameMap, bossRoom);
        createBossMob();
    }

    private void printRoomShelterToMap(int[][] gameMap, Room room) {
        ArrayList<Instruction> temp = room.shelter.shelters.get(room.shelter.index).tmp;
        for (int i = 0; i < temp.size(); ++i) {
            Instruction inst = temp.get(i);
            printInstructionToMap(gameMap, room, inst);
        }
    }

    private void printInstructionToMap(int[][] gameMap, Room room, Instruction inst) {
        int tempX = (int) ((float) room.x + inst.x * (float) room.height);
        int tempY = (int) ((float) room.y + inst.y * (float) room.width);
        Vector3 vec = inst.vec;
        if (vec.x == 0 && vec.y == 0) {
            gameMap[tempX][tempY] = wallCode;
        } else if (vec.x == 0 && vec.y != 0) {
            int delta = (int) ((float) room.width * inst.len);
            for (int i = tempY; i >= room.y && i < room.y + room.width && Math.abs(i - tempY) <= delta; i += vec.y) {
                gameMap[tempX][i] = wallCode;
            }
        } else if (vec.x != 0 && vec.y == 0) {
            int delta = (int) ((float) room.height * inst.len);
            for (int i = tempX; i >= room.x && i < room.x + room.height && Math.abs(i - tempX) <= delta; i += vec.x) {
                gameMap[i][tempY] = wallCode;
            }
        } else {
            int delta = Math.min((int) ((float) room.height * inst.len), (int) ((float) room.width * inst.len));
            for (int i = tempX, j = tempY; i >= room.x && i < room.x + room.height && Math.abs(i - tempX) <= delta &&
                    j >= room.y && j < room.y + room.width && Math.abs(j - tempY) <= delta; i += vec.x, j += vec.y) {
                gameMap[i][j] = wallCode;
            }
        }
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

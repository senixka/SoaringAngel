package com.mygdx.game.MapGenerator;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.Mobs.Boss;

import java.util.ArrayList;

public class BossMapGenerator extends MapGenerator {
    public static final int wallCode = 0, spaceCode = 1;
    public int WIDTH, HEIGHT, BORDER;
    public int[][] localGameMap, localGameMiniMap;
    public Node bossNode;
    public Room bossRoom;
    public Pixmap localPixMiniMap;
    public Texture miniMapTexture;

    public BossMapGenerator(int HEIGHT, int WIDTH, int BORDER) {
        this.HEIGHT = HEIGHT;
        this.WIDTH = WIDTH;
        this.BORDER = BORDER;
        this.localGameMap = new int[HEIGHT][WIDTH];
        this.localGameMiniMap = new int[HEIGHT][WIDTH];
        fillMatrix(localGameMap, wallCode);
        fillMatrix(localGameMiniMap, wallCode);
        this.bossNode = new Node(0, 0, HEIGHT, WIDTH, BORDER, HEIGHT, WIDTH, localGameMap);
        bossNode.createRoom();
        this.bossRoom = bossNode.room;
        this.localPixMiniMap = new Pixmap(HEIGHT, WIDTH, Pixmap.Format.RGBA8888);

        initBossMap();
        initGameMiniMap(localGameMiniMap);
        initPixelMiniMap(localGameMiniMap, localPixMiniMap);
        this.miniMapTexture = new Texture(localPixMiniMap);
    }

    public static void fillMatrix(int[][] matrix, int val) {
        for (int i = 0; i < matrix.length; ++i) {
            for (int j = 0; j < matrix[i].length; ++j) {
                matrix[i][j] = val;
            }
        }
    }

    private void initBossMap() {
        bossRoom.shelter.index = 0;
        printRoomToMap(localGameMap, bossRoom);
        printRoomShelterToMap(localGameMap, bossRoom);
        createBossMob();
    }

    private void initPixelMiniMap(int[][] gameMiniMap, Pixmap pixmap) {
        pixmap.setColor(Color.BLUE);
        pixmap.fill();
        pixmap.setColor(Color.RED);
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                if (gameMiniMap[i][j] != wallCode) {
                    pixmap.drawPixel(i, WIDTH - 1 - j);
                }
            }
        }
    }

    private void initGameMiniMap(int[][] gameMiniMap) {
        for (int i = 0; i < HEIGHT; ++i) {
            for (int j = 0; j < WIDTH; ++j) {
                gameMiniMap[i][j] = localGameMap[i][j];
            }
        }
    }

    private void printRoomShelterToMap(int[][] gameMap, Room room) {
        ArrayList<Instruction> temp = Shelter.shelters.get(room.shelter.index).tmp;
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
}

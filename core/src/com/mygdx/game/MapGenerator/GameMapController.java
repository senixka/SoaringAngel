package com.mygdx.game.MapGenerator;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.Helper;
import com.mygdx.game.Mobs.Slime;
import com.mygdx.game.Rectangle;
import com.mygdx.game.World;

import java.util.ArrayList;
import java.util.HashMap;

public class GameMapController {

    public static final int wallCode = 0, spaceCode = 1, openDoorCode = 2, closeDoorCode = -2, roomPassedCode = 3, wayPassedCode = 3;
    public final int WIDTH, HEIGHT;
    public int[][] localGameMap, localGameMiniMap;
    public int roomPassed, roomQuantity;

    public GameMapGenerator mapGenerator;
    public int[][] nodeGameMap, roomGameMap;
    public HashMap<Integer, Room> intToRoom;
    public HashMap<Integer, Node> intToNode;
    public Node root, mazeEnter;
    public ArrayList<Room> localRooms;
    public Pixmap localPixMiniMap;
    public Texture miniMapTexture;
    public boolean wasUpdateMiniMap;

    public GameMapController(GameMapGenerator generator) {
        this.mapGenerator = generator;
        this.HEIGHT = generator.HEIGHT;
        this.WIDTH = generator.WIDTH;
        this.root = generator.root;
        this.roomPassed = 0;
        this.wasUpdateMiniMap = true;
        this.mazeEnter = generator.mazeEnter;
        this.miniMapTexture = generator.miniMapTexture;
        this.localPixMiniMap = mapGenerator.localPixMiniMap;

        this.localGameMap = new int[HEIGHT][WIDTH];
        this.localGameMiniMap = new int[HEIGHT][WIDTH];
        this.nodeGameMap = new int[HEIGHT][WIDTH];
        this.roomGameMap = new int[HEIGHT][WIDTH];
        copyMatrix(generator.localGameMap, this.localGameMap);
        copyMatrix(generator.localGameMiniMap, this.localGameMiniMap);
        copyMatrix(generator.nodeGameMap, this.nodeGameMap);
        copyMatrix(generator.roomGameMap, this.roomGameMap);

        this.intToRoom = generator.intToRoom;
        this.intToNode = generator.intToNode;

        this.localRooms = new ArrayList<>();
        copyList(generator.localRooms, this.localRooms);
        this.roomQuantity = localRooms.size();
    }

    public int[][] getMap() {
        int[][] gameMap = new int[HEIGHT][WIDTH];
        copyMatrix(localGameMap, gameMap);
        return gameMap;
    }

    public Texture getMiniMap() {
        Texture temp = new Texture(localPixMiniMap);
        return temp;
    }

    public static Vector3 gameCordsToMap(Vector3 vec) {
        Vector3 tmp = new Vector3();
        tmp.x = (int) (vec.x / (float) World.pixSize);
        tmp.y = (int) (vec.y / (float) World.pixSize);
        return tmp;
    }

    public static Vector3 mapCordsToGame(Vector3 vec) {
        Vector3 tmp = new Vector3();
        tmp.x = (float) ((int) vec.x * World.pixSize) + (float) World.pixSize / (float) 2;
        tmp.y = (float) ((int) vec.y * World.pixSize) + (float) World.pixSize / (float) 2;
        return tmp;
    }

    public static void copyMatrix(int[][] from, int[][] to) {
        for (int i = 0; i < from.length; ++i) {
            for (int j = 0; j < from[i].length; ++j) {
                to[i][j] = from[i][j];
            }
        }
    }

    public static void copyList(ArrayList<Room> from, ArrayList<Room> to) {
        for (int i = 0; i < from.size(); ++i) {
            to.add(from.get(i));
        }
    }

    public static void fillMatrix(int[][] matrix, int val) {
        for (int i = 0; i < matrix.length; ++i) {
            for (int j = 0; j < matrix[i].length; ++j) {
                matrix[i][j] = val;
            }
        }
    }

    public void update() {
        Vector3 persVec = new Vector3(World.pers.getCenter());
        persVec = gameCordsToMap(persVec);
        int persX = (int) persVec.x, persY = (int) persVec.y;
        if (!isPointInMaze(persX, persY)) {
            return;
        }

        Room room = pointToRoom(persX, persY);
        if (room == null || room.isPassed) {
            if (room == null) {
                if (localGameMap[persX][persY] != wallCode) {
                    localGameMiniMap[persX][persY] = wayPassedCode;
                    wasUpdateMiniMap = true;
                }
            } else {
                updatePixelMiniMap(localGameMiniMap, localPixMiniMap);
            }
            return;
        }

        if (room.isBonus) {
            room.isPassed = true;
            ++roomPassed;
            openAllDoors(room);
            breakAllDoors(room);
            markRoomInMiniMap(localGameMiniMap, room);
            updatePixelMiniMap(localGameMiniMap, localPixMiniMap);
            return;
        }

        if (room.isEnter) {
            room.isPassed = true;
            ++roomPassed;
            openAllDoors(room);
            breakAllDoors(room);
            markRoomInMiniMap(localGameMiniMap, room);
            updatePixelMiniMap(localGameMiniMap, localPixMiniMap);
            return;
        }

        //!!!ЭТО СРАШНЫЙ КОСТЫЛЬ, ЕГО НУЖНО ИСПРАВИТЬ!!!
        //>>>>>>>>>>>>>>>>
        if (!room.isActivated) {
            closeAllDoors(room);
            if (Helper.intersectWall(new Rectangle(World.pers.getX(), World.pers.getY(), World.pers.sizeX, (float) World.pers.sizeY / 2f))) {
                openAllDoors(room);
                return;
            }
        }
        //<<<<<<<<<<<<<<<<

        if (!room.isActivated) {
            room.isActivated = true;
            closeAllDoors(room);
            createMobs(room);
        } else if (room.mobs.size() <= 0) {
            room.isPassed = true;
            ++roomPassed;
            openAllDoors(room);
            breakAllDoors(room);
            markRoomInMiniMap(localGameMiniMap, room);
            updatePixelMiniMap(localGameMiniMap, localPixMiniMap);
        }
    }

    public Pair teleportPersInMaze() {
        if (mazeEnter != null) {
            return mazeEnter.room.getCenterPointInRoom();
        }
        for (int i = 0; i < HEIGHT; ++i) {
            for (int j = 0; j < WIDTH; ++j) {
                if (localGameMap[i][j] == spaceCode && pointToRoom(i, j) == null &&
                        localGameMap[i][j] != openDoorCode && localGameMap[i][j] != closeDoorCode) {
                    return (new Pair(i, j));
                }
            }
        }
        return (new Pair(0, 0));
    }

    private void markRoomInMiniMap(int[][] gameMiniMap, Room room) {
        for (int i = room.x; i < room.x + room.height; ++i) {
            for (int j = room.y; j < room.y + room.width; ++j) {
                gameMiniMap[i][j] = roomPassedCode;
            }
        }
        wasUpdateMiniMap = true;
    }

    private void updatePixelMiniMap(int[][] gameMiniMap, Pixmap pixmap) {
        if (wasUpdateMiniMap) {
            wasUpdateMiniMap = false;
            pixmap.setColor(Color.YELLOW);
            for (int i = 0; i < HEIGHT; i++) {
                for (int j = 0; j < WIDTH; j++) {
                    if (gameMiniMap[i][j] == roomPassedCode || gameMiniMap[i][j] == wayPassedCode) {
                        pixmap.drawPixel(i, WIDTH - 1 - j);
                    }
                }
            }
            miniMapTexture.dispose();
            miniMapTexture = new Texture(pixmap);
            World.miniMap.dispose();
            World.miniMap = new Texture(pixmap);
        }
    }

    //######################### HELPER #########################

    private boolean isPointInMaze(int pX, int pY) {
        return pX >= 0 && pY >= 0 && pX < HEIGHT && pY < WIDTH;
    }

    private void closeAllDoors(Room room) {
        for (int i = 0; i < room.doors.size(); ++i) {
            Door door = room.doors.get(i);
            door.close();
            localGameMap[door.x][door.y] = wallCode;
            World.map[door.x][door.y] = wallCode;
        }
    }

    private void openAllDoors(Room room) {
        for (int i = 0; i < room.doors.size(); ++i) {
            Door door = room.doors.get(i);
            door.open();
            localGameMap[door.x][door.y] = openDoorCode;
            World.map[door.x][door.y] = openDoorCode;
        }
    }

    private void breakAllDoors(Room room) {
        for (int i = 0; i < room.doors.size(); ++i) {
            Door door = room.doors.get(i);
            door.open();
            localGameMap[door.x][door.y] = spaceCode;
            World.map[door.x][door.y] = spaceCode;
        }
    }

    private void createMobs(Room room) {
        for (int j = 0; j < 5; ++j) {
            room.createMob(new Slime());
        }
    }

    public boolean goToNextLevel() {
        return roomQuantity == roomPassed;
    }

    private Node pointToNode(int pointX, int pointY) {
        return intToNode.get(nodeGameMap[pointX][pointY]);
    }

    private Room pointToRoom(int pointX, int pointY) {
        return intToRoom.get(roomGameMap[pointX][pointY]);
    }
}

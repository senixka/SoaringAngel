package com.mygdx.game.MapGenerator;

import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.Helper;
import com.mygdx.game.Mobs.Slime;
import com.mygdx.game.Rectangle;
import com.mygdx.game.World;

import java.util.ArrayList;
import java.util.HashMap;

public class GameMapController {

    public final int WIDTH, HEIGHT;
    public static final int wallCode = 0, spaceCode = 1, openDoorCode = 2, closeDoorCode = -2, roomPassedCode = 3;
    public int[][] localGameMap, localGameMiniMap;
    public int roomPassed, roomQuantity;

    public GameMapGenerator mapGenerator;
    public int[][] nodeGameMap, roomGameMap;
    public HashMap<Integer, Room> intToRoom;
    public HashMap<Integer, Node> intToNode;
    public Node root;
    public ArrayList<Room> localRooms;

    public GameMapController(GameMapGenerator generator) {
        this.mapGenerator = generator;
        this.HEIGHT = generator.HEIGHT;
        this.WIDTH = generator.WIDTH;
        this.root = generator.root;
        this.roomPassed = 0;

        this.localGameMap = new int[HEIGHT][WIDTH];
        this.localGameMiniMap = new int[HEIGHT][WIDTH];
        this.nodeGameMap = new int[HEIGHT][WIDTH];
        this.roomGameMap = new int[HEIGHT][WIDTH];
        copyMatrix(generator.localGameMap, this.localGameMap);
        copyMatrix(generator.localGameMiniMap, this.localGameMiniMap);
        copyMatrix(generator.nodeGameMap, this.nodeGameMap);
        copyMatrix(generator.roomGameMap, this.roomGameMap);

        this.intToRoom = new HashMap<>();
        this.intToNode = new HashMap<>();
        this.intToRoom = (HashMap<Integer, Room>) generator.intToRoom.clone();
        this.intToNode = (HashMap<Integer, Node>) generator.intToNode.clone();

        this.localRooms = new ArrayList<>();
        copyList(generator.localRooms, this.localRooms);
        this.roomQuantity = localRooms.size();
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
            markRoomInMiniMap(World.miniMap, room);
        }
    }

    private void markRoomInMiniMap(int[][] gameMiniMap, Room room) {
        for (int i = room.x; i < room.x + room.height; ++i) {
            for (int j = room.y; j < room.y + room.width; ++j) {
                gameMiniMap[i][j] = roomPassedCode;
            }
        }
    }

    private boolean isPointInMaze(int pX, int pY) {
        if (pX >= 0 && pY >= 0 && pX < HEIGHT && pY < WIDTH) {
            return true;
        }
        return false;
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
        if (roomQuantity == roomPassed) {
            return true;
        }
        return false;
    }

    //######################### HELPER #########################

    private Node pointToNode(int pointX, int pointY) {
        return intToNode.get(nodeGameMap[pointX][pointY]);
    }

    private Room pointToRoom(int pointX, int pointY) {
        return intToRoom.get(roomGameMap[pointX][pointY]);
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

    public static void fillMatrix(boolean[][] matrix, boolean val) {
        for (int i = 0; i < matrix.length; ++i) {
            for (int j = 0; j < matrix[i].length; ++j) {
                matrix[i][j] = val;
            }
        }
    }
}

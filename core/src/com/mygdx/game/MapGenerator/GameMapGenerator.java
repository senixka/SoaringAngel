package com.mygdx.game.MapGenerator;

import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.World;

import java.util.*;

public class GameMapGenerator {
    /**
     * 0 <= ROOM_EPS <= 1000
     * 0 <= WAY_EPS <= 10000
     */
    public static int WIDTH, HEIGHT, ROOM_EPS, WAY_EPS;
    public static int[][] localGameMap;

    public GameMapGenerator(int HEIGHT, int WIDTH, int BORDER, int MIN_HEIGHT, int MIN_WIDTH, int ROOM_EPS, int WAY_EPS) {
        this.HEIGHT = HEIGHT;
        this.WIDTH = WIDTH;
        this.ROOM_EPS = ROOM_EPS;
        this.WAY_EPS = WAY_EPS;
        Node.BORDER = BORDER;
        Node.MIN_HEIGHT = MIN_HEIGHT;
        Node.MIN_WIDTH = MIN_WIDTH;
    }

    public void setParameters(int HEIGHT, int WIDTH, int BORDER, int MIN_HEIGHT, int MIN_WIDTH, int ROOM_EPS, int WAY_EPS) {
        this.HEIGHT = HEIGHT;
        this.WIDTH = WIDTH;
        this.ROOM_EPS = ROOM_EPS;
        this.WAY_EPS = WAY_EPS;
        Node.BORDER = BORDER;
        Node.MIN_HEIGHT = MIN_HEIGHT;
        Node.MIN_WIDTH = MIN_WIDTH;
    }

    public int[][] generate() {
        int[][] gameMap = new int[HEIGHT][WIDTH];
        localGameMap = new int[HEIGHT][WIDTH];
        Node root = new Node(0, 0, HEIGHT - 1, WIDTH - 1);
        ArrayList<Room> rooms = new ArrayList<>();

        createTree(root);
        createRooms(root);
        getLeafsRoom(rooms, root);
        sparseRooms(rooms);
        connectRooms(rooms);

        initGameMap(gameMap, rooms);
        for (int i = 0; i < HEIGHT; ++i) {
            for (int j = 0; j < WIDTH; ++j) {
                localGameMap[i][j] = gameMap[i][j];
            }
        }
        return gameMap;
    }

    public static Vector3 gameCordsToMap(Vector3 vec) {
        vec.x = (int) (vec.x / (float) World.pixSize);
        vec.y = (int) (vec.y / (float) World.pixSize);
        return vec;
    }

    private void initGameMap(int[][] gameMap, ArrayList<Room> rooms) {
        for (int i = 0; i < rooms.size(); ++i) {
            Room temp = rooms.get(i);
            printRoomToMap(gameMap, temp);
            printWaysToMap(gameMap, temp);
        }
        for (int i = 0; i < rooms.size(); ++i) {
            Room temp = rooms.get(i);
            printRoomShelterToMap(gameMap, temp);
        }
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
            gameMap[tempX][tempY] = 0;
        } else if (vec.x == 0 && vec.y != 0) {
            int delta = (int) ((float) room.width * inst.len);
            for (int i = tempY; i >= room.y && i < room.y + room.width && Math.abs(i - tempY) <= delta; i += vec.y) {
                gameMap[tempX][i] = 0;
            }
        } else if (vec.x != 0 && vec.y == 0) {
            int delta = (int) ((float) room.height * inst.len);
            for (int i = tempX; i >= room.x && i < room.x + room.height && Math.abs(i - tempX) <= delta; i += vec.x) {
                gameMap[i][tempY] = 0;
            }
        } else {
            int delta = Math.min((int) ((float) room.height * inst.len), (int) ((float) room.width * inst.len));
            for (int i = tempX, j = tempY; i >= room.x && i < room.x + room.height && Math.abs(i - tempX) <= delta &&
                    j >= room.y && j < room.y + room.width && Math.abs(j - tempY) <= delta; i += vec.x, j += vec.y) {
                gameMap[i][j] = 0;
            }
        }
    }

    private void printRoomToMap(int[][] gameMap, Room room) {
        for (int i = room.x; i < room.x + room.height; ++i) {
            for (int j = room.y; j < room.y + room.width; ++j) {
                gameMap[i][j] = 1;
            }
        }
    }

    private void printWaysToMap(int[][] gameMap, Room room) {
        for (int i = 0; i < room.ways.size(); ++i) {
            ArrayList<Pair> way = room.ways.get(i);
            for (int j = 0; j < way.size(); ++j) {
                gameMap[way.get(j).first][way.get(j).second] = 1;
            }
        }
    }

    private void createTree(Node v) {
        int splittable = v.isPossibleToSplit();
        if (splittable != -1) {
            int newH = (v.height - 2 * (v.MIN_HEIGHT + 2 * v.BORDER)) / 2, newW = (v.width - 2 * (v.MIN_WIDTH + 2 * v.BORDER)) / 2;
            int deltaH = 0, deltaW = 0;
            if (newH > 0) {
                deltaH = Rand.AbsModInt(newH);
            }
            if (newW > 0) {
                deltaW = Rand.AbsModInt(newW);
            }

            if (splittable == 0) {
                v.leftChild = new Node(v.x, v.y, v.height / 2 - deltaH, v.width);
                v.rightChild = new Node(v.x + v.height / 2 - deltaH, v.y, v.height - v.height / 2 + deltaH, v.width);
            } else {
                v.leftChild = new Node(v.x, v.y, v.height, v.width / 2 - deltaW);
                v.rightChild = new Node(v.x, v.y + v.width / 2 - deltaW, v.height, v.width - v.width / 2 + deltaW);
            }
            createTree(v.leftChild);
            createTree(v.rightChild);
        }
    }

    private void createRooms(Node v) {
        if (v.rightChild != null) {
            createRooms(v.leftChild);
            createRooms(v.rightChild);
        } else {
            v.createRoom();
        }
    }

    private void connectTwoRooms(Room a, Room b) {
        Pair s = a.getRandomPointInRoom();
        Pair f = b.getRandomPointInRoom();
        ArrayList<Pair> fromS = new ArrayList<>();

        if (s.second <= f.second) {
            for (int i = s.second; i < f.second; ++i) {
                fromS.add(new Pair(s.first, i));
            }
        } else {
            for (int i = s.second; i > f.second; --i) {
                fromS.add(new Pair(s.first, i));
            }
        }
        if (s.first <= f.first) {
            for (int i = s.first; i <= f.first; ++i) {
                fromS.add(new Pair(i, f.second));
            }
        } else {
            for (int i = s.first; i >= f.first; --i) {
                fromS.add(new Pair(i, f.second));
            }
        }

        a.addWay(fromS);
        Collections.reverse(fromS);
        b.addWay(fromS);
    }

    private void getLeafsRoom(ArrayList<Room> rooms, Node v) {
        if (v.leftChild == null) {
            rooms.add(v.room);
        } else {
            getLeafsRoom(rooms, v.leftChild);
            getLeafsRoom(rooms, v.rightChild);
        }
    }

    private void sparseRooms(ArrayList<Room> rooms) {
        for (int i = 0; i < rooms.size(); ++i) {
            if (Rand.AbsModInt(1000) < ROOM_EPS) {
                rooms.get(i).isRoomVisible = false;
                rooms.remove(i);
                --i;
            }
        }
    }

    private void connectRooms(ArrayList<Room> rooms) {

//        boolean[] used = new boolean[rooms.size()];
//        for (int i = 0; i < used.length; used[i++] = false);
//        int v = 0, to = 0;
//        used[v] = true;
//
//        for (int i = 0; i < rooms.size() - 1; ++i, v = to) {
//            to = Math.abs(secureRandom.nextInt()) % rooms.size();
//            while (used[to]) {
//                to = Math.abs(secureRandom.nextInt()) % rooms.size();
//            }
//            connectTwoRooms(rooms.get(v), rooms.get(to));
//            used[to] = true;
//        }
//        connectTwoRooms(rooms.get(to), rooms.get(0));

        for (int i = 0; i < rooms.size(); ++i) {
            connectTwoRooms(rooms.get(i), rooms.get((i + 1) % rooms.size()));
        }

        for (int i = 0; i < rooms.size(); ++i) {
            for (int j = i + 1; j < rooms.size(); ++j) {
                if (Rand.AbsModInt(10000) < WAY_EPS) {
                    connectTwoRooms(rooms.get(i), rooms.get(j));
                }
            }
        }
    }
}

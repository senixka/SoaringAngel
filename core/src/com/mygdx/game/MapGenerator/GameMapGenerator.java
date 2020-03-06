package com.mygdx.game.MapGenerator;

import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.Helper;
import com.mygdx.game.MyGame;
import com.mygdx.game.World;
import com.sun.jmx.remote.internal.ArrayQueue;

import java.lang.reflect.Array;
import java.util.*;

public class GameMapGenerator {
    /**
     * 0 <= ROOM_EPS <= 1000
     * 0 <= WAY_EPS <= 10000
     */
    public static int WIDTH, HEIGHT, ROOM_EPS, WAY_EPS;
    public static int[][] localGameMap;
    public static int[][] nodeGameMap, roomGameMap;
    public static HashMap<Integer, Room> intToRoom;
    public static HashMap<Integer, Node> intToNode;

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
        nodeGameMap = new int[HEIGHT][WIDTH];
        roomGameMap = new int[HEIGHT][WIDTH];
        intToNode = new HashMap<>();
        intToRoom = new HashMap<>();
        Node root = new Node(0, 0, HEIGHT, WIDTH);
        ArrayList<Room> rooms = new ArrayList<>();

        createTree(root);
        createRooms(root);
        initNodeGameMap(root);
        initRoomGameMap(root);
        getLeafsRoom(rooms, root);
        sparseRooms(rooms);

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

    public Node pointToNode(int pointX, int pointY) {
        return intToNode.get(nodeGameMap[pointX][pointY]);
    }

    public Room pointToRoom(int pointX, int pointY) {
        return intToRoom.get(roomGameMap[pointX][pointY]);
    }

    private void initGameMap(int[][] gameMap, ArrayList<Room> rooms) {
        for (int i = 0; i < rooms.size(); ++i) {
            Room temp = rooms.get(i);
            printRoomToMap(gameMap, temp);
        }
        int[][] tempMap = new int[HEIGHT][WIDTH];
        for (int i = 0; i < HEIGHT; ++i) {
            for (int j = 0; j < WIDTH; ++j) {
                tempMap[i][j] = gameMap[i][j];
            }
        }

        connectRooms(tempMap, rooms);
        for (int i = 0; i < rooms.size(); ++i) {
            Room temp = rooms.get(i);
            printWaysToMap(gameMap, temp);
        }
        for (int i = 0; i < rooms.size(); ++i) {
            Room temp = rooms.get(i);
            printRoomShelterToMap(gameMap, temp);
        }
    }

    private void initRoomGameMap(Node root) {
        ArrayList<Room> rooms = new ArrayList<>();
        getLeafsRoom(rooms, root);
        for (int r = 0; r < rooms.size(); ++r) {
            Room room = rooms.get(r);
            for (int i = room.x; i < room.x + room.height; ++i) {
                for (int j = room.y; j < room.y + room.width; ++j) {
                    roomGameMap[i][j] = r + 1;
                }
            }
            intToRoom.put(r + 1, room);
        }
    }

    private void initNodeGameMap(Node root) {
        ArrayList<Node> nodes = new ArrayList<>();
        getLeafsNode(nodes, root);
        for (int r = 0; r < nodes.size(); ++r) {
            Node node = nodes.get(r);
            for (int i = node.x; i < node.x + node.height; ++i) {
                for (int j = node.y; j < node.y + node.width; ++j) {
                    nodeGameMap[i][j] = r + 1;
                }
            }
            intToNode.put(r + 1, node);
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

    private ArrayList<Pair> connectToPoints(int[][] gameMap, Pair a, Pair b) {
        ArrayList<Pair> path = new ArrayList<>();
        int[][] used = new int[HEIGHT][WIDTH];
        for (int i = 0; i < HEIGHT; ++i) {
            for (int j = 0; j < WIDTH; ++j) {
                used[i][j] = -1;
            }
        }

        if (gameMap[a.first][a.second] == 1 || gameMap[b.first][b.second] == 1) {
            //path.add(a);
            return path;
        }

        ArrayDeque<Pair> q = new ArrayDeque<>();
        q.addLast(a);
        used[a.first][a.second] = 0;
        int[] cordsX = {0, 0, 1, -1, 1, 1, -1, -1}, cordsY = {1, -1, 0, 0, -1, 1, -1, 1};
        int[] cX = {0, 0, 1, -1}, cY = {1, -1, 0, 0};

        while (!q.isEmpty()) {
            Pair temp = q.getFirst();
            q.removeFirst();
            int x = temp.first, y = temp.second;
            for (int i = 0; i < 4; ++i) {
                int newX = x + cX[i], newY = y + cY[i];
                if (newX >= 0 && newX < HEIGHT && newY >= 0 && newY < WIDTH) {
                    boolean flag = false;
                    for (int j = 0; j < 8; ++j) {
                        int newNewX = newX + cordsX[j], newNewY = newY + cordsY[j];
                        if (newX == newNewX && newY == newNewY) {
                            continue;
                        }
                        if (newNewX >= 0 && newNewX < HEIGHT && newNewY >= 0 && newNewY < WIDTH) {
                            if (newNewX == 0 || newNewX == HEIGHT - 1 || newNewY == 0 || newNewY == WIDTH - 1 || gameMap[newNewX][newNewY] == 1) {
                                flag = true;
                                break;
                            }
                        }
                    }
                    if (!flag) {
                        if (used[newX][newY] == -1) {
                            used[newX][newY] = used[x][y] + 1;
                            q.addLast(new Pair(newX, newY));
                        }
                    }
                }
            }
        }

//        for (int i = 0; i < HEIGHT; ++i) {
//            for (int j = 0 ; j < WIDTH; ++j) {
//                System.out.print(used[i][j] + "/");
//            }
//            System.out.println();
//        }

        path.add(b);
        int dist = used[b.first][b.second];
        //System.out.println(dist);
        int x = b.first, y = b.second;
        while (dist > 0) {
            for (int i = 0; i < 4; ++i) {
                int newX = x + cX[i], newY = y + cY[i];
                if (newX >= 0 && newX < HEIGHT && newY >= 0 && newY < WIDTH) {
                    if (used[newX][newY] == dist - 1) {
                        x = newX;
                        y = newY;
                        --dist;
                        path.add(new Pair(x, y));
                        break;
                    }
                }
            }
        }

//        for (int i = 0; i < path.size(); ++i) {
//            Pair temp = path.get(i);
//            gameMap[temp.first][temp.second] = 1;
//        }
        Collections.reverse(path);
        return path;
    }

    private ArrayList<Pair> getCenteredEntersToRoom(Room r) {
        ArrayList<Pair> doors = new ArrayList<>();
        int x1 = r.x - 1, y1 = r.y + r.width / 2;
        int x2 = r.x + r.height / 2, y2 = r.y - 1;
        int x3 = r.x + r.height / 2, y3 = r.y + r.width;
        int x4 = r.x + r.height, y4 = r.y + r.width / 2;
        if (x1 - 1 >= 0 && x1 < HEIGHT && y1 >= 0 && y1 < WIDTH) {
            doors.add(new Pair(x1, y1));
            doors.add(new Pair(x1 - 1, y1));
        }
        if (x2 >= 0 && x2 < HEIGHT && y2 - 1 >= 0 && y2 < WIDTH) {
            doors.add(new Pair(x2, y2));
            doors.add(new Pair(x2, y2 - 1));
        }
        if (x3 >= 0 && x3 < HEIGHT && y3 >= 0 && y3 + 1 < WIDTH) {
            doors.add(new Pair(x3, y3));
            doors.add(new Pair(x3, y3 + 1));
        }
        if (x4 >= 0 && x4 + 1 < HEIGHT && y4 >= 0 && y4 < WIDTH) {
            doors.add(new Pair(x4, y4));
            doors.add(new Pair(x4 + 1, y4));
        }
        return doors;
    }

    private void connectTwoRooms(int[][] gameMap, Room a, Room b) {
        //Pair s = a.getRandomPointInRoom();
        //Pair s = a.getCenterPointInRoom();
        //Pair f = b.getRandomPointInRoom();
        //Pair f = b.getCenterPointInRoom();

        ArrayList<Pair> fromS = getCenteredEntersToRoom(a);
        ArrayList<Pair> fromF = getCenteredEntersToRoom(b);

        int bestCnt = 100000;
        for (int i = 0; i < fromS.size(); i += 2) {
            for (int j = 0; j < fromF.size(); j += 2) {
                ArrayList<Pair> path = connectToPoints(gameMap, fromS.get(i + 1), fromF.get(j + 1));
                if (path.size() <= 3) {
                    continue;
                }
                if (path.get(path.size() - 1).first == fromF.get(j + 1).first &&
                        path.get(path.size() - 1).second == fromF.get(j + 1).second &&
                        path.get(0).second == fromS.get(i + 1).second &&
                        path.get(0).second == fromS.get(i + 1).second) {
//                    if (path.size() == 1) {
//                        System.out.println(fromF.get(j + 1).first + " " + fromF.get(j + 1).second);
//                        System.out.println(fromS.get(i + 1).first + " " + fromS.get(i + 1).second);
//                        System.out.println();
//                    }
                    path.add(0, fromS.get(i));
                    path.add(fromF.get(j));
                    a.addWay(path);
                    Collections.reverse(path);
                    b.addWay(path);
                    for (int k = 0; k < path.size(); ++k) {
                        Pair temp = path.get(k);
                        gameMap[temp.first][temp.second] = 1;
                    }
                    return;
                }
            }
        }
        System.out.println("cheburek");
    }

    private void connectRooms(int[][] gameMap, ArrayList<Room> rooms) {
//        for (int i = 0; i < HEIGHT; ++i) {
//            for (int j = 0; j < WIDTH; ++j) {
//                System.out.print(nodeGameMap[i][j] + "/");
//            }
//            System.out.print("\n");
//        }
//        System.out.print("\n");

        boolean[][] used = new boolean[HEIGHT][WIDTH];
        HashMap<Node, HashSet<Node>> neighbor = new HashMap<>();
        HashMap<Integer, HashSet<Integer>> intNeighbor = new HashMap<>();
        int[] cordsX = {0, 0, 1, -1}, cordsY = {1, -1, 0, 0};

        for (int i = 0; i < HEIGHT; ++i) {
            for (int j = 0; j < WIDTH; ++j) {
                //System.out.println(nodeGameMap[i][j]);
                for (int k = 0; k < 4; ++k) {
                    int newX = i + cordsX[k], newY = j + cordsY[k];
                    if (newX >= 0 && newX < HEIGHT && newY >= 0 && newY < WIDTH) {
                        if (nodeGameMap[i][j] != nodeGameMap[newX][newY]) {
                            if (!neighbor.containsKey(pointToNode(i, j))) {
                                neighbor.put(pointToNode(i, j), new HashSet<Node>());
                                intNeighbor.put(nodeGameMap[i][j], new HashSet<Integer>());
                            }
                            //System.out.println(nodeGameMap[i][j] + " -> " + nodeGameMap[newX][newY]);
                            neighbor.get(pointToNode(i, j)).add(pointToNode(newX, newY));
                            intNeighbor.get(nodeGameMap[i][j]).add(nodeGameMap[newX][newY]);
                        }
                    }
                }
            }
        }

        int cnt = 0;
        HashSet<Pair> connect = new HashSet<>();
        for (Map.Entry<Integer, HashSet<Integer>> it : intNeighbor.entrySet()) {
            int key = it.getKey();
            HashSet<Integer> st = it.getValue();
            for (int node : st) {
                if (rooms.contains(intToNode.get(key).room) && rooms.contains(intToNode.get(node).room)) {
                    boolean flag = false;
                    for (Pair np : connect) {
                        if ((np.first == key && np.second == node) ||
                                (np.first == node && np.second == key)) {
                            flag = true;
                            break;
                        }
                    }
                    if (flag) {
                        continue;
                    }
                    connectTwoRooms(gameMap, intToNode.get(key).room, intToNode.get(node).room);
//                    for (int i = 0; i < HEIGHT; ++i) {
//                        for (int j = 0; j < WIDTH; ++j) {
//                            System.out.print(gameMap[i][j] + " ");
//                        }
//                        System.out.println();
//                    }
//                    System.out.println();
                    connect.add(new Pair(key, node));
                    connect.add(new Pair(node, key));
                }
            }
            ++cnt;
            if (cnt == 2) {
                //return;
            }
            //printWaysToMap(gameMap, intToRoom.get(key));
        }
    }

    private void getLeafsRoom(ArrayList<Room> rooms, Node v) {
        if (v.leftChild == null) {
            rooms.add(v.room);
        } else {
            getLeafsRoom(rooms, v.leftChild);
            getLeafsRoom(rooms, v.rightChild);
        }
    }

    private void getLeafsNode(ArrayList<Node> nodes, Node v) {
        if (v.leftChild == null) {
            nodes.add(v);
        } else {
            getLeafsNode(nodes, v.leftChild);
            getLeafsNode(nodes, v.rightChild);
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
}

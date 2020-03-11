package com.mygdx.game.MapGenerator;

import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.Mobs.Slime;
import com.mygdx.game.World;

import java.util.*;

public class GameMapGenerator {
    /**
     *  0 <= BONUS_ROOM_EPS <= 100
     * 0 <= ROOM_EPS <= 1000
     * 0 <= WAY_EPS <= 10000
     */
    public final int WIDTH, HEIGHT, ROOM_EPS, BORDER, MIN_HEIGHT, MIN_WIDTH, WAY_EPS, BONUS_ROOM_EPS;
    public static final int wallCode = 0, spaceCode = 1, openDoorCode = 2, closeDoorCode = -2;
    public int[][] localGameMap, localGameMiniMap;

    public int[][] nodeGameMap, roomGameMap;
    public HashMap<Integer, Room> intToRoom;
    public HashMap<Integer, Node> intToNode;
    public ArrayList<Room> localRooms;
    public Node root;

    //######################### PUBLIC #########################

    public GameMapGenerator(int HEIGHT, int WIDTH, int BORDER, int MIN_HEIGHT, int MIN_WIDTH, int ROOM_EPS, int WAY_EPS, int BONUS_ROOM_EPS) {
        this.HEIGHT = HEIGHT;
        this.WIDTH = WIDTH;
        this.ROOM_EPS = ROOM_EPS;
        this.WAY_EPS = WAY_EPS;
        this.BORDER = BORDER;
        this.MIN_HEIGHT = MIN_HEIGHT;
        this.MIN_WIDTH = MIN_WIDTH;
        this.BONUS_ROOM_EPS = BONUS_ROOM_EPS;

        while (true) {
            this.localGameMap = new int[HEIGHT][WIDTH];
            this.localGameMiniMap = new int[HEIGHT][WIDTH];
            fillMatrix(this.localGameMap, wallCode);
            fillMatrix(this.localGameMiniMap, wallCode);
            this.nodeGameMap = new int[HEIGHT][WIDTH];
            this.roomGameMap = new int[HEIGHT][WIDTH];
            this.intToNode = new HashMap<>();
            this.intToRoom = new HashMap<>();
            this.root = new Node(0, 0, HEIGHT, WIDTH, BORDER, MIN_HEIGHT, MIN_WIDTH, localGameMap);
            ArrayList<Room> rooms = new ArrayList<>();

            createTree(root);
            createRooms(root);
            getLeafsRoom(rooms, root);
            sparseRooms(rooms);
            createBonusRooms(rooms);

            initRoomGameMap(rooms);
            initNodeGameMap(rooms);
            initGameMap(localGameMap, rooms);
            initGameMiniMap(localGameMiniMap, rooms);
            this.localRooms = rooms;

            if (checkRoomsConnection(localGameMap)) {
                break;
            }
            break;
        }
    }

    public int[][] getMap() {
        int[][] gameMap = new int[HEIGHT][WIDTH];
        copyMatrix(localGameMap, gameMap);
        return gameMap;
    }

    public int[][] getMiniMap() {
        int[][] gameMiniMap = new int[HEIGHT][WIDTH];
        copyMatrix(localGameMiniMap, gameMiniMap);
        return gameMiniMap;
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

    //######################### INIT #########################

    private void initGameMap(int[][] gameMap, ArrayList<Room> rooms) {
        for (int i = 0; i < rooms.size(); ++i) {
            Room temp = rooms.get(i);
            printRoomToMap(gameMap, temp);
        }

        int[][] tempMap = new int[HEIGHT][WIDTH];
        copyMatrix(gameMap, tempMap);
        connectRooms(tempMap, rooms);
        for (int i = 0; i < rooms.size(); ++i) {
            Room temp = rooms.get(i);
            printWaysToMap(gameMap, temp);
        }

        configureRooms(rooms);

        for (int i = 0; i < rooms.size(); ++i) {
            Room temp = rooms.get(i);
            printRoomShelterToMap(gameMap, temp);
            printDoorsToMap(gameMap, temp);
        }
    }

    private void initGameMiniMap(int[][] gameMiniMap, ArrayList<Room> rooms) {
        for (int i = 0; i < rooms.size(); ++i) {
            Room temp = rooms.get(i);
            printRoomToMap(gameMiniMap, temp);
        }
        int[][] tempMap = new int[HEIGHT][WIDTH];
        copyMatrix(gameMiniMap, tempMap);

        connectRooms(tempMap, rooms);
        for (int i = 0; i < rooms.size(); ++i) {
            Room temp = rooms.get(i);
            printWaysToMap(gameMiniMap, temp);
        }
    }

    private void initRoomGameMap(ArrayList<Room> rooms) {
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

    private void initNodeGameMap(ArrayList<Room> rooms) {
        ArrayList<Node> nodes = new ArrayList<>();
        for (Room it : rooms) nodes.add(it.node);

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
    //######################### PRINT #########################

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

    private void printWaysToMap(int[][] gameMap, Room room) {
        for (int i = 0; i < room.ways.size(); ++i) {
            ArrayList<Pair> way = room.ways.get(i);
            for (int j = 0; j < way.size(); ++j) {
                gameMap[way.get(j).first][way.get(j).second] = spaceCode;
            }
        }
    }

    private void printDoorsToMap(int[][] gameMap, Room room) {
        for (int i = 0; i < room.doors.size(); ++i) {
            Door door = room.doors.get(i);
            if (door.isOpen) {
                gameMap[door.x][door.y] = openDoorCode;
            } else {
                gameMap[door.x][door.y] = closeDoorCode;
            }
        }
    }

    //######################### CREATE #########################

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
                v.leftChild = new Node(v.x, v.y, v.height / 2 - deltaH, v.width, BORDER, MIN_HEIGHT, MIN_WIDTH, localGameMap);
                v.rightChild = new Node(v.x + v.height / 2 - deltaH, v.y, v.height - v.height / 2 + deltaH, v.width,
                        BORDER, MIN_HEIGHT, MIN_WIDTH, localGameMap);
            } else {
                v.leftChild = new Node(v.x, v.y, v.height, v.width / 2 - deltaW, BORDER, MIN_HEIGHT, MIN_WIDTH, localGameMap);
                v.rightChild = new Node(v.x, v.y + v.width / 2 - deltaW, v.height, v.width - v.width / 2 + deltaW,
                        BORDER, MIN_HEIGHT, MIN_WIDTH, localGameMap);
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

    private void createBonusRooms(ArrayList<Room> rooms) {
        for (int i = 0; i < rooms.size(); ++i) {
            Room room = rooms.get(i);
            if (Rand.AbsModInt(100) < BONUS_ROOM_EPS) {
                room.isBonus = true;
            }
        }
    }

    //######################### CONNECT #########################

    private ArrayList<Pair> connectToPoints(int[][] gameMap, Pair a, Pair b) {
        ArrayList<Pair> path = new ArrayList<>();
        if (gameMap[a.first][a.second] == spaceCode || gameMap[b.first][b.second] == spaceCode) {
            return path;
        }

        int[][] used = new int[HEIGHT][WIDTH];
        fillMatrix(used, -1);

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
                    if (used[newX][newY] != -1) {
                        continue;
                    }
                    boolean flag = false;
                    for (int j = 0; j < 8; ++j) {
                        int newNewX = newX + cordsX[j], newNewY = newY + cordsY[j];
                        if (newX == newNewX && newY == newNewY) {
                            continue;
                        }
                        if (newNewX >= 0 && newNewX < HEIGHT && newNewY >= 0 && newNewY < WIDTH) {
                            if (newNewX == 0 || newNewX == HEIGHT - 1 || newNewY == 0 || newNewY == WIDTH - 1 || gameMap[newNewX][newNewY] == spaceCode) {
                                flag = true;
                                break;
                            }
                        }
                    }
                    if (!flag) {
                        used[newX][newY] = used[x][y] + 1;
                        q.addLast(new Pair(newX, newY));
                    }
                }
            }
        }

        int dist = used[b.first][b.second];
        if (dist == -1) {
            return path;
        }

        path.add(b);
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

        Collections.reverse(path);
        return path;
    }

    private ArrayList<Pair> getCenteredEntersToRoom(Room r, Room to) {
        ArrayList<Pair> doors = new ArrayList<>();
        int x1 = r.x - 1, y1 = r.y + r.width / 2;
        int x2 = r.x + r.height / 2, y2 = r.y - 1;
        int x3 = r.x + r.height / 2, y3 = r.y + r.width;
        int x4 = r.x + r.height, y4 = r.y + r.width / 2;
        doors.add(new Pair(x1, y1));
        doors.add(new Pair(x1 - 1, y1));
        doors.add(new Pair(x2, y2));
        doors.add(new Pair(x2, y2 - 1));
        doors.add(new Pair(x3, y3));
        doors.add(new Pair(x3, y3 + 1));
        doors.add(new Pair(x4, y4));
        doors.add(new Pair(x4 + 1, y4));

        int[] prt = new int[4];
        int dX = r.getCenterPointInRoom().first - to.getCenterPointInRoom().first;
        int dY = r.getCenterPointInRoom().second - to.getCenterPointInRoom().second;
        if (Math.abs(dX) > Math.abs(dY)) {
            if (dX > 0) {
                if (dY > 0) {
                    prt[0] = 4;
                    prt[1] = 3;
                    prt[2] = 2;
                    prt[3] = 1;
                } else {
                    prt[0] = 4;
                    prt[1] = 2;
                    prt[2] = 3;
                    prt[3] = 1;
                }
            } else {
                if (dY > 0) {
                    prt[0] = 1;
                    prt[1] = 3;
                    prt[2] = 2;
                    prt[3] = 4;
                } else {
                    prt[0] = 1;
                    prt[1] = 2;
                    prt[2] = 3;
                    prt[3] = 4;
                }
            }
        } else {
            if (dX > 0) {
                if (dY > 0) {
                    prt[0] = 3;
                    prt[1] = 4;
                    prt[2] = 2;
                    prt[3] = 1;
                } else {
                    prt[0] = 3;
                    prt[1] = 2;
                    prt[2] = 4;
                    prt[3] = 1;
                }
            } else {
                if (dY > 0) {
                    prt[0] = 1;
                    prt[1] = 4;
                    prt[2] = 2;
                    prt[3] = 3;
                } else {
                    prt[0] = 1;
                    prt[1] = 2;
                    prt[2] = 4;
                    prt[3] = 3;
                }
            }
        }

        ArrayList<Pair> prtDoors = new ArrayList<>();
        for (int i = 4; i > 0; --i) {
            for (int j = 0; j < 4; ++j) {
                if (prt[j] == i) {
                    Pair temp = doors.get(j * 2 + 1);
                    int x = temp.first, y = temp.second;
                    if (x >= 0 && y >= 0 && x < HEIGHT && y < WIDTH) {
                        prtDoors.add(doors.get(j * 2));
                        prtDoors.add(doors.get(j * 2 + 1));
                        break;
                    }
                }
            }
        }
        return prtDoors;
    }

    private void connectTwoRoomsByWayPriority(int[][] gameMap, Room a, Room b) {
        ArrayList<Pair> fromS = getCenteredEntersToRoom(a, b);
        ArrayList<Pair> fromF = getCenteredEntersToRoom(b, a);

        for (int alpha = 0; alpha < Math.max(fromF.size(), fromS.size()); alpha += 2) {
            for (int i = 0; i < fromS.size() && i < alpha; i += 2) {
                for (int j = 0; j < fromF.size() && j < alpha; j += 2) {
                    ArrayList<Pair> path = connectToPoints(gameMap, fromS.get(i + 1), fromF.get(j + 1));
                    if (path.size() == 0) {
                        continue;
                    }
                    if (path.get(path.size() - 1).first == fromF.get(j + 1).first && path.get(path.size() - 1).second == fromF.get(j + 1).second &&
                            path.get(0).second == fromS.get(i + 1).second && path.get(0).second == fromS.get(i + 1).second) {
                        path.add(0, fromS.get(i));
                        path.add(fromF.get(j));
                        a.addWay(path);
                        a.addDoor(new Door(path.get(0).first, path.get(0).second, true, a));
                        Collections.reverse(path);
                        b.addWay(path);
                        b.addDoor(new Door(path.get(0).first, path.get(0).second, true, b));
                        for (int k = 0; k < path.size(); ++k) {
                            Pair temp = path.get(k);
                            gameMap[temp.first][temp.second] = spaceCode;
                        }
                        return;
                    }
                }
            }
        }
    }

    private void connectTwoRoomsByMinDist(int[][] gameMap, Room a, Room b) {
        ArrayList<Pair> fromS = getCenteredEntersToRoom(a, b);
        ArrayList<Pair> fromF = getCenteredEntersToRoom(b, a);

        int minDist = 100000;
        for (int i = 0; i < fromS.size(); i += 2) {
            for (int j = 0; j < fromF.size(); j += 2) {
                ArrayList<Pair> path = connectToPoints(gameMap, fromS.get(i + 1), fromF.get(j + 1));
                if (path.size() == 0) {
                    continue;
                }
                if (path.get(path.size() - 1).first == fromF.get(j + 1).first && path.get(path.size() - 1).second == fromF.get(j + 1).second &&
                        path.get(0).second == fromS.get(i + 1).second && path.get(0).second == fromS.get(i + 1).second) {
                    if (minDist > path.size()) {
                        minDist = path.size();
                    }
                }
            }
        }
        for (int i = 0; i < fromS.size(); i += 2) {
            for (int j = 0; j < fromF.size(); j += 2) {
                ArrayList<Pair> path = connectToPoints(gameMap, fromS.get(i + 1), fromF.get(j + 1));
                if (path.size() != minDist) {
                    continue;
                }

                if (path.get(path.size() - 1).first == fromF.get(j + 1).first && path.get(path.size() - 1).second == fromF.get(j + 1).second &&
                        path.get(0).second == fromS.get(i + 1).second && path.get(0).second == fromS.get(i + 1).second) {

                    path.add(0, fromS.get(i));
                    path.add(fromF.get(j));
                    a.addWay(path);
                    Collections.reverse(path);
                    b.addWay(path);
                    for (int k = 0; k < path.size(); ++k) {
                        Pair temp = path.get(k);
                        gameMap[temp.first][temp.second] = spaceCode;
                    }
                    return;
                }
            }
        }
    }

    private void connectRooms(int[][] gameMap, ArrayList<Room> rooms) {
        HashMap<Node, HashSet<Node>> neighbor = new HashMap<>();
        HashMap<Integer, HashSet<Integer>> intNeighbor = new HashMap<>();
        int[] cordsX = {0, 0, 1, -1}, cordsY = {1, -1, 0, 0};

        for (int i = 0; i < HEIGHT; ++i) {
            for (int j = 0; j < WIDTH; ++j) {
                if (nodeGameMap[i][j] == 0) {
                    continue;
                }
                for (int k = 0; k < 4; ++k) {
                    int newX = i + cordsX[k], newY = j + cordsY[k];
                    if (newX >= 0 && newX < HEIGHT && newY >= 0 && newY < WIDTH) {
                        if (nodeGameMap[newX][newY] == 0) {
                            continue;
                        }
                        if (nodeGameMap[i][j] != nodeGameMap[newX][newY]) {
                            if (!neighbor.containsKey(pointToNode(i, j))) {
                                neighbor.put(pointToNode(i, j), new HashSet<Node>());
                                intNeighbor.put(nodeGameMap[i][j], new HashSet<Integer>());
                            }
                            neighbor.get(pointToNode(i, j)).add(pointToNode(newX, newY));
                            intNeighbor.get(nodeGameMap[i][j]).add(nodeGameMap[newX][newY]);
                        }
                    }
                }
            }
        }

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
                    connectTwoRoomsByWayPriority(gameMap, intToNode.get(key).room, intToNode.get(node).room);
                    connect.add(new Pair(key, node));
                    connect.add(new Pair(node, key));
                }
            }
        }
    }

    //######################### HELPER #########################

    private void configureRooms(ArrayList<Room> rooms) {
        for (int i = 0; i < rooms.size(); ++i) {
            Room room = rooms.get(i);
            if (room.isBonus) {
                room.configureBonusEnvironment();
            }
        }
        //System.out.println(rooms.size());
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

    private Node pointToNode(int pointX, int pointY) {
        return intToNode.get(nodeGameMap[pointX][pointY]);
    }

    private Room pointToRoom(int pointX, int pointY) {
        return intToRoom.get(roomGameMap[pointX][pointY]);
    }

    private boolean checkRoomsConnection(int[][] gameMap) {
        int x = 0, y = 0;
        boolean flag = false;
        for (int i = 0; i < HEIGHT; ++i) {
            for (int j = 0; j < WIDTH; ++j) {
                if (gameMap[i][j] != wallCode) {
                    x = i;
                    y = j;
                    flag = true;
                }
            }
            if (flag) {
                break;
            }
        }

        flag = true;
        boolean[][] used = new boolean[HEIGHT][WIDTH];
        fillMatrix(used, false);
        int[] cordsX = {0, 0, 1, -1}, cordsY = {1, -1, 0, 0};
        ArrayDeque<Pair> q = new ArrayDeque<>();
        q.addLast(new Pair(x, y));
        used[x][y] = true;

        while (!q.isEmpty()) {
            Pair temp = q.getFirst();
            q.removeFirst();
            x = temp.first;
            y = temp.second;
            for (int i = 0; i < 4; ++i) {
                int newX = x + cordsX[i], newY = y + cordsY[i];
                if (newX >= 0 && newY >= 0 && newX < HEIGHT && newY < WIDTH) {
                    if (!used[newX][newY] && gameMap[newX][newY] != wallCode) {
                        used[newX][newY] = true;
                        q.addLast(new Pair(newX, newY));
                    }
                }
            }
        }

        for (int i = 0; i < HEIGHT; ++i) {
            for (int j = 0; j < WIDTH; ++j) {
                if (gameMap[i][j] != wallCode && !used[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }
}

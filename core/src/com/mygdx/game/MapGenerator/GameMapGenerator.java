package com.mygdx.game.MapGenerator;

import java.awt.*;
import java.io.*;
import java.security.SecureRandom;
import java.util.*;

public class GameMapGenerator {
    public static int WIDTH, HEIGHT, EPS;
    public SecureRandom secureRandom;

    public GameMapGenerator(int HEIGHT, int WIDTH, int BORDER, int MIN_HEIGHT, int MIN_WIDTH, int EPS) {
        this.HEIGHT = HEIGHT;
        this.WIDTH = WIDTH;
        Node.BORDER = BORDER;
        Node.MIN_HEIGHT = MIN_HEIGHT;
        Node.MIN_WIDTH = MIN_WIDTH;
    }

    public GameMapGenerator() {
        this.HEIGHT = 300;
        this.WIDTH = 300;
        this.EPS = 250;
        Node.BORDER = 3;
        Node.MIN_HEIGHT = 5;
        Node.MIN_WIDTH = 5;
    }

    void initGameMap(int[][] gameMap, Node v) {
        if (v.leftChild != null) {
            initGameMap(gameMap, v.leftChild);
            initGameMap(gameMap, v.rightChild);
        } else {
            for (int i = v.room.x; i < v.room.x + v.room.height; ++i) {
                for (int j = v.room.y; j < v.room.y + v.room.width; ++j) {
                    gameMap[i][j] = 1;
                }
            }
//            for (int i = v.x + 1; i < v.x + v.height; ++i) {
//                for (int j = v.y + 1; j < v.y + v.width; ++j) {
//                    gameMap[i][j] = 1;
//                }
//            }
        }
    }

    void createTree(Node v) {
        int splittable = v.isPossibleToSplit();
        if (splittable != -1) {
            int newH = (v.height - 2 * (v.MIN_HEIGHT + 2 * v.BORDER)) / 2, newW = (v.width - 2 * (v.MIN_WIDTH + 2 * v.BORDER)) / 2;
            int deltaH = 0, deltaW = 0;
            if (newH > 0) {
                deltaH = (Math.abs(secureRandom.nextInt()) % newH);
            }
            if (newW > 0) {
                deltaW = (Math.abs(secureRandom.nextInt()) % newW);
            }
            if (splittable == 0) {
                // horizontal
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

    void createRooms(Node v) {
        if (v.rightChild != null) {
            createRooms(v.leftChild);
            createRooms(v.rightChild);
        } else {
            v.createRoom();
        }
    }

    public Pair getRandomPointInRoom(Rectangle r) {
        int newX = (Math.abs(secureRandom.nextInt()) % r.height);
        int newY = (Math.abs(secureRandom.nextInt()) % r.width);
        return (new Pair(r.x + newX, r.y + newY));
    }

    void connectTwoRooms(int[][] gameMap, Rectangle a, Rectangle b) {
        Pair s = getRandomPointInRoom(a);
        Pair f = getRandomPointInRoom(b);
        if (s.second <= f.second) {
            for (int i = s.second; i < f.second; ++i) {
                gameMap[s.first][i] = 1;
            }
        } else {
            for (int i = s.second; i > f.second; --i) {
                gameMap[s.first][i] = 1;
            }
        }
        if (s.first <= f.first) {
            for (int i = s.first; i <= f.first; ++i) {
                gameMap[i][f.second] = 1;
            }
        } else {
            for (int i = s.first; i >= f.first; --i) {
                gameMap[i][f.second] = 1;
            }
        }
    }

    void getLeafsRoom(ArrayList<Rectangle> rooms, Node v) {
        if (v.leftChild == null) {
            rooms.add(v.room);
        } else {
            getLeafsRoom(rooms, v.leftChild);
            getLeafsRoom(rooms, v.rightChild);
        }
    }

    void connectAllRooms(int[][] gameMap, Node root) {
        ArrayList<Rectangle> rooms = new ArrayList<>();
        getLeafsRoom(rooms, root);
        int[][] used = new int[HEIGHT][WIDTH];
        for (int i = 0, cnt = 1; i < rooms.size(); ++i, ++cnt) {
            Rectangle rec = rooms.get(i);
            for (int x = rec.x; x < rec.x + rec.height; ++x) {
                for (int y = rec.y; y < rec.y + rec.width; ++y) {
                    used[x][y] = cnt;
                }
            }
            if (Math.abs(secureRandom.nextInt()) % 1000 < EPS) {
                for (int x = rec.x; x < rec.x + rec.height; ++x) {
                    for (int y = rec.y; y < rec.y + rec.width; ++y) {
                        gameMap[x][y] = 0;
                    }
                }
                rooms.remove(i);
            }
        }

        for (int i = 0; i < rooms.size(); ++i) {
            connectTwoRooms(gameMap, rooms.get(i), rooms.get((i + 1) % rooms.size()));
        }
/*
        for (int i = 0; i < rooms.size(); ++i) {
            connectTwoRooms(gameMap, rooms.get(i), rooms.get((Math.abs(secureRandom.nextInt()) % rooms.size())));
        }*/

        /*ArrayList<ArrayList<Rectangle>> neighbor = new ArrayList<>();
        for (int i = 0; i < rooms.size(); ++i) {
            neighbor.add(new ArrayList<>());
        }
        for (int i = 0; i < rooms.size(); ++i) {
            Rectangle ul, uR, dR, dL, temp = rooms.get(i);
            uL = getNeighbor(used, temp.x, temp.y, "ul");
            connectTwoRooms(gameMap, temp, uL);
        }*/
    }

    public int[][] generate() {
        secureRandom = new SecureRandom();
        int[][] gameMap = new int[HEIGHT][WIDTH];
        Node root = new Node(0, 0, HEIGHT - 1, WIDTH - 1);

        createTree(root);
        createRooms(root);
        initGameMap(gameMap, root);
        connectAllRooms(gameMap, root);
        //printMap(gameMap);
        return gameMap;
    }
}
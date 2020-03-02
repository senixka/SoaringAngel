package com.mygdx.game.MapGenerator;

import com.mygdx.game.World;

import java.security.SecureRandom;

public class Node {
    public static int BORDER, MIN_HEIGHT, MIN_WIDTH;
    public int y, x, width, height;
    public Node leftChild, rightChild;
    public Room room;
    public boolean isNodeVisible;
    private SecureRandom secureRandom;

    public Node(int x, int y, int height, int width) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.isNodeVisible = true;
        secureRandom = new SecureRandom();
    }

    public int isPossibleToSplit() {
        int lenH = 2 * (2 * BORDER + MIN_HEIGHT);
        int lenW = 2 * (2 * BORDER + MIN_WIDTH);
        if (height - 1 < lenH || width - 1 < lenW) {
            return -1;
        }
        boolean splitH = (Math.abs(secureRandom.nextInt()) % 1000) > 500;
        if (width > height && (double)width / (double)height >= 1.25) {
            splitH = false;
        } else if (height > width && (double)height / (double)width >= 1.25) {
            splitH = true;
        }
        if (splitH) {
            return 0;
        }
        return 1;
    }

    public void createRoom() {
        int newH = (height - 2 * BORDER - MIN_HEIGHT) / 2, newW = (width - 2 * BORDER - MIN_WIDTH) / 2;
        int newX0 = x + BORDER, newY0 = y + BORDER;
        int newX1 = x + height - BORDER, newY1 = y + width - BORDER;
        newX0 = (int)(newX0 + Math.abs(secureRandom.nextInt()) % (newH + 1));
        newY0 = (int)(newY0 + Math.abs(secureRandom.nextInt()) % (newW + 1));
        newX1 = (int)(newX1 - Math.abs(secureRandom.nextInt()) % (newH + 1));
        newY1 = (int)(newY1 - Math.abs(secureRandom.nextInt()) % (newW + 1));
        room = new Room(newX0, newY0, newY1 - newY0, newX1 - newX0);

        Pair cords = room.getRandomPointInRoom();
        room.addMob(cords.first * World.pixSize, cords.second * World.pixSize);
    }
}

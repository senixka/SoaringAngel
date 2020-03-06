package com.mygdx.game.MapGenerator;

public class Node {
    public static int BORDER, MIN_HEIGHT, MIN_WIDTH;
    public int y, x, width, height;
    public Node leftChild, rightChild;
    public Room room;
    public boolean isNodeVisible;

    public Node(int x, int y, int height, int width, int BORDER, int MIN_HEIGHT, int MIN_WIDTH) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.isNodeVisible = true;
        this.BORDER = BORDER;
        this.MIN_HEIGHT = MIN_HEIGHT;
        this.MIN_WIDTH = MIN_WIDTH;
    }

    public int isPossibleToSplit() {
        int lenH = 2 * (2 * BORDER + MIN_HEIGHT);
        int lenW = 2 * (2 * BORDER + MIN_WIDTH);
        if (height - 1 < lenH || width - 1 < lenW) {
            return -1;
        }
        boolean splitH = Rand.AbsModInt(1000) > 500;

        if (width > height && (double) width / (double) height >= 1.25) {
            splitH = false;
        } else if (height > width && (double) height / (double) width >= 1.25) {
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
        newX0 += Rand.AbsModInt(newH + 1);
        newY0 += Rand.AbsModInt(newW + 1);
        newX1 -= Rand.AbsModInt(newH + 1);
        newY1 -= Rand.AbsModInt(newW + 1);
        room = new Room(this, newX0, newY0, newY1 - newY0, newX1 - newX0);
    }
}

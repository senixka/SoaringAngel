package com.mygdx.game.MapGenerator;

public class Door {
    public int x, y;
    public boolean isOpen;
    public Room room;

    public Door(int x, int y, boolean isOpen, Room room) {
        this.room = room;
        this.x = x;
        this.y = y;
        this.isOpen = isOpen;
    }

    public void close() {
        isOpen = false;
    }

    public void open() {
        isOpen = true;
    }
}

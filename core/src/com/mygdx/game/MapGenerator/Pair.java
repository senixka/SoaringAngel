package com.mygdx.game.MapGenerator;

public class Pair implements Comparable {
    public int first;
    public int second;

    public Pair(int first, int second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public int compareTo(Object o) {
        return first - ((Pair) o).first;
    }
}

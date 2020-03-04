package com.mygdx.game.MapGenerator;

class Pair implements Comparable {
    int first;
    int second;

    public Pair(int first, int second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public int compareTo(Object o) {
        return first - ((Pair) o).first;
    }
}

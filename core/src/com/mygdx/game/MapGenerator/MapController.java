package com.mygdx.game.MapGenerator;

import com.badlogic.gdx.graphics.Texture;

public class MapController {
    MapGenerator mapGenerator;

    public MapController(MapGenerator mapGenerator) {
        this.mapGenerator = mapGenerator;
    }

    public int[][] getMap() {
        return null;
    }

    public Pair teleportPersInMaze() {
        return null;
    }

    public void update() {

    }

    public boolean goToNextLevel() {
        return false;
    }

    public Texture getMiniMap() {
        return null;
    }
}

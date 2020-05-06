package com.mygdx.game.MapGenerator;

public class ConstantMapController extends MapController {
    MapGenerator mapGenerator;

    public ConstantMapController(MapGenerator mapGenerator) {
        super(mapGenerator);
        this.mapGenerator = mapGenerator;
    }

    @Override
    public int[][] getMap() {
        return mapGenerator.getMap();
    }

    @Override
    public Pair teleportPersInMaze() {
        return new Pair(3, 3);
    }
}

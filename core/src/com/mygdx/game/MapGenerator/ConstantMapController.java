package com.mygdx.game.MapGenerator;

public class ConstantMapController extends MapController {
    ConstantMapGenerator mapGenerator;

    public ConstantMapController(MapGenerator mapGenerator) {
        super(mapGenerator);
        this.mapGenerator = (ConstantMapGenerator) mapGenerator;
    }

    @Override
    public int[][] getMap() {
        return mapGenerator.getMap();
    }

    @Override
    public Pair teleportPersInMaze() {
        return new Pair(3 + ConstantMapGenerator.frame, 3 + ConstantMapGenerator.frame);
    }
}

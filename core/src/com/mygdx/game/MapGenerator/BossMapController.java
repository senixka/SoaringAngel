package com.mygdx.game.MapGenerator;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.World;

public class BossMapController extends MapController {
    public static final int wallCode = 0, spaceCode = 1;
    public int WIDTH, HEIGHT, BORDER;
    public int[][] localGameMap;
    public Node bossNode;
    public Room bossRoom;
    public Pixmap localPixMiniMap;
    public Texture miniMapTexture;
    public boolean isControllerActivate, isPortalCreated;

    public BossMapController(BossMapGenerator generator) {
        super(generator);
        this.HEIGHT = generator.HEIGHT;
        this.WIDTH = generator.WIDTH;
        this.BORDER = generator.BORDER;
        this.localGameMap = generator.localGameMap;
        this.bossNode = generator.bossNode;
        this.bossRoom = generator.bossRoom;
        this.isControllerActivate = false;
        this.isPortalCreated = false;
        this.miniMapTexture = generator.miniMapTexture;
        this.localPixMiniMap = generator.localPixMiniMap;
    }

    public static void copyMatrix(int[][] from, int[][] to) {
        for (int i = 0; i < from.length; ++i) {
            for (int j = 0; j < from[i].length; ++j) {
                to[i][j] = from[i][j];
            }
        }
    }

    public static Vector3 mapCordsToGame(Vector3 vec) {
        Vector3 tmp = new Vector3();
        tmp.x = (float) ((int) vec.x * World.pixSize) + (float) World.pixSize / (float) 2;
        tmp.y = (float) ((int) vec.y * World.pixSize) + (float) World.pixSize / (float) 2;
        return tmp;
    }

    public void update() {
        if (bossRoom.mobs.size() <= 0) {
            if (!isPortalCreated) {
                bossRoom.configurePortalEnvironment();
                isPortalCreated = true;
            }
        }
    }

    public boolean goToNextLevel() {
        return bossRoom.mobs.size() <= 0;
    }

    public int[][] getMap() {
        int[][] bossMap = new int[HEIGHT][WIDTH];
        copyMatrix(localGameMap, bossMap);
        return bossMap;
    }

    public Texture getMiniMap() {
        Texture temp = new Texture(localPixMiniMap);
        return temp;
    }

    public Pair teleportPersInMaze() {
        if (bossRoom != null) {
            return bossRoom.getCenterPointInRoom();
        }
        return (new Pair(0, 0));
    }

}

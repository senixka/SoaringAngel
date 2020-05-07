package com.mygdx.game.MapGenerator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class ConstantMapGenerator extends MapGenerator {
    public static final int frame = 20;
    int[][] map;

    public ConstantMapGenerator(String filename) {
        //System.out.println(Gdx.files.internal(filename).readString());
        String[] s = Gdx.files.internal(filename).readString().split("\r\n");
        //System.out.println(s.length);
//        for (int i = 0; i < s.length; i++) {
//            System.out.println(s[i]);
//        }
        map = new int[s[0].length() + 2 * frame][s.length + 2 * frame];
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                map[i][j] = 0;
            }
        }
        for (int i = 0; i < s[0].length(); i++) {
            for (int j = 0; j < s.length; j++) {
                map[i + frame][j + frame] = s[s.length - j - 1].charAt(i) - '0';
            }
        }
    }

    @Override
    public int[][] getMap() {
        return map;
    }

    @Override
    public Texture getMiniMap() {
        return super.getMiniMap();
    }
}

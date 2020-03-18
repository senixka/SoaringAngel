package com.mygdx.game.Animations;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.MyAnimation;

public class MonsterAppear extends MyAnimation {
    public static final Texture texture = new Texture(Gdx.files.internal("MonsterAppear.png"));
    public static final int FRAME_COLS = 10;
    public static final int FRAME_ROWS = 7;

    public MonsterAppear() {
        super(texture, FRAME_COLS, FRAME_ROWS);
        setSize(300, 300);
        looping = false;
    }

    public MonsterAppear(float x, float y, float sizeX, float sizeY) {
        super(texture, FRAME_COLS, FRAME_ROWS);
        setPosition(x - Math.max(sizeX, 150 - sizeX / 2), y - Math.max(sizeX, 150 - sizeX / 2));
        setSize(Math.max(sizeX * 3, 300), Math.max(sizeX * 3, 300));
        looping = false;
    }
}

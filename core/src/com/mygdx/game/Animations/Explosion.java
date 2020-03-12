package com.mygdx.game.Animations;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.MyAnimation;

public class Explosion extends MyAnimation {
    public static final Texture texture = new Texture(Gdx.files.internal("RoundExplosion.png"));
    public static final int FRAME_COLS = 10;
    public static final int FRAME_ROWS = 7;

    public Explosion() {
        super(texture, FRAME_COLS, FRAME_ROWS);
        setSize(300, 300);
        looping = false;
    }

    public Explosion(float x, float y) {
        super(texture, FRAME_COLS, FRAME_ROWS);
        setPosition(x - 150, y - 150);
        setSize(300, 300);
        looping = false;
    }
}

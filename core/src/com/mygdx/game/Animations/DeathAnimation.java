package com.mygdx.game.Animations;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.mygdx.game.MyAnimation;

public class DeathAnimation extends MyAnimation {
    public static final Texture texture = new Texture(Gdx.files.internal("DeathAnimation.psd"));
    public static final int FRAME_COLS = 5;
    public static final int FRAME_ROWS = 2;

    public DeathAnimation() {
        super(texture, FRAME_COLS, FRAME_ROWS);
        setSize(100, 100);
        looping = false;
    }

    public DeathAnimation(float x, float y) {
        super(texture, FRAME_COLS, FRAME_ROWS);
        setPosition(x - 50, y - 50);
        setSize(100, 100);
        looping = false;
        walkAnimation = new Animation<>(0.055f, walkFrames);
    }
}

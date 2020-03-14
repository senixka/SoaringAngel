package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class MyAnimation {
    private int FRAME_COLS;
    private int FRAME_ROWS;
    private float stateTime;
    private float x, y, sizeX, sizeY;

    private Animation<TextureRegion> walkAnimation;
    private Texture walkSheet;
    private TextureRegion[] walkFrames;
    private TextureRegion currentFrame;

    public boolean looping;

    public MyAnimation(Texture texture, int FRAME_COLS, int FRAME_ROWS) {
        walkSheet = texture;
        this.FRAME_COLS = FRAME_COLS;
        this.FRAME_ROWS = FRAME_ROWS;
        create();
    }

    private void create() {
        TextureRegion[][] tmp = TextureRegion.split(walkSheet, walkSheet.getWidth() / FRAME_COLS, walkSheet.getHeight() / FRAME_ROWS); // #10
        walkFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
        int index = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COLS; j++) {
                walkFrames[index++] = tmp[i][j];
            }
        }
        walkAnimation = new Animation<>(0.025f, walkFrames);
        stateTime = 0f;
    }

    public void update(float delta) {
        stateTime += delta;
        currentFrame = walkAnimation.getKeyFrame(stateTime, looping);
    }

    public void draw() {
        if (currentFrame == null) {
            return;
        }
        MyGame.batch.draw(currentFrame, x, y, sizeX, sizeY);
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void setSize(float sizeX, float sizeY) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
    }

    public boolean isDead() {
        return !looping && (stateTime > 50);
    }
}

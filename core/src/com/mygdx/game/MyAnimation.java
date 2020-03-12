package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class MyAnimation {
    public int FRAME_COLS; // #1
    public int FRAME_ROWS; // #2

    Animation walkAnimation; // #3
    Texture walkSheet; // #4
    TextureRegion[] walkFrames; // #5
    TextureRegion currentFrame; // #7

    float x, y, sizeX, sizeY;

    float stateTime; // #8
    public boolean looping;

    public MyAnimation(Texture texture, int FRAME_COLS, int FRAME_ROWS) {
        walkSheet = texture;
        this.FRAME_COLS = FRAME_COLS;
        this.FRAME_ROWS = FRAME_ROWS;
        create();
    }

    public void create() {
        //walkSheet = new Texture(Gdx.files.internal("animation_sheet.png")); // #9
        TextureRegion[][] tmp = TextureRegion.split(walkSheet, walkSheet.getWidth()/FRAME_COLS, walkSheet.getHeight()/FRAME_ROWS); // #10
        walkFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
        int index = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COLS; j++) {
                walkFrames[index++] = tmp[i][j];
            }
        }
        walkAnimation = new Animation(0.025f, walkFrames); // #11
        stateTime = 0f; // #13
    }


    public void update(float delta) {
        stateTime += delta; // #15
        currentFrame = (TextureRegion) walkAnimation.getKeyFrame(stateTime, looping); // #16
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
    public void setSize(float sixeX, float sizeY) {
        this.sizeX = sixeX;
        this.sizeY = sizeY;
    }

    public boolean isDead() {
        return !looping && (stateTime > 50);
    }
}

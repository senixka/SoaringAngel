package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class SpeechFrame {
    //Very special instrument, don't use it

    String text;
    public boolean hasHead;
    public static final Texture background = new Texture(Gdx.files.internal("blue.png"));
    public static final float x = 0, y = 0, sizeX = 800, sizeY = 160;
    public static final float headSizeX = 160, headSizeY = 160;
    public static final int lineLenNoHead = 64, lineLenHead = 44;
    public int lineLen;
    public String[] write = new String[5];
    public Texture head;

    public SpeechFrame(Texture head) {
        this.hasHead = true;
        this.head = head;
        lineLen = lineLenHead;
    }

    public SpeechFrame() {
        hasHead = false;
        lineLen = lineLenNoHead;
    }

    public void setText(String text) {
        this.text = text;
        String[] s = text.split(" ");
        int cur = 0;
        for (int i = 0; i < write.length; i++) {
            write[i] = "";
        }
        for (int i = 0; i < s.length; i++) {
            if (write[cur].length() + 1 + s[i].length() <= lineLen) {
                write[cur] = write[cur] + " " + s[i];
            } else {
                cur++;
                write[cur] = s[i];
            }
        }
    }

    public void draw(float x, float y, float scale) {
        MyGame.font.getData().setScale(2 * scale);
        MyGame.batch.draw(background, x, y, sizeX * scale, sizeY * scale);
        if (hasHead) {
            MyGame.batch.draw(head, x, y, headSizeX * scale, headSizeY * scale);
            x += scale * headSizeX;
        }
        for (int i = 0; i < write.length; i++) {
            MyGame.font.draw(MyGame.batch, write[i], x, y + sizeY * scale);
            y -= (sizeY / write.length) * scale;
        }
    }
}

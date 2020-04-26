package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;

public class Dialogue {
    public ArrayList<SpeechFrame> speechFrames = new ArrayList<>();
    public NPC npc;
    public int cur = 0;

    public Dialogue(NPC npc) {
        this.npc = npc;
    }

    public void addSpeechFrame(Texture head, String text) {
        if (head != null) {
            SpeechFrame s = new SpeechFrame(head);
            s.setText(text);
            speechFrames.add(s);
        } else {
            SpeechFrame s = new SpeechFrame();
            s.setText(text);
            speechFrames.add(s);
        }
    }

    public void draw(float x, float y, float scale) {
        speechFrames.get(cur).draw(x, y, scale);
    }

    public void click() {
        cur++;
        if (cur >= speechFrames.size()) {
            World.controller.removeDialogue();
            npc.finalTalk();
        }
    }
}

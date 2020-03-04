package com.mygdx.game.MapGenerator;

import com.badlogic.gdx.math.Vector3;

public class Instruction {
    public float x, y, len;
    public Vector3 vec;

    Instruction(float x, float y, float len, Vector3 vec) {
        this.x = x;
        this.y = y;
        this.len = len;
        this.vec = vec;
    }
}

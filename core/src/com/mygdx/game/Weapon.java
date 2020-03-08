package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;

public class Weapon extends Subject{

    public Weapon(Texture texture, String name) {
        super(texture, name);
    }

    public void draw() {
    }

    public void update(float delta) {

    }

    public void attackDown() {
    }

    public void attackUp() {

    }

    public Pers getPers() {
        return World.pers;
    }

    public Vector3 getVector() {
        if (getPers().target == null) {
            return Helper.norm(getPers().lastVect.cpy());
        }

        float x = getPers().target.x - getPers().getX();
        float y = getPers().target.y - getPers().getY();
        return Helper.norm(new Vector3(x, y, 0));
    }

    public Subject getSubject() {
        return null;
    }
}

package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;

public class Weapon extends Subject {
    public int energy = 0;


    public Weapon(Texture texture, String name, int energy) {
        super(texture, name);
        this.energy = energy;
        setText(getDiscript());
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

        float x = getPers().target.getCenter().x - getPers().getCenter().x;
        float y = getPers().target.getCenter().y - getPers().getCenter().y;
        return Helper.norm(new Vector3(x, y, 0));
    }

    public Subject getSubject() {
        return null;
    }

    public boolean energyEnough() {
        if (getPers().energy >= energy) {
            getPers().energy -= energy;
            return true;
        }
        return false;
    }

    public String getDiscript() {
        String s = "Rapid: " + getRapid() + "\n" + "Energy: " + energy + "\n" + "Damage: " + getDamage() + "\n" + addInf();
        return s;
    }

    public String  getDamage() {
        return "";
    }

    public String getRapid() {
        return "";
    }

    public String addInf() {
        return "";
    }
}

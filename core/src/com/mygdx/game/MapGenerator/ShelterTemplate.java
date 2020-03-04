package com.mygdx.game.MapGenerator;

import java.util.ArrayList;

public class ShelterTemplate {
    public ArrayList<Instruction> tmp;

    public ShelterTemplate() {
        tmp = new ArrayList<>();
    }

    public void addInst(Instruction inst) {
        tmp.add(inst);
    }
}

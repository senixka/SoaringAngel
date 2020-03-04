package com.mygdx.game.MapGenerator;

import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;

public class Shelter {

//    public class Instruction {
//        public float x, y, len;
//        public Vector3 vec;
//
//        Instruction(float x, float y, float len, Vector3 vec) {
//            this.x = x;
//            this.y = y;
//            this.len = len;
//            this.vec = vec;
//        }
//    }

//    public class ShelterTemplate {
//        public ArrayList<Instruction> tmp;
//
//        public ShelterTemplate() {
//            tmp = new ArrayList<>();
//        }
//
//        public void addInst(Instruction inst) {
//            tmp.add(inst);
//        }
//    }

    public ArrayList<ShelterTemplate> shelters;
    public int index;

    public Shelter() {
        this.shelters = new ArrayList<>();
        initShelters();
        this.index = Rand.AbsModInt(shelters.size());
    }

    public void initShelters() {
        shelters.add(template1());
        shelters.add(template2());
        shelters.add(template3());
    }

    public ShelterTemplate template1() {
        /**
         * # # # # # # # # # #
         * #                 #
         * #   @        @    #
         * #                 #
         * #                 #
         * #   @        @    #
         * #                 #
         * # # # # # # # # # #
         */
        ShelterTemplate tmp = new ShelterTemplate();
        tmp.addInst(new Instruction(0.20f, 0.20f, 0.01f, new Vector3(0, 0, 0)));
        tmp.addInst(new Instruction(0.80f, 0.80f, 0.01f, new Vector3(0, 0, 0)));
        tmp.addInst(new Instruction(0.80f, 0.20f, 0.01f, new Vector3(0, 0, 0)));
        tmp.addInst(new Instruction(0.20f, 0.80f, 0.01f, new Vector3(0, 0, 0)));
        return tmp;
    }

    public ShelterTemplate template2() {
        /**
         * # # # # # # # # # #
         * #                 #
         * #   @        @    #
         * #   @        @    #
         * #   @        @    #
         * #   @        @    #
         * #                 #
         * # # # # # # # # # #
         */
        ShelterTemplate tmp = new ShelterTemplate();
        tmp.addInst(new Instruction(0.20f, 0.80f, 0.61f, new Vector3(1, 0, 0)));
        tmp.addInst(new Instruction(0.20f, 0.20f, 0.61f, new Vector3(1, 0, 0)));
        return tmp;
    }

    public ShelterTemplate template3() {
        /**
         * # # # # # # # # # #
         * #                 #
         * #   @ @ @ @ @ @   #
         * #   @         @   #
         * #   @    @    @   #
         * #   @         @   #
         * #                 #
         * # # # # # # # # # #
         */
        ShelterTemplate tmp = new ShelterTemplate();
        tmp.addInst(new Instruction(0.20f, 0.20f, 0.61f, new Vector3(1, 0, 0)));
        tmp.addInst(new Instruction(0.20f, 0.20f, 0.61f, new Vector3(0, 1, 0)));
        tmp.addInst(new Instruction(0.20f, 0.80f, 0.61f, new Vector3(1, 0, 0)));
        tmp.addInst(new Instruction(0.51f, 0.51f, 0.01f, new Vector3(0, 0, 0)));
        return tmp;
    }
}

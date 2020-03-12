package com.mygdx.game.MapGenerator;

import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;

public class Shelter {
    public static ArrayList<ShelterTemplate> shelters;
    public static ShelterTemplate tmpEmpty, tmp1, tmp2, tmp3, tmp4;
    public int index;
    private static boolean wasInit = false;

    public Shelter() {
        if (!wasInit) {
            wasInit = true;
            this.shelters = new ArrayList<>();
            initShelters();
        }
        this.index = Rand.AbsModInt(shelters.size());
        this.index = 1;
    }

    public void initShelters() {
        templateEmpty();
        shelters.add(tmpEmpty);
        template1();
        shelters.add(tmp1);
//        template2();
//        shelters.add(tmp2);
//        template3();
//        shelters.add(tmp3);
//        template4();
//        shelters.add(tmp3);
    }

    public void templateEmpty() {
        /**
         * # # # # # # # # # #
         * #                 #
         * #                 #
         * #                 #
         * #                 #
         * #                 #
         * #                 #
         * # # # # # # # # # #
         */
        tmpEmpty = new ShelterTemplate();
    }

    public void template1() {
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
        tmp1 = new ShelterTemplate();
        tmp1.addInst(new Instruction(0.20f, 0.20f, 0.01f, new Vector3(0, 0, 0)));
        tmp1.addInst(new Instruction(0.80f, 0.80f, 0.01f, new Vector3(0, 0, 0)));
        tmp1.addInst(new Instruction(0.80f, 0.20f, 0.01f, new Vector3(0, 0, 0)));
        tmp1.addInst(new Instruction(0.20f, 0.80f, 0.01f, new Vector3(0, 0, 0)));
    }

    public void template2() {
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
        tmp2 = new ShelterTemplate();
        tmp2.addInst(new Instruction(0.20f, 0.80f, 0.61f, new Vector3(1, 0, 0)));
        tmp2.addInst(new Instruction(0.20f, 0.20f, 0.61f, new Vector3(1, 0, 0)));
    }

    public void template3() {
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
        tmp3 = new ShelterTemplate();
        tmp3.addInst(new Instruction(0.20f, 0.20f, 0.60f, new Vector3(1, 0, 0)));
        tmp3.addInst(new Instruction(0.20f, 0.20f, 0.60f, new Vector3(0, 1, 0)));
        tmp3.addInst(new Instruction(0.20f, 0.80f, 0.60f, new Vector3(1, 0, 0)));
        tmp3.addInst(new Instruction(0.51f, 0.51f, 0.01f, new Vector3(0, 0, 0)));
    }

    public void template4() {
        /**
         * # # # # # # # # # #
         * #                 #
         * #   @     @ @ @   #
         * #   @             #
         * #   @         @   #
         * #   @         @   #
         * #             @   #
         * # # # # # # # # # #
         */
        tmp4 = new ShelterTemplate();
        tmp4.addInst(new Instruction(0.20f, 0.20f, 0.40f, new Vector3(1, 0, 0)));
        tmp4.addInst(new Instruction(0.20f, 0.70f, 0.50f, new Vector3(0, 1, 0)));
        tmp4.addInst(new Instruction(0.60f, 0.70f, 0.40f, new Vector3(1, 0, 0)));
    }
}

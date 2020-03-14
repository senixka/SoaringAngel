package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;

import java.util.LinkedList;
import java.util.List;

public class Inventory {
    public static List<Subject> subjects;
    public static Subject usingSubject;
    public static Rectangle[] icons;
    public static final Texture frame = new Texture(Gdx.files.internal("red.png"));
    public static Button take;
    public static int aim = -1;


    public static void create() {
        subjects = new LinkedList<>();
        icons = new Rectangle[16];
        icons[0] = new Rectangle(65, 200, 50, 40);
        icons[1] = new Rectangle(125, 200, 50, 40);
        icons[2] = new Rectangle(185, 200, 50, 40);
        icons[3] = new Rectangle(245, 200, 50, 40);
        icons[4] = new Rectangle(305, 200, 50, 40);
        icons[5] = new Rectangle(65, 150, 50, 40);
        icons[6] = new Rectangle(125, 150, 50, 40);
        icons[7] = new Rectangle(185, 150, 50, 40);
        icons[8] = new Rectangle(245, 150, 50, 40);
        icons[9] = new Rectangle(305, 150, 50, 40);

        icons[10] = new Rectangle(65, 285, 50, 40);
        icons[11] = new Rectangle(65, 335, 50, 40);
        icons[12] = new Rectangle(65, 385, 50, 40);
        icons[13] = new Rectangle(140, 385, 50, 40);
        icons[14] = new Rectangle(140, 335, 50, 40);
        icons[15] = new Rectangle(220, 345, 100, 80);

        MyGame.font.getData().setScale(2);
        take = new Button(400, 100, 200, 50);
        take.setText("Equip");

    }

    public static void draw() {
//        for (int i = 0; i < 16; i++) {
//            MyGame.batch.draw(frame, icons[i].x, icons[i].y, icons[i].width, icons[i].height);
//        }

        if (aim != -1) {
            MyGame.batch.draw(frame, icons[aim].x, icons[aim].y, icons[aim].width, icons[aim].height);
            take.draw(MyGame.batch, MyGame.font);
        }
        for (int i = 0; i < 10; i++) {
            if (subjects.size() > i) {
                MyGame.batch.draw(subjects.get(i).texture, icons[i].x, icons[i].y, icons[i].width, icons[i].height);
            }
        }


        if (usingSubject != null) {
            MyGame.batch.draw(usingSubject.texture, icons[15].x, icons[15].y, icons[15].width, icons[15].height);
        }
    }

    public static void add(Subject Subject) {
        if (subjects.size() == 10) {
            return;
        }
        if (usingSubject == null) {
            usingSubject = Subject;
        } else {
            subjects.add(Subject);
        }
        if (usingSubject != null) {
            World.pers.weapon = (Weapon) usingSubject;
        }
    }

    public static boolean isFull() {
        return subjects.size() == 10;
    }

    public static void equip() {
        if (aim == -1) {
            return;
        }
        if (aim >= subjects.size()) {
            return;
        }
        Subject s = usingSubject;
        usingSubject = subjects.get(aim);
        subjects.remove(usingSubject);
        subjects.add(s);
        World.pers.weapon = (Weapon) usingSubject;
        aim = -1;
    }

    public static void setAim(Vector3 v) {
        for (int i = 0; i < icons.length; i++) {
            if (icons[i].contains(v)) {
                aim = i;
                return;
            }
        }
        aim = -1;
    }

    public static void touchDown(Vector3 v) {
        if (aim != -1 && take.isPressed(v)) {
            System.out.println("Yeeep");
            equip();
        }
        setAim(v);
    }

}

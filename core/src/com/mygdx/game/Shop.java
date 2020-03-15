package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;

import java.util.LinkedList;
import java.util.List;

public class Shop {
    public static final Texture frame = new Texture(Gdx.files.internal("red.png"));
    public List<Subject> subjects;
    public static Rectangle[] icons;
    public static Button take;
    public int aim = -1;


    public Shop() {
        subjects = new LinkedList<>();
    }

    public static void create() {
        MyGame.font.getData().setScale(2);
        take = new Button(400, 100, 200, 50);
        take.setText("Buy");

        icons = new Rectangle[10];
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
    }

    public void draw() {
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


    }

    public boolean isFull() {
        return subjects.size() == 10;
    }

    public void buy() {
        if (aim == -1) {
            return;
        }
        if (aim >= subjects.size()) {
            return;
        }
        if (Inventory.isFull()) {
            return;
        }
        Inventory.add(subjects.get(aim));
        subjects.remove(aim);
        aim = -1;
    }

    public void setAim(Vector3 v) {
        for (int i = 0; i < icons.length; i++) {
            if (icons[i].contains(v)) {
                aim = i;
                return;
            }
        }
        aim = -1;
    }

    public void touchDown(Vector3 v) {
        if (aim != -1 && take.isPressed(v)) {
            System.out.println("Yeeep");
            buy();
        }
        setAim(v);
    }

    public void add(Subject Subject) {
        if (subjects.size() == 10) {
            return;
        }
        subjects.add(Subject);
    }

}

package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MyGame extends Game {
    public static SpriteBatch batch;
    public static BitmapFont font;
    public static OrthographicCamera camera;
    public static MyGame game;
    public static GameScreen gameScreen;

    public static void staticSetScreen(Screen sc) {
        game.setScreen(sc);
    }

    @Override
    public void create() {
        System.out.println("We start");
        batch = new SpriteBatch();
        font = new BitmapFont();
        camera = new OrthographicCamera();
        game = this;
        game.setScreen(new MainMenuScreen());
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}

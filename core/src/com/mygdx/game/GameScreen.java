package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameScreen implements Screen {
    public MyGame game;
    public SpriteBatch batch;
    public BitmapFont font;
    public OrthographicCamera camera;
    public GameController controller;

    public GameScreen() {
        batch = MyGame.batch;
        font = MyGame.font;
        font.getData().setScale(3);
        camera = MyGame.camera;
        camera.setToOrtho(false, 3 * 800, 3 * 480);
        controller = new GameController();
        Gdx.input.setInputProcessor(controller);
        World.start(controller);
        MyGame.gameScreen = this;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(12f / 256, 175f / 256, 77f / 256, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        World.update(delta);
        camera.position.set(World.getFocus());
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        World.draw();
        batch.end();
    }

    public void contin() {
        controller = new GameController();
        Gdx.input.setInputProcessor(controller);
        MyGame.staticSetScreen(this);
        World.setController(controller);
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}

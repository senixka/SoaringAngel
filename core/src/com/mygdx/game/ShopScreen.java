package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ShopScreen implements Screen {
    public MyGame game;
    public SpriteBatch batch;
    public BitmapFont font;
    public OrthographicCamera camera;
    public Texture background;
    public ShopController controller;
    public Shop shop;

    public ShopScreen(Shop shop) {
        this.shop = shop;
        batch = MyGame.batch;
        font = MyGame.font;
        font.getData().setScale(3);
        camera = MyGame.camera;
        camera.setToOrtho(false, 800, 480);
        background = new Texture(Gdx.files.internal("ShopScreen.psd"));

        controller = new ShopController(shop);
        Gdx.input.setInputProcessor(controller);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 1, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(background, 0, 0, 800, 480);
        shop.draw();
        batch.end();
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

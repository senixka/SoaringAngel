package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

public class MainMenuScreen implements Screen {
    SpriteBatch batch;
    BitmapFont font;
    OrthographicCamera camera;
    Button button1;


    public MainMenuScreen() {
        batch = MyGame.batch;
        font = MyGame.font;
        font.getData().setScale(3);
        camera = MyGame.camera;
        camera.setToOrtho(false, 800, 480);
        camera.lookAt(400, 240, 0);

        batch.setProjectionMatrix(camera.combined);
        button1 = new Button(200, 300, 400, 100);
        button1.setText("Start");
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 1, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        batch.begin();
        button1.draw(batch, font);
        batch.end();
        if (Gdx.input.justTouched()) {
            Vector3 vector3 = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(vector3);

            if (button1.isPressed(vector3)) {
                MyGame.staticSetScreen(new GameScreen());
            }
        }
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

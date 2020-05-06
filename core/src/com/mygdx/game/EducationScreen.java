package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;

public class EducationScreen implements Screen {
    public static final Texture ED0 = new Texture(Gdx.files.internal("education0.psd"));
    public static final Texture ED1 = new Texture(Gdx.files.internal("education1.psd"));
    public static final Texture ED2 = new Texture(Gdx.files.internal("education2.psd"));
    public static final Texture ED3 = new Texture(Gdx.files.internal("education3.psd"));
    public static final Texture ED4 = new Texture(Gdx.files.internal("education4.psd"));
    public Texture[] textures;

    int ind;

    public EducationScreen() {
        ind = 0;
        textures = new Texture[]{ED0, ED1, ED2, ED3, ED4};
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 1, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        MyGame.camera.update();
        MyGame.batch.begin();
        MyGame.batch.draw(textures[ind], 0, 0, 800, 480);
        MyGame.batch.end();
        if (Gdx.input.justTouched()) {
            ind++;
            if (ind == 5) {
                MyGame.staticSetScreen(new MainMenuScreen());
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

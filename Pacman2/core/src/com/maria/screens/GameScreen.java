package com.maria.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import com.maria.model.World;
import com.maria.view.TextureFactory;



public class GameScreen implements Screen, InputProcessor {
    private BitmapFont font;
    private SpriteBatch batch;
    private boolean won;
    private OrthographicCamera mCamera;
    private int score;
    private World mWorld;
    private Game mGame;

    public GameScreen(boolean won, int score, World world, Game game) {
        this.won = won;
        setScore(score);
        setGame(game);
        setWorld(world);
        Gdx.input.setInputProcessor(this);
        init();
    }

    public World getWorld() {
        return mWorld;
    }

    public void setWorld(World world) {
        mWorld = world;
    }

    public void setGame(Game game) {
        mGame = game;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void init() {
        mCamera = new OrthographicCamera(5, 5);
        mCamera.position.set(0, 0, 0);
        mCamera.update();
    }

    @Override
    public void show() {
        font = new BitmapFont(false);
        font.setColor(Color.GOLD);
        batch = new SpriteBatch();
    }

    @Override
    public void render(float delta) {
        String text;
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        if (won) {
            text = "¡Has ganado !";
        }
        else {
            text = "¡Has perdido !";
        }
        text = text + "\nPuntuacion = " + score + "\n Pulsa Q para salir.\n" +
                "Pulsa otra tecla para volver a jugar\n Espero que os haya gustado <3";
        font.draw(batch, text, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, 0,
                Align.center, false);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        mCamera.viewportWidth = (5 / height) *
                width;
        mCamera.update();
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

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.Q) {
            Gdx.app.exit();
        }
        replay();
        return false;
    }

    public void replay() {
        TextureFactory.getInstance().resetTextureFactory();
        GameOverScreen screen = new GameOverScreen(mGame);
        mGame.setScreen(screen);
        screen.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        // Handle scrolling here if needed
        return false;
    }
    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

}

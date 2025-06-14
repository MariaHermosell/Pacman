package com.maria.screens;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import com.maria.controller.SoundManager;
import com.maria.controller.WorldRenderer;
import com.maria.model.World;
import com.maria.view.TextureFactory;


public class GameOverScreen implements Screen {
    private final World mWorld;
    private final WorldRenderer mWorldRenderer;
    private final OrthographicCamera mCamera;
    private final OrthographicCamera GUIcamera;
    private final float VIEWPORT_GUI_WIDTH = 800.0f;
    private final float VIEWPORT_GUI_HEIGHT = 480.0f;
    private BitmapFont font;
    private SpriteBatch batch;


    public GameOverScreen(Game game)
    {
        mWorld = new World();
        mWorld.createGhosts();
        mWorldRenderer = new WorldRenderer(mWorld, game);
        mCamera = new OrthographicCamera(40, 40);
        GUIcamera = new OrthographicCamera(VIEWPORT_GUI_WIDTH, VIEWPORT_GUI_HEIGHT);
    }

    @Override
    public void show() {
        font = new BitmapFont(true);
        font.setColor(Color.GOLD);
        batch = new SpriteBatch();
        mCamera.setToOrtho(true);
        GUIcamera.position.set(0, 0, 0);
        GUIcamera.setToOrtho(true);
        GUIcamera.update();
        SoundManager.getInstance().getBeginningMusic().play();
    }

    @Override
    public void render(float delta) {
        mWorldRenderer.render(mCamera, delta);
        renderGUI(batch);
    }

    @Override
    public void resize(int width, int height) {
        mCamera.viewportWidth = 30;
        mCamera.viewportHeight = 35f * height/width;
        mCamera.position.set(0, 0, 0);
        mCamera.update();

        mWorldRenderer.setSize(Math.min(mCamera.viewportWidth / mWorld.getWidth(),
                mCamera.viewportHeight / mWorld.getHeight()));
        
        GUIcamera.viewportHeight = VIEWPORT_GUI_HEIGHT * (width / height);
        GUIcamera.update();
    }

    public void renderGUISCore (SpriteBatch batch) {
        float x = GUIcamera.viewportWidth / 2;
        font.draw(batch, "Puntuacion: " + mWorld.getScore(), x, 10, 0,
                Align.center, false);
    }

    public void renderLives(SpriteBatch batch) {
        Texture livesTexture = TextureFactory.getInstance().getLivesTexture();
        float spriteWidth = livesTexture.getWidth() + 5;

        float x = (GUIcamera.viewportWidth / 2) - (World.maxLife * spriteWidth) / 2;
        float y = GUIcamera.viewportHeight - (livesTexture.getHeight() + 5);

        for (int i = 0; i < World.maxLife; i++) {
            if (World.getLifeCounter() <= i)
                batch.setColor(0.5f, 0.5f, 0.5f, 0.5f);
            batch.draw(livesTexture,
                     x + i * spriteWidth, y);
            batch.setColor(1, 1, 1, 1);
        }
    }

    public void renderGUI(SpriteBatch batch) {
        batch.setProjectionMatrix(GUIcamera.combined);
        batch.begin();
        renderGUISCore(batch);
        renderLives(batch);
        batch.end();
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

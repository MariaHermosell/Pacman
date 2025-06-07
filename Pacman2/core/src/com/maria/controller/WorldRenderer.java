package com.maria.controller;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.maria.model.GameItems;
import com.maria.model.Ghost;
import com.maria.model.MovingItems.Direction;
import com.maria.model.Pacman;
import com.maria.model.Vector2D;
import com.maria.model.World;
import com.maria.screens.GameScreen;
import com.maria.view.TextureFactory;


// Clase encargada de dibujar el mundo del juego en pantalla y gestionar entradas del usuario

public class WorldRenderer implements InputProcessor {
    float size; // Tamaño visual de cada celda del juego
    private final SpriteBatch batch; // Maneja el renderizado gráfico
    private final TextureFactory textureFactory; // Proporciona texturas para elementos del juego
    private final World mWorld; // Mundo del juego que contiene todos los elementos del juego
    private final Game mGame; // Instancia del juego para gestionar pantallas
    private FPSLogger mFPSLogger = new FPSLogger(); // Muestra FPS en consola (para depuración)


    // Constructor inicializando el mundo del juego y asignando controles de entrada
    public WorldRenderer(World world, Game game) {
        textureFactory = TextureFactory.getInstance(); // Inicializa texturas
        batch = new SpriteBatch(); // Inicializa el SpriteBatch para dibujar elementos
        mWorld = world; // Asigna el mundo actual
        Gdx.input.setInputProcessor(this); // Asigna esta clase como procesadora de inputs
        mWorld.getMaze().loadDemoLevel(World.getCoef()); // Carga el nivel del laberinto
        mGame = game; // Asigna instancia del juego actual
    }

    // Establece el tamaño visual de los elementos del juego
    public void setSize(float size) {
        this.size = size;
    }

    public float getSize() {
        return size;
    }


    // Renderiza todos los elementos visuales del juego cada frame
    public void render(OrthographicCamera camera, float deltaTime) {
        updateEpsilons(deltaTime); // Actualiza precisión para movimiento de personajes
        checkEndGame(); // Verifica si el juego ha terminado (ganar o perder)
        batch.setProjectionMatrix(camera.combined); // Establece la cámara
        drawWorld(); // Dibuja el mundo en pantalla
        textureFactory.update(deltaTime); // Actualiza animaciones de texturas si es necesario

        if (!isPaused(deltaTime)) { // Si el juego no está pausado, mueve personajes
            moveGameElements(deltaTime);
        }

//        mFPSLogger.log();
    }

    // Determina si el juego está en pausa temporal (muerte de Pacman o pausa global)
    public boolean isPaused(float deltaTime) {
        Pacman pacman = mWorld.getPacman();
        if (pacman.getDeadCounter() > 0) { // Pacman muerto recientemente
            pacman.decreaseDeadCounter(deltaTime); // Reduce el tiempo hasta revivir
            return true; // Juego pausado
        }
        if (mWorld.getGlobalPauseTimer() > 0) { // Pausa global activada
            mWorld.decreaseGlobalPauseTimer(deltaTime); // Reduce el tiempo de pausa global
            return true;
        }
        return false; // Juego continúa normalmente
    }

    // Método para simular retraso (para pruebas)
    public void setLag(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    // Actualiza el valor de epsilon para movimientos precisos según FPS
    public void updateEpsilons(float deltaTime) {
        mWorld.getPacman().getMovementController().updateEpsilon(deltaTime);
        for (Ghost ghost : mWorld.getGhosts()) {
            ghost.getMovementController().updateEpsilon(deltaTime);
        }
    }

    // Comprueba si la partida ha terminado (ganar o perder)
    public void checkEndGame() {
        if (mWorld.getMaze().getPelletNumber() == 0) { // Si no quedan bolas, jugador gana
            Gdx.app.log(WorldRenderer.class.getSimpleName(), "Has ganado !");
            mGame.setScreen(new GameScreen(true, mWorld.getScore(), mWorld, mGame));
        } else if (World.getLifeCounter() <= 0) { // Si no quedan vidas, jugador pierde
            Gdx.app.log(getClass().getSimpleName(), "Has perdido !");
            mGame.setScreen(new GameScreen(false, mWorld.getScore(), mWorld, mGame));
        }
    }


    // Dibuja todos los elementos del juego en pantalla
    public void drawWorld() {
        batch.begin(); // Inicia dibujo en pantalla
        Gdx.gl.glClearColor(0, 0, 0, 1); // Limpia pantalla con fondo negro
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // Limpia buffer de colores

        // Dibuja cada elemento del juego usando su textura correspondiente
        for (GameItems e : mWorld) {
            Vector2D position = e.getPosition();
            Texture texture = textureFactory.getTexture(e);
            batch.draw(texture,
                    ((position.x / ((float) World.getCoef())) - mWorld.getMaze().getWidth() / 2f) * size,
                    ((position.y / ((float) World.getCoef())) - mWorld.getMaze().getHeight() / 2f) * size,
                    size, size,
                    0, 0,
                    texture.getWidth(), texture.getHeight(), false, true);
        }
        batch.end(); // Finaliza dibujo en pantalla
    }

    // Mueve Pacman y fantasmas cada frame
    public void moveGameElements(float deltaTime) {
        mWorld.getPacman().move(deltaTime); // Mueve a Pacman
        for (Ghost ghost : mWorld.getGhosts()) {
            ghost.useAI(); // Aplica IA del fantasma
            ghost.move(deltaTime); // Mueve al fantasma
        }
    }

// Gestiona entradas de teclado
    public boolean keyDown(int keycode) {
        Pacman pacman = mWorld.getPacman();
        switch (keycode) {
            case Input.Keys.LEFT:
                pacman.setWantedDirection(Direction.LEFT);
                break;
            case Input.Keys.RIGHT:
                pacman.setWantedDirection(Direction.RIGHT);
                break;
            case Input.Keys.UP:
                pacman.setWantedDirection(Direction.UP);
                break;
            case Input.Keys.DOWN:
                pacman.setWantedDirection(Direction.DOWN);
                break;
        }

        return true;
    }


    public boolean keyUp(int keycode) {
        return false;
    }


    public boolean keyTyped(char character) {
        return false;
    }


    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }


    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }


    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }


    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }


    public boolean scrolled(float amountX, float amountY) {

        return false;
    }

    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {

        return false;
    }

}

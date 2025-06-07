package com.maria.model;
import com.badlogic.gdx.utils.TimeUtils;
import com.maria.controller.MovementControllerGhost;
import com.maria.controller.MovementControllerPacman;
import com.maria.controller.RandomAI;
import com.maria.controller.ChasePacmanAI;
import com.maria.controller.ShortestPathAI;
import com.maria.controller.AdaptativeAI;

import java.util.ArrayList;
import java.util.Iterator;
/**
 * Representa  el mundo del juego (laberinto, personajes, elementos, etc.).
 * Contiene métodos para gestionar el estado global del juego, como vidas, puntos y elementos móviles
 */
public class World implements Iterable<GameItems> {
    private int width = 30;
    private int height = 35;

    private final Pacman mPacman;
    private final ArrayList<Ghost> mGhosts = new ArrayList<Ghost>();

    private Maze mMaze;

    private final static int mCoef = 100;

    private int score = 0;

    long startTime;

    private ArrayList<GameItems> mGameElements;
    private static int lifeCounter = 3;
    public static final int maxLife = 3;
    private float globalPauseTimer = 4;
    public static Vector2D pacmanStartingPosition = new Vector2D(14 * mCoef, 17 * mCoef);
    public static Vector2D redGhostStartingPos = new Vector2D(14 * mCoef, 13 * mCoef);
    public static Vector2D blueGhostStartingPos = new Vector2D(14 * mCoef, 14 * mCoef);
    public static Vector2D yellowGhostStartingPos = new Vector2D(13 * mCoef, 13 * mCoef);
    public static Vector2D pinkGhostStartingPos = new Vector2D(13 * mCoef, 14 * mCoef);


    public World() {
        mPacman = new Pacman(new Vector2D(pacmanStartingPosition), this,
                500, null);
        mPacman.setMovementController(new MovementControllerPacman(this));
        mMaze = new Maze(this);
        startTime = TimeUtils.millis();
        setLifeCounter(maxLife);
    }

    public static Vector2D getPacmanStartingPosition() {
        return pacmanStartingPosition;
    }

    public static int getLifeCounter() {
        return lifeCounter;
    }

    public static void setLifeCounter(int lifeCounter) {
        World.lifeCounter = lifeCounter;
    }

    public static void decreaseLifeCounter() {
        lifeCounter--;
        if (lifeCounter <= 0) {
            lifeCounter = 0;
        }
    }

    public void createGhosts() {
        // Se crean las instancias de los cuatro fantasmas con sus posiciones iniciales y velocidad
        RedGhost redGhost = new RedGhost(new Vector2D(redGhostStartingPos), this, 500);
        YellowGhost yellowGhost = new YellowGhost(new Vector2D(yellowGhostStartingPos), this, 500);
        PinkGhost pinkGhost = new PinkGhost(new Vector2D(pinkGhostStartingPos), this, 500);
        BlueGhost blueGhost = new BlueGhost(new Vector2D(blueGhostStartingPos), this, 500);
        // Se inicializa la lista de elementos del juego
        mGameElements = new ArrayList<GameItems>();
        // Se añaden a la lista de elementos del juego: Pacman y los fantasmas
        mGameElements.add(mPacman);
        mGameElements.add(redGhost);
        mGameElements.add(yellowGhost);
        mGameElements.add(pinkGhost);
        mGameElements.add(blueGhost);
        // Se añaden los fantasmas a la lista específica de fantasmas
        mGhosts.add(pinkGhost);
        mGhosts.add(yellowGhost);
        mGhosts.add(redGhost);
        mGhosts.add(blueGhost);

        // Se asigna un controlador de movimiento a cada fantasma
        for (Ghost ghost : mGhosts) {
            ghost.setMovementController(new MovementControllerGhost(this, ghost));
        }

        // Se establece el tipo de inteligencia artificial (IA) de cada fantasma
        pinkGhost.initAI(new AdaptativeAI(pinkGhost)); // El fantasma rosa usa una IA adaptativa
        yellowGhost.initAI(new ChasePacmanAI(yellowGhost)); // El fantasma amarillo persigue a Pacman
        redGhost.initAI(new RandomAI(redGhost)); // El fantasma rojo se mueve de forma aleatoria
        blueGhost.initAI(new ShortestPathAI(blueGhost, mPacman)); // El fantasma azul sigue el camino más corto a Pacman
    }

    public ArrayList<Ghost> getGhosts() {
        return mGhosts;
    }

    public long getStartTime() {
        return startTime;
    }

    public int getScore() {
        return score;
    }

    public void winPoint(int wonPoint) {
        if (wonPoint <= 0) {
            wonPoint = 5;
        }
        score += wonPoint;
    }


    public int getWidth() {
//        return mMaze.getWidth() + 3;
        return width;
    }


    public int getHeight() {
//        return mMaze.getHeight() + 2;
        return height;
    }


    public Pacman getPacman() {
        return mPacman;
    }


    public Maze getMaze() {
        return mMaze;
    }


    @Override
    public Iterator<GameItems> iterator() {
        return new WorldIterator();
    }


    class WorldIterator implements Iterator<GameItems> {

        final Iterator<GameItems> mazeIterator = mMaze.iterator();
        final Iterator<GameItems> mGameElementIterator = mGameElements.iterator();

        @Override
        public boolean hasNext() {
            return mazeIterator.hasNext() || mGameElementIterator.hasNext();
        }

        @Override
        public GameItems next() {
            if (mazeIterator.hasNext()) {
                return mazeIterator.next();
            }
            return mGameElementIterator.next();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("No se puede borrar");
        }
    }

    public static int getCoef() {
        return mCoef;
    }

    public float getGlobalPauseTimer() {
        return globalPauseTimer;
    }

    public void setGlobalPauseTimer(float globalPauseTimer) {
        this.globalPauseTimer = globalPauseTimer;
    }

    public void decreaseGlobalPauseTimer(float deltaTime) {
        globalPauseTimer -= deltaTime;
        if (globalPauseTimer < 0) {
            globalPauseTimer = 0;
        }
    }
}

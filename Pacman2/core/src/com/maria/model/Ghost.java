package com.maria.model;
import com.maria.controller.DeadGhostAI;
import com.maria.controller.GhostAI;
import com.maria.controller.OutOfHouseAI;
import com.maria.controller.RandomAI;



public abstract class Ghost extends MovingItems {

    private GhostAI usedAI = null;
    private GhostAI defaultAI = null;
    private GhostAI frightenedAI = null;
    private GhostAI outOfHouseAI = null;
    private GhostAI deadAI = null;
    private boolean alive = true;
    private float mFrightenedTimer = 0;
    private int frightenedSpeed = 100;
    private int normalSpeed;
    private Vector2D startingPos;


    protected Ghost(Vector2D position, World world, int speed) {
        super(position, world, speed);
        normalSpeed = speed;
        startingPos = new Vector2D(position);
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public Vector2D getStartingPos() {
        return startingPos;
    }

    public void initAI(GhostAI defaultAI) {
        this.defaultAI = defaultAI;
        frightenedAI = new RandomAI(this);
        outOfHouseAI = new OutOfHouseAI(this);
        deadAI = new DeadGhostAI(this);
        usedAI = outOfHouseAI;
    }

    public void switchToDeadAI() {
        usedAI = deadAI;
    }

    public void switchToFrightenedAI() {
        usedAI = frightenedAI;
    }

    public void switchToOutAI() {
        usedAI = outOfHouseAI;
    }

    public void switchToDefaultAI() {
        usedAI = defaultAI;
    }

    public GhostAI getUsedAI() {
        return usedAI;
    }

    public void setUsedAI(GhostAI usedAI) {
        if (usedAI == null) {
            throw new IllegalArgumentException("Ai no puede ser nulo");
        }
        this.usedAI = usedAI;
    }

    public GhostAI getDefaultAI() {
        return defaultAI;
    }

    public void useAI() {
        usedAI.setDirection(this);
    }

    public void setFrightenedTimer(float frightenedTimer) {
        mFrightenedTimer = frightenedTimer;
        if (frightenedTimer > 0) {
            switchToFrightenedAI();
            setSpeed(frightenedSpeed);
        }
        else if (frightenedTimer == 0) {
            setSpeed(normalSpeed);
        }
    }

    public void decreaseFrightenedTimer(float time) {
        mFrightenedTimer -= time;
        if (mFrightenedTimer < 0) {
            mFrightenedTimer = 0;
            switchToDefaultAI();
            setSpeed(normalSpeed);
        }
    }

    public float getFrightenedTimer() {
        return mFrightenedTimer;
    }

    public boolean isFrightened() {
        return mFrightenedTimer > 0;
    }
}
/*Esta clase define la lógica interna de un fantasma del juego Pacman, gestionando su comportamiento según diferentes estados del juego:

Estado Normal: persigue activamente a Pacman.
Estado Asustado: se mueve aleatoriamente y puede ser comido por Pacman.
Estado Muerto: regresa rápidamente a la casa para reaparecer.
Saliendo de la Casa: movimiento inicial tras ser comido o al inicio del nivel.
Utiliza una estructura clara basada en diferentes IA para cambiar fácilmente entre comportamientos específicos según eventos del juego (ser comido, ser asustado, etc.).*/
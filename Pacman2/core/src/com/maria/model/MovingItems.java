package com.maria.model;
import com.maria.controller.MovementController;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;


// Clase  que representa cualquier elemento que se mueve dentro del juego (Pacman, fantasmas, etc.)

public class MovingItems extends GameItems {





    // Enumeración para representar las direcciones en las que se pueden mover los elementos

    public enum Direction {
        LEFT, UP, RIGHT, DOWN;

        // Lista inmutable que contiene todas las direcciones posibles
        private static final List<Direction> VALUES =
                Collections.unmodifiableList(Arrays.asList(values()));

        // Tamaño de la lista de direcciones
        private static final int SIZE = VALUES.size();
        // Generador de números aleatorios para seleccionar direcciones aleatorias
        private static final Random RANDOM = new Random();
        // Método para obtener una dirección aleatoria
        public static Direction randomDirection()  {
            return VALUES.get(RANDOM.nextInt(SIZE));
        }
    }

    // Posición inicial del objeto en el laberinto
    private Vector2D startingPos;

    // Dirección deseada por el jugador o IA (hacia dónde quiere moverse)
    private Direction mWantedDirection;

    // Dirección actual en la que se está moviendo el objeto
    private Direction mCurrentDirection;

    // Velocidad del objeto
    public int mSpeed;
    // Controlador de movimiento que maneja la lógica de desplazamiento del objeto
    protected MovementController mMovementController;
    // Constructor de la clase
    protected MovingItems(Vector2D position, World world, int speed) {
        super(position, world);
        mCurrentDirection = Direction.RIGHT;
        mWantedDirection = Direction.RIGHT;
        setSpeed(speed);
        startingPos = new Vector2D(position);
    }
    // Restablece la posición del objeto a su posición inicial
    public void resetPosition() {
        mPosition = new Vector2D(startingPos);
    }

    // Devuelve la dirección actual en la que se mueve el objeto
    public Direction getCurrentDirection() {
        return mCurrentDirection;
    }

    // Establece una nueva dirección actual para el objeto
    public void setCurrentDirection(Direction currentDirection) {
        mCurrentDirection = currentDirection;
    }

    // Devuelve la dirección que el jugador o IA desea que el objeto tome
    public Direction getWantedDirection() {

        return mWantedDirection;
    }

    // Establece una nueva dirección deseada para el objeto
    public void setWantedDirection(Direction wantedDirection) {
        mWantedDirection = wantedDirection;
    }
    // Obtiene la velocidad actual del objeto
    public int getSpeed() {
        return mSpeed;
    }
    // Establece la velocidad del objeto, asegurando que sea un valor positivo
    public void setSpeed(int speed) {
        if (speed <= 0) {
            throw new IllegalArgumentException("La velocidad deberia ser positiva.");
        }
        mSpeed = speed;
    }
    // Obtiene el controlador de movimiento asignado al objeto
    public MovementController getMovementController() {
        return mMovementController;
    }
    // Asigna un nuevo controlador de movimiento al objeto
    public void setMovementController(MovementController movementController) {
        mMovementController = movementController;
    }

    // Actualiza la posición del objeto según su dirección y velocidad
    public void updatePosition(float deltaTime) {
        switch (mCurrentDirection) {
            case LEFT:
                mPosition.x -= (mSpeed * deltaTime); // Mueve el objeto a la izquierda
                break;
            case RIGHT:
                mPosition.x += (mSpeed * deltaTime); // Mueve el objeto a la derecha
                break;
            case UP:
                mPosition.y -= (mSpeed * deltaTime); // Mueve el objeto hacia arriba
                break;
            case DOWN:
                mPosition.y += (mSpeed * deltaTime); // Mueve el objeto hacia abajo
                break;
        }
    }
    // Método para mover el objeto, delegando la lógica al controlador de movimiento
    public void move(float deltaTime) {
        if (mMovementController == null) {
            throw new RuntimeException("El controller de movimiento ha sido seteado");
        }
        mMovementController.moveElement(this, deltaTime);
    }
}

/*MovingItems define los elementos que tienen movimiento en el juego, como Pacman o los fantasmas.
Gestiona la dirección actual, la dirección deseada, la velocidad del movimiento y la lógica asociada a cómo se mueve dentro del laberinto.
Su movimiento es controlado mediante una clase separada (MovementController), facilitando diferentes comportamientos según el tipo de elemento.*/
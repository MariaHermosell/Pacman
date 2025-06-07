package com.maria.controller;

import com.maria.model.Ghost;
import com.maria.model.MovingItems;

/**
 * Define la estructura base para la inteligencia artificial de los fantasmas.
 * Todas las IA de los fantasmas deben extender esta clase e implementar su propia lógica de movimiento.
 */
public abstract class GhostAI {
    protected MovementController mMovementController; // Controlador de movimiento del fantasma
    protected Ghost mGhost; // Fantasma asociado a esta IA

    /**
     * Constructor de la IA del fantasma.
     * Inicializa el fantasma y obtiene su controlador de movimiento.
     *
     * @param ghost El fantasma que utilizará esta IA.
     */
    public GhostAI(Ghost ghost) {
        mGhost = ghost;
        mMovementController = ghost.getMovementController();
    }

    /**
     * Establece un nuevo controlador de movimiento para el fantasma.
     *
     * @param movementController Nuevo controlador de movimiento.
     * @throws IllegalArgumentException Si el controlador de movimiento es nulo.
     */
    public void setMovementController(MovementController movementController) {
        if (movementController == null) {
            throw new IllegalArgumentException("El movimiento controlado no puede ser nulo ");
        }
        mMovementController = movementController;
    }

    /**
     * Método abstracto que cada IA de fantasma debe implementar para definir la dirección del movimiento.
     *
     * @param movableGameElement Elemento en movimiento (fantasma) al que se le asignará la dirección.
     */
    public abstract void setDirection(MovingItems movableGameElement);
}

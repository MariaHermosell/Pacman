package com.maria.controller;

import com.maria.model.Ghost;
import com.maria.model.MovingItems;

/**
 *Extiende GhostAI y representa una inteligencia artificial
 * adaptativa para los fantasmas en el juego. Cambia entre dos estrategias:
 * - RandomAI: movimiento aleatorio.
 * - ChasePacmanAI: persecución de Pac-Man.
 */

public class AdaptativeAI extends GhostAI {
    private GhostAI currentAI;
    private GhostAI randomAI;
    private GhostAI chaseAI;

    public AdaptativeAI(Ghost ghost) {
        super(ghost);
        randomAI = new RandomAI(ghost);
        chaseAI = new ChasePacmanAI(ghost);
        currentAI = chaseAI;
    }

    /**
     * Establece la dirección del fantasma basado en su ubicación en el mapa.
     * Si el fantasma se encuentra en una intersección, cambia su estrategia de IA:
     * - Si está persiguiendo a Pac-Man, cambia a movimiento aleatorio.
     * - Si está en movimiento aleatorio, cambia a persecución de Pac-Man.
     *
     * @param movableGameElement El elemento en movimiento (fantasma) al que se le asignará la dirección.
     */
    public void setDirection(MovingItems movableGameElement) {
        // Verifica si el fantasma está en una intersección
        if (mMovementController.isAtIntersection(movableGameElement.getPosition(),
                movableGameElement.getCurrentDirection())) {
            // Aplica la estrategia actual de movimiento
            currentAI.setDirection(movableGameElement);
            // Alterna entre las estrategias de IA
            if (currentAI instanceof ChasePacmanAI) {
                currentAI = randomAI; // Si estaba persiguiendo, ahora se moverá aleatoriamente
            }
            else {
                currentAI = chaseAI;// Si se movía aleatoriamente, ahora perseguirá a Pac-Man
            }
        }
    }
}

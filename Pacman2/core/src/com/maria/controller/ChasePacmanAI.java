package com.maria.controller;

import com.maria.model.Ghost;
import com.maria.model.MazeItems;
import com.maria.model.MovingItems;
import com.maria.model.MovingItems.Direction;
import com.maria.model.Pacman;
import com.maria.model.Vector2D;

import java.util.HashMap;
import java.util.Map;

/**
 * Extiende GhostAI y representa la lógica de un fantasma que persigue a Pac-Man.
 * Su estrategia consiste en calcular la dirección más corta para acercarse a Pac-Man en cada intersección.
 */

public class ChasePacmanAI extends GhostAI {
    public ChasePacmanAI(Ghost ghost) {
        super(ghost);
    }
    /**
     * Determina la dirección que el fantasma debe tomar para acercarse a Pac-Man.
     * Solo cambia de dirección cuando el fantasma se encuentra en una intersección.
     *
     * @param movableGameElement El fantasma cuyo movimiento será determinado por la IA.
     */
    @Override
    public void setDirection(MovingItems movableGameElement) {
        // Verifica si el fantasma está en una intersección
        if (mMovementController.isAtIntersection(movableGameElement.getPosition(),
                movableGameElement.getCurrentDirection())) {

            // Mapa de direcciones posibles con los elementos del laberinto en esas direcciones
            HashMap<Direction, MazeItems> availableDirections;

            // Obtiene la referencia de Pac-Man
            Pacman pacman = mMovementController.getWorld().getPacman();

            // Posición actual del fantasma
            Vector2D posicion = movableGameElement.getPosition();

            // Variable para encontrar la distancia mínima a Pac-Man
            double min = Double.MAX_VALUE;
            Direction chosenDirection = null; // Dirección elegida

            // Obtiene las direcciones disponibles desde la posición actual del fantasma
            availableDirections = mMovementController.getPossibleDirections(movableGameElement.getPosition(),
                    movableGameElement.getCurrentDirection());

            // Itera sobre cada dirección disponible y calcula la distancia hasta Pac-Man
            for (Map.Entry<Direction, MazeItems> entry : availableDirections.entrySet()) {
                double distance = mMovementController.getDistance(entry.getValue().getPosition(),
                        pacman.getPosition());

                // Si la distancia es menor que la mínima encontrada, actualiza la dirección elegida
                if (distance < min) {
                    chosenDirection = entry.getKey();
                    min = distance;
                }
            }

            // Si no se encontró una dirección válida, lanza una excepción
            if (chosenDirection == null) {
                throw new RuntimeException("Dirección no válida");
            }

            // Solo cambia de dirección si el fantasma está alineado en una intersección exacta
            if (posicion.getX() % 100 == 0 && posicion.getY() % 100 == 0) {
                movableGameElement.setWantedDirection(chosenDirection);
            }
        }
    }
}


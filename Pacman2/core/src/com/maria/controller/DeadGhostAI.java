package com.maria.controller;

import com.maria.model.GameItems;
import com.maria.model.Ghost;
import com.maria.model.GhostHouse;
import com.maria.model.MovingItems;
import com.maria.model.MovingItems.Direction;
import com.maria.model.Vector2D;

import java.util.HashMap;
import java.util.Map;

/**
 * Extiende GhostAI y maneja la lógica de los fantasmas cuando han sido
 * "comidos" por Pac-Man. Su objetivo es encontrar el camino de regreso a la casa de los fantasmas
 * (GhostHouse) para revivir.
 */
public class DeadGhostAI extends GhostAI {

    /**
     * Constructor de la IA para fantasmas muertos.
     *
     * @param ghost El fantasma que utilizará esta IA cuando esté muerto.
     */
    public DeadGhostAI(Ghost ghost) {
        super(ghost);
    }

    /**
     * Determina la dirección en la que el fantasma debe moverse para regresar a su casa.
     * Una vez que llega, revive y cambia su IA a la de salida.
     *
     * @param movableGameElement El fantasma cuyo movimiento será determinado por la IA.
     */
    @Override
    public void setDirection(MovingItems movableGameElement) {
        // Verifica si el fantasma ha llegado a la casa de los fantasmas
        if (mGhost.getMovementController().itemAtPosition(mGhost) instanceof GhostHouse) {
            // Revive al fantasma y cambia su IA para salir de la casa
            mGhost.setAlive(true);
            mGhost.switchToOutAI();
        }

        // Si el fantasma aún está muerto, calcula el camino de regreso a la casa
        if (!mGhost.isAlive()) {
            HashMap<Direction, GameItems> availableDirections;
            Vector2D position = movableGameElement.getPosition();

            // Variables para encontrar la dirección con la menor distancia a la casa
            double min = Double.MAX_VALUE;
            Direction resDirection = null;

            // Obtiene las direcciones posibles para moverse
            availableDirections = ((MovementControllerGhost) mMovementController).getPossibleDirections(
                    mGhost,
                    movableGameElement.getCurrentDirection());

            // Itera sobre cada dirección posible para encontrar la más cercana a la casa de los fantasmas
            for (Map.Entry<Direction, GameItems> entry : availableDirections.entrySet()) {
                double distance = mMovementController.getDistance(entry.getValue().getPosition(),
                        mGhost.getStartingPos());

                // Si la distancia es menor que la mínima registrada, actualiza la dirección a seguir
                if (distance < min) {
                    resDirection = entry.getKey();
                    min = distance;
                }
            }

            // Si no se encontró una dirección válida, lanza una excepción
            if (resDirection == null) {
                throw new RuntimeException("Dirección no válida");
            }

            // Solo cambia de dirección si el fantasma está exactamente en una intersección
            if (position.getX() % 100 == 0 && position.getY() % 100 == 0) {
                movableGameElement.setWantedDirection(resDirection);
            }
        }
    }
}

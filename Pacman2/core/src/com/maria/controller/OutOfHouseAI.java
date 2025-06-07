package com.maria.controller;

import com.maria.model.Ghost;
import com.maria.model.GhostHouse;
import com.maria.model.MovingItems;
import com.maria.model.MovingItems.Direction;


/**
 * Define el comportamiento del fantasma cuando está saliendo de la casa.
 * El fantasma siempre intenta moverse hacia arriba para salir de la casa.
 */
public class OutOfHouseAI extends GhostAI {

    // Constructor que crea una IA específica para sacar al fantasma de la casa
    public OutOfHouseAI(Ghost ghost) {
        super(ghost); // Llama al constructor de la clase superior GhostAI
    }

    @Override
    public void setDirection(MovingItems movableGameElement) {
        // Verifica si el fantasma está actualmente dentro de la casa de fantasmas
        if (mMovementController.itemAtPosition(movableGameElement) instanceof GhostHouse) {
            // Si está dentro de la casa, intenta salir hacia arriba
            movableGameElement.setWantedDirection(Direction.UP);
        }
        else {
            // Si el fantasma ya no está en la casa, cambia a la IA normal
            ((Ghost) movableGameElement).switchToDefaultAI();
        }
    }
}

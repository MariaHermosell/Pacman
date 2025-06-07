package com.maria.controller;

import com.maria.model.Ghost;
import com.maria.model.MovingItems;
import com.maria.model.Vector2D;


// Clase que define la Inteligencia Artificial aleatoria para un fantasma
public class RandomAI extends GhostAI {
    // Constructor que recibe un fantasma específico al que asignarle esta IA.

    public RandomAI(Ghost ghost) {
        super(ghost);
    }

// Método que establece la direccion del fantasma.
    public void setDirection(MovingItems movableGameElement) {
        Vector2D position = movableGameElement.getPosition();
        // Verifica si el fantasma está en una intersección.
        if (mMovementController.isAtIntersection(position,
                movableGameElement.getCurrentDirection())) {
            if (position.getX() % 100 == 0 && position.getY() % 100 == 0) {
                movableGameElement.setWantedDirection(MovingItems.Direction.randomDirection());
            }
        }
    }
}
/*
- Esta clase implementa una IA sencilla que mueve al fantasma de manera aleatoria.
- Cada vez que el fantasma llega a una intersección (posición exacta múltiplo de 100 en X e Y), decide una nueva dirección al azar.
- Utiliza el método `randomDirection()` para obtener la dirección aleatoria.
- Se asegura de que el cambio de dirección solo ocurra en posiciones perfectamente alineadas con las celdas del laberinto para evitar movimientos incorrectos.*/
package com.maria.controller;

import com.maria.model.GameItems;
import com.maria.model.Ghost;
import com.maria.model.MazeItems;
import com.maria.model.MovingItems;

import java.util.Map;

import static com.maria.model.MovingItems.Direction;



// Clase que define la Inteligencia Artificial para que un fantasma siga la ruta más corta hacia un objetivo específico.
public class ShortestPathAI extends GhostAI {

    // El objetivo al que se dirige el fantasma (por ejemplo, Pacman o una posición concreta del laberinto)
    private GameItems target;

    // Constructor que recibe únicamente al fantasma. La meta (target) puede definirse después.
    public ShortestPathAI(Ghost ghost) {
        super(ghost); // Inicializa la clase padre (GhostAI)
    }

    // Constructor que recibe al fantasma y al objetivo específico al cual dirigirse
    public ShortestPathAI(Ghost ghost, GameItems target) {
        super(ghost); // Inicializa la clase padre (GhostAI)
        setTarget(target); // Establece el objetivo al que se dirigirá el fantasma
    }

    // Devuelve el objetivo actual del fantasma.
    public GameItems getTarget() {
        return target;
    }

    // Establece o modifica el objetivo del fantasma.
    public void setTarget(GameItems target) {
        this.target = target;
    }

    // Método que determina la dirección que tomará el fantasma para llegar al objetivo utilizando la ruta más corta.
    @Override
    public void setDirection(MovingItems movableGameElement) {
        // Calcula la ruta más corta únicamente cuando el fantasma está alineado en la cuadrícula y llega a una intersección.
        if (mMovementController.isAtIntersection(movableGameElement.getPosition(),
                movableGameElement.getCurrentDirection())) {

            // Calcula la ruta más corta desde la posición actual hacia el objetivo (target).
            mMovementController.findShortestPath(target.getPosition());

            // Obtiene la siguiente dirección (y bloque correspondiente) que acerca más al objetivo.
            Map.Entry<Direction, MazeItems> result = mMovementController.getBlockWithSmallestLabel(
                    movableGameElement.getPosition(),
                    movableGameElement.getCurrentDirection()
            );

            // Actualiza la dirección deseada del fantasma hacia el bloque más cercano al objetivo.
            movableGameElement.setWantedDirection(result.getKey());

        }
    }
}

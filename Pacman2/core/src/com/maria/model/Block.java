package com.maria.model;

/**
 * Esta clase representa los bloques del juego
 */

public class Block extends MazeItems {
    /**
     * Crea un bloque en una mPosition determinada en un mWorld determinado.
     *
     * @param position La posición m del bloque.
     * @param world El mWorld del bloque.
     */
    public Block(Vector2D position, World world) {
        super(position, world);
    }


}

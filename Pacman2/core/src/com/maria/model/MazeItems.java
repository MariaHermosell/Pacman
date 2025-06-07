package com.maria.model;


public class MazeItems extends GameItems {
    private int shortestPathLabel;

    protected MazeItems(Vector2D position, World world) {
        super(position, world);
    }

    public int getShortestPathLabel() {
        return shortestPathLabel;
    }

    public void setShortestPathLabel(int shortestPathLabel) {
        this.shortestPathLabel = shortestPathLabel;
    }
}
// Se recorrerá el laberinto y se asignará un valor a shortest para cada bloque
package com.maria.model;

// Representa una posición o vector en un espacio bidimensional (2D). Se utiliza para gestionar posiciones en el juego, como ubicaciones de personajes, objetos o elementos gráficos.
public class Vector2D {
    public int x; // Coordenada X del vector
    public int y; // Coordenada Y del vector

    // Constructor que crea un vector a partir de dos enteros (coordenadas x e y).
    public Vector2D(int x, int y) {
        this.x = x; // Asigna el valor recibido a la coordenada X
        this.y = y; // Asigna el valor recibido a la coordenada Y
    }

    // Constructor de copia que crea un nuevo vector a partir de otro Vector2D.
    public Vector2D(Vector2D position) {
        setX(position.getX()); // Asigna la coordenada X del vector recibido
        setY(position.getY()); // Asigna la coordenada Y del vector recibido
    }


    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "Vector2D{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    public boolean equals(Vector2D o) {
        return x == o.x && y == o.y;
    }
}

package com.maria.model;


// Clase abstracta que representa elementos generales del juego (por ejemplo, Pacman, bolas, fantasmas, paredes, etc.)
public abstract class GameItems {

    // Posición del elemento del juego en coordenadas 2D.
    protected Vector2D mPosition;

    // Referencia al mundo al que pertenece el elemento (por ejemplo, el laberinto actual).
    @SuppressWarnings("unused") // Puede no usarse directamente aquí, sino en clases derivadas.
    protected World mWorld;

    // Constructor que asigna posición y mundo al elemento del juego al momento de crearse.
    protected GameItems(Vector2D position, World world) {
        setPosition(position); // Establece la posición inicial
        setWorld(world);       // Establece el mundo en el que se encuentra
    }

    // Método para obtener la posición actual del elemento en el juego.
    public Vector2D getPosition() {
        return mPosition;
    }

    // Método que establece o modifica la posición del elemento en el juego.
    public void setPosition(Vector2D position) {
        // Comprueba que la posición proporcionada no sea nula
        if (position == null)
            throw new IllegalArgumentException("La posicion no puede ser nula");
        this.mPosition = position; // Asigna la posición válida
    }

    // Método para asignar o modificar el mundo en el que se encuentra el elemento del juego.
    public void setWorld(World world) {
        // Comprueba que el mundo proporcionado no sea nulo
        if (world == null)
            throw new IllegalArgumentException("World no puede ser nulo");
        this.mWorld = world; // Asigna el mundo válido
    }
}

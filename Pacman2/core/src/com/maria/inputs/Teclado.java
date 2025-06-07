package com.maria.inputs;

import com.badlogic.gdx.input.GestureDetector;



// Clase personalizada que extiende GestureDetector para reconocer gestos direccionales
public class Teclado extends GestureDetector {

    // Interfaz interna que define métodos para responder a los gestos de dirección
    public interface DirectionListener {
        void onLeft();  // Método para gestionar el gesto hacia la izquierda
        void onRight(); // Método para gestionar el gesto hacia la derecha
        void onUp();    // Método para gestionar el gesto hacia arriba
        void onDown();  // Método para gestionar el gesto hacia abajo
    }

    // Constructor que recibe un objeto DirectionListener y lo vincula con GestureDetector
    public Teclado(DirectionListener directionListener) {
        // Llama al constructor de GestureDetector con una instancia de la clase interna DirectionGestureListener
        super(new DirectionGestureListener(directionListener));
    }

    // Clase interna estática para manejar específicamente gestos direccionales
    private static class DirectionGestureListener extends GestureAdapter {
        DirectionListener directionListener; // Guarda una referencia al listener de direcciones

        // Constructor que recibe e inicializa el listener de direcciones
        public DirectionGestureListener(DirectionListener directionListener){
            this.directionListener = directionListener;
        }

        // Método llamado automáticamente cuando se realiza un gesto rápido ("fling")
        @Override
        public boolean fling(float velocityX, float velocityY, int button) {
            // Comprueba si el movimiento es mayor en el eje X que en el eje Y
            if(Math.abs(velocityX) > Math.abs(velocityY)){
                // Movimiento horizontal: derecha o izquierda
                if(velocityX > 0){
                    directionListener.onRight(); // Deslizó hacia la derecha
                }else{
                    directionListener.onLeft();  // Deslizó hacia la izquierda
                }
            } else {
                // Movimiento vertical: arriba o abajo
                if(velocityY > 0){
                    directionListener.onDown(); // Deslizó hacia abajo
                }else{
                    directionListener.onUp();   // Deslizó hacia arriba
                }
            }
            // Retorna resultado del método padre (GestureAdapter) para mantener comportamiento estándar
            return super.fling(velocityX, velocityY, button);
        }

    }

}

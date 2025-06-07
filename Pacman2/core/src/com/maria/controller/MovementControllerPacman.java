package com.maria.controller;

import com.badlogic.gdx.audio.Music;
import com.maria.model.BasicPellet;
import com.maria.model.Block;
import com.maria.model.GameItems;
import com.maria.model.Ghost;
import com.maria.model.GhostHouse;
import com.maria.model.MovingItems;
import com.maria.model.PowerPellet;
import com.maria.model.Vector2D;
import com.maria.model.World;

import static com.maria.model.MovingItems.Direction.RIGHT;



public class MovementControllerPacman extends MovementController {
    public MovementControllerPacman(World world) {
        super(world);
        epsilon = (world.getPacman().getSpeed() / 6000f);
    }



    public void moveElement(MovingItems movableGameElement, float deltaTime) {
        // Verifica si Pacman ha colisionado con un fantasma.
        checkPacmanGhostCollision();

        // Comprueba si Pacman está usando el túnel (para teletransportarse de un lado al otro del mapa).
        checkTunnel(movableGameElement);

        // Verifica si Pacman puede moverse en la dirección deseada por el jugador.
        checkDesiredDirection(movableGameElement, movableGameElement.getWantedDirection());

        // Obtiene el siguiente bloque en la dirección en la que Pacman está intentando moverse.
        GameItems nextBlock = getNextElement(movableGameElement.getPosition(),
                movableGameElement.getCurrentDirection());

        // Si el siguiente bloque NO es una pared (Block) ni la casa de los fantasmas (GhostHouse), permite el movimiento.
        if (!(nextBlock instanceof Block) && !(nextBlock instanceof GhostHouse)) {
            movableGameElement.updatePosition(deltaTime); // Actualiza la posición de Pacman.
        }

        // Ajusta la posición de Pacman para asegurarse de que se alinee correctamente con la cuadrícula del laberinto.
        fixedPosition(movableGameElement);

        // Obtiene la posición actual de Pacman en coordenadas del laberinto.
        Vector2D currentPosition = movableGameElement.getPosition();
        GameItems currentGameElement = mWorld.getMaze().getBlock(
                (currentPosition.getX() + 50) / mCoef,  // Ajusta la posición X en la cuadrícula.
                (currentPosition.getY() + 50) / mCoef   // Ajusta la posición Y en la cuadrícula.
        );

        // Si Pacman está sobre una bola comestible (BasicPellet o PowerPellet), la come y actualiza el estado del juego.
        if (currentGameElement instanceof BasicPellet || currentGameElement instanceof PowerPellet) {
            eatPellet(currentGameElement);
        }

        // Reproduce un sonido si Pacman ha comido una bola.
        playEatingSound(nextBlock);
    }


    public void playEatingSound(GameItems nextBlock) {
        Music pacmanSound = SoundManager.getInstance().getEatingPelletSound();
        if (nextBlock instanceof BasicPellet
                || nextBlock instanceof PowerPellet) {

            if (!pacmanSound.isLooping()) {
                pacmanSound.setLooping(true);
                pacmanSound.play();
            }
        }
        else {
            pacmanSound.setLooping(false);
        }
    }


    // Método que comprueba si Pacman colisiona con algún fantasma
    public void checkPacmanGhostCollision() {
        Vector2D pacmanPosition = mWorld.getPacman().getPosition(); // Obtiene posición de Pacman
        for (Ghost ghost : mWorld.getGhosts()) { // Recorre todos los fantasmas
            if (isOnSameTile(pacmanPosition, ghost.getPosition())) { // Si comparten celda, resuelve colisión
                resolveCollision(ghost);
            }
        }
    }
    // Resuelve lo que sucede tras la colisión con un fantasma (Pacman lo come o pierde vida)

    public void resolveCollision(Ghost ghost) {
        // Si el fantasma está asustado, Pacman puede comerlo
        if (ghost.isFrightened()) {
            SoundManager.getInstance().getEatingGhostSound().play(); // Reproducir sonido de comer fantasma
            ghost.setFrightenedTimer(0);  // Se termina el estado de miedo del fantasma
            ghost.switchToDeadAI();       // Cambia su IA a "muerto" para volver a la casa
            ghost.setAlive(false);        // Lo marcamos como eliminado temporalmente
            mWorld.winPoint(500);         // Pacman gana 500 puntos
        }
        // Si el fantasma NO está asustado, Pacman pierde una vida
        else if (ghost.isAlive()) {
            SoundManager.getInstance().getDeadPacmanSound().play(); // Reproducir sonido de muerte de Pacman
            World.decreaseLifeCounter(); // Pacman pierde una vida

            // Reiniciar la posición de Pacman
            mWorld.getPacman().resetPosition();
            mWorld.getPacman().setDeadCounter(2); // Tiempo de espera antes de revivir
            mWorld.getPacman().setCurrentDirection(RIGHT); // Se mueve hacia la derecha al revivir

            // Reiniciar la posición y estado de todos los fantasmas
            for (Ghost ghost2 : mWorld.getGhosts()) {
                ghost2.resetPosition();  // Fantasma vuelve a su punto de inicio
                ghost2.setAlive(true);   // Lo marcamos como vivo nuevamente
                ghost2.switchToOutAI();  // Le asignamos la IA para salir de la casa
            }
        }
    }

    //Actualiza la precision del movimiento segun FPS
    public void updateEpsilon(float deltaTime) {
        float fps = 1 / deltaTime; // calcula FPS actuales
        float newEpsilon = mWorld.getPacman().getSpeed() / (fps * World.getCoef());// Calcula nueva precision
        if (newEpsilon < 0.5) { // Limita epsilon a máximo 0.5
            epsilon = newEpsilon;
        }
        else {
            epsilon = 0.45;// Usa valor por defecto en caso contrario
        }
    }
}

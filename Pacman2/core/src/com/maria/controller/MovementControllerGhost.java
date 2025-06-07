package com.maria.controller;

import com.badlogic.gdx.Gdx;
import com.maria.model.Block;
import com.maria.model.GameItems;
import com.maria.model.Ghost;
import com.maria.model.GhostHouse;
import com.maria.model.MovingItems;
import com.maria.model.MovingItems.Direction;
import com.maria.model.Vector2D;
import com.maria.model.World;

import java.util.HashMap;

import static com.maria.model.MovingItems.Direction.DOWN;

/**
 * MovementControllerGhost es una clase que extiende MovementController y se encarga
 * de gestionar el movimiento de los fantasmas en el juego.
 */
public class MovementControllerGhost extends MovementController {
    private Ghost mGhost;// Fantasma asociado a este controlador
    /**
     * Constructor del controlador de movimiento de fantasmas.
     *
     * @param world El mundo del juego.
     * @param ghost El fantasma que usará este controlador.
     */
    public MovementControllerGhost(World world, Ghost ghost) {
        super(world);
        epsilon = (world.getGhosts().get(0).getSpeed() / 6000f);// Establece la precisión basada en la velocidad del fantasma
        setGhost(ghost);
    }

    public Ghost getGhost() {
        return mGhost;
    }

    public void setGhost(Ghost ghost) {
        mGhost = ghost;
    }
    /**
     * Obtiene las direcciones válidas en las que el fantasma puede moverse.
     *
     * @param ghost     El fantasma que se está moviendo.
     * @param direction La dirección actual del fantasma.
     * @return Un mapa con las direcciones posibles y los elementos del laberinto correspondientes.
     */
    public HashMap<Direction, GameItems> getPossibleDirections(Ghost ghost, Direction direction) {
        Vector2D position = ghost.getPosition();
        HashMap<Direction, GameItems> possibleDirections
                = new HashMap<Direction, GameItems>();
        // Verifica si la izquierda es una dirección válida
        GameItems element = leftItem(position);
        if (!(element instanceof Block) && direction != Direction.RIGHT
                && (!ghost.isAlive() || !(element instanceof GhostHouse))) {
            possibleDirections.put(Direction.LEFT, element);
        }
// Verifica si la derecha es una dirección válida
        element = rightItem(position);
        if (!(element instanceof Block) && direction != Direction.LEFT
                && (!ghost.isAlive() || !(element instanceof GhostHouse))) {
            possibleDirections.put(Direction.RIGHT, element);
        }
// Verifica si arriba es una dirección válida
        element = getNextUpElement(position);
        if (!(element instanceof Block)  && direction != Direction.DOWN
                && (!ghost.isAlive() || !(element instanceof GhostHouse))) {
            possibleDirections.put(Direction.UP, element);
        }
// Verifica si abajo es una dirección válida
        element = downItem(position);
        if (!(element instanceof Block) && direction != Direction.UP
                && (!ghost.isAlive() || !(element instanceof GhostHouse))) {
            possibleDirections.put(Direction.DOWN, element);
        }

        return possibleDirections;
    }
    /**
     * Verifica si el fantasma puede cambiar a la dirección deseada y la establece si es válida.
     *
     * @param movableGameElement El fantasma en movimiento.
     * @param wantedDirection    La dirección deseada por el fantasma.
     */
    @Override
    public void checkDesiredDirection(MovingItems movableGameElement, Direction wantedDirection) {
        GameItems nextBlock = getNextElement(movableGameElement.getPosition(),
                movableGameElement.getWantedDirection());

        Ghost ghost = (Ghost) movableGameElement;

        switch (wantedDirection) {
            case LEFT:
            case RIGHT:
                if (movableGameElement.getPosition().getY() % 100 == 0) {
                    if (!(nextBlock instanceof Block)
                            && (!ghost.isAlive() || !(nextBlock instanceof GhostHouse))) {
                        movableGameElement.setCurrentDirection(wantedDirection);
                    }
                }
                break;
            case UP:
                if (movableGameElement.getPosition().getX() % 100 == 0) {
                    if (!(nextBlock instanceof Block)) {
                        movableGameElement.setCurrentDirection(wantedDirection);
                    }
                }
                break;
            case DOWN:
                if (movableGameElement.getPosition().getX() % 100 == 0) {
                    if (!(nextBlock instanceof Block)
                            && (!ghost.isAlive() || !(nextBlock instanceof GhostHouse))) {
                        movableGameElement.setCurrentDirection(wantedDirection);
                    }
                }
                break;
        }
    }
    /**
     * Mueve el fantasma en la dirección válida y gestiona su desplazamiento en el laberinto.
     *
     * @param movableGameElement El fantasma en movimiento.
     * @param deltaTime          El tiempo transcurrido desde el último fotograma.
     */
    @Override
    public void moveElement(MovingItems movableGameElement, float deltaTime) {
        checkTunnel(movableGameElement); // Comprueba si el fantasma está en un túnel y lo teletransporta si es necesario.


        checkDesiredDirection(movableGameElement, movableGameElement.getWantedDirection());

        GameItems nextBlock = getNextElement(movableGameElement.getPosition(),
                movableGameElement.getCurrentDirection());
        // Verifica si el próximo bloque es válido para el movimiento
        if (!(nextBlock instanceof Block) &&
                !((movableGameElement.getCurrentDirection() == DOWN)
                        && (nextBlock instanceof GhostHouse) &&
                        ((Ghost) movableGameElement).isAlive())) {
            movableGameElement.updatePosition(deltaTime);
        }

        fixedPosition(movableGameElement);// Asegura que la posición del fantasma esté alineada con la cuadrícula

     // checkLocked();
    }
    /**
     * Actualiza la precisión de movimiento basada en la velocidad del fantasma y los FPS.
     *
     * @param deltaTime El tiempo transcurrido desde el último fotograma.
     */
    @Override
    public void updateEpsilon(float deltaTime) {
        float fps = 1 / deltaTime;
        float newEpsilon = mGhost.getSpeed() / (fps * World.getCoef());
        if (newEpsilon < 0.5) {
            epsilon = newEpsilon;
        }
        else {
            epsilon = 0.45;
        }
    }
    /**
     * Verifica si el fantasma está bloqueado en una posición.
     * Si está bloqueado, imprime un mensaje en la consola de depuración.
     */
    public void checkLocked() {
        if (mGhost.getPosition().getX() % 100 <= epsilon * 10
                || mGhost.getPosition().getY() % 100 <= epsilon * 10) {
            Gdx.app.log(getClass().getSimpleName(), "Bloqueado en : " + mGhost.getPosition()
                    + " con " + epsilon);
        }
    }
}

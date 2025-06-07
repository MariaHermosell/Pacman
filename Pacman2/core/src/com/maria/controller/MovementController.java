package com.maria.controller;

import com.badlogic.gdx.utils.TimeUtils;
import com.maria.model.Block;
import com.maria.model.EmptyTile;
import com.maria.model.GameItems;
import com.maria.model.Ghost;
import com.maria.model.GhostHouse;
import com.maria.model.MazeItems;
import com.maria.model.MovingItems;
import com.maria.model.MovingItems.Direction;
import com.maria.model.PowerPellet;
import com.maria.model.Vector2D;
import com.maria.model.World;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
/**
 * Gestiona el movimiento de los personajes dentro del mundo del juego.
 * Controla la interacción con el laberinto y determina las direcciones válidas.
 */

@SuppressWarnings("SameParameterValue")
public abstract class MovementController {
    protected World mWorld; //este representa el mundo del juego, el laberinto
    protected int mCoef = World.getCoef();// Es un coeficiente para calcular las posiciones el laberinto
    protected double epsilon; // precision para ver si un elemento móvil está alineado con una celda/cuadro del laberinto

    public MovementController(World world) { // Inicia el controller con un objeto World.Necesitamos el mapa del juego para funcionar
        setWorld(world);
    }

    public void setWorld(World world) { //Asigna el mapa al controller, si no hay mapa ERROR.
        if (world == null) { // Que el mundo no sea null
            throw new IllegalArgumentException();
        }
        mWorld = world;
    }

    public World getWorld() {
        return mWorld;
    } // Devuelve el World/mapa actual

    public void setEpsilon(double epsilon) {  // Ajustamos la precisdion para saber si un personaje está bien alineado con el cuadro del laberinto
        this.epsilon = epsilon;
    } // Asigna el valor de precision

    public double getEpsilon() {
        return epsilon;
    } // devuelve el valor de la precisión


    /**
     * Obtiene el elemento del mapa en la dirección a la que se mueve un personaje.
     *
     * @param position  Posición actual.
     * @param direction Dirección en la que se mueve el personaje.
     * @return El elemento en la dirección especificada.
     */
    public GameItems getNextElement(Vector2D position, Direction direction) {
        switch (direction) { // Evalua la direccion del personaje
            case LEFT: // En caso de que vaya a la izquierda, llamamos al metodo leftItem y así sucesivamente
                return leftItem(position);
            case RIGHT:
                return rightItem(position);
            case UP:
                return getNextUpElement(position);
            case DOWN:
                return downItem(position);
        }

        throw new IllegalArgumentException("Dirección no reconocida");
    }

//Identificamos los items que hay arriba, abajo, derecha e izquierda.
//Pueden ser muros, bolas, fantasmas u otros elementos del laberinto.

    public MazeItems getNextUpElement(Vector2D position) {
        // Devuelve el elemento que está justo arriba de la posición actual.
        return mWorld.getMaze(). //Accedemos al objeto Maze (laberinto) del mundo del juego.
                getBlock(position.getX() / mCoef, //Obtenemos la posición actual en coordenadas del laberinto (X)
                (int) Math.ceil((position.getY() / ((float) mCoef))) - 1); //Posición actual Y, ajustando hacia arriba (celda inmediatamente superior).
    }

    public MazeItems downItem(Vector2D position) {
        // Devuelve el elemento ubicado justo debajo de la posición actual.
        return mWorld.getMaze().getBlock( // Accede al laberinto para buscar un bloque específico.
                position.getX() / mCoef, // Coordenada X actual convertida a coordenadas del laberinto.
                (position.getY() / mCoef) + 1); // Coordenada Y actual (siguiente bloque hacia abajo).
    }

    public MazeItems rightItem(Vector2D position) {
        // Método que obtiene el elemento que está inmediatamente a la derecha del personaje.
        return mWorld.getMaze().getBlock( //Accede al laberinto del juego.
                (position.getX() / mCoef) + 1, // Calcula la posición X del siguiente bloque hacia la derecha.
                position.getY() / mCoef); // Mantiene la misma posición Y, ya que solo se desplaza hacia derecha.
    }

    public MazeItems leftItem(Vector2D position) {
        return mWorld.getMaze().getBlock( // Accede al laberinto para buscar el bloque hacia la izquierda.
                (int) Math.ceil((position.getX() / ((float) mCoef)) - 1), // Calcula la posición del bloque inmediatamente a la izquierda.
                position.getY() / mCoef); // Mantiene la misma posición Y actual.
    }


    public HashMap<Direction, MazeItems>
    getPossibleDirections(Vector2D position, Direction direction) {
        HashMap<Direction, MazeItems> possibleDirections
                = new HashMap<Direction, MazeItems>(); // Almacenamos las direcciones validas y los elementos del mapa a los que se puede mover el personaje

        MazeItems item = leftItem(position);//Obtenemos el bloque que está a la izquierda de la posic actual.
        if (!(item instanceof Block) && direction != Direction.RIGHT// Verificamos que no sea una pared , que no esté intentando girar a derecha
                && !(item instanceof GhostHouse)) { // y que no sea la casa de los fantasmas
            possibleDirections.put(Direction.LEFT, item); // entonces es una dirección válida
        }

        item = rightItem(position);
        if (!(item instanceof Block) && direction != Direction.LEFT
                && !(item instanceof GhostHouse)) {
            possibleDirections.put(Direction.RIGHT, item);
        }

        item = getNextUpElement(position);
        if (!(item instanceof Block)  && direction != Direction.DOWN
                && !(item instanceof GhostHouse)) {
            possibleDirections.put(Direction.UP, item);
        }

        item = downItem(position);
        if (!(item instanceof Block) && direction != Direction.UP
                && !(item instanceof GhostHouse)) {
            possibleDirections.put(Direction.DOWN, item);
        }

        return possibleDirections;
    }

    public LinkedList<MazeItems> // Creamos una lista para guardar los bloques disponibles, donde el personaje puede moverse
    getAvailableBlocks(Vector2D position) {
        LinkedList<MazeItems> possibleDirections
                = new LinkedList<MazeItems>();

        MazeItems item = leftItem(position); // Obtenemos la izquierda
        if (!(item instanceof Block) // Si no es un bloque y no es la casa de los fantasmas
                && !(item instanceof GhostHouse)) {
            possibleDirections.add(item); //Se añade a la lista de bloques disponibles
        }

        item = rightItem(position);
        if (!(item instanceof Block)
                && !(item instanceof GhostHouse)) {
            possibleDirections.add(item);
        }

        item = getNextUpElement(position);
        if (!(item instanceof Block)
                && !(item instanceof GhostHouse)) {
            possibleDirections.add(item);
        }

        item = downItem(position);
        if (!(item instanceof Block)
                && !(item instanceof GhostHouse)) {
            possibleDirections.add(item);
        }

        return possibleDirections; // Devuelve la lista con todas las direcciones validas que el personaje puede tomar
    }

    //Ahora vamos a buscar el mejor bloque para movernos entre las direcciones posibles
    public Map.Entry<Direction, MazeItems>
    getBlockWithSmallestLabel(Vector2D position, Direction direction) {
        HashMap<Direction, MazeItems> availableBlocks
                = getPossibleDirections(position, direction); // Obtenemos las direcciones posibles

        Map.Entry<Direction, MazeItems> result = null; // Guardamos la dirección y el bloque con el menor valor de shortestPathLabel (que indica que está mas cerca del objetivo)
        int min = Integer.MAX_VALUE; // Inicializamos el valor mínimo como el máximo posible (para comparar más adelante)

        for (Map.Entry<Direction, MazeItems> entry : availableBlocks.entrySet()) { // Recorre todas las direcciones disponibles
            int shortestPathLabel = entry.getValue().getShortestPathLabel();
            if (shortestPathLabel < min) {
                result = entry;
                min = entry.getValue().getShortestPathLabel();
            }
        }

        return result;
    }
    /**
     * Calcula la distancia entre dos posiciones en el juego.
     *
     * @param posicion1 Primera posición.
     * @param posicion2 Segunda posición.
     * @return Distancia calculada.
     */
    public double getDistance(Vector2D posicion1, Vector2D posicion2) {
        double diffX = posicion1.getX() - posicion2.getX();
        double diffY = posicion1.getY() - posicion2.getY();
        return Math.sqrt((diffX * diffX) + (diffY * diffY));
    }
    /**
     * Verifica si un personaje está en una intersección.
     *
     * @param position Posición actual del personaje.
     * @param direction Dirección actual del personaje.
     * @return true si está en una intersección o bloqueado, false en caso contrario.
     */
    boolean isAtIntersection(Vector2D position, Direction direction) {
        if (isOnTile(position)) { // Verifica si el personaje está exactamente sobre una baldosa
            int emptyBlockCounter = 0; // Contador de bloques vacíos alrededor

            // Revisa cada dirección para verificar si no es un bloque (pared)
            if (!(getNextUpElement(position) instanceof Block)) emptyBlockCounter++;
            if (!(downItem(position) instanceof Block)) emptyBlockCounter++;
            if (!(rightItem(position) instanceof Block)) emptyBlockCounter++;
            if (!(leftItem(position) instanceof Block)) emptyBlockCounter++;

            // Retorna true si hay al menos 3 direcciones libres o si la dirección actual está bloqueada
            return (emptyBlockCounter >= 3 || (getNextElement(position, direction) instanceof Block));
        } else {
            return false; // No está exactamente en una baldosa
        }
    }
    public boolean isOnTile(Vector2D position) {
        return position.getX() % 100 == 0 && position.getY() % 100 == 0;
    }


    public void fixedPosition(MovingItems element) {
        switch (element.getCurrentDirection()) {
            case LEFT:
                if (element.getPosition().getX() / ((float) mCoef)
                        - (element.getPosition().getX() / mCoef) < epsilon) {
                    element.getPosition().setX(element.getPosition().getX() / mCoef * mCoef);
                }
                break;
            case RIGHT:
                if ((element.getPosition().getX() / mCoef) + 1
                        - element.getPosition().getX() / ((float) mCoef) < epsilon) {
                    Vector2D viejo = new Vector2D(element.getPosition());
                    element.getPosition().setX(((element.getPosition().x / mCoef) + 1) * mCoef);
                }
                break;
            case UP:
                if (element.getPosition().y / ((float) mCoef)
                        - (element.getPosition().y / mCoef) < epsilon) {
                    element.getPosition().setY((element.getPosition().y / mCoef) * mCoef);
                }
                break;
            case DOWN:
                if ((element.getPosition().y / mCoef) + 1
                        - element.getPosition().y / ((float) mCoef) < epsilon) {
                    element.getPosition().setY(((element.getPosition().y / mCoef) + 1) * mCoef);
                }
                break;
        }
    }

    /**
     * Obtiene el {@link GameItems} en la posición del elemento.
     * Element @param El elemento utilizado para obtener la posición.
     * @return El {@link GameItems} en la posición del elemento.
     */
    public GameItems itemAtPosition(GameItems element) {
        int x = element.getPosition().getX() / mCoef; // Calcula la coordenada X en la cuadrícula del laberinto
        int y = element.getPosition().getY() / mCoef; // Calcula la coordenada Y en la cuadrícula del laberinto
        return mWorld.getMaze().getBlock(x, y); // Retorna el bloque en la posición calculada
    }

    /**
     * Obtiene el {@link GameItems} en la posición del elemento.
     * @param position La posición.
     * @return El {@link GameItems} en la posición del elemento.
     */
    public MazeItems itemAtPosition(Vector2D position) {
        int x = position.getX() / mCoef;
        int y = position.getY() / mCoef;
        return mWorld.getMaze().getBlock(x, y);
    }

    /**
     * Obtiene la posición redondeada.
     * @param position La posición sin formato.
     * @return La posición redondeada
     */
    public Vector2D exactPosition(Vector2D position) {
        return new Vector2D(position.getX() / mCoef, position.getY() / mCoef);
    }

    public boolean isOnSameTile(Vector2D position1, Vector2D position2) {
        return exactPosition(position1).equals(exactPosition(position2));
    }

    /**
     * Verifica si el personaje está pasando por un túnel.
     * Si es así, teletransporta al personaje al lado opuesto del laberinto.
     *
     * @param movableGameElement El personaje móvil que puede atravesar el túnel.
     */
    public void checkTunnel(MovingItems movableGameElement) {
    /* Handles TP . Teletransporta al lado opuesto*/
        if ((movableGameElement.getPosition().x / mCoef) == mWorld.getMaze().getWidth() - 1) {
            movableGameElement.setPosition(new Vector2D(0, movableGameElement.getPosition().y));
        }

        if ((movableGameElement.getPosition().x / mCoef) == 0
                && movableGameElement.getCurrentDirection() == Direction.LEFT) {
            movableGameElement.setPosition(new Vector2D(27 * mCoef, movableGameElement.getPosition().y));
        }
    }


    public void eatPellet(GameItems gameItem) {
        Vector2D position = new Vector2D(gameItem.getPosition().getX() / mCoef,
                gameItem.getPosition().getY() / mCoef);

    // Reemplaza la bola comestible por un espacio vacío en el laberinto
        Vector2D gameElementPosition = gameItem.getPosition();
        mWorld.getMaze().setBlock(new EmptyTile(gameElementPosition, mWorld),
                position.getX(), position.getY());

        // Calcula la puntuación y el tiempo de penalización basado en el tiempo transcurrido
        long elapsedTime = TimeUtils.timeSinceMillis(mWorld.getStartTime());
        int baseScore;
        int timePenalty = (int) (elapsedTime / 1000);
        int frightenedDuration = 5;
        if (gameItem instanceof PowerPellet) {
            baseScore = 100;
            // Poner a los fantasmas en modo asustado
            for (Ghost ghost : mWorld.getGhosts()) {
                if (ghost.isAlive()) {
                    ghost.setFrightenedTimer(frightenedDuration);
                }
            }
        }
        else {
            baseScore = 10;
        }
        // Actualiza la puntuación del jugador
        mWorld.winPoint(baseScore - timePenalty);
        mWorld.getMaze().decreasePelletNumber();
    }

    /**
     * Comprueba si el personaje puede moverse en la dirección deseada y actualiza la dirección actual.
     *
     * @param pacman El personaje móvil (Pacman).
     * @param wantedDirection La dirección deseada.
     */
    public void checkDesiredDirection(MovingItems pacman, Direction wantedDirection) {
        // Obtiene el siguiente bloque según la dirección deseada del personaje
        GameItems nextBlock = getNextElement(pacman.getPosition(), pacman.getWantedDirection());

        switch (wantedDirection) {
            case LEFT:
            case RIGHT:
                // Verifica que Pacman esté alineado horizontalmente
                if (pacman.getPosition().getY() % 100 == 0) {
                    // Si el bloque siguiente no es un bloque, actualiza la dirección
                    if (!(nextBlock instanceof Block)) {
                        pacman.setCurrentDirection(wantedDirection);
                    }
                }
                break;
            case UP:
                // Verifica que Pacman esté alineado verticalmente
                if (pacman.getPosition().getX() % 100 == 0) {
                    if (!(nextBlock instanceof Block)) {
                        pacman.setCurrentDirection(wantedDirection);
                    }
                }
                break;
            case DOWN:
                if (pacman.getPosition().getX() % 100 == 0) {
                    // Además verifica que el bloque siguiente no sea ni bloque ni la casa de fantasmas
                    if (!(nextBlock instanceof Block) && !(nextBlock instanceof GhostHouse)) {
                        pacman.setCurrentDirection(wantedDirection);
                    }
                }
                break;
        }
    }

    /**
     * Método abstracto para mover un personaje en el laberinto.
     *
     * @param movableGameElement El personaje a mover.
     * @param deltaTime          El tiempo transcurrido desde la última actualización.
     */
    public abstract void moveElement(MovingItems movableGameElement, float deltaTime);
    /**
     * Método abstracto para actualizar la precisión del movimiento.
     *
     * @param deltaTime El tiempo transcurrido desde la última actualización.
     */
    public abstract void updateEpsilon(float deltaTime);


    /**
     * Encuentra y etiqueta la ruta más corta desde la posición objetivo a todas las posiciones accesibles.
     *
     * @param positionObjective La posición desde la que se inicia el etiquetado.
     */
    public void findShortestPath(Vector2D positionObjective) {
        // Inicializa todas las etiquetas de camino más corto con un valor muy grande (infinito)
        for (GameItems gameElement : mWorld.getMaze()) {
            if (gameElement instanceof MazeItems) {
                MazeItems mazeElement = (MazeItems) gameElement;
                mazeElement.setShortestPathLabel(Integer.MAX_VALUE); // Establece etiqueta inicial máxima
            }
        }

        Queue<MazeItems> blockQueue = new LinkedList<MazeItems>(); // Cola para gestionar la búsqueda por anchura
        MazeItems beginningBlock = itemAtPosition(positionObjective); // Bloque inicial desde el que inicia la búsqueda
        MazeItems currentBlock; // Bloque actual durante la búsqueda
        LinkedList<MazeItems> neighboursBlocks; // Lista de bloques vecinos
        int label = 0; // Etiqueta inicial para la posición objetivo

        blockQueue.add(beginningBlock); // Añade el bloque inicial a la cola
        beginningBlock.setShortestPathLabel(label); // Etiqueta el bloque inicial con 0

        // Mientras haya elementos en la cola, continúa buscando vecinos
        while (!blockQueue.isEmpty()) {
            currentBlock = blockQueue.remove(); // Retira el siguiente bloque de la cola
            label = currentBlock.getShortestPathLabel() + 1; // Incrementa la etiqueta para los vecinos
            neighboursBlocks = getAvailableBlocks(currentBlock.getPosition()); // Obtiene bloques vecinos accesibles

            for (MazeItems neighbour : neighboursBlocks) { // Para cada vecino accesible
                if (neighbour.getShortestPathLabel() == Integer.MAX_VALUE) { // Si aún no fue etiquetado
                    blockQueue.add(neighbour); // Añádelo a la cola para procesarlo
                    neighbour.setShortestPathLabel(label); // Etiqueta el vecino con la distancia actual
                }
            }

        }
    }
}

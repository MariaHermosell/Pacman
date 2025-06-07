package com.maria.model;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

/*El laberinto del juego */

@SuppressWarnings("SameParameterValue")
public class Maze implements Iterable<GameItems> {

    private int width;

    private int height;

    private World world;

    private MazeItems[][] blocks;

    private int pelletNumber = 0;


    public Maze(World world) {
        setWorld(world);
    }


    public int getWidth() {
        return width;
    }


    public void setWidth(int width) {
        if (width <= 0) {
            throw new IllegalArgumentException("El ancho no puede ser negativo");
        }
        this.width = width;
    }


    public int getHeight() {
        return height;
    }


    public void setHeight(int height) {
        if (height <= 0) {
            throw new IllegalArgumentException("El largo del laberinto no puede ser negativo.");
        }

        this.height = height;
    }

    public void setWorld(World world) {
        if (world == null) {
            throw new IllegalArgumentException("El mworld no puede ser negativo ");
        }
        this.world = world;
    }


    public MazeItems getBlock(int x, int y) {
        if (x >= width) {
            x = width - 1;
        }
        else if (x < 0) {
            x = 0;
        }
        if (x >= height) {
            y = height - 1;
        }
        else if (x < 0) {
            y = 0;
        }
        return blocks[x][y];
    }


    public void setBlock(MazeItems gameElement, int x, int y) {
        blocks[x][y] = gameElement;
    }

    public int getPelletNumber() {
        return pelletNumber;
    }

    public void decreasePelletNumber() {
        if (pelletNumber > 0)
            pelletNumber--;
    }


    @Override
    public Iterator<GameItems> iterator() {

        return new Iterator<GameItems>() {
            private int x = 0;
            private int y = 0;

            @Override
            public boolean hasNext() {
                return x < Maze.this.width && y < Maze.this.height;
            }

            @Override
            public MazeItems next() {
                MazeItems gameElement;
                gameElement = Maze.this.blocks[x][y];
                x++;
                if (x >= Maze.this.width) {
                    x = 0;
                    y++;
                }
                return gameElement;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    public void loadDemoLevel(int coeff) {
        setWidth(28);
        setHeight(31);
        blocks = new MazeItems[width][height];
        FileHandle handle = Gdx.files.internal("levels/base_lvl.txt");
        InputStream stream = handle.read();
        try {
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    char c = (char) stream.read();
                    if (c == '\n')
                        c = (char) stream.read();

                    if (c == '0')
                        blocks[x][y] = new Block(new Vector2D(x * coeff, y * coeff), world);
                    else if (c == '1')
                        blocks[x][y] = new EmptyTile(new Vector2D(x * coeff, y * coeff), world);
                    else if (c == '2') {
                        blocks[x][y] = new BasicPellet(new Vector2D(x * coeff, y * coeff), world);
                        pelletNumber++;
                    }
                    else if (c == '3') {
                        blocks[x][y] = new PowerPellet(new Vector2D(x * coeff, y * coeff), world);
                        pelletNumber++;
                    }
                    else if (c == '4') {
                        blocks[x][y] = new GhostHouse(new Vector2D(x * coeff, y * coeff), world);
                    }
                    else
                        blocks[x][y] = new EmptyTile(new Vector2D(x * coeff, y * coeff), world);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
/*La clase Maze define el laberinto donde ocurre el juego.
Gestiona la colocación de elementos como paredes (Block), bolas normales (BasicPellet) y bolas especiales (PowerPellet).
Implementa un iterador que permite recorrer todos sus elementos fácilmente.
También se encarga de cargar un nivel demo desde un archivo externo, permitiendo cambiar fácilmente la estructura del laberinto.*/
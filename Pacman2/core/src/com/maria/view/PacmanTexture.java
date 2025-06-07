package com.maria.view;
import com.maria.model.GameItems;
import com.maria.model.Pacman;



public class PacmanTexture extends Texture {

    private final com.badlogic.gdx.graphics.Texture[] UpTextures = new com.badlogic.gdx.graphics.Texture[4];

    private final com.badlogic.gdx.graphics.Texture[] DownTextures = new com.badlogic.gdx.graphics.Texture[4];

    private final com.badlogic.gdx.graphics.Texture[] LeftTextures = new com.badlogic.gdx.graphics.Texture[4];

    private final com.badlogic.gdx.graphics.Texture[] RightTextures = new com.badlogic.gdx.graphics.Texture[4];

    private float time = 0;
    private int state = 0;


    public PacmanTexture() {
        super();
        com.badlogic.gdx.graphics.Texture pacmanUpClosed = new com.badlogic.gdx.graphics.Texture("pacmanUp.png");
        com.badlogic.gdx.graphics.Texture pacmanLeftClosed = new com.badlogic.gdx.graphics.Texture("pacmanLeft.png");
        com.badlogic.gdx.graphics.Texture pacmanDownClosed = new com.badlogic.gdx.graphics.Texture("pacmanDown.png");
        com.badlogic.gdx.graphics.Texture pacmanRightClosed = new com.badlogic.gdx.graphics.Texture("pacmanRight.png");
        com.badlogic.gdx.graphics.Texture pacmanUpOpen = new com.badlogic.gdx.graphics.Texture("pacmanUp-2.png");
        com.badlogic.gdx.graphics.Texture pacmanLeftOpen = new com.badlogic.gdx.graphics.Texture("pacmanLeft-2.png");
        com.badlogic.gdx.graphics.Texture pacmanDownOpen = new com.badlogic.gdx.graphics.Texture("pacmanDown-2.png");
        com.badlogic.gdx.graphics.Texture pacmanRightOpen = new com.badlogic.gdx.graphics.Texture("pacmanRight-2.png");
        com.badlogic.gdx.graphics.Texture pacmanClosed = new com.badlogic.gdx.graphics.Texture("pacman-3.png");

        UpTextures[0] = pacmanClosed;
        UpTextures[1] = pacmanUpClosed;
        UpTextures[2] = pacmanUpOpen;
        UpTextures[3] = pacmanUpClosed;

        DownTextures[0] = pacmanClosed;
        DownTextures[1] = pacmanDownClosed;
        DownTextures[2] = pacmanDownOpen;
        DownTextures[3] = pacmanDownClosed;

        LeftTextures[0] = pacmanClosed;
        LeftTextures[1] = pacmanLeftClosed;
        LeftTextures[2] = pacmanLeftOpen;
        LeftTextures[3] = pacmanLeftClosed;

        RightTextures[0] = pacmanClosed;
        RightTextures[1] = pacmanRightClosed;
        RightTextures[2] = pacmanRightOpen;
        RightTextures[3] = pacmanRightClosed;

    }

    public void update(float deltaTime) {
        time += deltaTime;
        state = (int) (time * (getPacman().getSpeed() / 50)) % 4;
    }

    public Pacman getPacman() {
        if (getWrappedObject() instanceof Pacman) {
            return (Pacman) getWrappedObject();
        }
        throw new RuntimeException("The wrapped object wasn't a pacman : " +getWrappedObject());
    }

    @Override
    public void setWrappedObject(GameItems wrappedObject) {
        super.setWrappedObject(wrappedObject);

        if (!(wrappedObject instanceof Pacman)) {
            throw new IllegalArgumentException("PacmanTextureWrapper's wrapped object should" +
                    " be a pacman.");
        }
    }

    @Override
    public com.badlogic.gdx.graphics.Texture getTexture() {
        Pacman pacman = (Pacman) getWrappedObject();
        switch (pacman.getCurrentDirection()) {
            case UP:
                return UpTextures[state];
            case RIGHT:
                return RightTextures[state];
            case DOWN:
                return DownTextures[state];
            case LEFT:
                return LeftTextures[state];
            default:
                throw new RuntimeException("Direction inconnue.");
        }

    }
}

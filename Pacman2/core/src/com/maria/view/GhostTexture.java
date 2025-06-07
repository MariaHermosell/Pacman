package com.maria.view;
import com.maria.model.GameItems;
import com.maria.model.Ghost;



public class GhostTexture extends Texture {
    public com.badlogic.gdx.graphics.Texture normalTexture;
    public com.badlogic.gdx.graphics.Texture frightenedTexture = new com.badlogic.gdx.graphics.Texture("ghostEscaping.png");
    public com.badlogic.gdx.graphics.Texture deadTexture = new com.badlogic.gdx.graphics.Texture("ghostDead.png");
    public float frightenedTimer = 0;


    public GhostTexture(com.badlogic.gdx.graphics.Texture defaultTexture) {
        super();
        setNormalTexture(defaultTexture);
    }

    public void setNormalTexture(com.badlogic.gdx.graphics.Texture normalTexture) {
        if (normalTexture == null) {
            throw new IllegalArgumentException("NULL texture");
        }
        this.normalTexture = normalTexture;
    }

    public void setFrightenedTimer(float frightenedTimer) {
        this.frightenedTimer = frightenedTimer;
    }

    public void update(float deltaTime) {
        if (((Ghost) wrappedObject).getFrightenedTimer() > 0) {
            ((Ghost) wrappedObject).decreaseFrightenedTimer(deltaTime);
        }
    }

    @Override
    public void setWrappedObject(GameItems wrappedObject) {
        super.setWrappedObject(wrappedObject);

        if (!(wrappedObject instanceof Ghost)) {
            throw new IllegalArgumentException("GhostTextureWrapper's wrapped object should" +
                    " be a ghost.");
        }
    }

    @Override
    public com.badlogic.gdx.graphics.Texture getTexture() {
        if (((Ghost) wrappedObject).getFrightenedTimer() > 0) {
            return frightenedTexture;
        }
        if (!((Ghost) wrappedObject).isAlive()) {
            return deadTexture;
        }
        return normalTexture;
    }
}

package com.maria.view;
import com.maria.model.GameItems;



public abstract class Texture implements ITexturable {

    protected GameItems wrappedObject;

    public Texture() {
        this.wrappedObject = null;
    }


    public GameItems getWrappedObject() {
        return wrappedObject;
    }


    public void setWrappedObject(GameItems wrappedObject) {
        if (wrappedObject == null) {
            throw new IllegalArgumentException("The wrapped object can't be null");
        }
        this.wrappedObject = wrappedObject;
    }

    public abstract void update(float deltaTime);

    public void resetWrapper() {
        this.wrappedObject = null;
    }
    @Override
    public abstract com.badlogic.gdx.graphics.Texture getTexture();
}

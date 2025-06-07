package com.maria.view;

import com.badlogic.gdx.graphics.Texture;


public class DefaultTexture implements ITexturable {

    private Texture mDefaultTexture;


    public DefaultTexture(Texture defaultTexture) {
        setDefaultTexture(defaultTexture);
    }


    public void setDefaultTexture(Texture defaultTexture) {
        if (defaultTexture == null) {
            throw new IllegalArgumentException("defaultTexture no puede ser null.");
        }
        this.mDefaultTexture = defaultTexture;
    }

    @Override
    public Texture getTexture() {
        return mDefaultTexture;
    }
}

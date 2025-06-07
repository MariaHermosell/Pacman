package com.maria.view;



public class PowerPelletTexture extends Texture {
    private float time;
    private int state;

    private com.badlogic.gdx.graphics.Texture[] mTextures;

    public PowerPelletTexture() {
        mTextures = new com.badlogic.gdx.graphics.Texture[2];
        mTextures[0] = new com.badlogic.gdx.graphics.Texture("superpellet.png");
        mTextures[1] = new com.badlogic.gdx.graphics.Texture("superpellet-2.png");
    }

    @Override
    public void update(float deltaTime) {
        time += deltaTime;
        state = (int) (time * 5) % 2;
    }

    @Override
    public com.badlogic.gdx.graphics.Texture getTexture() {
        return mTextures[state];
    }
}

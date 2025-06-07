package com.maria.view;
import com.maria.model.BasicPellet;
import com.maria.model.Block;
import com.maria.model.BlueGhost;
import com.maria.model.EmptyTile;
import com.maria.model.GameItems;
import com.maria.model.GhostHouse;
import com.maria.model.Pacman;
import com.maria.model.PinkGhost;
import com.maria.model.RedGhost;
import com.maria.model.PowerPellet;
import com.maria.model.YellowGhost;

import java.util.HashMap;
import java.util.Map;



public class TextureFactory {

    private final Map<Class<?>, ITexturable> mTextureMap;

    private static TextureFactory instance;

    private com.badlogic.gdx.graphics.Texture livesTexture = new com.badlogic.gdx.graphics.Texture("pacmanRight.png");


    private TextureFactory() {
        com.badlogic.gdx.graphics.Texture blocTexture = new com.badlogic.gdx.graphics.Texture("bloc.png");
        com.badlogic.gdx.graphics.Texture emptyTexture = new com.badlogic.gdx.graphics.Texture("dark.png");
        com.badlogic.gdx.graphics.Texture basicPelletTexture = new com.badlogic.gdx.graphics.Texture("pellet.png");
        com.badlogic.gdx.graphics.Texture redGhost = new com.badlogic.gdx.graphics.Texture("ghost1.png");
        com.badlogic.gdx.graphics.Texture pinkGhost = new com.badlogic.gdx.graphics.Texture("ghost2.png");
        com.badlogic.gdx.graphics.Texture blueGhost = new com.badlogic.gdx.graphics.Texture("ghost3.png");
        com.badlogic.gdx.graphics.Texture yellowGhost = new com.badlogic.gdx.graphics.Texture("ghost4.png");
        com.badlogic.gdx.graphics.Texture superPellet = new com.badlogic.gdx.graphics.Texture("superpellet.png");
        mTextureMap = new HashMap<Class<?>, ITexturable>();
        mTextureMap.put(Pacman.class, new PacmanTexture());
        mTextureMap.put(RedGhost.class, new GhostTexture(redGhost));
        mTextureMap.put(PinkGhost.class, new GhostTexture(pinkGhost));
        mTextureMap.put(BlueGhost.class, new GhostTexture(blueGhost));
        mTextureMap.put(YellowGhost.class, new GhostTexture(yellowGhost));

        mTextureMap.put(Block.class, new DefaultTexture(blocTexture));
        mTextureMap.put(EmptyTile.class, new DefaultTexture(emptyTexture));
        mTextureMap.put(BasicPellet.class, new DefaultTexture(basicPelletTexture));
        mTextureMap.put(GhostHouse.class, new DefaultTexture(emptyTexture));

        mTextureMap.put(PowerPellet.class, new PowerPelletTexture());
    }


    public static TextureFactory getInstance() {
        if (instance == null) {
            instance = new TextureFactory();
        }
        return instance;
    }

    public void update(float deltaTime) {
        ((PacmanTexture) mTextureMap.get(Pacman.class)).update(deltaTime);
        ((GhostTexture) mTextureMap.get(PinkGhost.class)).update(deltaTime);
        ((GhostTexture) mTextureMap.get(RedGhost.class)).update(deltaTime);
        ((GhostTexture) mTextureMap.get(YellowGhost.class)).update(deltaTime);
        ((GhostTexture) mTextureMap.get(BlueGhost.class)).update(deltaTime);
        ((PowerPelletTexture) mTextureMap.get(PowerPellet.class)).update(deltaTime);
    }


    public com.badlogic.gdx.graphics.Texture getTexture(GameItems element) {
        ITexturable iTexturable = mTextureMap.get(element.getClass());

        if (iTexturable instanceof Texture) {
            Texture textureWrapper = (Texture) iTexturable;
            if (textureWrapper.getWrappedObject() == null) {
                textureWrapper.setWrappedObject(element);
            }
        }

        return iTexturable.getTexture();
    }

    public com.badlogic.gdx.graphics.Texture getLivesTexture() {
        return livesTexture;
    }

    public void resetTextureFactory() {
        for (ITexturable texturable : mTextureMap.values()) {
            if (texturable instanceof Texture) {
                ((Texture) texturable).resetWrapper();
            }
        }
    }
}

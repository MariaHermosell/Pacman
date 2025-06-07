package com.maria;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.maria.screens.LoadingScreen;
// CLASE PRINCIPAL
public class Pacman extends Game implements ApplicationListener {

    @Override
    public void create () {
        setScreen(new LoadingScreen(this));
//        setScreen(new GameScreen(this));
    }
}
package com.towerdefense.lwjgl3;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
//import com.towerdefense.Main;

import java.util.ArrayList;
import java.util.List;

/** Launches the desktop (LWJGL3) application. */
public class Lwjgl3Launcher {
//	public static void main(String[] args) {
//		createApplication();
//	}

//	private static Lwjgl3Application createApplication() {
//		return new Lwjgl3Application(new Main(), getDefaultConfiguration());
//	}
    
    private static Lwjgl3ApplicationConfiguration getDefaultConfiguration() {
        Lwjgl3ApplicationConfiguration configuration = new Lwjgl3ApplicationConfiguration();
        configuration.setTitle("tower_defense");
        configuration.setWindowedMode(640, 480);
        configuration.setWindowIcon("libgdx128.png", "libgdx64.png", "libgdx32.png", "libgdx16.png");
        return configuration;
    }
    
    
   
    
}























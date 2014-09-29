package com.makezero.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.makezero.game.MakeZeroGdxGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.height = (1280 + 640)/2;//640;//1280;//(1280 + 640)/2;
		config.width = (720 + 360)/2;//360;//720;//(720 + 360)/2;
		new LwjglApplication(new MakeZeroGdxGame(), config);
	}
}

package com.toam.pong2016.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.toam.pong2016.main.Pong2016;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.title = Pong2016.TITLE;
		config.width = Pong2016.V_WIDTH;
		config.height = Pong2016.V_HEIGHT;

		new LwjglApplication(new Pong2016(), config);
	}
}

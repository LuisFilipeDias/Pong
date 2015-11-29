package com.toam.pong2016.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.toam.pong2016.handlers.Common;
import com.toam.pong2016.main.Pong2016;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.title = Common.TITLE;
		config.width = Common.V_WIDTH * Common.SCALE;
		config.height = Common.V_HEIGHT * Common.SCALE;

		new LwjglApplication(new Pong2016(), config);
	}
}

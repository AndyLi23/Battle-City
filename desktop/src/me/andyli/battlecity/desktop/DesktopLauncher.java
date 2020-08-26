package me.andyli.battlecity.desktop;

import me.andyli.battlecity.Main;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import org.apache.commons.lang3.SystemUtils;

public class DesktopLauncher {
	public static void main (String[] arg) {

		if(SystemUtils.IS_OS_WINDOWS || SystemUtils.IS_OS_LINUX || SystemUtils.IS_OS_MAC) {
			LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

			config.title = "Battle City";
			config.addIcon("img/tank.png", Files.FileType.Internal);
			config.resizable = false;
			config.width = 624; //16*3 by 13
			config.height = 624; //16*3 by 13

			new LwjglApplication(new Main(), config);
		} else {
			System.out.println("Incompatible Operating System.");
		}
	}
}
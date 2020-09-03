package me.andyli.battlecity;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import me.andyli.battlecity.screens.MenuScreen;

public class Main extends Game {

	private Game game;

	@Override
	public void create () {
		game = this;
		//menu screen
		setScreen(new MenuScreen(game));
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0,0,0,1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		super.render();
	}
	
	@Override
	public void dispose () {
		Constants.disposeAll();
	}
}

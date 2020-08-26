package me.andyli.battlecity;

import me.andyli.battlecity.screens.GameScreen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

public class Main extends Game {

	private Game game;

	@Override
	public void create () {

		game = this;

		setScreen(new GameScreen(game, 1));
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0.3f,0.3f,0.3f,1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		super.render();
	}
	
	@Override
	public void dispose () {
		Constants.disposeAll();
	}
}

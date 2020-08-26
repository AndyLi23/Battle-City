package me.andyli.battlecity.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import me.andyli.battlecity.blocks.BlockManager;
import me.andyli.battlecity.tanks.Player;

import java.io.BufferedReader;
import java.io.FileReader;

public class GameScreen implements Screen {

    private final Stage stage;
    private final Game game;
    private final SpriteBatch batch;
    private final ShapeRenderer renderer;
    private Player player;
    private int map;
    private BufferedReader br;
    private String arr[];
    private BlockManager blockManager;

    public GameScreen(final Game game, int map) {
        this.map = map;
        this.game = game;
        this.batch = new SpriteBatch();
        this.renderer = new ShapeRenderer();
        this.stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()), batch);
        arr = new String[19];

        player = new Player(new Vector2(0, 0));

        try {
            br = new BufferedReader(new FileReader(Gdx.files.internal("maps/" + this.map + ".txt").file()));

            for(int i = 0; i < 13; ++i) {
                arr[i] = br.readLine();
            }

        } catch (Exception e) {
            Gdx.app.exit();
        }

        blockManager = new BlockManager(arr);


    }

    @Override
    public void render(float delta) {
        player.update(batch);
        blockManager.update(batch);
    }



    @Override
    public void dispose() {

    }

    @Override
    public void resize(int width, int height) { }
    @Override
    public void show() { }
    @Override
    public void pause() { }
    @Override
    public void resume() { }
    @Override
    public void hide() { }
}
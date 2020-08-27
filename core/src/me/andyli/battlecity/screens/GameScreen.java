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
import me.andyli.battlecity.tanks.TankManager;

import java.io.BufferedReader;
import java.io.FileReader;

public class GameScreen implements Screen {

    private final Stage stage;
    private static Game game;
    private final SpriteBatch batch;
    private final ShapeRenderer renderer;
    private int map;
    private BufferedReader br;
    private String arr[];
    public BlockManager blockManager;
    public static TankManager tankManager;
    public static int left;


    public GameScreen(final Game game, int map) {

        renderer = new ShapeRenderer();


        this.map = map;
        GameScreen.game = game;
        this.batch = new SpriteBatch();
        this.stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()), batch);
        arr = new String[19];

        try {
            br = new BufferedReader(new FileReader(Gdx.files.internal("maps/" + this.map + ".txt").file()));

            for(int i = 0; i < 13; ++i) {
                arr[i] = br.readLine();
            }

        } catch (Exception e) {
            Gdx.app.exit();
        }

        blockManager = new BlockManager(arr);
        tankManager = new TankManager();

        tankManager.addTank(new Player(new Vector2(0, 0)));

        left = 17;


    }

    @Override
    public void render(float delta) {
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(0, 0, 0, 1);
        renderer.rect(0, 0, 624, 624);

        renderer.end();

        blockManager.updateGround(batch);
        tankManager.updateBullets(batch);
        tankManager.updateTanks(batch);
        blockManager.update(batch);
        tankManager.updateExplosions(batch);
    }

    public static void gameOver() {
        game.setScreen(new GameOverScreen(game));
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
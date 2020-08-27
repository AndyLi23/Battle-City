package me.andyli.battlecity.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import me.andyli.battlecity.Constants;
import me.andyli.battlecity.blocks.Block;
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
    private static int map;
    private BufferedReader br;
    private String arr[];
    public BlockManager blockManager;
    public static TankManager tankManager;
    public static int left;
    public static Sprite[] leftVisual;
    public static int lives;
    private Label l1, l2, l3;


    public GameScreen(final Game game, int map, int lives) {

        renderer = new ShapeRenderer();

        Label.LabelStyle lStyle = new Label.LabelStyle();
        lStyle.font = Constants.FONT;
        lStyle.fontColor = Color.BLACK;

        GameScreen.map = map;
        GameScreen.game = game;
        this.batch = new SpriteBatch();
        this.stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()), batch);

        Gdx.input.setInputProcessor(this.stage);

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

        TankManager.addTank(new Player(new Vector2(5, 5)));

        left = 20;
        GameScreen.lives = lives;

        leftVisual = new Sprite[left];

        for(int i = 0 ; i < left/2; ++i) {
            leftVisual[i] = new Sprite(new Texture(Gdx.files.internal("img/tank5.png")));
            leftVisual[i].setPosition(670, 300+i*23);
        }
        for(int i = left/2 ; i < left; ++i) {
            leftVisual[i] = new Sprite(new Texture(Gdx.files.internal("img/tank5.png")));
            leftVisual[i].setPosition(700, 300+(i-(left/2))*23);
        }

        l1 = new Label("I P", lStyle);
        l1.setAlignment(Align.left);
        l2 = new Label(" 3", lStyle);
        l2.setAlignment(Align.left);
        l3 = new Label("Level "+map, lStyle);
        l3.setAlignment(Align.left);

        l1.setPosition(650, 220);
        l2.setPosition(650, 190);
        l3.setPosition(650, 100);

        stage.addActor(l1);
        stage.addActor(l2);
        stage.addActor(l3);


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

        batch.begin();
        for(int i = 0; i < left; ++i) {
            leftVisual[i].draw(batch);
        }
        batch.end();

        if(left == 0 && TankManager.tanks.size == 1) {
            nextLevel();
        }

        if(lives<=0) {
            gameOver();
        }

        l2.setText(lives+"");

        stage.act(delta);
        stage.draw();
    }

    public static void gameOver() {
        TankManager.tanks.clear();
        TankManager.bullets.clear();
        TankManager.explosions.clear();
        BlockManager.arr = new Block[13][13];
        game.setScreen(new GameOverScreen(game));
    }

    public static void nextLevel() {
        TankManager.tanks.clear();
        TankManager.bullets.clear();
        TankManager.explosions.clear();
        BlockManager.arr = new Block[13][13];
        game.setScreen(new GameScreen(game, map+1, lives));
    }



    @Override
    public void dispose() {
        renderer.dispose();
        stage.dispose();
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
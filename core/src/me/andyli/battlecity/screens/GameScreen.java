package me.andyli.battlecity.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import me.andyli.battlecity.Constants;
import me.andyli.battlecity.blocks.Block;
import me.andyli.battlecity.blocks.BlockManager;
import me.andyli.battlecity.tanks.Player;
import me.andyli.battlecity.tanks.TankManager;
import me.andyli.battlecity.utility.Tools;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;

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
    public static int left, left2;
    public static Sprite[] leftVisual;
    public static int lives, total, total1;
    private Label l1, l2, l3;
    public static int[] scores = new int[4];
    public boolean ended;


    public GameScreen(final Game game, int map, int lives, int total, int total1) {

        renderer = new ShapeRenderer();

        GameScreen.total = total;
        GameScreen.total1 = total1;

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
            br = new BufferedReader(new FileReader(Gdx.files.internal("maps/" + GameScreen.map + ".txt").file()));

            for(int i = 0; i < 13; ++i) {
                arr[i] = br.readLine();
            }

        } catch (Exception e) {
            Gdx.app.exit();
        }

        blockManager = new BlockManager(arr);
        tankManager = new TankManager();

        TankManager.addTank(new Player(new Vector2(5, 5)));

        left = left2 = 3;
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

        l1 = new Label("IP", lStyle);
        l1.setAlignment(Align.left);
        l2 = new Label(" 3", lStyle);
        l2.setAlignment(Align.left);
        l3 = new Label("Lvl"+map, lStyle);
        l3.setAlignment(Align.left);

        l1.setPosition(670, 220);
        l2.setPosition(670, 190);
        l3.setPosition(670, 100);

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
        blockManager.updatePowerups(batch);

        batch.begin();
        for(int i = 0; i < left2+TankManager.tanks.size-1; ++i) {
            leftVisual[i].draw(batch);
        }
        batch.end();

        if(!ended) {
            if (left2 == 0 && TankManager.tanks.size == 1 && !BlockManager.spawning()) {
                nextLevel();
                ended = true;
            }

            if (lives <= 0) {
                gameOver();
                ended = true;
            }
        }

        l2.setText(lives+"");

        stage.act(delta);
        stage.draw();
    }

    public static void gameOver() {
        Tools.timer.scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                TankManager.tanks.clear();
                TankManager.bullets.clear();
                TankManager.explosions.clear();
                BlockManager.arr = new Block[13][13];
                BlockManager.powerups.clear();
                game.setScreen(new PointsScreen(game, scores, map, lives, false, true, total, total1));
                GameScreen.scores = new int[4];
            }
        }, 2f);
    }

    public static void nextLevel() {
        Tools.timer.scheduleTask(new Timer.Task() {
             @Override
             public void run() {
                 TankManager.tanks.clear();
                 TankManager.bullets.clear();
                 TankManager.explosions.clear();
                 BlockManager.arr = new Block[13][13];
                 BlockManager.powerups.clear();
                 Gdx.app.log(total+"", "");
                 if(GameScreen.map == 2) {
                     game.setScreen(new PointsScreen(game, scores, map, lives, true, false, total, total1));
                 } else {
                     //game.setScreen(new GameScreen(game, map + 1, lives));
                     game.setScreen(new PointsScreen(game, scores, map, lives, false, false, total, total1));
                 }
                 GameScreen.scores = new int[4];
             }
         }, 2f);
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
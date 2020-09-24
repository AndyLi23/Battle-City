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
    public static int left, left2, players;
    public static Sprite[] leftVisual;
    public static int lives, total, total1, lives2;
    private Label l1, l2, l3, l4, l5;
    public static int[] scores = new int[4];
    public boolean ended;
    private String ts;


    public GameScreen(final Game game, int map, int lives, int total, int total1, int players, int lives2) {

        //scores from previous rounds
        GameScreen.total = total;
        GameScreen.total1 = total1;

        //player mode
        GameScreen.players = players;

        //label style
        Label.LabelStyle lStyle = new Label.LabelStyle();
        lStyle.font = Constants.FONT;
        lStyle.fontColor = Color.BLACK;

        //map, game, spritebatch, stage, renderer
        renderer = new ShapeRenderer();
        GameScreen.map = map;
        GameScreen.game = game;
        this.batch = new SpriteBatch();
        this.stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()), batch);

        //mouse controls this screen
        Gdx.input.setInputProcessor(this.stage);


        //INITIALIZE MAP/SPAWNING------------------------------------------------

        //hold the map
        arr = new String[13];

        try {
            //map
            br = new BufferedReader(new FileReader(Gdx.files.internal("maps/" + GameScreen.map + ".txt").file()));

            for(int i = 0; i < 13; ++i) {
                arr[i] = br.readLine();
            }

            //spawning
            br = new BufferedReader(new FileReader(Gdx.files.internal("plans/" + GameScreen.map + ".txt").file()));

            //number of tanks left
            left = left2 = Integer.parseInt(br.readLine());
            ts = br.readLine();

        } catch (Exception e) {
            Gdx.app.exit();
        }
        //----------------------------------------------------------------

        //blockmanager, tankmanager
        blockManager = new BlockManager(arr);
        tankManager = new TankManager(ts, left);

        //override left = left2 = 5;

        //add player(s)
        TankManager.addTank(new Player(new Vector2(200, 5), false));
        if(players == 2) {
            TankManager.addTank(new Player(new Vector2(385, 5), true));
        }

        //lives
        GameScreen.lives = lives;
        GameScreen.lives2 = lives2;


        //how many tanks are left------------------------------------------------
        leftVisual = new Sprite[left];

        for(int i = 0 ; i < left/2; ++i) {
            leftVisual[i] = new Sprite(new Texture(Gdx.files.internal("img/tank5.png")));
            leftVisual[i].setPosition(670, 300+i*23);
        }
        for(int i = left/2 ; i < left; ++i) {
            leftVisual[i] = new Sprite(new Texture(Gdx.files.internal("img/tank5.png")));
            leftVisual[i].setPosition(700, 300+(i-(left/2))*23);
        }
        //--------------------------------------------------------------------------------



        //side info--------------------------------------------------------------------------------
        l1 = new Label("IP", lStyle);
        l1.setAlignment(Align.left);
        l2 = new Label(lives+"", lStyle);
        l2.setAlignment(Align.left);
        if(players == 2) {
            l3 = new Label("2P", lStyle);
            l3.setAlignment(Align.left);
            l4 = new Label(lives2 + "", lStyle);
            l4.setAlignment(Align.left);
        } else {
            l4 = new Label("", lStyle);
            l3 = new Label("", lStyle);
        }
        l5 = new Label("Lvl "+map, lStyle);
        l5.setAlignment(Align.left);

        l1.setPosition(670, 220);
        l2.setPosition(670, 190);
        l3.setPosition(670, 140);
        l4.setPosition(670, 110);
        l5.setPosition(670, 50);

        Tools.addActors(stage, l1, l2, l3, l4, l5);
        //------------------------------------------------------------------------------------------------

    }

    @Override
    public void render(float delta) {
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(0, 0, 0, 1);
        renderer.rect(0, 0, 624, 624);

        renderer.end();

        //render game--------------------------------
        blockManager.updateGround(batch);
        tankManager.updateBullets(batch);
        tankManager.updateTanks(batch);
        blockManager.update(batch);
        tankManager.updateExplosions(batch);
        blockManager.updatePowerups(batch);
        tankManager.updateHealth(renderer);
        //------------------------------------------------


        //render side menu--------------------------------
        batch.begin();
        for(int i = 0; i < left2+TankManager.getNonPlayers(); ++i) {
            leftVisual[i].draw(batch);
        }
        batch.end();

        l2.setText(""+lives);

        if(lives < 0) {
            l2.setPosition(670, 180);
            l2.setText("GAME\nOVER");
        }

        if(players == 2) {
            l4.setText("" + lives2);

            if(lives2 < 0) {
                l4.setPosition(670, 100);
                l4.setText("GAME\nOVER");
            }
        }
        //------------------------------------------------


        //check for game end-------------------------
        if(!ended) {
            if (left2 == 0 && TankManager.tanks.size == players && !BlockManager.spawning()) {
                nextLevel();
                ended = true;
            }

            if ((players == 1 && lives < 0) || (lives < 0 && lives2 < 0)) {
                gameOver();
                ended = true;
            }
        }
        //--------------------------------------------------

        stage.act(delta);
        stage.draw();
    }

    public static void gameOver() {
        //in 2 seconds:
        Tools.timer.scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                //clear items
                TankManager.tanks.clear();
                TankManager.bullets.clear();
                TankManager.explosions.clear();
                BlockManager.arr = new Block[13][13];
                BlockManager.powerups.clear();

                //change screen
                game.setScreen(new PointsScreen(game, scores, map, lives, false, true, total, total1, players, lives2));
                GameScreen.scores = new int[4];
            }
        }, 2f);
    }

    public static void nextLevel() {
        //in 2 seconds:
        Tools.timer.scheduleTask(new Timer.Task() {
             @Override
             public void run() {
                 //clear items
                 TankManager.tanks.clear();
                 TankManager.bullets.clear();
                 TankManager.explosions.clear();
                 BlockManager.arr = new Block[13][13];
                 BlockManager.powerups.clear();

                 //win
                 if(GameScreen.map == Constants.LEVELS) {
                     game.setScreen(new PointsScreen(game, scores, map, lives, true, false, total, total1, players, lives2));
                 } else {
                     //next level
                     game.setScreen(new PointsScreen(game, scores, map, lives, false, false, total, total1, players, lives2));
                 }
                 GameScreen.scores = new int[4];
             }
         }, 2f);
    }



    @Override
    public void dispose() {
        stage.dispose();
        renderer.dispose();
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
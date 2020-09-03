package me.andyli.battlecity.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import me.andyli.battlecity.Constants;
import me.andyli.battlecity.utility.Tools;


public class PointsScreen implements Screen {

    private final Stage stage;
    private final Game game;
    private final SpriteBatch batch;
    private final ShapeRenderer renderer;
    private Label l1, l2, t1, t2, t3, t1a, t2a, t3a, tt, tta, ttt, t, ot, ttta, b, ba;
    private Sprite t1s, t2s, t3s;
    private TextButton playagain, menu;
    private int players;

    public PointsScreen(final Game game, int[] scores, int map, int lives, boolean win, boolean lose, int total, int total1, int players, int lives2) {


        //initialize
        renderer = new ShapeRenderer();

        this.game = game;
        this.batch = new SpriteBatch();
        this.stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()), batch);
        this.players = players;

        Gdx.input.setInputProcessor(this.stage);

        int[] s_scores = scores.clone();


        //styling--------------------------------------------
        Label.LabelStyle lStyle = new Label.LabelStyle();
        lStyle.font = Constants.FONT_LARGE2;
        lStyle.fontColor = Color.RED;

        Label.LabelStyle l2Style = new Label.LabelStyle();
        l2Style.font = Constants.FONT;
        l2Style.fontColor = Color.WHITE;

        Label.LabelStyle l3Style = new Label.LabelStyle();
        l3Style.font = Constants.FONT_LARGE;
        l3Style.fontColor = Color.RED;

        Label.LabelStyle l4Style = new Label.LabelStyle();
        l4Style.font = Constants.FONT_MEDIUM;
        l4Style.fontColor = Color.WHITE;

        TextButton.TextButtonStyle tStyle = new TextButton.TextButtonStyle();
        tStyle.font = Constants.FONT;
        tStyle.up = Constants.SKIN.getDrawable("button_03");
        tStyle.down = Constants.SKIN.getDrawable("button_02");
        tStyle.fontColor = Color.BLACK;
        //------------------------------------------------------



        //GRAPHICS------------------------------------------------------------------------------------------

        //title------------------
        if(win) {
            l1 = new Label("You Win!", l3Style);
            l2 = new Label(map+" Levels Completed", l4Style);
        } else if (lose) {
            l1 = new Label("Game Over", l3Style);
            l2 = new Label("On Level " + (map), l4Style);
        } else {
            l1 = new Label("Level " + map + " Complete", lStyle);
            l2 = new Label("", l4Style);
        }

        l1.setAlignment(Align.center);
        l1.setPosition(400-l1.getWidth()/2, 520);

        l2.setPosition(400-l2.getWidth()/2, 485);
        //------------------

        //labels------------------------------------------------------
        t1 = new Label(s_scores[0]+"", l2Style);
        t1.setAlignment(Align.left);
        t1.setPosition(300, 400);

        t2 = new Label(s_scores[1]+"", l2Style);
        t2.setAlignment(Align.left);
        t2.setPosition(300, 350);

        t3 = new Label(s_scores[2]+"", l2Style);
        t3.setAlignment(Align.left);
        t3.setPosition(300, 300);

        tt = new Label(s_scores[2]+s_scores[1]+s_scores[0]+"", l2Style);
        tt.setAlignment(Align.left);
        tt.setPosition(300, 210);

        ttta = new Label(s_scores[2]+s_scores[1]+s_scores[0]+total1+"", l2Style);
        ttta.setAlignment(Align.right);
        ttta.setPosition(300, 180);

        b = new Label("Bonus", l2Style);
        b.setAlignment(Align.left);
        b.setPosition(170, 240);

        ot = new Label("Total", l2Style);
        ot.setAlignment(Align.left);
        ot.setPosition(170, 210);

        t = new Label("Game", l2Style);
        t.setAlignment(Align.left);
        t.setPosition(170, 180);

        t1a = new Label(s_scores[0]*100+"", l2Style);
        t1a.setAlignment(Align.right);
        t1a.setPosition(520-t1a.getWidth(), 400);

        t2a = new Label(s_scores[1]*500+"", l2Style);
        t2a.setAlignment(Align.right);
        t2a.setPosition(520-t2a.getWidth(), 350);

        t3a = new Label(s_scores[2]*300+"", l2Style);
        t3a.setAlignment(Align.right);
        t3a.setPosition(520-t3a.getWidth(), 300);

        ba = new Label(s_scores[3]*500+"", l2Style);
        ba.setAlignment(Align.right);
        ba.setPosition(520-ba.getWidth(), 240);

        tta = new Label(s_scores[2]*300+s_scores[1]*500+s_scores[0]*100+"", l2Style);
        tta.setAlignment(Align.right);
        tta.setPosition(520-tta.getWidth(), 210);

        ttt = new Label(s_scores[2]*300+s_scores[1]*500+s_scores[0]*100+total+"", l2Style);
        ttt.setAlignment(Align.right);
        ttt.setPosition(520-ttt.getWidth(), 180);

        //------------------------------------------------------


        //tank sprites------------------
        t1s = new Sprite(new Texture(Gdx.files.internal("img/tank2.png")));
        t1s.setCenter(400, 415);

        t2s = new Sprite(new Texture(Gdx.files.internal("img/tank34.png")));
        t2s.setCenter(400, 365);

        t3s = new Sprite(new Texture(Gdx.files.internal("img/tank4.png")));
        t3s.setCenter(400, 315);
        //------------------------------------


        //buttons------------------------------------
        if(win || lose) {
            playagain = new TextButton("Play Again", tStyle);
            playagain.setWidth(200);
            playagain.setPosition(400 - playagain.getWidth() / 2, 110);

            playagain.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    dispose();
                    game.setScreen(new LevelScreen(game, 1, false, Constants.LIVES, 0, 0, players, Constants.LIVES));
                }
            });
        } else {
            playagain = new TextButton("Continue", tStyle);
            playagain.setWidth(200);
            playagain.setPosition(400 - playagain.getWidth() / 2, 110);

            playagain.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    dispose();
                    game.setScreen(new LevelScreen(game, map+1, true, lives, total + s_scores[2]*300+s_scores[1]*500+s_scores[0]*100+s_scores[3]*500, s_scores[2]+s_scores[1]+s_scores[0]+total1, players, lives2));
                }
            });
        }

        menu = new TextButton("Menu", tStyle);
        menu.setWidth(200);
        menu.setPosition(400 - playagain.getWidth() / 2, 60);

        menu.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                dispose();
                game.setScreen(new MenuScreen(game));
            }
        });
        //------------------------------------------------------


        Tools.addActors(stage, l1, l2, playagain, t1, t2, t3, t1a, t2a, t3a, tt, tta, ttt, t, ot, ttta, menu, b, ba);
        //------------------------------------------------------------------------------------------------------------

    }

    @Override
    public void render(float delta) {
        //render background and dividing line
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(Color.BLACK);
        renderer.rect(0, 0, 800, 624);

        renderer.setColor(Color.WHITE);
        renderer.rect(270, 280, 280, 2);

        renderer.end();


        //draw sprites
        batch.begin();
        t1s.draw(batch);
        t2s.draw(batch);
        t3s.draw(batch);
        batch.end();


        stage.act(delta);
        stage.draw();

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
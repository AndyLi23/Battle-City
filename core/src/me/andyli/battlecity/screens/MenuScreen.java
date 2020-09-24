package me.andyli.battlecity.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
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

import java.awt.*;
import java.net.URL;

import static com.badlogic.gdx.Gdx.input;

public class MenuScreen implements Screen {

    private final Stage stage;
    private final Game game;
    private final SpriteBatch batch;
    private final ShapeRenderer renderer;
    private Label title, credit, help;
    private int x, y;
    private TextButton start, start2, settings;



    public MenuScreen(final Game game) {

        //init
        renderer = new ShapeRenderer();

        this.game = game;
        this.batch = new SpriteBatch();
        this.stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()), batch);

        input.setInputProcessor(this.stage);


        //styling----------------------------------------------------------------------------------------
        Label.LabelStyle lStyle = new Label.LabelStyle();
        lStyle.font = Constants.FONT_HUGE;
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

        Label.LabelStyle l5Style = new Label.LabelStyle();
        l5Style.font = Constants.FONT_SMALL;
        l5Style.fontColor = Color.GRAY;

        TextButton.TextButtonStyle tStyle = new TextButton.TextButtonStyle();
        tStyle.font = Constants.FONT;
        tStyle.up = Constants.SKIN.getDrawable("button_03");
        tStyle.down = Constants.SKIN.getDrawable("button_02");
        tStyle.fontColor = Color.BLACK;
        //----------------------------------------------------------------------------------------



        //graphics----------------------------------------------------------------------------------------
        title = new Label("Battle\nCity", lStyle);
        title.setAlignment(Align.center);
        title.setPosition(400-title.getWidth()/2, 410);

        credit = new Label("Created by Andy Li", l2Style);
        credit.setAlignment(Align.center);
        credit.setPosition(400-credit.getWidth()/2, 370);

        help = new Label("Player 1: WASD to move, B to shoot\nPlayer 2: Arrow keys to move, M to shoot\n\nObjective: Kill all the enemy tanks\n and protect your own flag", l5Style);
        help.setAlignment(Align.center);
        help.setPosition(400-help.getWidth()/2, 240);

        start = new TextButton("1 Player", tStyle);
        start.setWidth(200);
        start.setPosition(400 - start.getWidth() / 2, 180);

        start2 = new TextButton("2 Player", tStyle);
        start2.setWidth(200);
        start2.setPosition(400 - start2.getWidth() / 2, 120);

        settings = new TextButton("Settings", tStyle);
        settings.setWidth(200);
        settings.setPosition(400 - settings.getWidth() / 2, 60);

        start.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                dispose();
                game.setScreen(new LevelScreen(game, 1, false, Constants.LIVES, 0, 0, 1, Constants.LIVES));
            }
        });

        start2.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                dispose();
                game.setScreen(new LevelScreen(game, 1, false, Constants.LIVES, 0, 0, 2, Constants.LIVES));
            }
        });

        settings.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                dispose();
                game.setScreen(new SettingsScreen(game));
            }
        });

        Tools.addActors(stage, title, credit, help, start, start2, settings);
        //----------------------------------------------------------------------------------------

    }

    @Override
    public void render(float delta) {
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(0, 0, 0, 1);
        renderer.rect(0, 0, 800, 624);

        renderer.end();


        //link to github--------------------------------------------
        x = input.getX();
        y = input.getY();

        Gdx.app.log(y+"", "");

        if(x > 400-credit.getWidth()/2 && x < 400+credit.getWidth()/2 && y < 256 && y > 232) {
            credit.setColor(Color.LIGHT_GRAY);
            if(input.isButtonJustPressed(0)) {
                openWebpage("https://github.com/AndyLi23");
            }
        } else {
            credit.setColor(Color.WHITE);
        }
        //-----------------------------------------------------------

        stage.act(delta);
        stage.draw();

    }


    @Override
    public void dispose() {
        renderer.dispose();
        stage.dispose();
    }

    public static void openWebpage(String urlString) {
        //open webpage in browser
        try {
            Desktop.getDesktop().browse(new URL(urlString).toURI());
        } catch (Exception e) {
            e.printStackTrace();
        }
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
package me.andyli.battlecity.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import me.andyli.battlecity.Constants;
import me.andyli.battlecity.blocks.Block;
import me.andyli.battlecity.blocks.BlockManager;
import me.andyli.battlecity.tanks.Player;
import me.andyli.battlecity.tanks.TankManager;
import me.andyli.battlecity.utility.Tools;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.net.URL;
import java.util.Arrays;

import static com.badlogic.gdx.Gdx.input;

public class MenuScreen implements Screen {

    private final Stage stage;
    private static Game game;
    private final SpriteBatch batch;
    private final ShapeRenderer renderer;
    private Label title, credit;
    private int x, y;
    private TextButton start, start2;



    public MenuScreen(final Game game) {

        renderer = new ShapeRenderer();

        MenuScreen.game = game;
        this.batch = new SpriteBatch();
        this.stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()), batch);

        input.setInputProcessor(this.stage);


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


        TextButton.TextButtonStyle tStyle = new TextButton.TextButtonStyle();
        tStyle.font = Constants.FONT;
        tStyle.up = Constants.SKIN.getDrawable("button_03");
        tStyle.down = Constants.SKIN.getDrawable("button_02");
        tStyle.fontColor = Color.BLACK;

        title = new Label("Battle\nCity", lStyle);
        title.setAlignment(Align.center);
        title.setPosition(400-title.getWidth()/2, 400);

        credit = new Label("Created by Andy Li", l2Style);
        credit.setAlignment(Align.center);
        credit.setPosition(400-credit.getWidth()/2, 350);

        start = new TextButton(" 1 Player ", tStyle);
        start.setWidth(200);
        start.setPosition(400 - start.getWidth() / 2, 160);

        start2 = new TextButton(" 2 Player ", tStyle);
        start2.setWidth(200);
        start2.setPosition(400 - start2.getWidth() / 2, 100);

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

        Tools.addActors(stage, title, credit, start, start2);

    }

    @Override
    public void render(float delta) {
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(0, 0, 0, 1);
        renderer.rect(0, 0, 800, 624);

        renderer.end();

        x = input.getX();
        y = input.getY();

        if(x > 400-credit.getWidth()/2 && x < 400+credit.getWidth()/2 && y < 274 && y > 274 - credit.getHeight()) {
            credit.setColor(Color.LIGHT_GRAY);
            if(input.isButtonJustPressed(0)) {
                openWebpage("https://github.com/AndyLi23");
            }
        } else {
            credit.setColor(Color.WHITE);
        }

        stage.act(delta);
        stage.draw();

    }


    @Override
    public void dispose() {
        renderer.dispose();
        stage.dispose();
    }

    public static void openWebpage(String urlString) {
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
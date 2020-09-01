package me.andyli.battlecity.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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

import java.util.Arrays;


public class LevelScreen implements Screen {

    private final Stage stage;
    private final Game game;
    private final SpriteBatch batch;
    private final ShapeRenderer renderer;
    private Label l1;
    private int map, left, lives, total, total1;
    private boolean timed;


    public LevelScreen(final Game game, int map, boolean timed, int lives, int total, int total1) {

        renderer = new ShapeRenderer();


        this.game = game;
        this.map = map;
        this.timed = timed;

        this.lives = lives;
        this.total = total;
        this.total1 = total1;

        this.batch = new SpriteBatch();
        this.stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()), batch);

        left = 120;

        Gdx.input.setInputProcessor(this.stage);


        Label.LabelStyle lStyle = new Label.LabelStyle();
        lStyle.font = Constants.FONT_MEDIUM;
        lStyle.fontColor = Color.BLACK;

        TextButton.TextButtonStyle tStyle = new TextButton.TextButtonStyle();
        tStyle.font = Constants.FONT;
        tStyle.up = Constants.SKIN.getDrawable("button_03");
        tStyle.down = Constants.SKIN.getDrawable("button_02");
        tStyle.fontColor = Color.BLACK;

        l1 = new Label("LEVEL " + map, lStyle);
        l1.setAlignment(Align.center);
        l1.setPosition(400-l1.getWidth()/2, 312);


        Tools.addActors(stage, l1);

    }

    @Override
    public void render(float delta) {
        if(left != 0) {
            left--;
        }
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(Color.LIGHT_GRAY);
        renderer.rect(0, 0, 800, 624);

        renderer.end();


        if(!timed) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT) || Gdx.input.isKeyJustPressed(Input.Keys.D)) {
                if (map != Constants.LEVELS) {
                    map++;
                }
            }

            if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT) || Gdx.input.isKeyJustPressed(Input.Keys.A)) {
                if (map != 1) {
                    map--;
                }
            }
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER) || (timed && left == 0)) {
            game.setScreen(new GameScreen(game, map, lives, total, total1));
        }

        l1.setText("LEVEL " + map);

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
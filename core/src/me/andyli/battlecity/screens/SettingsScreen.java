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

import java.awt.*;
import java.net.URL;

import static com.badlogic.gdx.Gdx.input;

public class SettingsScreen implements Screen {

    private final Stage stage;
    private final Game game;
    private final SpriteBatch batch;
    private final ShapeRenderer renderer;
    private Label difficulty, tank;
    private TextButton done, easy, medium, hard, rifle, mg, smg;
    private final String[] color_s = new String[]{"red", "orange", "yellow", "green", "cyan", "blue", "violet", "pink", "white"};
    private final Color[] colors = new Color[]{Color.RED, Color.ORANGE, Color.YELLOW, new Color(35/255f, 150/255f, 50/255f, 1), Color.CYAN, Color.BLUE, new Color(120/255f, 0/255f, 230/255f, 1), Color.PINK, Color.WHITE};
    private final Sprite base = new Sprite(new Texture(Gdx.files.internal("img/tank1_pink.png")));


    public SettingsScreen(final Game game) {

        //init
        renderer = new ShapeRenderer();

        this.game = game;
        this.batch = new SpriteBatch();
        this.stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()), batch);

        input.setInputProcessor(this.stage);

        base.setScale(2f);
        base.setPosition(550-base.getWidth()/2, 180);
        base.rotate(270);


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


        TextButton.TextButtonStyle tStyle = new TextButton.TextButtonStyle();
        tStyle.font = Constants.FONT;
        tStyle.up = Constants.SKIN.getDrawable("button_03");
        tStyle.down = Constants.SKIN.getDrawable("button_02");
        tStyle.fontColor = Color.BLACK;

        TextButton.TextButtonStyle t2Style = new TextButton.TextButtonStyle();
        t2Style.font = Constants.FONT;
        t2Style.up = Constants.SKIN_ALTERNATE.getDrawable("button_03");
        t2Style.down = Constants.SKIN_ALTERNATE.getDrawable("button_02");
        t2Style.fontColor = Color.WHITE;

        TextButton.TextButtonStyle t3Style = new TextButton.TextButtonStyle();
        t3Style.font = Constants.FONT_SMALL;
        t3Style.up = Constants.SKIN_ALTERNATE.getDrawable("button_03");
        t3Style.down = Constants.SKIN_ALTERNATE.getDrawable("button_02");
        t3Style.fontColor = Color.WHITE;
        //----------------------------------------------------------------------------------------


        //graphics----------------------------------------------------------------------------------------

        difficulty = new Label("Difficulty", l4Style);
        difficulty.setAlignment(Align.center);
        difficulty.setPosition(400-difficulty.getWidth()/2, 550);

        tank = new Label("Customize Tank", l4Style);
        tank.setAlignment(Align.center);
        tank.setPosition(400-tank.getWidth()/2, 380);

        easy = new TextButton("Easy", t2Style);
        easy.setWidth(150);
        easy.setPosition(200-easy.getWidth()/2, 480);

        medium = new TextButton("Medium", t2Style);
        medium.setWidth(150);
        medium.setPosition(400-medium.getWidth()/2, 480);

        hard = new TextButton("Hard", t2Style);
        hard.setWidth(150);
        hard.setPosition(600-hard.getWidth()/2, 480);

        done = new TextButton("Done", tStyle);
        done.setWidth(200);
        done.setPosition(400 - done.getWidth() / 2, 60);

        done.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                dispose();
                game.setScreen(new MenuScreen(game));
            }
        });

        easy.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Constants.MODE = 0;
                Constants.playerHealth = 2f;
            }
        });

        medium.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Constants.MODE = 1;
                Constants.playerHealth = 2f;
            }
        });

        hard.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Constants.MODE = 1;
                Constants.playerHealth = 0.5f;
            }
        });


        rifle = new TextButton("Rifle", t3Style);
        rifle.setWidth(200);
        rifle.setPosition(300 - rifle.getWidth() / 2, 230);

        mg = new TextButton("Machine Gun", t3Style);
        mg.setWidth(200);
        mg.setPosition(300 - mg.getWidth() / 2, 180);

        smg = new TextButton("Submachine Gun", t3Style);
        smg.setWidth(200);
        smg.setPosition(300 - smg.getWidth() / 2, 130);

        rifle.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Constants.GUN_TYPE = 3;
            }
        });

        smg.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Constants.GUN_TYPE = 5;
            }
        });

        mg.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Constants.GUN_TYPE = 4;
            }
        });


        Tools.addActors(stage, done, difficulty, easy, medium, hard, tank, rifle, mg, smg);
        //----------------------------------------------------------------------------------------

    }

    @Override
    public void render(float delta) {
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(0, 0, 0, 1);
        renderer.rect(0, 0, 800, 624);

        renderer.setColor(Color.RED);
        if(Constants.MODE == 0) {
            Tools.roundedRect(renderer, easy.getX()-5, easy.getY()-5, easy.getWidth()+10, easy.getHeight()+10, 10);
        } else if (Constants.playerHealth == 2f) {
            Tools.roundedRect(renderer, medium.getX()-5, medium.getY()-5, medium.getWidth()+10, medium.getHeight()+10, 10);
        } else {
            Tools.roundedRect(renderer, hard.getX()-5, hard.getY()-5, hard.getWidth()+10, hard.getHeight()+10, 10);
        }

        if(Constants.GUN_TYPE == 3) {
            Tools.roundedRect(renderer, rifle.getX()-3, rifle.getY()-3, rifle.getWidth()+6, rifle.getHeight()+6, 7);
        } else if (Constants.GUN_TYPE == 4) {
            Tools.roundedRect(renderer, mg.getX()-3, mg.getY()-3, mg.getWidth()+6, mg.getHeight()+6, 7);
        } else {
            Tools.roundedRect(renderer, smg.getX()-3, smg.getY()-3, smg.getWidth()+6, smg.getHeight()+6, 7);
        }
        renderer.end();


        if(Gdx.input.isButtonJustPressed(0)) {
            if(Gdx.input.getY() >= 297 && Gdx.input.getY() <= 343) {
                for(int i = 0; i < 9; ++i) {
                    if(Gdx.input.getX() >= i*60+137 && Gdx.input.getX() <= i*60+183) {
                        Constants.COLOR = color_s[i];
                    }
                }
            }
        }



        renderer.begin(ShapeRenderer.ShapeType.Filled);
        for(int i = 0; i < 9; ++i) {
            renderer.setColor(Color.DARK_GRAY);
            renderer.rect(i*60+137, 297, 46, 46);

            if(Constants.COLOR.equals(color_s[i])) {
                renderer.setColor(Color.WHITE);
                renderer.rect(i*60+135, 295, 50, 50);
            }

            renderer.setColor(colors[i]);
            renderer.rect(i*60+140, 300, 40, 40);
        }

        renderer.end();

        base.setTexture(new Texture(Gdx.files.internal("img/tank1_" + Constants.COLOR + ".png")));

        batch.begin();
        base.draw(batch);
        batch.end();

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
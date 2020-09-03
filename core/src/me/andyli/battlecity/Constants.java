package me.andyli.battlecity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import me.andyli.battlecity.utility.Tools;

public class Constants {

    //fonts
    public static final BitmapFont FONT = Tools.renderFont("font/joystick.ttf", 20);
    public static final BitmapFont FONT_HUGE = Tools.renderFont("font/joystick.ttf", 80);
    public static final BitmapFont FONT_LARGE = Tools.renderFont("font/joystick.ttf", 50);
    public static final BitmapFont FONT_LARGE2 = Tools.renderFont("font/joystick.ttf", 35);
    public static final BitmapFont FONT_MEDIUM = Tools.renderFont("font/joystick.ttf", 25);

    //skins
    public static final TextureAtlas ATLAS = new TextureAtlas(Gdx.files.internal("skin/ui-red.atlas"));
    public static final TextureAtlas ATLAS_ALTERNATE = new TextureAtlas(Gdx.files.internal("skin/ui-blue.atlas"));
    public static final Skin SKIN = new Skin(ATLAS);
    public static final Skin SKIN_ALTERNATE = new Skin(ATLAS_ALTERNATE);

    //game settings
    public static final int LEVELS = 2;
    public static final int LIVES = 30;
    public static int MODE = 1;

    public static void disposeAll() {
        FONT.dispose();
        FONT_MEDIUM.dispose();
        ATLAS.dispose();
        ATLAS_ALTERNATE.dispose();
        SKIN_ALTERNATE.dispose();
        SKIN.dispose();
    }
}

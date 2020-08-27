package me.andyli.battlecity;

import me.andyli.battlecity.utility.Tools;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Constants {

    public static final BitmapFont FONT = Tools.renderFont("font/Anonymous_Pro_B.ttf", 35, true);
    public static final BitmapFont FONT_LARGE = Tools.renderFont("font/Anonymous_Pro_B.ttf", 80, true);
    public static final BitmapFont FONT_MEDIUM = Tools.renderFont("font/Abel-Regular.ttf", 25);
    public static final BitmapFont FONT_SMALL = Tools.renderFont("font/Abel-Regular.ttf", 20);
    public static final TextureAtlas ATLAS = new TextureAtlas(Gdx.files.internal("skin/ui-gray.atlas"));
    public static final TextureAtlas ATLAS_ALTERNATE = new TextureAtlas(Gdx.files.internal("skin/ui-blue.atlas"));
    public static final Skin SKIN = new Skin(ATLAS);
    public static final Skin SKIN_ALTERNATE = new Skin(ATLAS_ALTERNATE);

    public static void disposeAll() {
        FONT.dispose();
        FONT_MEDIUM.dispose();
        FONT_SMALL.dispose();
        ATLAS.dispose();
        ATLAS_ALTERNATE.dispose();
        SKIN_ALTERNATE.dispose();
        SKIN.dispose();
    }
}

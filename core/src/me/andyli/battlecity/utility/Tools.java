package me.andyli.battlecity.utility;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class Tools {


    public static BitmapFont renderFont(String fontfile, int size, boolean... bold) {
        FileHandle fontFile = Gdx.files.internal(fontfile);
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(fontFile);
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = size;
        if(bold.length > 0) {
            parameter.borderWidth = 2;
        }
        BitmapFont fnt = generator.generateFont(parameter);
        generator.dispose();
        return fnt;
    }

}
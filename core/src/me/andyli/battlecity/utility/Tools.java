package me.andyli.battlecity.utility;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

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

    public static boolean collide(Vector2 r1p1, Vector2 r1p2, Vector2 r2p1, Vector2 r2p2) {
        return !(r1p2.x <= r2p1.x ||
                r1p2.y <= r2p1.y ||
                r1p1.x >= r2p2.x ||
                r1p1.y >= r2p2.y);
    }

    public static int selectRandom(int[] list) {
        Random rand = new Random();
        return list[rand.nextInt(list.length)];
    }

    public static boolean choose(int num) {
        Random rand = new Random();
        return 0 == rand.nextInt(num);
    }

}
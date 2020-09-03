package me.andyli.battlecity.utility;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Timer;
import me.andyli.battlecity.blocks.Spawner;

import java.util.ArrayList;
import java.util.Random;

public class Tools {
    public static Timer timer = new Timer();
    public static Random rand = new Random();

    public static BitmapFont renderFont(String fontfile, int size, boolean... bold) {
        FileHandle fontFile = Gdx.files.internal(fontfile);
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(fontFile);
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = size;
        if(bold.length > 0) {
            parameter.borderWidth = 0.7f;
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
        return list[rand.nextInt(list.length)];
    }

    public static Spawner selectRandom(ArrayList<Spawner> list) {
        return list.get(rand.nextInt(list.size()));
    }

    public static boolean choose(int num) {
        return 0 == rand.nextInt(num);
    }

    public static int random(int limit) {
        return rand.nextInt(limit);
    }

    public static void addActors(Stage stage, Actor... actors) {
        if(actors.length > 0) {
            for(Actor a : actors) {
                stage.addActor(a);
            }
        }
    }

}
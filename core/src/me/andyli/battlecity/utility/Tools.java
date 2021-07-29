package me.andyli.battlecity.utility;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Timer;

import java.util.ArrayList;
import java.util.Random;

public class Tools {
    //timer and rand tools
    public static Timer timer = new Timer();
    public static Random rand = new Random();


    //render font
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


    //two rectangles (each rep. by two corners) collide
    public static boolean collide(Vector2 r1p1, Vector2 r1p2, Vector2 r2p1, Vector2 r2p2) {
        return !(r1p2.x <= r2p1.x ||
                r1p2.y <= r2p1.y ||
                r1p1.x >= r2p2.x ||
                r1p1.y >= r2p2.y);
    }


    //select random (for paths)
    public static Pair selectRandom(ArrayList<Pair> list) {
        return list.get(rand.nextInt(list.size()));
    }

    //get true with some probability
    public static boolean choose(int num) {
        return 0 == rand.nextInt(num);
    }

    //get a random number
    public static int random(int limit) {
        return rand.nextInt(limit);
    }

    public static int chooseNum(int... nums) {
        if(nums.length > 0) {
            return nums[rand.nextInt(nums.length)];
        }
        return -1;
    }


    //add several actors to a stage
    public static void addActors(Stage stage, Actor... actors) {
        if(actors.length > 0) {
            for(Actor a : actors) {
                stage.addActor(a);
            }
        }
    }

    public static void roundedRect(ShapeRenderer renderer, float x, float y, float width, float height, float radius){
        // Central rectangle
        renderer.rect(x + radius, y + radius, width - 2*radius, height - 2*radius);

        // Four side rectangles, in clockwise order
        renderer.rect(x + radius, y, width - 2*radius, radius);
        renderer.rect(x + width - radius, y + radius, radius, height - 2*radius);
        renderer.rect(x + radius, y + height - radius, width - 2*radius, radius);
        renderer.rect(x, y + radius, radius, height - 2*radius);

        // Four arches, clockwise too
        renderer.arc(x + radius, y + radius, radius, 180f, 90f);
        renderer.arc(x + width - radius, y + radius, radius, 270f, 90f);
        renderer.arc(x + width - radius, y + height - radius, radius, 0f, 90f);
        renderer.arc(x + radius, y + height - radius, radius, 90f, 90f);
    }

}
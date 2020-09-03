package me.andyli.battlecity.tanks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import me.andyli.battlecity.utility.Tools;

import java.util.Random;

public class Explosion {
    public Sprite base;
    public float max;
    public float speed;
    public float scale = 0.1f;
    public boolean adding = true;

    public int counter;

    public Explosion(Vector2 center, float max, float speed) {
        //initialize
        base = new Sprite(new Texture(Gdx.files.internal("img/explosion1.png")));
        base.setCenter(center.x, center.y);

        base.setScale(scale);

        //args
        this.max = max;
        this.speed = speed;

        counter = 0;

    }

    public void update(SpriteBatch batch) {
        //countdown
        counter++;


        if(counter % 5 == 0) {
            //rotate every 5 frames
            base.rotate(Tools.random(360));
        }

        //get smaller
        if(scale >= max) {
            adding = false;
        }

        //explosion ends
        if(scale <= 0f) {
            TankManager.explosions.removeValue(this, true);
        }

        //make explosion larger or smaller
        if(adding) {
            scale+=speed;
        } else {
            scale-=speed;
        }


        //set scale
        base.setScale(scale);

        batch.begin();
        base.draw(batch);
        batch.end();

    }
}

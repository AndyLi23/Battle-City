package me.andyli.battlecity.tanks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

public class Explosion {
    public Sprite base;
    public float max;
    public float speed;
    public float scale = 0.1f;
    public boolean adding = true;

    public Random rand;
    public int counter;

    public Explosion(Vector2 center, float max, float speed) {
        base = new Sprite(new Texture(Gdx.files.internal("img/explosion1.png")));
        base.setCenter(center.x, center.y);

        base.setScale(scale);

        rand = new Random();

        this.max = max;
        this.speed = speed;

        counter = 0;

    }

    public void update(SpriteBatch batch) {
        counter++;
        if(counter % 5 == 0) {
            base.rotate(rand.nextInt(360));
        }
        if(scale >= max) {
            adding = false;
        }
        if(scale <= 0f) {
            TankManager.explosions.removeValue(this, true);
        }
        if(adding) {
            scale+=speed;
        } else {
            scale-=speed;
        }

        base.setScale(scale);

        batch.begin();
        base.draw(batch);
        batch.end();

    }
}

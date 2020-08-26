package me.andyli.battlecity.blocks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import me.andyli.battlecity.screens.GameScreen;
import me.andyli.battlecity.tanks.Tank;
import me.andyli.battlecity.tanks.TankManager;
import me.andyli.battlecity.utility.Tools;

import java.util.Random;

public class Spawner extends Block {

    private Sprite base;
    private boolean spawning = false;
    private int counter;
    private int[] list;

    public Spawner(Vector2 position, int i, int j) {
        super(position, i, j);
        if(j == 0) {
            list = new int[]{3, 2,3};
        } else if (j == 12) {
            list = new int[]{1, 2,1};
        } else {
            list = new int[]{1, 2, 3};
        }

        base = new Sprite(new Texture(Gdx.files.internal("img/water.png")));
        base.setPosition(position.x, position.y);
    }

    public void update(SpriteBatch batch) {
        if(spawning) {
            counter--;
            if (counter == 0) {
                GameScreen.tankManager.addTank(0, new Vector2(position.x, position.y), Tools.selectRandom(list));
                spawning = false;
            } else if((counter/10)%2 == 0) {
                base.setTexture(new Texture(Gdx.files.internal("img/water.png")));
            } else if ((counter/10)%2 == 1) {
                base.setTexture(new Texture(Gdx.files.internal("img/iron.png")));
            }
            batch.begin();
            base.draw(batch);
            batch.end();
        } else {
            boolean good = true;
            for(Tank t : TankManager.tanks) {
                if(Tools.collide(t.position, new Vector2(t.position.x+39, t.position.y+39), position, position2)) {
                    good = false;
                    break;
                }
            }
            if(good && Tools.choose(500)) {
                spawning = true;
                counter = 100;
            }
        }
    }

    public boolean collideTank(Vector2 r1p1, Vector2 r1p2) {
        if(spawning) {
            return Tools.collide(r1p1, r1p2, position, position2);
        }
        return false;
    }

    public boolean collideBullet(Vector2 r1p1, Vector2 r1p2, int direction) {
        return false;
    }

    public void spawn() {
        spawning = true;
        counter = 100;
    }
}

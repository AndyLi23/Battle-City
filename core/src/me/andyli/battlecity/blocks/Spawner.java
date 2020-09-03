package me.andyli.battlecity.blocks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import javafx.util.Pair;
import me.andyli.battlecity.screens.GameScreen;
import me.andyli.battlecity.tanks.TankManager;
import me.andyli.battlecity.utility.Tools;

import java.util.ArrayList;

public class Spawner extends Block {

    private Sprite base;
    private boolean spawning = false;
    public int counter, tankType;
    private Vector2 pos;
    private int[] list;
    ArrayList<Pair<Integer, Integer>> path;
    private boolean powerup;

    public Spawner(Vector2 position, int i, int j) {
        super(position, i, j);
        if(j == 0) {
            list = new int[]{2, 3};
        } else if (j == 12) {
            list = new int[]{1, 2};
        } else {
            list = new int[]{1, 2, 3};
        }

        base = new Sprite(new Texture(Gdx.files.internal("img/spawner.png")));
        base.setPosition(position.x+1, position.y+1);
    }

    public void update(SpriteBatch batch) {
        if(spawning) {
            counter--;
            if (counter == 0) {
                GameScreen.left2--;
                int dir = Tools.selectRandom(list);
                pos = new Vector2(position.x, position.y);
                path = BlockManager.getPath(x, y);
                GameScreen.tankManager.addTank(tankType, pos, dir, path, powerup);
                spawning = false;

            } else if((counter/10)%2 == 0) {
                base.setTexture(new Texture(Gdx.files.internal("img/spawner1.png")));
            } else if ((counter/10)%2 == 1) {
                base.setTexture(new Texture(Gdx.files.internal("img/spawner2.png")));
            }

        } else {
            base.setTexture(new Texture(Gdx.files.internal("img/spawner.png")));
        }
        
        batch.begin();
        base.draw(batch);
        batch.end();
    }

    public boolean collideBullet(Vector2 r1p1, Vector2 r1p2, int direction) {
        return false;
    }

    public boolean collideTank(Vector2 r1p1, Vector2 r1p2) {
        return Tools.collide(r1p1, r1p2, position, position2);
    }

    public void spawn(int tankType, boolean powerup) {
        spawning = true;
        counter = 80;
        this.powerup = powerup;
        this.tankType = tankType;
    }
}

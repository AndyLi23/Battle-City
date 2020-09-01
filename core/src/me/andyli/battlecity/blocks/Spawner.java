package me.andyli.battlecity.blocks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import me.andyli.battlecity.screens.GameScreen;
import me.andyli.battlecity.tanks.TankManager;
import me.andyli.battlecity.utility.Tools;

public class Spawner extends Block {

    private Sprite base;
    private boolean spawning = false;
    public int counter;
    private Vector2 pos;
    private int[] list;
    int[][] path;

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
                if(dir == 3) {
                    pos = new Vector2(position.x+9, position.y);
                } else {
                    pos = new Vector2(position.x, position.y);
                }

                GameScreen.tankManager.addTank(Tools.selectRandom(new int[]{0, 1, 2}), pos, dir);
                spawning = false;
            } else if((counter/10)%2 == 0) {
                base.setTexture(new Texture(Gdx.files.internal("img/spawner1.png")));
            } else if ((counter/10)%2 == 1) {
                base.setTexture(new Texture(Gdx.files.internal("img/spawner2.png")));
            }
        } else {
            base.setTexture(new Texture(Gdx.files.internal("img/spawner.png")));
            if(GameScreen.left > 0) {
                if (TankManager.tanks.size == 1) {
                    if (Tools.choose(300)) {
                        GameScreen.left--;
                        spawning = true;
                        counter = 80;
                        path = BlockManager.generatePath(x, y);
                    }
                }
                if (Tools.choose(1000)) {
                    GameScreen.left--;
                    spawning = true;
                    counter = 80;
                    path = BlockManager.generatePath(x, y);
                }
            }
        }
        batch.begin();
        base.draw(batch);
        batch.end();
    }

    public boolean collideBullet(Vector2 r1p1, Vector2 r1p2, int direction) {
        return false;
    }

    public void spawn() {
        spawning = true;
        counter = 80;
    }
}

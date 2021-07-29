package me.andyli.battlecity.blocks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import me.andyli.battlecity.utility.Pair;
import me.andyli.battlecity.Constants;
import me.andyli.battlecity.screens.GameScreen;
import me.andyli.battlecity.utility.Tools;

import java.util.ArrayList;

public class Spawner extends Block {

    private Sprite base;
    private boolean spawning = false;
    public int counter, tankType, direction;
    ArrayList<Pair> path;
    private boolean powerup;

    public Spawner(Vector2 position, int i, int j) {
        super(position, i, j);

        base = new Sprite(new Texture(Gdx.files.internal("img/spawner.png")));
        base.setPosition(position.x+1, position.y+1);
    }

    public void update(SpriteBatch batch) {
        //SPAWNING---------------------------------------------------------------------------------------------------------
        if(spawning) {
            //coundown to spawn
            counter--;
            if (counter == 0) {
                //SPAWN-----------------------------------
                //one less tank in level
                GameScreen.left2--;

                //generate path for tank
                if(Constants.MODE == 1) {
                    path = BlockManager.getPath(x, y);
                }

                if(Constants.MODE == 1) {
                    direction = 0;
                } else {
                    if(y == 0) {
                        direction = Tools.chooseNum(3, 2);
                    } else if (y == 12) {
                        direction = Tools.chooseNum(1, 2);
                    } else {
                        direction = Tools.chooseNum(1, 2, 3);
                    }
                }

                GameScreen.tankManager.addTank(tankType, new Vector2(position.x, position.y), direction, path, powerup);
                spawning = false;
                //-----------------------------------

            //draw based on time (flickering)-----------------------------------
            } else if((counter/10)%2 == 0) {
                base.setTexture(new Texture(Gdx.files.internal("img/spawner1.png")));
            } else if ((counter/10)%2 == 1) {
                base.setTexture(new Texture(Gdx.files.internal("img/spawner2.png")));
            }
            //----------------------------------------------------------------------

        //----------------------------------------------------------------------------------------------------------------
        } else {
            base.setTexture(new Texture(Gdx.files.internal("img/spawner.png")));
        }
        
        batch.begin();
        base.draw(batch);
        batch.end();
    }

    public boolean collideBullet(Vector2 r1p1, Vector2 r1p2, int direction, float damage) {
        return false;
    }

    public boolean collideTank(Vector2 r1p1, Vector2 r1p2) {
        return Tools.collide(r1p1, r1p2, position, position2);
    }

    public void spawn(int tankType, boolean powerup) {
        //spawn tank
        spawning = true;
        counter = 80;
        this.powerup = powerup;
        this.tankType = tankType;
    }
}

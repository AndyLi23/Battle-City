package me.andyli.battlecity.blocks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import me.andyli.battlecity.utility.Tools;

public class Powerup extends Block{
    private Sprite base;
    public int type;
    public int left;

    public Powerup(int type) {

        super(new Vector2(0, 0), 0, 0);


        while(true) {
            position = new Vector2(Tools.random(586), Tools.random(586));
            position2 = new Vector2(position.x + 39, position.y + 39);

            boolean good = true;
            for(int i = 0; i < 13; ++i) {
                for(int j = 0; j < 13; ++j) {
                    if(BlockManager.arr[i][j] != null && (BlockManager.arr[i][j].collideTank(position, position2) && (BlockManager.arr[i][j] instanceof Water || BlockManager.arr[i][j] instanceof Iron || BlockManager.arr[i][j] instanceof Spawner || BlockManager.arr[i][j] instanceof Flag))) {
                        good = false;
                        break;
                    }
                }
            }

            if(good) {
                break;
            }
        }

        this.type = type;

        left = 300+ Tools.random(200);

        if(type == 0) {
            base = new Sprite(new Texture(Gdx.files.internal("img/helmet.png")));
        } else if(type == 1) {
            base = new Sprite(new Texture(Gdx.files.internal("img/star.png")));
        } else if(type == 2) {
            base = new Sprite(new Texture(Gdx.files.internal("img/shovel.png")));
        } else if(type == 3) {
            base = new Sprite(new Texture(Gdx.files.internal("img/tankp.png")));
        }

        base.setPosition(position.x, position.y);

    }

    public void update(SpriteBatch batch) {
        left--;

        if(left == 0) {
            BlockManager.deletePowerup(this);
        }

        batch.begin();
        base.draw(batch);
        batch.end();
    }

}

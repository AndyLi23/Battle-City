package me.andyli.battlecity.blocks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class BlockManager {
    public static Block[][] arr = new Block[13][13];

    public BlockManager(String[] arr) {
        for(int i = 0; i < 13; ++i) {
            for(int j = 0; j < 13; ++j) {
                if(arr[i].charAt(j) == 'B') {
                    this.arr[i][j] = new Brick(new Vector2(j*48, (12-i)*48), 0);
                }
            }
        }
    }

    public void update(SpriteBatch batch) {
        for(Block[] c : arr) {
            for(Block b : c) {
                if(b != null) {
                    b.update(batch);
                }
            }
        }
    }
}

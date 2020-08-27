package me.andyli.battlecity.blocks;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.DelayedRemovalArray;
import me.andyli.battlecity.tanks.Bullet;

public class BlockManager {
    public static Block[][] arr = new Block[13][13];

    public BlockManager(String[] arr) {
        for(int i = 0; i < 13; ++i) {
            for(int j = 0; j < 13; ++j) {
                if(arr[i].charAt(j) == 'B') {
                    BlockManager.arr[i][j] = new Brick(new Vector2(j*48, (12-i)*48), 0, i, j);
                } else if(arr[i].charAt(j) == 'I') {
                    BlockManager.arr[i][j] = new Iron(new Vector2(j*48, (12-i)*48), i, j);
                } else if(arr[i].charAt(j) == 'W') {
                    BlockManager.arr[i][j] = new Water(new Vector2(j*48, (12-i)*48), i, j);
                } else if(arr[i].charAt(j) == 'C') {
                    BlockManager.arr[i][j] = new Ice(new Vector2(j*48, (12-i)*48), i, j);
                } else if(arr[i].charAt(j) == 'G') {
                    BlockManager.arr[i][j] = new Grass(new Vector2(j*48, (12-i)*48), i, j);
                } else if(arr[i].charAt(j) == 'X') {
                    BlockManager.arr[i][j] = new Flag(new Vector2(j*48, (12-i)*48), i, j);
                } else if(arr[i].charAt(j) == 'S') {
                    BlockManager.arr[i][j] = new Spawner(new Vector2(j*48, (12-i)*48), i, j);
                }
            }
        }
    }

    public void update(SpriteBatch batch) {
        for(Block[] c : arr) {
            for(Block b : c) {
                if(b != null && !(b instanceof Ice || b instanceof Water)) {
                    b.update(batch);
                }
            }
        }
    }

    public void updateGround(SpriteBatch batch) {
        for(Block[] c : arr) {
            for(Block b : c) {
                if(b instanceof Ice || b instanceof Water) {
                    b.update(batch);
                }
            }
        }
    }
}

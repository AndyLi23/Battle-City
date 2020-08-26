package me.andyli.battlecity.tanks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import me.andyli.battlecity.blocks.Block;
import me.andyli.battlecity.blocks.BlockManager;
import me.andyli.battlecity.utility.Tools;

public class Player {

    public Vector2 position;
    public Sprite base;
    public int direction = 0;
    public int speed = 2;

    public Player(Vector2 position) {
        this.position = position;

        base = new Sprite(new Texture(Gdx.files.internal("img/tank1.png")));

    }

    public void update(SpriteBatch batch) {
        takeInput();

        base.setPosition(position.x, position.y);
        base.rotate(direction*90 - base.getRotation());

        batch.begin();
        base.draw(batch);
        batch.end();
    }

    public boolean collide(int direction) {
        int y1 = 12-((int) position.y / 48);
        int y2 = 12-((int) (position.y+39)/48);
        int x1 = (int) position.x / 48;
        int x2 = (int) (position.x+39)/48;

        Vector2 r1p1 = new Vector2(position.x, position.y);
        Vector2 r1p2 = new Vector2(position.x+39, position.y+39);

        if(y1 == -1 || x1 == 13 || y2 == -1 || x2 == 13) {
            return false;
        }

        if(direction == 0) {
            for(int i = x1; i <= x2; ++i) {
                if(BlockManager.arr[y2][i] != null && Tools.collide(r1p1, r1p2, BlockManager.arr[y2][i].position, BlockManager.arr[y2][i].position2)) {
                    return true;
                }
            }
        } else if(direction == 2) {
            for(int i = x1; i <= x2; ++i) {
                if(BlockManager.arr[y1][i] != null && Tools.collide(r1p1, r1p2, BlockManager.arr[y1][i].position, BlockManager.arr[y1][i].position2)) {
                    return true;
                }
            }
        } else if (direction == 1) {
            for(int i = y1; i <= y2; ++i) {
                if(BlockManager.arr[i][x1] != null && Tools.collide(r1p1, r1p2, BlockManager.arr[i][x1].position, BlockManager.arr[i][x1].position2)) {
                    return true;
                }
            }
        } else if (direction == 3) {
            for(int i = y1; i <= y2; ++i) {
                if(BlockManager.arr[i][x2] != null && Tools.collide(r1p1, r1p2, BlockManager.arr[i][x2].position, BlockManager.arr[i][x2].position2)) {
                    return true;
                }
            }
        }

        

        return false;
    }

    public void takeInput() {
        if(Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) || Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT)) {
            speed = 1;
        } else {
            speed = 2;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) {
            direction = 0;
            position.y += speed;
            if(collide(0)) {
                position.y -= speed;
            }
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) {
            direction = 2;
            position.y -= speed;
            if(collide(2)) {
                position.y += speed;
            }
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
            direction = 3;
            position.x += speed;
            if(collide(3)) {
                position.x -= speed;
            }
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
            direction = 1;
            position.x -= speed;
            if(collide(1)) {
                position.x += speed;
            }
        }

        if(position.x < 0) {
            position.x = 0;
        }
        if(position.x > 585) {
            position.x = 585;
        }
        if(position.y < 0) {
            position.y = 0;
        }
        if(position.y > 585) {
            position.y = 585;
        }

    }
}

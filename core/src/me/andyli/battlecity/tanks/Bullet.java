package me.andyli.battlecity.tanks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import me.andyli.battlecity.blocks.BlockManager;

public class Bullet {

    public Vector2 position, speed, r1p1, r1p2;
    public Sprite base;
    public int direction;
    int x1, x2, y1, y2;

    public Bullet(Vector2 position, Vector2 speed, int direction) {
        this.position = position;
        this.speed = speed;
        this.direction = direction;

        base = new Sprite(new Texture(Gdx.files.internal("img/bullet.png")));
        base.setCenter(position.x + base.getWidth()/2, position.y + base.getHeight()/2);
        base.rotate(direction*90);
    }

    public void update(SpriteBatch batch) {
        position.add(this.speed);

        if(collide(direction)) {
            TankManager.bullets.removeValue(this, true);
        }

        base.setCenter(position.x + base.getWidth()/2, position.y + base.getHeight()/2);

        batch.begin();
        base.draw(batch);
        batch.end();
    }

    public boolean collide(int direction) {
        if(direction == 0 || direction == 2) {
            y1 = 12 - ((int) position.y / 48);
            y2 = 12 - ((int) (position.y + 4) / 48);
            x1 = (int) position.x / 48;
            x2 = (int) (position.x + 3) / 48;

            r1p1 = new Vector2(position.x, position.y);
            r1p2 = new Vector2(position.x+3, position.y+4);
        } else {
            y1 = 12 - ((int) position.y / 48);
            y2 = 12 - ((int) (position.y + 3) / 48);
            x1 = (int) position.x / 48;
            x2 = (int) (position.x + 4) / 48;

            r1p1 = new Vector2(position.x, position.y);
            r1p2 = new Vector2(position.x+4, position.y+3);
        }


        if(y2 == -1 || x1 == 13 || y1 == -1 || x2 == 13 || y2 == 13 || x1 == -1 || y1 == 13 || x2 == -1) {
            return true;
        }

        if(direction == 0) {
            for(int i = x1; i <= x2; ++i) {
                if(BlockManager.arr[y2][i] != null && BlockManager.arr[y2][i].collideBullet(r1p1,r1p2, direction)) {
                    return true;
                }
            }
        } else if(direction == 2) {
            for(int i = x1; i <= x2; ++i) {
                if(BlockManager.arr[y1][i] != null && BlockManager.arr[y1][i].collideBullet(r1p1,r1p2, direction)) {
                    return true;
                }
            }
        } else if (direction == 1) {
            for(int i = y2; i <= y1; ++i) {
                if(BlockManager.arr[i][x1] != null && BlockManager.arr[i][x1].collideBullet(r1p1,r1p2, direction)) {
                    return true;
                }
            }
        } else if (direction == 3) {
            for(int i = y2; i <= y1; ++i) {
                if(BlockManager.arr[i][x2] != null && BlockManager.arr[i][x2].collideBullet(r1p1,r1p2, direction)) {
                    return true;
                }
            }
        }



        return false;
    }
}

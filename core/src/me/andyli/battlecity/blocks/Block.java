package me.andyli.battlecity.blocks;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import me.andyli.battlecity.utility.Tools;

public class Block {
    public Vector2 position;
    public Vector2 position2;
    public int x,y;

    public Block(Vector2 position,int x,int y) {
        this.position = position;
        this.position2 = new Vector2(position.x + 48, position.y + 48);
        this.x = x;
        this.y = y;

    }

    public void update(SpriteBatch batch) {
    }

    public boolean collideTank(Vector2 r1p1, Vector2 r1p2) {
        return Tools.collide(r1p1, r1p2, position, position2);
    }

    public boolean collideBullet(Vector2 r1p1, Vector2 r1p2, int direction) {
        return Tools.collide(r1p1, r1p2, position, position2);
    }
}

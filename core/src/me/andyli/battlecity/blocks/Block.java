package me.andyli.battlecity.blocks;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Block {
    public Vector2 position;
    public Vector2 position2;
    public Block(Vector2 position) {
        this.position = position;
        this.position2 = new Vector2(position.x + 48, position.y + 48);

    }

    public void update(SpriteBatch batch) {}
}

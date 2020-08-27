package me.andyli.battlecity.blocks;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import me.andyli.battlecity.screens.GameScreen;
import me.andyli.battlecity.utility.Tools;

public class Flag extends Block {

    private Sprite base;
    private int health;

    public Flag(Vector2 position, int i, int j) {
        super(position, i, j);

        base = new Sprite(new Texture(Gdx.files.internal("img/flag.png")));
        base.setPosition(position.x, position.y);

        health = 1;

    }

    public void update(SpriteBatch batch) {
        batch.begin();
        base.draw(batch);
        batch.end();
    }

    public boolean collideBullet(Vector2 r1p1, Vector2 r1p2, int direction) {
        health--;
        if(health == 0) {
            BlockManager.arr[x][y] = null;
            GameScreen.gameOver();
        }
        return Tools.collide(r1p1, r1p2, position, position2);
    }

}

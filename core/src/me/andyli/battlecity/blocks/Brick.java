package me.andyli.battlecity.blocks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Brick extends Block {
    private Sprite[][] sprites;
    public Brick(Vector2 position, int half) {
        super(position);

        sprites = new Sprite[4][4];

        for(int i = 0; i < 4; ++i) {
            for(int j = 0; j < 4; ++j) {
                sprites[i][j] = new Sprite(new Texture(Gdx.files.internal("img/brick.png")));
                sprites[i][j].setPosition(position.x + i*12, position.y + j*12);
            }
        }
    }

    public void update(SpriteBatch batch) {
        batch.begin();
        for(int i = 0; i < 4; ++i) {
            for(int j = 0; j < 4; ++j) {
                sprites[i][j].draw(batch);
            }
        }
        batch.end();
    }
}

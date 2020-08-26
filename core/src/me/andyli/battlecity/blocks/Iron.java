package me.andyli.battlecity.blocks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Iron extends Block {

    private Sprite base;

    public Iron(Vector2 position, int i, int j) {
        super(position, i, j);

        base = new Sprite(new Texture(Gdx.files.internal("img/iron.png")));
        base.setPosition(position.x, position.y);

    }

    public void update(SpriteBatch batch) {
        batch.begin();
        base.draw(batch);
        batch.end();
    }

}

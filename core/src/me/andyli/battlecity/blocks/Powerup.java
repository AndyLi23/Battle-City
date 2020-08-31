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

    public Powerup(Vector2 position, int type) {
        super(position, 0, 0);
        this.position2 = new Vector2(position.x + 39, position.y + 39);

        this.type = type;

        left = Tools.random(500);

        if(type == 0) {
            base = new Sprite(new Texture(Gdx.files.internal("img/helmet.png")));
        } else if(type == 1) {
            base = new Sprite(new Texture(Gdx.files.internal("img/star.png")));
        } else if(type == 2) {
            base = new Sprite(new Texture(Gdx.files.internal("img/shovel.png")));
        }

        base.setPosition(position.x, position.y);

    }

    public void update(SpriteBatch batch) {
        left--;

        if(left == 0) {
            BlockManager.powerups.removeValue(this, true);
        }

        batch.begin();
        base.draw(batch);
        batch.end();
    }

}

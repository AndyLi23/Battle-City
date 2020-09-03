package me.andyli.battlecity.blocks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import me.andyli.battlecity.utility.Tools;

public class Iron extends Block {

    //2x2 array of sprites
    private Sprite[][] sprites;

    public Iron(Vector2 position, int x, int y, String direction) {
        super(position, x, y);

        //INIT SPRITES---------------------------------------------------------------------------------------------------------
        sprites = new Sprite[2][2];

        for(int i = 0; i < 2; ++i) {
            for(int j = 0; j < 2; ++j) {
                sprites[i][j] = new Sprite(new Texture(Gdx.files.internal("img/iron.png")));
                sprites[i][j].setPosition(position.x + i*24, position.y + j*24);
            }
        }

        //delete based on which half

        if(direction.equals("[") || direction.equals("/") || direction.equals("!") || direction.equals("#") || direction.equals("@")) {
            sprites[0][0] = null;
        }

        if(direction.equals("]") || direction.equals("/") || direction.equals("!") || direction.equals("$") || direction.equals("@")) {
            sprites[0][1] = null;
        }

        if(direction.equals("[") || direction.equals("\\") || direction.equals("!") || direction.equals("$") || direction.equals("#")) {
            sprites[1][0] = null;
        }

        if(direction.equals("]") || direction.equals("\\") || direction.equals("$") || direction.equals("#") || direction.equals("@")) {
            sprites[1][1] = null;
        }
        //---------------------------------------------------------------------------------------------------------------------

    }

    public void update(SpriteBatch batch) {
        //draw each sprite
        batch.begin();
        for(int i = 0; i < 2; ++i) {
            for(int j = 0; j < 2; ++j) {
                if(sprites[i][j] != null) {
                    sprites[i][j].draw(batch);
                }
            }
        }
        batch.end();
    }

    public boolean collideTank(Vector2 r1p1, Vector2 r1p2) {
        //check for any collisions
        for(int i = 0; i < 2; ++i) {
            for (int j = 0; j < 2; ++j) {
                if(sprites[i][j] != null && Tools.collide(r1p1, r1p2, new Vector2(position.x + i*24, position.y + j*24), new Vector2(position.x + i*24+24, position.y + j*24+24))) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean collideBullet(Vector2 r1p1, Vector2 r1p2, int direction) {
        //collide but don't delete
        for(int i = 0; i < 2; ++i) {
            for (int j = 0; j < 2; ++j) {
                if(sprites[i][j] != null && Tools.collide(r1p1, r1p2, new Vector2(position.x + i*24, position.y + j*24), new Vector2(position.x + i*24+24, position.y + j*24+24))) {
                    return true;
                }
            }
        }
        return false;
    }

}

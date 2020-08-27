package me.andyli.battlecity.blocks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import me.andyli.battlecity.utility.Tools;

public class Brick extends Block {
    private Sprite[][] sprites;
    public Brick(Vector2 position, int half, int x, int y) {
        super(position, x, y);

        sprites = new Sprite[4][4];

        for(int i = 0; i < 4; ++i) {
            for(int j = 0; j < 4; ++j) {
                if((i % 2 == 0 && j % 2 == 0)||(i % 2 == 1 && j % 2 == 1)) {
                    sprites[i][j] = new Sprite(new Texture(Gdx.files.internal("img/brick1.png")));
                } else {
                    sprites[i][j] = new Sprite(new Texture(Gdx.files.internal("img/brick2.png")));
                }
                sprites[i][j].setPosition(position.x + i*12, position.y + j*12);
            }
        }
    }

    public void update(SpriteBatch batch) {
        batch.begin();
        for(int i = 0; i < 4; ++i) {
            for(int j = 0; j < 4; ++j) {
                if(sprites[i][j] != null) {
                    sprites[i][j].draw(batch);
                }
            }
        }
        batch.end();
    }

    public boolean collideTank(Vector2 r1p1, Vector2 r1p2) {
        for(int i = 0; i < 4; ++i) {
            for (int j = 0; j < 4; ++j) {
                if(sprites[i][j] != null && Tools.collide(r1p1, r1p2, new Vector2(position.x + i*12, position.y + j*12), new Vector2(position.x + i*12+12, position.y + j*12+12))) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean collideBullet(Vector2 r1p1, Vector2 r1p2, int direction) {
        for(int i = 0; i < 4; ++i) {
            for (int j = 0; j < 4; ++j) {
                if(sprites[i][j] != null && Tools.collide(r1p1, r1p2, new Vector2(position.x + i*12, position.y + j*12), new Vector2(position.x + i*12+12, position.y + j*12+12))) {
                    if(direction == 1 || direction == 3) {
                        if(j == 0) {
                            sprites[i][0] = null;
                            sprites[i][1] = null;
                            if(BlockManager.arr[x+1][y] != null && BlockManager.arr[x+1][y] instanceof Brick) {
                                ((Brick) BlockManager.arr[x+1][y]).sprites[i][3] = null;
                                ((Brick) BlockManager.arr[x+1][y]).sprites[i][2] = null;
                            }
                        } else if(j == 3) {
                            sprites[i][3] = null;
                            sprites[i][2] = null;
                            if(BlockManager.arr[x-1][y] != null && BlockManager.arr[x-1][y] instanceof Brick) {
                                ((Brick) BlockManager.arr[x-1][y]).sprites[i][1] = null;
                                ((Brick) BlockManager.arr[x-1][y]).sprites[i][0] = null;
                            }

                        } else {
                            for (int k = 0; k < 4; ++k) {
                                sprites[i][k] = null;
                            }
                        }
                    }
                    if(direction == 0 || direction == 2) {
                        if(i == 0) {
                            sprites[0][j] = null;
                            sprites[1][j] = null;
                            if(BlockManager.arr[x][y-1] != null && BlockManager.arr[x][y-1] instanceof Brick) {
                                ((Brick) BlockManager.arr[x][y-1]).sprites[3][j] = null;
                                ((Brick) BlockManager.arr[x][y-1]).sprites[2][j] = null;
                            }
                        } else if(i == 3) {
                            sprites[2][j] = null;
                            sprites[3][j] = null;
                            if(BlockManager.arr[x][y+1] != null && BlockManager.arr[x][y+1] instanceof Brick) {
                                ((Brick) BlockManager.arr[x][y+1]).sprites[1][j] = null;
                                ((Brick) BlockManager.arr[x][y+1]).sprites[0][j] = null;
                            }
                        } else {
                            for (int k = 0; k < 4; ++k) {
                                sprites[k][j] = null;
                            }
                        }
                    }
                    return true;
                }
            }
        }
        return false;
    }

}

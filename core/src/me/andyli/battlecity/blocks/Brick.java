package me.andyli.battlecity.blocks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import me.andyli.battlecity.utility.Tools;

public class Brick extends Block {
    //16 sprites (to make deleting easier)
    private Sprite[][] sprites;

    public Brick(Vector2 position, int half, int x, int y) {
        super(position, x, y);


        //GENERATE INITIAL---------------------------------------------------------------------
        sprites = new Sprite[4][4];

        //init boundaries
        int i1 = 0;
        int j1 = 0;
        int i2 = 4;
        int j2 = 4;

        //update boundaries for initial state
        if(half == 1 || half == 5|| half == 8) {
            i1 = 2;
        }
        if (half == 3 || half == 6|| half == 7) {
            i2 = 2;
        }
        if (half == 2 || half == 7|| half == 8) {
            j1 = 2;
        }
        if (half == 4 || half == 5 || half == 6) {
            j2 = 2;
        }

        //fill in sprites
        for(int i = i1; i < i2; ++i) {
            for(int j = j1; j < j2; ++j) {
                if((i % 2 == 0 && j % 2 == 0)||(i % 2 == 1 && j % 2 == 1)) {
                    sprites[i][j] = new Sprite(new Texture(Gdx.files.internal("img/brick1.png")));
                } else {
                    sprites[i][j] = new Sprite(new Texture(Gdx.files.internal("img/brick2.png")));
                }
                sprites[i][j].setPosition(position.x + i*12, position.y + j*12);
            }
        }
        //--------------------------------------------------------------------------------------------
    }

    public void update(SpriteBatch batch) {
        //render each sprite
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
        //check if any sprite is colliding
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
        //BULLET COLLISION------------------------------------------------------------------------------------------------------------------------------------------
        for(int i = 0; i < 4; ++i) {
            for (int j = 0; j < 4; ++j) {
                //bullet hit this sprite
                if(sprites[i][j] != null && Tools.collide(r1p1, r1p2, new Vector2(position.x + i*12, position.y + j*12), new Vector2(position.x + i*12+12, position.y + j*12+12))) {
                    if(direction == 1 || direction == 3) {
                        if(j == 0) {
                            //delete this half (hit two on sides)
                            sprites[i][0] = null;
                            sprites[i][1] = null;

                            //delete connected bricks if they exist
                            if(BlockManager.arr[x+1][y] != null && BlockManager.arr[x+1][y] instanceof Brick) {
                                ((Brick) BlockManager.arr[x+1][y]).sprites[i][3] = null;
                                ((Brick) BlockManager.arr[x+1][y]).sprites[i][2] = null;
                            }
                        } else if(j == 3) {
                            //delete this half (hit two on sides)
                            sprites[i][3] = null;
                            sprites[i][2] = null;

                            //delete connected bricks if they exist
                            if(BlockManager.arr[x-1][y] != null && BlockManager.arr[x-1][y] instanceof Brick) {
                                ((Brick) BlockManager.arr[x-1][y]).sprites[i][1] = null;
                                ((Brick) BlockManager.arr[x-1][y]).sprites[i][0] = null;
                            }

                        } else {
                            //delete entire layer (hit the middle two)
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

                    //if all sprites are null, delete this brick
                    if(!check()) {
                        BlockManager.arr[x][y]=null;
                    }

                    return true;
                }
            }
        }
        return false;
        //-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    }

    public boolean check() {
        //check if any sprites exist
        for(int i = 0; i < 4; ++i) {
            for(int j = 0; j < 4; ++j) {
                if(sprites[i][j] != null) {
                    return true;
                }
            }
        }
        return false;
    }

}

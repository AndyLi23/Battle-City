package me.andyli.battlecity.tanks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import me.andyli.battlecity.blocks.BlockManager;
import me.andyli.battlecity.utility.Tools;

public class Bullet {

    public Vector2 position, speed, r1p1, r1p2, r2p2;
    public Sprite base;
    public int direction;
    public float damage;
    private int x1, x2, y1, y2;
    public Tank parent;

    public Bullet(Vector2 position, Vector2 speed, int direction, Tank parent, float damage) {
        //initialize
        this.speed = speed;
        this.direction = direction;
        this.parent = parent;
        this.damage = damage;


        //sprite
        if(parent.gunType == 5) {
            base = new Sprite(new Texture(Gdx.files.internal("img/bullet1.png")));
        } else if (parent.gunType == 1 || parent.gunType == 4) {
            base = new Sprite(new Texture(Gdx.files.internal("img/bullet2.png")));
        } else {
            base = new Sprite(new Texture(Gdx.files.internal("img/bullet.png")));
        }

        this.position = position.sub(new Vector2(base.getWidth()/2, base.getHeight()/2));

        base.setCenter(position.x + base.getWidth()/2, position.y + base.getHeight()/2);
        base.rotate(direction*90);
    }

    public void update(SpriteBatch batch) {
        //update position
        position.add(this.speed);

        //out of box
        if(position.x <= 4 || position.x >= 610 || position.y <= 4 || position.y >= 610) {
            TankManager.explosions.add(new Explosion(new Vector2(position.x + base.getWidth()/2, position.y + base.getHeight()/2), 1f, 0.3f));
            TankManager.bullets.removeValue(this, true);
        } else {

            //if collide
            if (collideTank() || collideBullet() || collide(direction)) {
                TankManager.bullets.removeValue(this, true);
            }

            //update sprite position, draw
            base.setCenter(position.x + base.getWidth() / 2, position.y + base.getHeight() / 2);

            batch.begin();
            base.draw(batch);
            batch.end();
        }
    }

    public boolean collideTank() {
        r1p1 = new Vector2(position.x, position.y);
        r1p2 = getPosition();

        //iterate through tanks
        for(Tank t : TankManager.tanks) {
            if(!t.equals(parent)) {
                //other edge of tank
                r2p2 = new Vector2(t.position.x+39, t.position.y+39);

                //if collide
                if(Tools.collide(r1p1, r1p2, t.position, r2p2)) {
                    if(t instanceof Player || parent instanceof Player) {
                        //player on player
                        if(t instanceof Player && parent instanceof Player) {
                            //freeze the hit player
                            TankManager.explosions.add(new Explosion(new Vector2(position.x + base.getWidth() / 2, position.y + base.getHeight() / 2), 1.5f, 0.3f));
                            t.freeze();
                        } else {
                            //subtract from tank health, explode
                            TankManager.explosions.add(new Explosion(new Vector2(position.x + base.getWidth() / 2, position.y + base.getHeight() / 2), 1.5f, 0.3f));
                            if (t.invulnerable == 0) {
                                t.health-=damage;
                            }
                        }
                    }
                    return true;
                }
            }
        }
        return false;
    }

    public Vector2 getPosition() {
        if(direction == 0 || direction == 2) {
            return new Vector2(position.x+base.getWidth(), position.y+base.getHeight());
        } else {
            return new Vector2(position.x+base.getHeight(), position.y+base.getWidth());
        }
    }

    public boolean collideBullet() {
        //own position
        r1p1 = new Vector2(position.x, position.y);
        r1p2 = getPosition();


        for(int i = 0; i < TankManager.bullets.size; ++i) {
            if(!TankManager.bullets.get(i).equals(this) && !(TankManager.bullets.get(i).parent.equals(parent))) {
                //other bullet position
                r2p2 = TankManager.bullets.get(i).getPosition();

                if (Tools.collide(r1p1, r1p2, TankManager.bullets.get(i).position, r2p2)) {
                    //two bullets hit
                    TankManager.explosions.add(new Explosion(new Vector2(position.x + base.getWidth()/2, position.y + base.getHeight()/2), 0.8f, 0.2f));
                    TankManager.bullets.removeIndex(i);
                    return true;
                }
            }
        }
        return false;
    }

    public boolean collide(int direction) {
        //collide with blocks

        //get positions----------------------------
        if(direction == 0 || direction == 2) {
            y1 = 12 - ((int) position.y / 48);
            y2 = 12 - ((int) (position.y + 12) / 48);
            x1 = (int) position.x / 48;
            x2 = (int) (position.x + 9) / 48;
        } else {
            y1 = 12 - ((int) position.y / 48);
            y2 = 12 - ((int) (position.y + 9) / 48);
            x1 = (int) position.x / 48;
            x2 = (int) (position.x + 12) / 48;
        }


        r1p1 = new Vector2(position.x, position.y);
        r1p2 = getPosition();
        //------------------------------------------


        //check for hit------------------------------------------
        if(direction == 0) {
            for(int i = x1; i <= x2; ++i) {
                if(BlockManager.arr[y2][i] != null && BlockManager.arr[y2][i].collideBullet(r1p1,r1p2, direction, damage)) {
                    TankManager.explosions.add(new Explosion(new Vector2(position.x + base.getWidth()/2, position.y + base.getHeight()/2), 1f, 0.3f));
                    return true;
                }
            }
        } else if(direction == 2) {
            for(int i = x1; i <= x2; ++i) {
                if(BlockManager.arr[y1][i] != null && BlockManager.arr[y1][i].collideBullet(r1p1,r1p2, direction, damage)) {
                    TankManager.explosions.add(new Explosion(new Vector2(position.x + base.getWidth()/2, position.y + base.getHeight()/2), 1f, 0.3f));
                    return true;
                }
            }
        } else if (direction == 1) {
            for(int i = y2; i <= y1; ++i) {
                if(BlockManager.arr[i][x1] != null && BlockManager.arr[i][x1].collideBullet(r1p1,r1p2, direction, damage)) {
                    TankManager.explosions.add(new Explosion(new Vector2(position.x + base.getWidth()/2, position.y + base.getHeight()/2), 1f, 0.3f));
                    return true;
                }
            }
        } else if (direction == 3) {
            for(int i = y2; i <= y1; ++i) {
                if(BlockManager.arr[i][x2] != null && BlockManager.arr[i][x2].collideBullet(r1p1,r1p2, direction, damage)) {
                    TankManager.explosions.add(new Explosion(new Vector2(position.x + base.getWidth()/2, position.y + base.getHeight()/2), 1f, 0.3f));
                    return true;
                }
            }
        }
        //--------------------------------------------------------



        return false;
    }
}

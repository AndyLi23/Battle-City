package me.andyli.battlecity.tanks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import me.andyli.battlecity.blocks.Block;
import me.andyli.battlecity.blocks.BlockManager;
import me.andyli.battlecity.blocks.Ice;
import me.andyli.battlecity.utility.Tools;

public class Tank {
    public int direction, cooldown, cd, health;
    public float speed;
    public Vector2 position, vel;
    public Sprite base;
    private int[] list;
    private int countdown;

    public Tank(Vector2 position, float speed, int direction, Sprite base, int cd, int health) {
        this.position = position;
        this.speed = speed;
        this.direction = direction;
        this.vel = new Vector2(0, 0);
        updateVel();
        this.cd = cd;

        this.health = health;

        list = new int[]{0,1,2,3};

        this.base = base;
        base.setPosition(position.x, position.y);
        countdown = 40;
    }

    public void update(SpriteBatch batch) {

        if(countdown > 0) {
            countdown--;
        }

        if(health == 0) {
            TankManager.explosions.add(new Explosion(new Vector2(position.x + base.getWidth()/2, position.y + base.getHeight()/2), 2f, 0.2f));
            TankManager.tanks.removeValue(this, true);
            if(this instanceof Player) {
                TankManager.addTank(new Player(new Vector2(0, 0)));
            }
        } else {

            if(speed == 0.8f) {
                base.setTexture(new Texture(Gdx.files.internal("img/tank3"+health+".png")));
            }

            if (cooldown != 0) {
                cooldown--;
            }

            takeInput();

            updateVel();

            position.add(vel);

            if ((collideTank() || (collide(direction) != null && !(collide(direction) instanceof Ice)))) {
                position.sub(vel);
            }

            if (position.x < 0) {
                position.x = 0;
            }
            if (position.x > 585) {
                position.x = 585;
            }
            if (position.y < 0) {
                position.y = 0;
            }
            if (position.y > 585) {
                position.y = 585;
            }

            base.setPosition(position.x, position.y);
            base.rotate(direction * 90 - base.getRotation());

            batch.begin();
            base.draw(batch);
            batch.end();
        }
    }

    public void updateVel() {
        if(direction == 0) {
            vel.x = 0;
            vel.y = speed;
        }
        if(direction == 2) {
            vel.x = 0;
            vel.y = -1*speed;
        }
        if(direction == 3) {
            vel.y = 0;
            vel.x = speed;
        }
        if(direction == 1) {
            vel.y = 0;
            vel.x = -1*speed;
        }
    }

    public Block collide(int direction) {
        int y1 = 12-((int) position.y / 48);
        int y2 = 12-((int) (position.y+39)/48);
        int x1 = (int) position.x / 48;
        int x2 = (int) (position.x+39)/48;

        Vector2 r1p1 = new Vector2(position.x, position.y);
        Vector2 r1p2 = new Vector2(position.x+39, position.y+39);

        if(y1 == -1 || x1 == 13 || y2 == -1 || x2 == 13) {
            return new Block(new Vector2(0, 0), 0, 0);
        }

        if(direction == 0) {
            for(int i = x1; i <= x2; ++i) {
                if(BlockManager.arr[y2][i] != null && BlockManager.arr[y2][i].collideTank(r1p1,r1p2)) {
                    return BlockManager.arr[y2][i];
                }
            }
        } else if(direction == 2) {
            for(int i = x1; i <= x2; ++i) {
                if(BlockManager.arr[y1][i] != null && BlockManager.arr[y1][i].collideTank(r1p1,r1p2)) {
                    return BlockManager.arr[y1][i];
                }
            }
        } else if (direction == 1) {
            for(int i = y2; i <= y1; ++i) {
                if(BlockManager.arr[i][x1] != null && BlockManager.arr[i][x1].collideTank(r1p1,r1p2)) {
                    return BlockManager.arr[i][x1];
                }
            }
        } else if (direction == 3) {
            for(int i = y2; i <= y1; ++i) {
                if(BlockManager.arr[i][x2] != null && BlockManager.arr[i][x2].collideTank(r1p1,r1p2)) {
                    return BlockManager.arr[i][x2];
                }
            }
        }



        return null;
    }

    public boolean collideTank() {
        Vector2 r1p1 = new Vector2(position.x, position.y);
        Vector2 r1p2 = new Vector2(position.x+39, position.y+39);

        for(int i = 0; i < TankManager.tanks.size; ++i) {
            if(!TankManager.tanks.get(i).equals(this)) {
                Vector2 r2p2 = new Vector2(TankManager.tanks.get(i).position.x + 39, TankManager.tanks.get(i).position.y + 39);
                if(Tools.collide(r1p1, r1p2, TankManager.tanks.get(i).position, r2p2)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void fire() {
        if(cooldown == 0) {
            Vector2 temp;
            if (direction == 0) {
                temp = new Vector2(0, 10);
            } else if (direction == 2) {
                temp = new Vector2(0, -10);
            } else if (direction == 1) {
                temp = new Vector2(-10, 0);
            } else {
                temp = new Vector2(10, 0);
            }
            Bullet b = new Bullet(new Vector2(position.x + 15, position.y + 13), temp, direction, this);
            TankManager.bullets.add(b);
            cooldown = cd;
        }
    }

    public void takeInput() {
        if(countdown == 0) {
            if (Tools.choose(60)) {
                direction = Tools.selectRandom(list);
            }

            if (Tools.choose(10)) {
                fire();
            }
        }
    }
}

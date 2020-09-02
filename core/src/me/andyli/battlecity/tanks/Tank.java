package me.andyli.battlecity.tanks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import javafx.util.Pair;
import me.andyli.battlecity.blocks.Block;
import me.andyli.battlecity.blocks.BlockManager;
import me.andyli.battlecity.blocks.Ice;
import me.andyli.battlecity.blocks.Powerup;
import me.andyli.battlecity.screens.GameScreen;
import me.andyli.battlecity.utility.Tools;

import java.util.ArrayList;

public class Tank {
    public int direction, cooldown, cd, health, invulnerable, type, countdown, frozen;
    public float speed;
    public Vector2 position, vel;
    public Sprite base;
    private int[] list;
    public boolean powerup;
    private ArrayList<Pair<Integer, Integer>> path;
    private int cur;

    public Tank(Vector2 position, float speed, int direction, Sprite base, int cd, int health, int type, ArrayList<Pair<Integer, Integer>> path) {
        this.position = position;
        this.speed = speed;
        this.direction = direction;
        this.vel = new Vector2(0, 0);
        updateVel();
        this.cd = cd;
        this.type = type;

        this.health = health;

        list = new int[]{0,1,2,3};

        this.base = base;
        base.setPosition(position.x, position.y);
        countdown = 40;
        invulnerable = 0;

        if(!(this instanceof Player) && Tools.choose(4)) {
            powerup = true;
            base.setColor(new Color(1, 0.6f, 0.6f, 1));
        }

        this.path = path;
        this.cur = 0;

    }

    public Pair<Integer, Integer> getXY() {
        int y1 = 12-((int) position.y / 48);
        int x1 = (int) position.x / 48;
        return new Pair<>(y1, x1);
    }

    public void update(SpriteBatch batch) {

        if(frozen > 0) {
            frozen--;
        }

        if(invulnerable > 0) {
            invulnerable--;
        }

        if(countdown > 0) {
            countdown--;
        }

        if(health == 0) {
            TankManager.explosions.add(new Explosion(new Vector2(position.x + base.getWidth()/2, position.y + base.getHeight()/2), 2f, 0.2f));
            TankManager.delete(this);
            if(this instanceof Player) {
                if(((Player) this).player1){
                    TankManager.addTank(new Player(new Vector2(385, 5), true));
                    GameScreen.lives2--;
                } else {
                    TankManager.addTank(new Player(new Vector2(200, 5), false));
                    GameScreen.lives--;
                }
            } else {
                GameScreen.scores[type]++;
            }
            if(powerup) {
                BlockManager.addPowerup(new Powerup(Tools.random(4)));
            }
        } else {

            if(type == 1) {
                base.setTexture(new Texture(Gdx.files.internal("img/tank3"+health+".png")));
                if(powerup) {
                    base.setColor(new Color(1, 0.6f, 0.6f, 1));
                }
            }


            if (cooldown > 0) {
                cooldown--;
            }

            takeInput();

            updateVel();

            if(frozen == 0) {
                position.add(vel);
            }

            if(this instanceof Player) {
                collidePowerup();
            }

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
        if (direction == 0) {
            vel.x = 0;
            vel.y = speed;
        }
        if (direction == 2) {
            vel.x = 0;
            vel.y = -1 * speed;
        }
        if (direction == 3) {
            vel.y = 0;
            vel.x = speed;
        }
        if (direction == 1) {
            vel.y = 0;
            vel.x = -1 * speed;
        }
    }

    public void freeze() {
        frozen = 400;
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

    public void collidePowerup() {
        Vector2 r1p1 = new Vector2(position.x, position.y);
        Vector2 r1p2 = new Vector2(position.x+39, position.y+39);

        for(Powerup p : BlockManager.powerups) {
            if(p.collideTank(r1p1, r1p2)) {
                if(p.type == 3) {
                    if(((Player) this).player1) {
                        GameScreen.lives2++;
                    } else {
                        GameScreen.lives++;
                    }
                } else if (p.type == 0) {
                    invulnerable += 500;
                } else if (p.type == 2) {
                    BlockManager.changeToIron();
                } else {
                    GameScreen.scores[3]++;
                }
                BlockManager.deletePowerup(p);
            }
        }
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
        // RANDOM -------------

        /*if(countdown == 0) {
            if (Tools.choose(60)) {
                direction = Tools.selectRandom(list);
            }

            if (Tools.choose(10)) {
                fire();
            }
        }*/

        // PATH -------------




    }
}

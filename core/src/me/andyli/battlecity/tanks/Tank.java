package me.andyli.battlecity.tanks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import javafx.util.Pair;
import me.andyli.battlecity.Constants;
import me.andyli.battlecity.blocks.Block;
import me.andyli.battlecity.blocks.BlockManager;
import me.andyli.battlecity.blocks.Ice;
import me.andyli.battlecity.blocks.Powerup;
import me.andyli.battlecity.screens.GameScreen;
import me.andyli.battlecity.utility.Tools;

import java.util.ArrayList;

public class Tank {
    public int direction, cooldown, gunType, invulnerable, type, countdown, frozen, cd, cur, mag;
    public float damage, speed, health, origHealth;
    public Vector2 position, vel;
    public Sprite base;
    public boolean powerup;
    private ArrayList<Pair<Integer, Integer>> path;
    public Sprite inv;

    public Tank(Vector2 position, float speed, int direction, Sprite base, int gunType, float health, int type, ArrayList<Pair<Integer, Integer>> path, boolean powerup) {
        //initialize------------------------
        this.position = position;
        this.speed = speed;
        this.direction = direction;
        this.vel = new Vector2(0, 0);
        updateVel();
        this.gunType = gunType;
        this.type = type;

        this.health = health;
        this.origHealth = health;

        this.base = base;
        base.setPosition(position.x, position.y);
        countdown = 40;
        invulnerable = 0;

        this.inv = new Sprite(new Texture(Gdx.files.internal("img/blank.png")));

        //gun types

        if(gunType == 0) {
            cd = 60;
            damage = 1;
        } else if (gunType == 1) {
            cd = 8;
            damage = 0.15f;
            mag = 5;
        } else if (gunType == 2) {
            cd = 40;
            damage = 0.5f;
        } else if (gunType == 3) {
            cd = 40;
            damage = 1;
        } else if (gunType == 4) {
            cd = 5;
            damage = 0.3f;
            mag = 5;
        } else if (gunType == 5) {
            cd = 3;
            damage = 0.1f;
            mag = 20;
        }



        //if powerup
        if(powerup) {
            this.powerup = true;
            //tint red
            base.setColor(new Color(1, 0.6f, 0.6f, 1));
        }

        //path and future grid
        this.path = path;
        this.cur = 1;

        //update direction if given path
        if(path != null) {
            updateDirection(0);
        }
        //------------------------------------
    }

    public void updateDirection(int cur) {
        //update direction based on next grid in path
        if(path.get(cur).getKey() > path.get(cur+1).getKey()) {
            direction = 0;
        }
        if(path.get(cur).getKey() < path.get(cur+1).getKey()) {
            direction = 2;
        }
        if(path.get(cur).getValue() < path.get(cur+1).getValue()) {
            direction = 3;
        }
        if(path.get(cur).getValue() > path.get(cur+1).getValue()) {
            direction = 1;
        }
    }

    public Pair<Integer, Integer> getXY() {
        //get what grid the left corner is in
        int y1 = 12-((int) position.y / 48);
        int x1 = (int) position.x / 48;
        return new Pair<>(y1, x1);
    }

    public Pair<Integer, Integer> getXY2() {
        //get what grid the right corner is in
        int y1 = 12-((int) (position.y+39) / 48);
        int x1 = (int) (position.x+39) / 48;
        return new Pair<>(y1, x1);
    }

    public void update(SpriteBatch batch) {
        //countdown------------
        if(frozen > 0) {
            frozen--;
        }

        if(invulnerable > 0) {
            invulnerable--;
        }

        if(countdown > 0) {
            countdown--;
        }
        //------------------------


        //DIE------------------------------------
        if(health <= 0) {
            //explode
            TankManager.explosions.add(new Explosion(new Vector2(position.x + base.getWidth()/2, position.y + base.getHeight()/2), 2f, 0.2f));
            TankManager.delete(this);

            //spawn new player
            if(this instanceof Player) {
                if(((Player) this).player1){
                    //player 2
                    GameScreen.lives2--;
                    if(GameScreen.lives2 >= 0) {
                        TankManager.addTank(new Player(new Vector2(385, 5), true));
                    }
                } else {
                    //player 1
                    GameScreen.lives--;
                    if(GameScreen.lives >= 0) {
                        TankManager.addTank(new Player(new Vector2(200, 5), false));
                    }
                }
            } else {
                //add to score
                GameScreen.scores[type]++;
            }
            //add powerup
            if(powerup) {
                BlockManager.addPowerup(new Powerup(Tools.random(4)));
            }
        //------------------------------------
        } else {

            //big tanks: change skin based on health
            if(type == 1) {
                base.setTexture(new Texture(Gdx.files.internal("img/tank3"+(int)Math.ceil(health)+".png")));
                if(powerup) {
                    base.setColor(new Color(1, 0.6f, 0.6f, 1));
                }
            }


            //firing cooldown
            if (cooldown > 0) {
                cooldown--;
            }


            //update direction and velocity
            takeInput();

            updateVel();


            //collision mechanics------------------------------------------------


            //check for powerup collisions
            if (this instanceof Player) {
                collidePowerup();
            }

            //already stuck in block
            if((collideTank() || (collide(direction) != null && !(collide(direction) instanceof Ice)))) {
                position.add(vel);

            } else {

                //move position if not frozen
                if (frozen == 0) {
                    position.add(vel);
                }

                //prevent from colliding with block
                if ((collideTank() || (collide(direction) != null && !(collide(direction) instanceof Ice)))) {
                    position.sub(vel);
                }
            }

            //keep in game area
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
            //------------------------------------------------------------------------


            //move sprite
            base.setPosition(position.x, position.y);
            base.rotate(direction * 90 - base.getRotation());

            inv.setPosition(position.x, position.y);
            inv.rotate(direction * 90 - inv.getRotation());

            batch.begin();
            base.draw(batch);
            inv.draw(batch);
            batch.end();


        }
    }

    public void updateHealth(ShapeRenderer renderer) {
        renderer.begin(ShapeRenderer.ShapeType.Filled);


        if(getXY().getKey() == 0) {
            renderer.setColor(0.3f, 0.3f, 0.3f, 1);
            renderer.rect(position.x, position.y - 13, 39, 7);

            if(health/origHealth > 0.7) {
                renderer.setColor(Color.GREEN);
            } else if (health/origHealth < 0.35) {
                renderer.setColor(Color.RED);
            } else {
                renderer.setColor(Color.YELLOW);
            }

            renderer.rect(position.x, position.y - 13, 39 * (health / origHealth), 7);
        } else {
            renderer.setColor(0.3f, 0.3f, 0.3f, 1);
            renderer.rect(position.x, position.y + 45, 39, 7);

            if(health/origHealth > 0.7) {
                renderer.setColor(Color.GREEN);
            } else if (health/origHealth < 0.35) {
                renderer.setColor(Color.RED);
            } else {
                renderer.setColor(Color.YELLOW);
            }

            renderer.rect(position.x, position.y + 45, 39 * (health / origHealth), 7);
        }
        renderer.end();
    }

    public void updateVel() {
        //update velocity based on direction
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
        //freeze for 400 frames
        frozen = 300;
    }

    public Block collide(int direction) {
        //get grid position of two corners
        int y1 = 12-((int) position.y / 48);
        int y2 = 12-((int) (position.y+39)/48);
        int x1 = (int) position.x / 48;
        int x2 = (int) (position.x+39)/48;

        //get positions
        Vector2 r1p1 = new Vector2(position.x, position.y);
        Vector2 r1p2 = new Vector2(position.x+39, position.y+39);


        //out of game area
        if(y1 == -1 || x1 == 13 || y2 == -1 || x2 == 13) {
            return new Block(new Vector2(0, 0), 0, 0);
        }


        //check for block collision------------------------------------------------------
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
        //------------------------------------------------------------------------



        return null;
    }

    public void collidePowerup() {
        Vector2 r1p1 = new Vector2(position.x, position.y);
        Vector2 r1p2 = new Vector2(position.x+39, position.y+39);

        //check for powerup collision
        for(Powerup p : BlockManager.powerups) {
            if(p.collideTank(r1p1, r1p2)) {

                //extra life
                if(p.type == 3) {
                    if(((Player) this).player1) {
                        GameScreen.lives2++;
                    } else {
                        GameScreen.lives++;
                    }

                //invulnerable
                } else if (p.type == 0) {
                    invulnerable += 500;

                //iron home
                } else if (p.type == 2) {
                    BlockManager.changeToIron();

                //add 500 score
                } else {
                    GameScreen.scores[3]++;
                }

                //remove powerup
                BlockManager.deletePowerup(p);
            }
        }
    }

    public boolean collideTank() {
        Vector2 r1p1 = new Vector2(position.x, position.y);
        Vector2 r1p2 = new Vector2(position.x+39, position.y+39);

        //check for any collisions
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

        //cooldown
        if(cooldown == 0) {

            //velocity of bullet
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

            //init bullet
            Bullet b = new Bullet(new Vector2(position.x + base.getWidth()/2, position.y + base.getHeight()/2), temp, direction, this, damage);
            TankManager.bullets.add(b);

            cooldown = cd;

            if(gunType == 1) {
                mag--;
                if(mag == 0) {
                    mag = 5;
                    cooldown = 80;
                }
            } else if (gunType == 4) {
                mag--;
                if(mag == 0) {
                    mag = 8;
                    cooldown = 40;
                }
            } else if (gunType == 5) {
                mag--;
                if(mag == 0) {
                    mag = 20;
                    cooldown = 60;
                }
            }
        }
    }

    public void takeInput() {

        //HARD MODE------------------------------------------------------
        if(Constants.MODE == 1) {

            //if on current block in path, update direction to the next path
            if ((path.get(cur).getKey().equals(getXY().getKey()) && path.get(cur).getValue().equals(getXY().getValue())) && path.get(cur).getKey().equals(getXY2().getKey()) && path.get(cur).getValue().equals(getXY2().getValue())) {
                if (cur != path.size() - 1) {
                    updateDirection(cur);
                    cur++;
                }
            }

            //fire if possible
            fire();
        //------------------------------------------------------------------------


        //EASY MODE------------------------------------------------------
        } else {

            if(countdown == 0) {
                //randomly switch direction
                if (Tools.choose(60)) {
                    direction = Tools.random(4);
                }

                //randomly fire
                if (Tools.choose(10)) {
                    fire();
                }
            }

        }
        //------------------------------------------------------




    }
}

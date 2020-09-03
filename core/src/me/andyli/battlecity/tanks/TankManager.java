package me.andyli.battlecity.tanks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.DelayedRemovalArray;
import javafx.util.Pair;
import me.andyli.battlecity.blocks.BlockManager;

import java.util.ArrayList;

public class TankManager {
    public static DelayedRemovalArray<Bullet> bullets = new DelayedRemovalArray<>();
    public static DelayedRemovalArray<Tank> tanks = new DelayedRemovalArray<>();
    public static DelayedRemovalArray<Explosion> explosions = new DelayedRemovalArray<>();
    public static Tank toBeDeleted;
    public String ts;
    public int count;
    public int cur;
    public int limit;

    public TankManager(String ts, int limit) {
        //init
        this.ts = ts;
        count = 0;
        cur = 0;

        //limit of tanks
        this.limit = limit;
    }

    public void updateBullets(SpriteBatch batch) {
        //update bullets
        for(Bullet b : bullets) {
            b.update(batch);
        }
    }

    public void updateTanks(SpriteBatch batch, ShapeRenderer renderer) {
        //every sixty frames, parse the string------------------------------------------------------
        if(count == 0 && limit > 0) {

            //if not blank
            if(ts.charAt(cur) != 'O') {
                //if powerup tank
                if(ts.charAt(cur) == 'X') {
                    //add tank of type
                    addTank(Integer.parseInt(String.valueOf(ts.charAt(cur+1))), true, ts.charAt(cur+2)-65);
                    cur++;
                } else {
                    //add tank of type
                    addTank(Integer.parseInt(String.valueOf(ts.charAt(cur))), false, ts.charAt(cur+1)-65);
                }

                //move current position in string
                cur++;

                //one more tank
                limit--;
            }

            //move current position in string if "O"
            cur++;

            //reset count
            count = 60;
        }

        //countdown
        count--;
        //------------------------------------------------------------------------------------------


        //delete tank (prevents visual glitch)
        if(toBeDeleted != null) {
            tanks.removeValue(toBeDeleted, true);
            toBeDeleted = null;
        }

        //update tanks
        for(Tank t : tanks) {
            t.update(batch, renderer);
        }
    }

    public void updateExplosions(SpriteBatch batch) {
        //update explosions
        for(Explosion e : explosions) {
            e.update(batch);
        }
    }

    public static int getNonPlayers() {
        //get how many non player tanks there are
        int ans = 0;
        for(Tank t : tanks) {
            if(!(t instanceof Player)) {
                ans++;
            }
        }
        return ans;
    }

    public static void delete(Tank t) {
        //delete on next frame
        toBeDeleted = t;
    }


    public void addTank(int type, Vector2 position, int direction, ArrayList<Pair<Integer, Integer>> path, boolean powerup) {
        //add a tank of type
        if(type == 0) {
            tanks.add(new Tank(position, 1.5f, direction, new Sprite(new Texture(Gdx.files.internal("img/tank2.png"))), 0, 1.5f, type, path, powerup));
        } else if(type == 1) {
            tanks.add(new Tank(position, 0.8f, direction, new Sprite(new Texture(Gdx.files.internal("img/tank34.png"))), 1, 4, type, path, powerup));
        } else if(type == 2) {
            tanks.add(new Tank(position, 2.5f, direction, new Sprite(new Texture(Gdx.files.internal("img/tank4.png"))), 2, 1, type, path, powerup));
        }
    }

    public void addTank(int tankType, boolean powerup, int s) {
        //set spawner to spawn a tank of a type
        BlockManager.getSpawners().get(s).spawn(tankType, powerup);
    }

    public static void removePlayer() {
        //remove a player
        for(Tank t : tanks) {
            if(t instanceof Player) {
                explosions.add(new Explosion(t.position.add(new Vector2(20, 20)), 2f, 0.1f));
                tanks.removeValue(t, true);
            }
        }
    }

    public static void addTank(Tank t) {
        //add already initialized tank
        tanks.add(t);
    }
}

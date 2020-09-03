package me.andyli.battlecity.tanks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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

    public TankManager(String ts) {
        this.ts = ts;
        count = 0;
        cur = 0;
    }

    public void updateBullets(SpriteBatch batch) {
        for(Bullet b : bullets) {
            b.update(batch);
        }
    }

    public void updateTanks(SpriteBatch batch) {
        //Gdx.app.log(count+"", "");
        if(count == 0) {
            if(ts.charAt(cur) != 'O') {
                if(ts.charAt(cur) == 'X') {
                    addTank(Integer.parseInt(String.valueOf(ts.charAt(cur+1))), true, ts.charAt(cur+2)-65);
                    cur++;
                } else {
                    addTank(Integer.parseInt(String.valueOf(ts.charAt(cur))), false, ts.charAt(cur+1)-65);
                }
                cur++;
            }
            cur++;
            count = 60;
        }

        count--;


        if(toBeDeleted != null) {
            tanks.removeValue(toBeDeleted, true);
            toBeDeleted = null;
        }
        for(Tank t : tanks) {
            t.update(batch);
        }
    }

    public void updateExplosions(SpriteBatch batch) {
        for(Explosion e : explosions) {
            e.update(batch);
        }
    }

    public static int getNonPlayers() {
        int ans = 0;
        for(Tank t : tanks) {
            if(!(t instanceof Player)) {
                ans++;
            }
        }
        return ans;
    }

    public static void delete(Tank t) {
        toBeDeleted = t;
    }

    public static boolean containsPlayer() {
        for(Tank t : tanks) {
            if(t instanceof Player) {
                return true;
            }
        }
        return false;
    }

    public void addTank(int type, Vector2 position, int direction, ArrayList<Pair<Integer, Integer>> path, boolean powerup) {
        if(type == 0) {
            tanks.add(new Tank(position, 1.5f, direction, new Sprite(new Texture(Gdx.files.internal("img/tank2.png"))), 50, 1, type, path, powerup));
        } else if(type == 1) {
            tanks.add(new Tank(position, 0.8f, direction, new Sprite(new Texture(Gdx.files.internal("img/tank34.png"))), 50, 4, type, path, powerup));
        } else if(type == 2) {
            tanks.add(new Tank(position, 2.5f, direction, new Sprite(new Texture(Gdx.files.internal("img/tank4.png"))), 40, 1, type, path, powerup));
        }
    }

    public void addTank(int tankType, boolean powerup, int s) {
        Gdx.app.log("spawning", "");
        BlockManager.getSpawners().get(s).spawn(tankType, powerup);
    }

    public static void removePlayer() {
        for(Tank t : tanks) {
            if(t instanceof Player) {
                explosions.add(new Explosion(t.position.add(new Vector2(20, 20)), 2f, 0.1f));
                tanks.removeValue(t, true);
            }
        }
    }

    public static void addTank(Tank t) {
        tanks.add(t);
    }
}

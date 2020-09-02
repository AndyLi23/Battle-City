package me.andyli.battlecity.tanks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.DelayedRemovalArray;
import javafx.util.Pair;

import java.util.ArrayList;

public class TankManager {
    public static DelayedRemovalArray<Bullet> bullets = new DelayedRemovalArray<>();
    public static DelayedRemovalArray<Tank> tanks = new DelayedRemovalArray<>();
    public static DelayedRemovalArray<Explosion> explosions = new DelayedRemovalArray<>();
    public static Tank toBeDeleted;

    public TankManager() {
    }

    public void updateBullets(SpriteBatch batch) {
        for(Bullet b : bullets) {
            b.update(batch);
        }
    }

    public void updateTanks(SpriteBatch batch) {
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

    public void addTank(int type, Vector2 position, int direction, ArrayList<Pair<Integer, Integer>> path) {
        if(type == 0) {
            tanks.add(new Tank(position, 1.5f, direction, new Sprite(new Texture(Gdx.files.internal("img/tank2.png"))), 50, 1, type, path));
        } else if(type == 1) {
            tanks.add(new Tank(position, 0.8f, direction, new Sprite(new Texture(Gdx.files.internal("img/tank34.png"))), 50, 4, type, path));
        } else if(type == 2) {
            tanks.add(new Tank(position, 2.5f, direction, new Sprite(new Texture(Gdx.files.internal("img/tank4.png"))), 40, 1, type, path));
        }
    }

    public static void addTank(Tank t) {
        tanks.add(t);
    }
}

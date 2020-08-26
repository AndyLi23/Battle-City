package me.andyli.battlecity.tanks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.DelayedRemovalArray;

public class TankManager {
    public static DelayedRemovalArray<Bullet> bullets = new DelayedRemovalArray<>();
    public static DelayedRemovalArray<Tank> tanks = new DelayedRemovalArray<>();

    public TankManager() {
    }

    public void updateBullets(SpriteBatch batch) {
        for(Bullet b : bullets) {
            b.update(batch);
        }
    }

    public void updateTanks(SpriteBatch batch) {
        for(Tank t : tanks) {
            t.update(batch);
        }
    }

    public void addTank(int type) {
        if(type == 0) {
            tanks.add(new Tank(new Vector2(0, 500), 2, 0, new Sprite(new Texture(Gdx.files.internal("img/tank1.png"))), 20));
        }
    }

    public void addTank(Tank t) {
        tanks.add(t);
    }
}

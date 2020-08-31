package me.andyli.battlecity.tanks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import me.andyli.battlecity.blocks.Ice;

public class Player extends Tank{

    public Player(Vector2 position) {
        super(position, 2, 0, new Sprite(new Texture(Gdx.files.internal("img/tank1.png"))), 25, 1);
        invulnerable = 100;
    }

    public void takeInput() {
        if(invulnerable > 0) {
            if(invulnerable/10 % 2 == 0) {
                base.setTexture(new Texture(Gdx.files.internal("img/inv1.png")));
            } else {
                base.setTexture(new Texture(Gdx.files.internal("img/inv2.png")));
            }
        } else {
            base.setTexture(new Texture(Gdx.files.internal("img/tank1.png")));
        }


        if(Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) {
            direction = 0;
            vel.x = 0;
            vel.y = speed;
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) {
            direction = 2;
            vel.x = 0;
            vel.y = -1*speed;
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
            direction = 3;
            vel.y = 0;
            vel.x = speed;
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
            direction = 1;
            vel.y = 0;
            vel.x = -1*speed;
        } else {
            if (collide(0) instanceof Ice || collide(1) instanceof Ice || collide(2) instanceof Ice || collide(3) instanceof Ice) {
                vel.x *= 0.95;
                vel.y *= 0.95;
                if(Math.abs(vel.x) < 0.01) {
                    vel.x = 0;
                }
                if(Math.abs(vel.y) < 0.01) {
                    vel.y = 0;
                }
            } else {
                vel.x = 0;
                vel.y = 0;
            }
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            fire();
        }

    }

    public void updateVel(){}
}

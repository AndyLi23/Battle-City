package me.andyli.battlecity.blocks;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.DelayedRemovalArray;


public class BlockManager {
    public static Block[][] arr = new Block[13][13];
    public static DelayedRemovalArray<Powerup> powerups = new DelayedRemovalArray<>();
    public static int left = -1;

    public static Powerup toBeDeleted;

    public BlockManager(String[] arr) {
        for(int i = 0; i < 13; ++i) {
            for(int j = 0; j < 13; ++j) {
                if (arr[i].charAt(j) == '1' || arr[i].charAt(j) == '2' || arr[i].charAt(j) == '3' || arr[i].charAt(j) == '4' || arr[i].charAt(j) == '5' || arr[i].charAt(j) == '6' || arr[i].charAt(j) == '7' || arr[i].charAt(j) == '8') {
                    BlockManager.arr[i][j] = new Brick(new Vector2(j * 48, (12 - i) * 48), Integer.parseInt(String.valueOf(arr[i].charAt(j))), i, j);
                } else if (arr[i].charAt(j) == '[' || arr[i].charAt(j) == ']' || arr[i].charAt(j) == '/' || arr[i].charAt(j) == '\\') {
                    BlockManager.arr[i][j] = new Iron(new Vector2(j*48, (12-i)*48), i, j, String.valueOf(arr[i].charAt(j)));
                } else if(arr[i].charAt(j) == 'B') {
                    BlockManager.arr[i][j] = new Brick(new Vector2(j*48, (12-i)*48), 0, i, j);
                } else if(arr[i].charAt(j) == 'I') {
                    BlockManager.arr[i][j] = new Iron(new Vector2(j*48, (12-i)*48), i, j, "");
                } else if(arr[i].charAt(j) == 'W') {
                    BlockManager.arr[i][j] = new Water(new Vector2(j*48, (12-i)*48), i, j);
                } else if(arr[i].charAt(j) == 'C') {
                    BlockManager.arr[i][j] = new Ice(new Vector2(j*48, (12-i)*48), i, j);
                } else if(arr[i].charAt(j) == 'G') {
                    BlockManager.arr[i][j] = new Grass(new Vector2(j*48, (12-i)*48), i, j);
                } else if(arr[i].charAt(j) == 'X') {
                    BlockManager.arr[i][j] = new Flag(new Vector2(j*48, (12-i)*48), i, j);
                } else if(arr[i].charAt(j) == 'S') {
                    BlockManager.arr[i][j] = new Spawner(new Vector2(j*48, (12-i)*48), i, j);
                }
            }
        }
    }

    public static boolean spawning() {
        for(Block[] c : arr) {
            for(Block b : c) {
                if(b instanceof Spawner) {
                    if(((Spawner) b).counter != 0) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void update(SpriteBatch batch) {
        if(left == 0) {
            arr[12][5] = new Brick(new Vector2(240, 0),1, 12, 5);
            arr[12][7] = new Brick(new Vector2(336, 0),3, 12, 5);
            arr[11][5] = new Brick(new Vector2(288, 48),4, 12, 5);
            arr[11][6] = new Brick(new Vector2(240, 48),5, 12, 5);
            arr[11][7] = new Brick(new Vector2(336, 48),6, 12, 5);
        }

        if (left != -1) {
            left--;
        }

        for(Block[] c : arr) {
            for(Block b : c) {
                if(b != null && !(b instanceof Ice || b instanceof Water)) {
                    b.update(batch);
                }
            }
        }
    }

    public void updatePowerups(SpriteBatch batch) {
        if(toBeDeleted != null) {
            powerups.removeValue(toBeDeleted, true);
            toBeDeleted = null;
        }
        for(Powerup p : powerups) {
            p.update(batch);
        }
    }

    public static void deletePowerup(Powerup p) {
        toBeDeleted = p;
    }

    public static void addPowerup(Powerup p) {
        powerups.add(p);
    }

    public void updateGround(SpriteBatch batch) {
        for(Block[] c : arr) {
            for(Block b : c) {
                if(b instanceof Ice || b instanceof Water) {
                    b.update(batch);
                }
            }
        }
    }

    public static void changeToIron() {
        arr[12][5] = new Iron(new Vector2(240, 0),12, 5, "/");
        arr[12][7] = new Iron(new Vector2(336, 0),12, 5, "\\");
        arr[11][5] = new Iron(new Vector2(240, 48),12, 5, "@");
        arr[11][6] = new Iron(new Vector2(288, 48),12, 5, "]");
        arr[11][7] = new Iron(new Vector2(336, 48),12, 5, "$");

        left = 500;
    }
}

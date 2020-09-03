package me.andyli.battlecity.blocks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.DelayedRemovalArray;
import javafx.util.Pair;
import me.andyli.battlecity.utility.Tools;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;


public class BlockManager {
    public static Block[][] arr = new Block[13][13];
    public static DelayedRemovalArray<Powerup> powerups = new DelayedRemovalArray<>();
    public static int left = -1;

    private static int min, i, j;

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

    public static ArrayList<Spawner> getSpawners() {
        ArrayList<Spawner> spawners = new ArrayList<>();
        for(Block[] c : arr) {
            for(Block b : c) {
                if(b instanceof Spawner) {
                    spawners.add((Spawner) b);
                }
            }
        }

        return spawners;
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


    public static int[][] getGrid() {
        int[][] ans = new int[13][13];

        for(int i = 0; i < 13; ++i) {
            for(int j = 0; j < 13; ++j) {
                if(arr[i][j] == null) {
                    ans[i][j] = 0;
                } else {
                    if(arr[i][j] instanceof Grass || arr[i][j] instanceof Ice) {
                        ans[i][j] = 0;
                    } else if (arr[i][j] instanceof Brick) {
                        ans[i][j] = 4;
                    } else {
                        ans[i][j] = -1;
                    }
                }
            }
        }

        return ans;
    }

    public static int[][] generatePath(int i, int j) {
        int[][] arr = getGrid();
        boolean[][] seen = new boolean[13][13];

        Queue<Pair<Integer, Integer>> queue = new LinkedList<>();;

        queue.add(new Pair<>(i, j));


        while(!queue.isEmpty()) {
            Pair<Integer, Integer> cur = queue.poll();

            if(!seen[cur.getKey()][cur.getValue()]) {
                seen[cur.getKey()][cur.getValue()] = true;

                int min = 10000;

                if (get(arr, cur.getKey() - 1, cur.getValue()) != -1) {
                    if (!seen[cur.getKey()-1][cur.getValue()]) {
                        queue.add(new Pair<>(cur.getKey() - 1, cur.getValue()));
                    } else {
                        min = Math.min(min, get(arr, cur.getKey() - 1, cur.getValue()));
                    }
                }

                if (get(arr, cur.getKey(), cur.getValue() - 1) != -1) {
                    if (!seen[cur.getKey()][cur.getValue()-1]) {
                        queue.add(new Pair<>(cur.getKey(), cur.getValue() - 1));
                    } else {
                        min = Math.min(min, get(arr, cur.getKey(), cur.getValue() - 1));
                    }
                }

                if (get(arr, cur.getKey() + 1, cur.getValue()) != -1) {
                    if (!seen[cur.getKey()+1][cur.getValue()]) {
                        queue.add(new Pair<>(cur.getKey() + 1, cur.getValue()));
                    } else {
                        min = Math.min(min, get(arr, cur.getKey() + 1, cur.getValue()));
                    }
                }

                if (get(arr, cur.getKey(), cur.getValue() + 1) != -1) {
                    if (!seen[cur.getKey()][cur.getValue()+1]) {
                        queue.add(new Pair<>(cur.getKey(), cur.getValue() + 1));
                    } else {
                        min = Math.min(min, get(arr, cur.getKey(), cur.getValue() + 1));
                    }
                }


                if (min == 10000) {
                    arr[cur.getKey()][cur.getValue()] = 1;
                } else {
                    arr[cur.getKey()][cur.getValue()] += min + 1;
                }

                //Gdx.app.log(arr[cur.getKey()][cur.getValue()] + "", "");
            }
        }

        return arr;

    }

    public static int get(int[][] arr, int i, int j) {
        if(i >= 0 && i < 13 && j >= 0 && j < 13) {
            return arr[i][j];
        }
        return -1;
    }

    public static ArrayList<Pair<Integer, Integer>> getPath(int i, int j) {
        int[][] path = generatePath(i, j);

        /*for(int k = 0; k < 13; ++k) {
            StringBuilder s = new StringBuilder();
            for(int l = 0; l < 13; ++l) {
                s.append(path[k][l]).append(" ");
            }
            Gdx.app.log(String.valueOf(s), "");
        }*/

        ArrayList<Pair<Integer, Integer>> ans = new ArrayList<>();

        Pair<Integer, Integer> cur = new Pair<>(12, 6);

        while(true) {
            ans.add(cur);
            i = cur.getKey();
            j = cur.getValue();
            min = 10000;

            if(get(path, i+1, j) != -1) {
                min = Math.min(min, get(path, i+1, j));
            }
            if(get(path, i-1, j) != -1) {
                min = Math.min(min, get(path, i-1, j));
            }
            if(get(path, i, j+1) != -1) {
                min = Math.min(min, get(path, i, j+1));
            }
            if(get(path, i, j-1) != -1) {
                min = Math.min(min, get(path, i, j-1));
            }


            if(path[i][j] != -1 && min > path[i][j]) {
                break;
            }

            if(get(path, i+1, j) == min) {
                cur = new Pair<>(i+1, j);
            } else if(get(path, i-1, j) == min) {
                cur = new Pair<>(i-1, j);
            } else if(get(path, i, j+1) == min) {
                cur = new Pair<>(i, j+1);
            } else if(get(path, i, j-1) == min) {
                cur = new Pair<>(i, j-1);
            }

        }

        ans = reverse(ans);
        return ans;
    }

    public static ArrayList<Pair<Integer, Integer>> reverse(ArrayList<Pair<Integer, Integer>> arr) {
        Pair<Integer, Integer> temp;
        for(int i = 0; i < arr.size()/2; ++i) {
            temp = arr.get(i);
            arr.set(i, arr.get(arr.size()-i-1));
            arr.set(arr.size()-i-1, temp);
        }
        return arr;
    }


}

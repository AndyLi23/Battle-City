package me.andyli.battlecity.blocks;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.DelayedRemovalArray;
import javafx.util.Pair;
import me.andyli.battlecity.Constants;
import me.andyli.battlecity.utility.Tools;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;


public class BlockManager {
    public static Block[][] arr = new Block[13][13];

    public static DelayedRemovalArray<Powerup> powerups = new DelayedRemovalArray<>();

    public static int left = -1;
    private static int min;
    public static Powerup toBeDeleted;

    public BlockManager(String[] arr) {
        //parse given text to build map ---------------------------------------------------

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
        //-------------------------------------------------------------------------------------
    }

    public static boolean spawning() {
        //check for spawning
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
        //get the spawners in a list
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
        //iron powerup --------------------------------------------------------------------
        //return to brick (after iron powerup)
        if(left == 0) {
            arr[12][5] = new Brick(new Vector2(240, 0),1, 12, 5);
            arr[12][7] = new Brick(new Vector2(336, 0),3, 12, 5);
            arr[11][5] = new Brick(new Vector2(288, 48),4, 12, 5);
            arr[11][6] = new Brick(new Vector2(240, 48),5, 12, 5);
            arr[11][7] = new Brick(new Vector2(336, 48),6, 12, 5);
        }

        //countdown iron powerup
        if (left != -1) {
            left--;
        }
        //-------------------------------------------------------------------------------------

        //update each block except for ice/water (rendering)
        for(Block[] c : arr) {
            for(Block b : c) {
                if(b != null && !(b instanceof Ice || b instanceof Water)) {
                    b.update(batch);
                }
            }
        }
    }

    public void updatePowerups(SpriteBatch batch) {
        //delete powerup (prevents glitch)
        if(toBeDeleted != null) {
            powerups.removeValue(toBeDeleted, true);
            toBeDeleted = null;
        }

        //update powerups
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
        //update ice and water (so it renders first)
        for(Block[] c : arr) {
            for(Block b : c) {
                if(b instanceof Ice || b instanceof Water) {
                    b.update(batch);
                }
            }
        }
    }

    public static void changeToIron() {
        //change to iron (powerup)
        arr[12][5] = new Iron(new Vector2(240, 0),12, 5, "/");
        arr[12][7] = new Iron(new Vector2(336, 0),12, 5, "\\");
        arr[11][5] = new Iron(new Vector2(240, 48),12, 5, "@");
        arr[11][6] = new Iron(new Vector2(288, 48),12, 5, "]");
        arr[11][7] = new Iron(new Vector2(336, 48),12, 5, "$");

        left = 500;
    }






    //PATH GENERATION ----------------------------------------------------------------------------------------------------------------------------------------
    //
    //

    public static int[][] getGrid() {
        //get grid of blocks for BFS -----------
        int[][] ans = new int[13][13];

        for(int i = 0; i < 13; ++i) {
            for(int j = 0; j < 13; ++j) {
                if(arr[i][j] == null) {
                    ans[i][j] = 0;
                } else {
                    if(arr[i][j] instanceof Grass || arr[i][j] instanceof Ice) {
                        //traversable
                        ans[i][j] = 0;
                    } else if (arr[i][j] instanceof Brick) {
                        //add cost
                        ans[i][j] = 4;
                    } else {
                        //not traversable
                        ans[i][j] = -1;
                    }
                }
            }
        }

        return ans;
        //------------------------------------
    }

    public static int[][] generatePath(int i, int j) {
        //BFS------------------------------------------------------------------------------------------------------

        //grid
        int[][] arr = getGrid();
        //visited
        boolean[][] seen = new boolean[13][13];

        //queue for BFS
        Queue<Pair<Integer, Integer>> queue = new LinkedList<>();;

        //beginning
        queue.add(new Pair<>(i, j));


        //start BFS----------------------------------
        while(!queue.isEmpty()) {
            //get element
            Pair<Integer, Integer> cur = queue.poll();

            //check for visited
            if(!seen[cur.getKey()][cur.getValue()]) {
                seen[cur.getKey()][cur.getValue()] = true;

                int min = 10000;


                //BFS four sides ---------------------------------------------------
                if (get(arr, cur.getKey() - 1, cur.getValue()) != -1) {
                    if (!seen[cur.getKey()-1][cur.getValue()]) {
                        //not seen: add to queue
                        queue.add(new Pair<>(cur.getKey() - 1, cur.getValue()));
                    } else {
                        //get minimum cost to current block
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
                //--------------------------------------------------------------------


                //origin
                if (min == 10000) {
                    arr[cur.getKey()][cur.getValue()] = 1;
                } else {

                    //update based on minimum cost
                    arr[cur.getKey()][cur.getValue()] += min + 1;
                }

            }
        }

        return arr;

        //------------------------------------------------------------------------------------------------------==

    }

    public static int get(int[][] arr, int i, int j) {
        //get element (if invalid, return -1)
        if(i >= 0 && i < 13 && j >= 0 && j < 13) {
            return arr[i][j];
        }
        return -1;
    }

    public static ArrayList<Pair<Integer, Integer>> getPath(int i, int j) {
        //PATH GENERATION----------------------------------------------------------------------------------------------------------------------
        int[][] path = generatePath(i, j);

        //answer
        ArrayList<Pair<Integer, Integer>> ans = new ArrayList<>();
        //possible next moves
        ArrayList<Pair<Integer, Integer>> possible = new ArrayList<>();


        //current place
        Pair<Integer, Integer> cur = new Pair<>(12, 6);

        while(true) {
            //add to path
            ans.add(cur);
            i = cur.getKey();
            j = cur.getValue();

            //get minimum cost path----------------------------------------------
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
            //---------------------------------------------------------------------


            //reached destination--------------------------------------------------
            if(path[i][j] != -1 && min > path[i][j]) {
                break;
            }
            //---------------------------------------------------------------------


            //pick random minimum cost path----------------------------------------------
            if(Constants.MODE == 1 || ans.size()==1) {
                if(get(path, i+1, j) == min) {
                    possible.add(new Pair<>(i+1, j));
                }
                if(get(path, i-1, j) == min) {
                    possible.add(new Pair<>(i-1, j));
                }
                if(get(path, i, j+1) == min) {
                    possible.add(new Pair<>(i, j+1));
                }
                if(get(path, i, j-1) == min) {
                    possible.add(new Pair<>(i, j-1));
                }
            }


            cur = Tools.selectRandom(possible);
            possible.clear();
            //-------------------------------------------------------------------------

        }

        //reverse to get path from origin
        return reverse(ans);
    }

    public static ArrayList<Pair<Integer, Integer>> reverse(ArrayList<Pair<Integer, Integer>> arr) {
        //reverse an arraylist
        Pair<Integer, Integer> temp;
        for(int i = 0; i < arr.size()/2; ++i) {
            temp = arr.get(i);
            arr.set(i, arr.get(arr.size()-i-1));
            arr.set(arr.size()-i-1, temp);
        }
        return arr;
    }

    //---------------------------------------------------------------------------------------------------------------------------------------------------------


}

package me.andyli.battlecity.utility;

public class Pair {
    private int a, b;
    public Pair(int a, int b) {
        this.a = a;
        this.b = b;
    }

    public int getKey() {
        return a;
    }

    public int getValue() {
        return b;
    }

    public boolean equals(Pair p) {
        return p.getKey() == this.getKey() && p.getValue() == this.getValue();
    }
}

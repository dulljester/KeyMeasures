package com.example.sj.keymeasures.algopuzzles.helpers;

public class Pair<A,B> {
    private A first;
    private B second;

    public A getFirst() {
        return first;
    }

    public B getSecond() {
        return second;
    }

    public Pair (A x, B y ) {
        first= x;
        second= y;
    }
}

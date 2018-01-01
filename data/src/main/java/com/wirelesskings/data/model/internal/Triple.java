package com.wirelesskings.data.model.internal;

/**
 * Created by alberto on 31/12/17.
 */

public class Triple<T, K, J> {
    private T left;
    private K middle;
    private J right;

    public Triple() {
    }

    public T getLeft() {
        return left;
    }

    public Triple setLeft(T left) {
        this.left = left;
        return this;
    }

    public K getMiddle() {
        return middle;
    }

    public Triple setMiddle(K middle) {
        this.middle = middle;
        return this;
    }

    public J getRight() {
        return right;
    }

    public Triple setRight(J right) {
        this.right = right;
        return this;
    }
}

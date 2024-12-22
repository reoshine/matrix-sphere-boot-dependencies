package com.roshine.matrixsphere.base.client.utils;

/**
 * @author roshine
 * @version 1.0.0
 * @since 2024-12-22 23:04
 */
class Sm4Context {
    public int mode;

    public int[] sk;

    public boolean isPadding;

    public Sm4Context() {
        this.mode = 1;
        this.isPadding = true;
        this.sk = new int[32];
    }
}

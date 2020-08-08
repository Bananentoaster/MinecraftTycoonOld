package me.bananentoast.minecrafttycoon.util;

public class Destroyable {

    public Destroyable(Object instance) {
        try {
            this.finalize();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}

package iamnp.musicguide;

import android.content.Context;

/**
 * Singleton class that holds SingersDb instance.
 */
public class DbSingleton {
    private static SingersDb sSingleton;

    public static SingersDb get(Context ctx) {
        if (sSingleton == null) sSingleton = new SingersDb(ctx);
        return sSingleton;
    }
}
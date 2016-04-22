package iamnp.musicguide;

import android.content.res.Resources;

/**
 * Class to resolve singer stats string in specific locale.
 */
public class StatsResolver {

    private static final String RU = "ru";

    public static String Resolve(int albums, int tracks, Resources resources) {
        String res = null;
        switch (resources.getConfiguration().locale.getLanguage()) {
            case RU: {
                res = ResolveRU(albums, tracks, resources);
                break;
            }
            default: {
                res = ResolveEN(albums, tracks, resources);
                break;
            }
        }

        return res;
    }

    private static String ResolveRU(int albums, int tracks, Resources resources) {
        String alb = albums + " " + (
                (albums >= 5 && albums <= 20) || albums % 10 >= 5 || albums % 10 == 0
                        ? resources.getString(R.string.albums2)
                        : (albums % 10 == 1
                        ? resources.getString(R.string.album)
                        : resources.getString(R.string.albums1))
        );
        String tra = tracks + " " + (
                (tracks >= 5 && tracks <= 20) || tracks % 10 >= 5 || tracks % 10 == 0
                        ? resources.getString(R.string.songs2)
                        : (tracks % 10 == 1
                        ? resources.getString(R.string.song)
                        : resources.getString(R.string.songs1))
        );

        return alb + "  " + resources.getString(R.string.stats_separator) + "  " + tra;
    }

    private static String ResolveEN(int albums, int tracks, Resources resources) {
        String alb = albums + " " + (
                albums > 1
                        ? resources.getString(R.string.albums1)
                        : resources.getString(R.string.album)
        );
        String tra = tracks + " " + (
                tracks > 1
                        ? resources.getString(R.string.songs1)
                        : resources.getString(R.string.song)
        );

        return alb + "  " + resources.getString(R.string.stats_separator) + "  " + tra;
    }
}
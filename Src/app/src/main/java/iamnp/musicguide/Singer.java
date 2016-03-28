package iamnp.musicguide;

import android.content.Context;
import android.text.TextUtils;

public class Singer {
    public long id;
    public String name;
    public String[] genres;
    public int tracks;
    public int albums;
    public String link;
    public String description;
    public Cover cover;

    public static class Cover {
        public String small;
        public String big;
    }

    public String GenresAsString() {
        return TextUtils.join(", ", genres);
    }

    public String StatsAsString(Context ctx) {
        String alb = albums + " " + ((albums >= 5 && albums <= 20) || albums % 10 >= 5 ? ctx.getString(R.string.albums2) : (albums % 10 == 1 ? ctx.getString(R.string.album) : ctx.getString(R.string.albums1)));
        String tra = tracks + " " + ((tracks >= 5 && tracks <= 20) || tracks % 10 >= 5 ? ctx.getString(R.string.songs2) : (tracks % 10 == 1 ? ctx.getString(R.string.song) : ctx.getString(R.string.songs1)));

        return alb + "  •  " + tra;
    }
}
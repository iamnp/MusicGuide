package iamnp.musicguide;

import android.content.Context;

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

    public String genresAsString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < genres.length; ++i) {
            sb.append(genres[i].trim());
            if (i != genres.length - 1) sb.append(", ");
        }
        return sb.toString();
    }

    public String statsAsString(Context ctx) {
        String alb = albums + " " + ((albums >= 5 && albums <= 20) || albums % 10 >= 5 ? ctx.getString(R.string.albums2) : (albums % 10 == 1 ? ctx.getString(R.string.album) : ctx.getString(R.string.albums1)));
        String tra = tracks + " " + ((tracks >= 5 && tracks <= 20) || tracks % 10 >= 5 ? ctx.getString(R.string.songs2) : (tracks % 10 == 1 ? ctx.getString(R.string.song) : ctx.getString(R.string.songs1)));

        return alb + "  •  " + tra;
    }
}
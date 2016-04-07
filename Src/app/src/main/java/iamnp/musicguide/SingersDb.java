package iamnp.musicguide;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to store and retrieve information about singers
 */
public class SingersDb extends SQLiteOpenHelper {

    // Table fields
    public static final String COLUMN_INDEX = "index_key";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_GENRES = "genres";
    public static final String COLUMN_TRACKS = "tracks";
    public static final String COLUMN_ALBUMS = "albums";
    public static final String COLUMN_LINK = "link";
    public static final String COLUMN_COVER_SMALL = "cover_small";
    public static final String COLUMN_COVER_BIG = "cover_big";
    public static final String COLUMN_DESCRIPTION = "description";
    private static final String DATABASE_NAME = "singers.db";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_TABLE = "singers";

    // query to create database
    private static final String DATABASE_CREATE = "create table "
            + DATABASE_TABLE + "("
            + COLUMN_INDEX + " integer primary key autoincrement, "
            + COLUMN_ID + " integer, "
            + COLUMN_NAME + " text, "
            + COLUMN_GENRES + " text, "
            + COLUMN_TRACKS + " integer, "
            + COLUMN_ALBUMS + " integer, "
            + COLUMN_LINK + " text, "
            + COLUMN_COVER_SMALL + " text, "
            + COLUMN_COVER_BIG + " text, "
            + COLUMN_DESCRIPTION + " text"
            + ");";

    public SingersDb(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
        onCreate(db);
    }

    public void addSinger(Singer s) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, s.id);
        values.put(COLUMN_NAME, s.name);
        values.put(COLUMN_GENRES, s.genresAsString());
        values.put(COLUMN_TRACKS, s.tracks);
        values.put(COLUMN_ALBUMS, s.albums);
        values.put(COLUMN_LINK, s.link);
        values.put(COLUMN_COVER_SMALL, s.cover.small);
        values.put(COLUMN_COVER_BIG, s.cover.big);
        values.put(COLUMN_DESCRIPTION, s.description);

        long row = db.insert(DATABASE_TABLE, null, values);
        db.close();
    }

    public Singer getSinger(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor mCursor = db.query(true, DATABASE_TABLE, new String[]
                        {COLUMN_ID, COLUMN_NAME, COLUMN_GENRES, COLUMN_TRACKS, COLUMN_ALBUMS, COLUMN_LINK, COLUMN_COVER_SMALL, COLUMN_COVER_BIG, COLUMN_DESCRIPTION},
                COLUMN_ID + "=" + id, null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        Singer s = new Singer();
        s.cover = new Singer.Cover();
        s.id = mCursor.getLong(mCursor.getColumnIndexOrThrow(COLUMN_ID));
        s.name = mCursor.getString(mCursor.getColumnIndexOrThrow(COLUMN_NAME));
        s.genres = mCursor.getString(mCursor.getColumnIndexOrThrow(COLUMN_GENRES)).split(",");
        s.tracks = mCursor.getInt(mCursor.getColumnIndexOrThrow(COLUMN_TRACKS));
        s.albums = mCursor.getInt(mCursor.getColumnIndexOrThrow(COLUMN_ALBUMS));
        s.link = mCursor.getString(mCursor.getColumnIndexOrThrow(COLUMN_LINK));
        s.cover.small = mCursor.getString(mCursor.getColumnIndexOrThrow(COLUMN_COVER_SMALL));
        s.cover.big = mCursor.getString(mCursor.getColumnIndexOrThrow(COLUMN_COVER_BIG));
        s.description = mCursor.getString(mCursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION));
        mCursor.close();
        db.close();
        return s;
    }

    public List<Singer> getAllSingers() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor mCursor = db.query(true, DATABASE_TABLE, new String[]
                        {COLUMN_ID, COLUMN_NAME, COLUMN_GENRES, COLUMN_TRACKS, COLUMN_ALBUMS, COLUMN_LINK, COLUMN_COVER_SMALL, COLUMN_COVER_BIG, COLUMN_DESCRIPTION},
                null, null, null, null, null, null);
        List<Singer> ss = new ArrayList<>();
        while (mCursor.moveToNext()) {
            Singer s = new Singer();
            s.cover = new Singer.Cover();
            s.id = mCursor.getLong(mCursor.getColumnIndexOrThrow(COLUMN_ID));
            s.name = mCursor.getString(mCursor.getColumnIndexOrThrow(COLUMN_NAME));
            s.genres = mCursor.getString(mCursor.getColumnIndexOrThrow(COLUMN_GENRES)).split(",");
            s.tracks = mCursor.getInt(mCursor.getColumnIndexOrThrow(COLUMN_TRACKS));
            s.albums = mCursor.getInt(mCursor.getColumnIndexOrThrow(COLUMN_ALBUMS));
            s.link = mCursor.getString(mCursor.getColumnIndexOrThrow(COLUMN_LINK));
            s.cover.small = mCursor.getString(mCursor.getColumnIndexOrThrow(COLUMN_COVER_SMALL));
            s.cover.big = mCursor.getString(mCursor.getColumnIndexOrThrow(COLUMN_COVER_BIG));
            s.description = mCursor.getString(mCursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION));
            ss.add(s);
        }
        mCursor.close();
        db.close();
        return ss;
    }

    public void deleteAllSingers() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DATABASE_TABLE, null, null);
        db.close();
    }
}
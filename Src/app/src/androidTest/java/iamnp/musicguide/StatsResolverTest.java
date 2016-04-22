package iamnp.musicguide;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.RenamingDelegatingContext;
import android.util.DisplayMetrics;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Locale;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class StatsResolverTest {

    Context mMockContext;
    String mSeparator;

    @Before
    public void setUp() {
        mMockContext = new RenamingDelegatingContext(InstrumentationRegistry.getInstrumentation().getTargetContext(), "test_");
        mSeparator = " " + mMockContext.getResources().getString(R.string.stats_separator) + " ";
    }

    @Test
    public void RUTest() {
        Resources resources = getResources(new Locale("ru"));

        assertEquals("1 альбом " + mSeparator + " 1 песня",
                StatsResolver.Resolve(1, 1, resources));

        assertEquals("2 альбома " + mSeparator + " 2 песни",
                StatsResolver.Resolve(2, 2, resources));

        assertEquals("5 альбомов " + mSeparator + " 5 песен",
                StatsResolver.Resolve(5, 5, resources));

        assertEquals("10 альбомов " + mSeparator + " 10 песен",
                StatsResolver.Resolve(10, 10, resources));

        assertEquals("12 альбомов " + mSeparator + " 12 песен",
                StatsResolver.Resolve(12, 12, resources));

        assertEquals("20 альбомов " + mSeparator + " 20 песен",
                StatsResolver.Resolve(20, 20, resources));

        assertEquals("42 альбома " + mSeparator + " 42 песни",
                StatsResolver.Resolve(42, 42, resources));

        assertEquals("48 альбомов " + mSeparator + " 48 песен",
                StatsResolver.Resolve(48, 48, resources));

        assertEquals("80 альбомов " + mSeparator + " 80 песен",
                StatsResolver.Resolve(80, 80, resources));

        assertEquals("1000 альбомов " + mSeparator + " 1000 песен",
                StatsResolver.Resolve(1000, 1000, resources));
    }

    @Test
    public void ENTest() {
        Resources resources = getResources(new Locale("en"));

        assertEquals("1 album " + mSeparator + " 1 song",
                StatsResolver.Resolve(1, 1, resources));

        assertEquals("2 albums " + mSeparator + " 2 songs",
                StatsResolver.Resolve(2, 2, resources));

        assertEquals("10 albums " + mSeparator + " 10 songs",
                StatsResolver.Resolve(10, 10, resources));

        assertEquals("42 albums " + mSeparator + " 42 songs",
                StatsResolver.Resolve(42, 42, resources));

        assertEquals("100 albums " + mSeparator + " 100 songs",
                StatsResolver.Resolve(100, 100, resources));

        assertEquals("1000 albums " + mSeparator + " 1000 songs",
                StatsResolver.Resolve(1000, 1000, resources));
    }

    private Resources getResources(Locale locale) {
        Configuration confTmp = new Configuration(mMockContext.getResources().getConfiguration());
        confTmp.locale = locale;
        // Not retrieving DisplayMetrics because we don't need it in this test
        return new Resources(mMockContext.getAssets(), new DisplayMetrics(), confTmp);
    }
}
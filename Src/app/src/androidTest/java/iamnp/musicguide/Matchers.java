package iamnp.musicguide;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class Matchers {
    public static Matcher<View> withListSize(final int size) {
        return new TypeSafeMatcher<View>() {
            @Override
            public boolean matchesSafely(final View view) {
                return ((RecyclerView) view).getChildCount() == size;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("RecycleView should have " + size + " items");
            }
        };
    }
}

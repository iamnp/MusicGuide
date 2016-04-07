package iamnp.musicguide;

import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

/**
 * Helper class with hamcrest matchers.
 */
public class Matchers {
    /**
     * Matches RecyclerViews of a particular size.
     *
     * @param size Size to match.
     * @return Matcher for RecyclerViews of a particular size.
     */
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

    /**
     * Matches RecyclerViews with matching item at a particular position.
     *
     * @param position    Position to match.
     * @param itemMatcher Item's matcher.
     * @return Matcher for RecyclerViews with matching item at a particular position.
     */
    public static Matcher<View> atPosition(final int position, final Matcher<View> itemMatcher) {
        return new BoundedMatcher<View, RecyclerView>(RecyclerView.class) {
            @Override
            public void describeTo(Description description) {
                description.appendText("has item at position " + position + ": ");
                itemMatcher.describeTo(description);
            }

            @Override
            protected boolean matchesSafely(final RecyclerView view) {
                RecyclerView.ViewHolder viewHolder
                        = view.findViewHolderForAdapterPosition(position);
                if (viewHolder == null) {
                    return false;
                }
                return itemMatcher.matches(viewHolder.itemView);
            }
        };
    }

    /**
     * Matches RecyclerViews with all matching items.
     *
     * @param itemMatcher Item's matcher.
     * @return Matcher for RecyclerViews with all matching items.
     */
    public static Matcher<View> allItems(final Matcher<View> itemMatcher) {
        return new BoundedMatcher<View, RecyclerView>(RecyclerView.class) {
            @Override
            public void describeTo(Description description) {
                description.appendText("all items must match: ");
                itemMatcher.describeTo(description);
            }

            @Override
            protected boolean matchesSafely(final RecyclerView view) {
                int cnt = view.getChildCount();
                for (int i = 0; i < cnt; ++i) {
                    RecyclerView.ViewHolder viewHolder = view.findViewHolderForAdapterPosition(i);
                    if (viewHolder == null || !itemMatcher.matches(viewHolder.itemView)) {
                        return false;
                    }
                }
                return true;
            }
        };
    }
}

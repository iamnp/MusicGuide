package iamnp.musicguide;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class SearchTest {

    @Rule
    public ActivityTestRule<SingerListActivity> mActivityRule
            = new ActivityTestRule<>(SingerListActivity.class);

    @Test
    public void SearchPositiveTest() {
        String mStringToBeSearched = "Noize MC";

        onView(withId(R.id.action_search)).perform(click());
        onView(withId(android.support.v7.appcompat.R.id.search_src_text))
                .perform(typeText(mStringToBeSearched), closeSoftKeyboard());

        onView(withId(R.id.singer_list))
                .check(matches(
                        Matchers.atPosition(0, hasDescendant(withText(mStringToBeSearched)))));
        onView(withId(R.id.singer_list))
                .check(matches(Matchers.withListSize(1)));
    }

    @Test
    public void SearchNegativeTest() {
        String mStringToBeSearched = "non_existsing_singer";

        onView(withId(R.id.action_search)).perform(click());
        onView(withId(android.support.v7.appcompat.R.id.search_src_text))
                .perform(typeText(mStringToBeSearched), closeSoftKeyboard());

        onView(withId(R.id.singer_list)).check(matches(Matchers.withListSize(0)));
    }

    @Test
    public void SearchPositiveManyTest() {
        String mStringToBeSearched = "rusrap";

        onView(withId(R.id.action_search)).perform(click());
        onView(withId(android.support.v7.appcompat.R.id.search_src_text))
                .perform(typeText(mStringToBeSearched), closeSoftKeyboard());

        onView(withId(R.id.singer_list))
                .check(matches(Matchers.allItems(hasDescendant(withText(mStringToBeSearched)))));
    }
}
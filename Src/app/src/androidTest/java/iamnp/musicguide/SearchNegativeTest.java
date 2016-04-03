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
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class SearchNegativeTest {

    @Rule
    public ActivityTestRule<SingerListActivity> mActivityRule = new ActivityTestRule<>(SingerListActivity.class);
    private String mStringToBetyped = "non_existsing_singer";

    @Test
    public void changeText_sameActivity() {
        onView(withId(R.id.action_search)).perform(click());
        onView(withId(android.support.v7.appcompat.R.id.search_src_text)).perform(typeText(mStringToBetyped), closeSoftKeyboard());

        onView(withId(R.id.singer_list)).check(matches(Matchers.withListSize(0)));
    }
}
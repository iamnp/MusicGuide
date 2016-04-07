package iamnp.musicguide;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class FloatingActionButtonTest {

    @Rule
    public ActivityTestRule<SingerListActivity> mActivityRule
            = new ActivityTestRule<>(SingerListActivity.class);

    @Test
    public void FloatingActionButtonVisibleTest() {
        onView(withId(R.id.singer_list))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.fab))
                .check(matches(isDisplayed()));
    }

    @Test
    public void FloatingActionButtonInvisibleTest() {
        onView(withId(R.id.singer_list))
                .perform(RecyclerViewActions.actionOnItemAtPosition(3, click()));
        onView(withId(R.id.fab))
                .check(matches(not(isDisplayed())));
    }
}
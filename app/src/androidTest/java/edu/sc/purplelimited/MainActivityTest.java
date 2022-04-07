package edu.sc.purplelimited;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void testEvent() {
        ActivityScenario<MainActivity> scenario = activityRule.getScenario();
    }

    /*@Test
    public void testDriveState() {



    }*/


    @Test // Checking if correct nav view is displayed
    public void mainActivityNavViewIsDisplayed() {
        onView(withId(R.id.nav_view)).check(matches(isDisplayed()));
    }
}
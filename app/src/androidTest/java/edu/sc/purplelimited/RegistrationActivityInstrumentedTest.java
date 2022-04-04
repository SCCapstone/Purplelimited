package edu.sc.purplelimited;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class RegistrationActivityInstrumentedTest {

    @Rule
    public ActivityScenarioRule<RegistrationActivity> activityRule =
            new ActivityScenarioRule<>(RegistrationActivity.class);

    @Test
    public void registrationTextIsDisplayed() {
        onView(withText("Register an Account")).check(matches(isDisplayed()));
    }
}

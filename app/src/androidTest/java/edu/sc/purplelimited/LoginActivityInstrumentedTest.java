package edu.sc.purplelimited;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.action.ViewActions.click;

import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class LoginActivityInstrumentedTest {



  @Rule // launch loginActivity
  public ActivityScenarioRule<LoginActivity> activityRule =
          new ActivityScenarioRule<>(LoginActivity.class);

  @Test
  public void signInTextIsDisplayed() {
    onView(withText("Sign in")).check(matches(isDisplayed()));
  }


  @Test
  public void correctViewIsDisplayed() {
    onView(withId(R.id.signin)).check(matches(isDisplayed()));
    onView(withId(R.id.userName)).check(matches(isDisplayed()));
    onView(withId(R.id.userPassword)).check(matches(isDisplayed()));
    onView(withId(R.id.loginbutton)).check(matches(isDisplayed()));
    onView(withId(R.id.registerbutton)).check(matches(isDisplayed()));
    onView(withId(R.id.noAttempts)).check(matches(isDisplayed()));
    onView(withId(R.id.RememberMe)).check(matches(isDisplayed()));
  }

  @Test
  public void correctTextIsDisplayed() {
    onView(withId(R.id.signin)).check(matches(withText("Sign in")));
  }

  /*@Test
  public void navRegistrationActivity() {
    onView(withId(R.id.registerbutton)).perform(click());
    ;
  }*/

}

package edu.sc.purplelimited.ui.search;

import static org.junit.Assert.*;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.action.ViewActions.click;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.Lifecycle;
import androidx.test.espresso.Espresso;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import edu.sc.purplelimited.R;


@RunWith(AndroidJUnit4.class)
public class SearchFragmentTest {

    private FragmentScenario<SearchFragment> scenario;
    private SearchFragment searchFragment;

    // Launching Fragment In Container
    @Before
    public void setUp() {

        scenario = FragmentScenario.launchInContainer(SearchFragment.class);
        scenario.moveToState(Lifecycle.State.STARTED);

    }

    @Test
    public void searchResultsAreDisplayed() {
        String searchInput = "pasta";

        onView(withId(R.id.search_recipe_text)).perform(typeText(searchInput));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.search_button)).perform(click());

        onView(withId(R.id.view_pager_suggestions)).check(matches(isDisplayed()));
    }

}
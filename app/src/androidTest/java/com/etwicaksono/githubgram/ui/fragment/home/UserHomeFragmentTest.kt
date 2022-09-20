package com.etwicaksono.githubgram.ui.fragment.home

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.etwicaksono.githubgram.R
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

@RunWith(AndroidJUnit4::class)
class UserHomeFragmentTest {


    @Test
    fun testNavigationFromHomeToFavorites() {
        // Create a mock NavController
        val mockNavController = mock(NavController::class.java)
        val toFavoriteFragment =
            UserHomeFragmentDirections.actionUserListFragmentToFavoriteFragment()

        // Create a graphical FragmentScenario for the TitleScreen
        val userHomeFragment = launchFragmentInContainer<UserHomeFragment>()

        // Set the NavController property on the fragment
        userHomeFragment.onFragment { fragment ->
            Navigation.setViewNavController(fragment.requireView(), mockNavController)
        }

        // Verify that performing a click prompts the correct Navigation action
        onView(withId(R.id.favorites)).perform(ViewActions.click())
        verify(mockNavController).navigate(toFavoriteFragment)
    }

    @Test
    fun testSearchNotFound() {
        launchFragmentInContainer<UserHomeFragment>()

        onView(withId(R.id.search)).perform(ViewActions.click())
        onView(withId(R.id.search_src_text)).perform(ViewActions.typeText("userkosong"))
        Thread.sleep(3000)
        onView(withId(R.id.tv_empty)).check(matches(isDisplayed()))
    }
}
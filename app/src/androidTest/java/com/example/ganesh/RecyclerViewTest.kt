package com.example.ganesh

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.rule.ActivityTestRule
import org.junit.Rule
import org.junit.Test

class RecyclerViewTest {

    @get:Rule
    val activityRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun listGoesOverTheFold() {
        onView(withText("Hello world!")).check(matches(isDisplayed()))
    }

    @Test
    fun scrollToItemBelowFold_checkItsText() {
        // First, scroll to the position that needs to be matched and click on it.
//        onView(withId(R.id.my_recycler_view)).perform(
//                RecyclerViewActions.actionOnItemAtPosition(
//                    0,
//                    click()
//                )
//            )

        // Match the text in an item below the fold and check that it's displayed.
        val itemElementText = "${activityRule.activity.resources
            .getString(R.string.app_name)} ${1}"
        onView(withText(itemElementText)).check(matches(isDisplayed()))
    }

    @Test
    fun itemInMiddleOfList_hasSpecialText() {
        // First, scroll to the view holder using the isInTheMiddle() matcher.
//        onView(withId(R.id.my_recycler_view)).perform(RecyclerViewActions.scrollToHolder(isClickable()))

        // Check that the item has the special text.
        val middleElementText = activityRule.activity.resources
            .getString(R.string.app_name)
        onView(withText(middleElementText)).check(matches(isDisplayed()))
    }
}
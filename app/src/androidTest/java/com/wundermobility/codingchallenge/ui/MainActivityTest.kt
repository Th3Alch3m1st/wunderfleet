package com.wundermobility.codingchallenge.ui

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.wundermobility.codingchallenge.R
import com.wundermobility.codingchallenge.utils.DataBindingIdlingResource
import com.wundermobility.codingchallenge.utils.monitorActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.CoreMatchers.not
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created By Rafiqul Hasan
 */
@RunWith(AndroidJUnit4ClassRunner::class)
@HiltAndroidTest
class MainActivityTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    private val dataBindingIdlingResource = DataBindingIdlingResource()

    @Before
    fun setUp() {
        hiltRule.inject()

        IdlingRegistry.getInstance().register(dataBindingIdlingResource)
    }

    /**
     * Unregister your Idling Resource so it can be garbage collected and does not leak any memory.
     */
    @After
    fun unregisterIdlingResource() {
        //IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
        IdlingRegistry.getInstance().unregister(dataBindingIdlingResource)
    }

    /*
    *   Test spec
    *   name: Activity is opened correctly.
    *   steps:
    *       - [Action] launch activity
    *       - [Assert] root view should be visible
    */
    @Test
    fun display_mainActivityOpen_opened() {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        onView(withId(R.id.root)).check(matches(isDisplayed()))
    }

    /*
    *   Test spec
    *   name: fragment is attached to activity correctly and map view, car location and my location button is visible.
    *   steps:
    *       - [Action] launch activity
    *       - [Assert] map_fragment view should be visible
    *       - [Assert] fab_car_location view should be visible
    *       - [Assert] fab_my_location view should be visible
    *       - [Assert] view_empty view should be invisible
    */
    @Test
    fun display_carListFragment_opened() {
        //open activity
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        Thread.sleep(500)

        //verifying ui is present on screen
        onView(withId(R.id.map_fragment)).check(matches(isDisplayed()))
        onView(withId(R.id.fab_car_location)).check(matches(isDisplayed()))
        onView(withId(R.id.fab_my_location)).check(matches(isDisplayed()))
        onView(withId(R.id.view_empty)).check(matches(not(isDisplayed())))
    }
}
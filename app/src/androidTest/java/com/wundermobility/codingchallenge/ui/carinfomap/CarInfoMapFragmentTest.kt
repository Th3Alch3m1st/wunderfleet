package com.wundermobility.codingchallenge.ui.carinfomap

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.wundermobility.codingchallenge.R
import com.wundermobility.codingchallenge.fakerepository.FakeCarInfoRepositoryImpl
import com.wundermobility.codingchallenge.utils.launchFragmentInHiltContainer
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.CoreMatchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import javax.inject.Inject


/**
 * Created By Rafiqul Hasan
 */
@RunWith(AndroidJUnit4ClassRunner::class)
@HiltAndroidTest
class CarInfoMapFragmentTest {
    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val taskExecutorRule = InstantTaskExecutorRule()

    @Inject
    lateinit var fakeRepositoryImpl: FakeCarInfoRepositoryImpl

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    /*
    *   Test spec
    *   name: fragment launch correctly and map view, car location and my location button is visible.
    *   steps:
    *       - [Action] launch fragment
    *       - [Assert] map_fragment view should be visible
    *       - [Assert] fab_car_location view should be visible
    *       - [Assert] fab_my_location view should be visible
    *       - [Assert] view_empty view should be invisible
    */
    @Test
    fun test_initialView_displayed() {
        //open fragment
        val mockNavController = Mockito.mock(NavController::class.java)
        launchFragmentInHiltContainer<CarInfoMapFragment>(
            null,
            R.style.Theme_WunderFleet
        ) {
            viewLifecycleOwnerLiveData.observeForever { viewLifecycleOwner ->
                if (viewLifecycleOwner != null) {
                    Navigation.setViewNavController(requireView(), mockNavController)
                }
            }
        }

        Thread.sleep(500)

        //verifying ui is present on screen
        onView(withId(R.id.map_fragment))
            .check(matches(isDisplayed()))
        onView(withId(R.id.fab_car_location))
            .check(matches(isDisplayed()))
        onView(withId(R.id.fab_my_location))
            .check(matches(isDisplayed()))
        onView(withId(R.id.view_empty))
            .check(matches(not(isDisplayed())))
    }

    /*
     *   Test spec
     *   name: if car list is empty, UI should display empty list UI and retry button will be gone.
     *   steps:
     *       - [Action] launch fragment
     *       - [Action] mock empty response scenario by setting testEmptyResponse to true
     *       - [Assert] UI should show empty UI
     *       - [Assert] retry button should be invisible
     */
    @Test
    fun test_emptyResponse_displayed() {
        fakeRepositoryImpl.testEmptyResponse = true
        //open fragment
        val mockNavController = Mockito.mock(NavController::class.java)
        launchFragmentInHiltContainer<CarInfoMapFragment>(
            null,
            R.style.Theme_WunderFleet
        ) {
            viewLifecycleOwnerLiveData.observeForever { viewLifecycleOwner ->
                if (viewLifecycleOwner != null) {
                    Navigation.setViewNavController(requireView(), mockNavController)
                }
            }
        }

        Thread.sleep(500)

        //verifying ui is present on screen

        onView(withId(R.id.view_empty))
            .check(matches(isDisplayed()))
        onView(withId(R.id.tv_error))
            .check(matches(withText(FakeCarInfoRepositoryImpl.MSG_EMPTY)))
        onView(withId(R.id.btn_retry))
            .check(matches(not(isDisplayed())))
    }

    /*
     *   Test spec
     *   name: during initial loading, error may happen. UI should display error reason with retry button.
     *   steps:
     *       - [Action] launch fragment
     *       - [Action] mock error scenario by setting testError to true
     *       - [Assert] UI should show error UI
     *       - [Assert] UI should show error message it received
     *       - [Assert] UI should show btn_retry button
     */
    @Test
    fun test_errorResponse_displayed() {
        fakeRepositoryImpl.testError = true
        //open fragment
        val mockNavController = Mockito.mock(NavController::class.java)
        launchFragmentInHiltContainer<CarInfoMapFragment>(
            null,
            R.style.Theme_WunderFleet
        ) {
            viewLifecycleOwnerLiveData.observeForever { viewLifecycleOwner ->
                if (viewLifecycleOwner != null) {
                    Navigation.setViewNavController(requireView(), mockNavController)
                }
            }
        }

        Thread.sleep(500)

        //verifying ui is present on screen
        onView(withId(R.id.view_empty))
            .check(matches(isDisplayed()))
        onView(withId(R.id.tv_error))
            .check(matches(withText(FakeCarInfoRepositoryImpl.MSG_ERROR)))
        onView(withId(R.id.btn_retry))
            .check(matches(isDisplayed()))
    }

    /*
     *   Test spec
     *   name: during initial loading, error may happen. UI should display error reason with retry button. And If retry button click it should fetch data again and error ui should be gone
     *   steps:
     *       - [Action] launch fragment
     *       - [Action] mock error scenario by setting testError to true
     *       - [Assert] UI should show error UI
     *       - [Action] setting testError to false
     *       - [Action] Click on btn_retry button
     *       - [Assert] Error UI should be gone
     */
    @Test
    fun test_errorResponse_displayed_retry_should_fetch_data() {
        fakeRepositoryImpl.testError = true
        //open fragment
        val mockNavController = Mockito.mock(NavController::class.java)
        launchFragmentInHiltContainer<CarInfoMapFragment>(
            null,
            R.style.Theme_WunderFleet
        ) {
            viewLifecycleOwnerLiveData.observeForever { viewLifecycleOwner ->
                if (viewLifecycleOwner != null) {
                    Navigation.setViewNavController(requireView(), mockNavController)
                }
            }
        }

        Thread.sleep(500)

        //verifying error ui is present on screen
        onView(withId(R.id.view_empty))
            .check(matches(isDisplayed()))
        onView(withId(R.id.tv_error))
            .check(matches(withText(FakeCarInfoRepositoryImpl.MSG_ERROR)))
        onView(withId(R.id.btn_retry))
            .check(matches(isDisplayed()))

        //resetting testError to false
        fakeRepositoryImpl.testError = false

        //click on btn_retry button
        onView(withId(R.id.btn_retry)).perform(click())

        Thread.sleep(500)

        //verifying cases
        onView(withId(R.id.view_empty))
            .check(matches(not(isDisplayed())))
        onView(withId(R.id.map_fragment))
            .check(matches(isDisplayed()))
    }
}
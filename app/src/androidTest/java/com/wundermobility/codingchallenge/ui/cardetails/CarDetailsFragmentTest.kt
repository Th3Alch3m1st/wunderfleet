package com.wundermobility.codingchallenge.ui.cardetails

import android.os.Bundle
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.wundermobility.codingchallenge.R
import com.wundermobility.codingchallenge.fakerepository.FakeCarInfoRepositoryImpl
import com.wundermobility.codingchallenge.fakerepository.FakeCarInfoRepositoryImpl.Companion.MSG_ERROR
import com.wundermobility.codingchallenge.network.model.CarInfoUIModel
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
class CarDetailsFragmentTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val taskExecutorRule = InstantTaskExecutorRule()

    @Inject
    lateinit var fakeCarInfoRepositoryImpl: FakeCarInfoRepositoryImpl

    private lateinit var bundle: Bundle
    private val carInfo by lazy {
        CarInfoUIModel(
            2,
            title = "Anton",
            latitude = 51.511998333333,
            longitude = 7.4625316666667
        )
    }

    @Before
    fun setUp() {
        hiltRule.inject()
        bundle = Bundle().apply {
            putParcelable("carInfo", carInfo)
        }
    }

    /*
    *   Test spec
    *   name: Initial item loaded. UI should display item list UI and error view will be invisible.
    *   steps:
    *       - [Action] launch fragment
    *       - [Assert] UI should show toolbar
    *       - [Assert] toolbar title should match with car info argument
    *       - [Assert] UI should show car image
    *       - [Assert] UI should show ll_car_details container
    *       - [Assert] UI should show btn_rent_this_car button
    *       - [Assert] Error UI should be invisible
    */
    @Test
    fun test_initialView_displayed() {
        //open fragment
        val mockNavController = Mockito.mock(NavController::class.java)
        launchFragmentInHiltContainer<CarDetailsFragment>(bundle, R.style.Theme_WunderFleet) {
            viewLifecycleOwnerLiveData.observeForever { viewLifecycleOwner ->
                if (viewLifecycleOwner != null) {
                    Navigation.setViewNavController(requireView(), mockNavController)
                }
            }
        }

        //verifying cases
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()))
        onView(withId(R.id.toolbar)).check(matches(hasDescendant(withText(carInfo.title))))
        onView(withId(R.id.iv_car_image)).check(matches(isDisplayed()))
        onView(withId(R.id.ll_car_details)).check(matches(isDisplayed()))
        onView(withId(R.id.btn_rent_this_car)).check(matches(isDisplayed()))
        onView(withId(R.id.view_empty)).check(matches(not(isDisplayed())))
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
        fakeCarInfoRepositoryImpl.testError = true
        //open fragment
        val mockNavController = Mockito.mock(NavController::class.java)
        launchFragmentInHiltContainer<CarDetailsFragment>(
            bundle,
            R.style.Theme_WunderFleet
        ) {
            viewLifecycleOwnerLiveData.observeForever { viewLifecycleOwner ->
                if (viewLifecycleOwner != null) {
                    Navigation.setViewNavController(requireView(), mockNavController)
                }
            }
        }

        Thread.sleep(500)

        //verifying cases
        onView(withId(R.id.view_empty))
            .check(matches(isDisplayed()))
        onView(withId(R.id.tv_error))
            .check(matches(withText(MSG_ERROR)))
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
     *       - [Assert] UI should show car image
     *       - [Assert] UI should show ll_car_details container
     *       - [Assert] UI should show btn_rent_this_car button
     */
    @Test
    fun test_errorResponse_displayed_retry_should_fetch_data() {
        fakeCarInfoRepositoryImpl.testError = true
        //open fragment
        val mockNavController = Mockito.mock(NavController::class.java)
        launchFragmentInHiltContainer<CarDetailsFragment>(
            bundle,
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
            .check(matches(withText(MSG_ERROR)))
        onView(withId(R.id.btn_retry))
            .check(matches(isDisplayed()))

        //resetting testError to false
        fakeCarInfoRepositoryImpl.testError = false

        //click on btn_retry button
        onView(withId(R.id.btn_retry)).perform(ViewActions.click())

        Thread.sleep(500)

        //verifying cases
        onView(withId(R.id.iv_car_image)).check(matches(isDisplayed()))
        onView(withId(R.id.ll_car_details)).check(matches(isDisplayed()))
        onView(withId(R.id.btn_rent_this_car)).check(matches(isDisplayed()))
        onView(withId(R.id.view_empty)).check(matches(not(isDisplayed())))
    }
}
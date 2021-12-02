package com.wundermobility.codingchallenge.testsuit

import com.wundermobility.codingchallenge.ui.MainActivityTest
import com.wundermobility.codingchallenge.ui.cardetails.CarDetailsFragmentTest
import com.wundermobility.codingchallenge.ui.carinfomap.CarInfoMapFragmentTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.runner.RunWith
import org.junit.runners.Suite

/**
 * Created By Rafiqul Hasan
 */
@ExperimentalCoroutinesApi
@RunWith(Suite::class)
@Suite.SuiteClasses(
    MainActivityTest::class,
    CarInfoMapFragmentTest::class,
    CarDetailsFragmentTest::class
)
class TestSuiteWunderFleetApp
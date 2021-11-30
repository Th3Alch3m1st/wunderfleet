package com.wundermobility.codingchallenge.ui.cardetails

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.wundermobility.codingchallenge.R
import com.wundermobility.codingchallenge.core.fragment.BaseFragment
import com.wundermobility.codingchallenge.databinding.FragmentCarDetailsBinding
import com.wundermobility.codingchallenge.ui.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created By Rafiqul Hasan
 */
@AndroidEntryPoint
class CarDetailsFragment : BaseFragment<MainViewModel, FragmentCarDetailsBinding>() {
    private val viewModel: MainViewModel by activityViewModels()
    private val args: CarDetailsFragmentArgs by navArgs()
    override val layoutResourceId: Int
        get() = R.layout.fragment_car_details

    override fun getVM(): MainViewModel = viewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.e("error", "${args.carInfo.carID}")
    }
}
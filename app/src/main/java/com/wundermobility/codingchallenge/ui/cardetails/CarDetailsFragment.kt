package com.wundermobility.codingchallenge.ui.cardetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.wundermobility.codingchallenge.R
import com.wundermobility.codingchallenge.core.fragment.BaseFragment
import com.wundermobility.codingchallenge.core.glide.GlideApp
import com.wundermobility.codingchallenge.databinding.FragmentCarDetailsBinding
import com.wundermobility.codingchallenge.databinding.LayoutCarInfoBinding
import com.wundermobility.codingchallenge.network.NetworkResult
import com.wundermobility.codingchallenge.network.model.CarInfo
import com.wundermobility.codingchallenge.ui.MainViewModel
import com.wundermobility.codingchallenge.utils.gone
import com.wundermobility.codingchallenge.utils.show
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getCarDetailsInfo(args.carInfo.carID)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initObserver()
    }


    private fun initView() {
        with(dataBinding) {

            toolbar.title = args.carInfo.title
            fragmentCallBack?.setActionBar(toolbar, true)
            btnRentThisCar.setOnClickListener {

            }
            llCarDetails.removeAllViews()
            llCarDetails.invalidate()
        }
    }

    private fun initObserver() {
        viewModel.carDetailsInfo.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    dataBinding.btnRentThisCar.show()
                    showCarDetailsView(response.data)
                }
                is NetworkResult.Error -> {
                    dataBinding.btnRentThisCar.gone()
                    dataBinding.viewEmpty.root.show()
                    dataBinding.viewEmpty.tvError.text = response.exception.message
                    dataBinding.viewEmpty.btnRetry.setOnClickListener {
                        viewModel.getCarDetailsInfo(args.carInfo.carID)
                        dataBinding.viewEmpty.root.gone()
                    }
                }
            }
        }
    }

    private fun showCarDetailsView(carInfo: CarInfo) {

        GlideApp
            .with(requireContext())
            .load(carInfo.vehicleTypeImageUrl)
            .placeholder(R.drawable.ic_car_place_holder)
            .error(R.drawable.ic_car_place_holder)
            .into(dataBinding.ivCarImage)

        addToLinearLayout(getString(R.string.car_id_x, carInfo.carId))
        carInfo.title?.let {
            addToLinearLayout(getString(R.string.title_x, it))
        }
        carInfo.licencePlate?.let {
            addToLinearLayout(getString(R.string.licence_plate_x, it))
        }
        carInfo.hardwareId?.let {
            addToLinearLayout(getString(R.string.hardware_id_x, it))
        }
        carInfo.vehicleStateId?.let {
            addToLinearLayout(getString(R.string.vehicle_state_id_x, it))
        }
        carInfo.vehicleTypeId?.let {
            addToLinearLayout(getString(R.string.vehicle_type_id_x, it))
        }

        carInfo.isClean?.let {
            addToLinearLayout(getString(R.string.is_clean_x, it.toString()))
        }
        carInfo.fuelLevel?.let {
            addToLinearLayout(getString(R.string.fuel_level_x, it))
        }
        carInfo.pricingTime?.let {
            addToLinearLayout(getString(R.string.pricing_time_x, it))
        }
        carInfo.pricingParking?.let {
            addToLinearLayout(getString(R.string.pricing_parking_x, it))
        }
        carInfo.isActivatedByHardware?.let {
            addToLinearLayout(getString(R.string.is_activated_by_hardware_x, it.toString()))
        }
        carInfo.locationId?.let {
            addToLinearLayout(getString(R.string.location_id_x, it))
        }
        addToLinearLayout(getString(R.string.lat_x, carInfo.lat))
        addToLinearLayout(getString(R.string.lon_x, carInfo.lon))
        carInfo.address?.let {
            addToLinearLayout(getString(R.string.address_x, it))
        }
        carInfo.zipCode?.let {
            addToLinearLayout(getString(R.string.zipCode_x, it))
        }
        carInfo.reservationState?.let {
            addToLinearLayout(getString(R.string.reservation_state_x, it))
        }
        carInfo.isDamaged?.let {
            addToLinearLayout(getString(R.string.is_damaged_x, it.toString()))
        }
        carInfo.damageDescription?.let {
            addToLinearLayout(getString(R.string.damage_description_x, it))
        }

    }

    private fun addToLinearLayout(info: String) {
        val binding =
            LayoutCarInfoBinding.inflate(LayoutInflater.from(context))
        binding.tvCarDetails.text = info
        dataBinding.llCarDetails.addView(binding.root)
    }
}
package com.wundermobility.codingchallenge.ui.cardetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.wundermobility.codingchallenge.R
import com.wundermobility.codingchallenge.core.fragment.BaseFragment
import com.wundermobility.codingchallenge.core.glide.GlideApp
import com.wundermobility.codingchallenge.databinding.FragmentCarDetailsBinding
import com.wundermobility.codingchallenge.databinding.LayoutCarInfoBinding
import com.wundermobility.codingchallenge.network.NetworkResult
import com.wundermobility.codingchallenge.network.model.CarInfo
import com.wundermobility.codingchallenge.network.model.CarRentRequestBody
import com.wundermobility.codingchallenge.ui.MainViewModel
import com.wundermobility.codingchallenge.utils.gone
import com.wundermobility.codingchallenge.utils.safeNavigate
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
            llCarDetails.removeAllViews()
            llCarDetails.invalidate()

            btnRentThisCar.setOnClickListener {
                viewModel.rentCar(CarRentRequestBody(args.carInfo.carID))
            }
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
                    showErrorUI(response)
                }
            }
        }

        viewModel.carRentResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    navigateToRentSuccessDialog()
                }

                is NetworkResult.Error -> {
                    Toast.makeText(
                        requireContext(),
                        response.exception.message,
                        Toast.LENGTH_LONG
                    ).show()
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

        dataBinding.btnRentThisCar.show()

        addToLinearLayout(getString(R.string.car_id_x, carInfo.carId))
        if (!carInfo.title.isNullOrEmpty())
            addToLinearLayout(getString(R.string.title_x, carInfo.title))

        if (!carInfo.licencePlate.isNullOrEmpty())
            addToLinearLayout(getString(R.string.licence_plate_x, carInfo.licencePlate))

        if (!carInfo.hardwareId.isNullOrEmpty())
            addToLinearLayout(getString(R.string.hardware_id_x, carInfo.hardwareId))

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

        if (!carInfo.pricingTime.isNullOrEmpty())
            addToLinearLayout(getString(R.string.pricing_time_x, carInfo.pricingTime))

        if (!carInfo.pricingParking.isNullOrEmpty())
            addToLinearLayout(getString(R.string.pricing_parking_x, carInfo.pricingParking))

        carInfo.isActivatedByHardware?.let {
            addToLinearLayout(getString(R.string.is_activated_by_hardware_x, it.toString()))
        }
        carInfo.locationId?.let {
            addToLinearLayout(getString(R.string.location_id_x, it))
        }
        addToLinearLayout(getString(R.string.lat_x, carInfo.lat))
        addToLinearLayout(getString(R.string.lon_x, carInfo.lon))

        if (!carInfo.address?.trim().isNullOrEmpty())
            addToLinearLayout(getString(R.string.address_x, carInfo.address))

        if (!carInfo.zipCode.isNullOrEmpty())
            addToLinearLayout(getString(R.string.zipCode_x, carInfo.zipCode))

        carInfo.reservationState?.let {
            addToLinearLayout(getString(R.string.reservation_state_x, it))
        }
        carInfo.isDamaged?.let {
            addToLinearLayout(getString(R.string.is_damaged_x, it.toString()))
        }
        if (!carInfo.damageDescription.isNullOrEmpty())
            addToLinearLayout(getString(R.string.damage_description_x, carInfo.damageDescription))
    }

    private fun addToLinearLayout(info: String) {
        val binding =
            LayoutCarInfoBinding.inflate(LayoutInflater.from(context))
        binding.tvCarDetails.text = info
        dataBinding.llCarDetails.addView(binding.root)
    }

    private fun showErrorUI(response: NetworkResult.Error) {
        dataBinding.btnRentThisCar.gone()
        dataBinding.viewEmpty.root.show()
        dataBinding.viewEmpty.tvError.text = response.exception.message
        dataBinding.viewEmpty.btnRetry.setOnClickListener {
            viewModel.getCarDetailsInfo(args.carInfo.carID)
            dataBinding.viewEmpty.root.gone()
        }
    }

    private fun navigateToRentSuccessDialog() {
        val direction =
            CarDetailsFragmentDirections.actionCarDetailsFragmentToRentCarSuccessDialog()
        findNavController().safeNavigate(direction)
    }
}
package com.wundermobility.codingchallenge.ui.carinfomap

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.wundermobility.codingchallenge.R
import com.wundermobility.codingchallenge.core.fragment.BaseFragment
import com.wundermobility.codingchallenge.databinding.FragmentCarInfoMapBinding
import com.wundermobility.codingchallenge.network.model.CarInfoUIModel
import com.wundermobility.codingchallenge.ui.MainViewModel
import com.wundermobility.codingchallenge.utils.safeNavigate
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created By Rafiqul Hasan
 */
@AndroidEntryPoint
class CarInfoMapFragment : BaseFragment<MainViewModel, FragmentCarInfoMapBinding>(),
    OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var mMap: GoogleMap

    private var selectedCarInfo: CarInfoUIModel? = null

    private val markerList: MutableList<Marker> = mutableListOf()

    override val layoutResourceId: Int
        get() = R.layout.fragment_car_info_map

    override fun getVM(): MainViewModel = viewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getCarList()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.map_fragment) as? SupportMapFragment
        mapFragment?.getMapAsync(this)
        addOnBackPressListener()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // removing all marker
        for (marker in markerList) {
            marker.remove()
        }
        markerList.clear()
    }

    override fun onMapReady(map: GoogleMap) {
        mMap = map
        mMap.setOnMarkerClickListener(this)
        initCarListObserver()
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        selectedCarInfo?.let { carInfo ->
            val currentClickedCarInfo = marker.tag as CarInfoUIModel
            if (carInfo.carID == currentClickedCarInfo.carID) {
                navigateToCarDetails(currentClickedCarInfo)
            }
        } ?: run {
            selectedCarInfo = marker.tag as CarInfoUIModel
            //when user select a item hide all other marker
            setVisibilityStatusForMarker(marker.tag as CarInfoUIModel, false)
            showToolbarBackIcon()
        }

        return false
    }

    private fun initCarListObserver() {
        viewModel.carList.observe(viewLifecycleOwner) { carList ->
            setMarkerOnMap(carList)
        }
    }

    private fun setMarkerOnMap(carList: List<CarInfoUIModel>) {
        val builder = LatLngBounds.Builder()

        for (car in carList) {
            val point = LatLng(car.latitude, car.longitude)
            val markerOptions = MarkerOptions()
            with(markerOptions)
            {
                position(point)
                title(car.title)
            }

            val marker = mMap.addMarker(markerOptions).apply {
                this?.tag = car
            }
            marker?.let {
                markerList.add(marker)
            }

            //setting state when fragment back from fragment details
            selectedCarInfo?.let { sCarInfo ->
                if (sCarInfo.carID == car.carID) {
                    marker?.showInfoWindow()
                }
            }
            builder.include(point)
        }
        val bounds = builder.build()
        val boundPadding = resources.getDimension(R.dimen._40sdp).toInt()

        var cameraUpdate: CameraUpdate? = null

        selectedCarInfo?.let { sCarInfo ->
            //setting state when fragment back from fragment details and moving camera to previous selected item
            showToolbarBackIcon()
            setVisibilityStatusForMarker(sCarInfo, false)
            cameraUpdate = CameraUpdateFactory.newLatLng(
                LatLng(
                    sCarInfo.latitude,
                    sCarInfo.longitude
                )
            )
        } ?: run {
            cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, boundPadding)
        }

        mMap.animateCamera(cameraUpdate!!)
    }

    /**
     * setting-up toolbar back button, when user selected a marker and on press back button user able to see all marker again
     */
    private fun showToolbarBackIcon() {
        dataBinding.toolbar.setNavigationIcon(R.drawable.ic_back)
        dataBinding.toolbar.setNavigationOnClickListener {
            //on press back button showing all marker again for selection
            showMarkerAndHideToolbarBackIcon()
        }
    }

    private fun showMarkerAndHideToolbarBackIcon() {
        setVisibilityStatusForMarker(
            selectedCarInfo!!,
            visibilityStatus = true,
            hideInfoWindow = true
        )
        dataBinding.toolbar.navigationIcon = null
        selectedCarInfo = null
    }

    /**
     * show and hide marker
     */
    private fun setVisibilityStatusForMarker(
        selectedCar: CarInfoUIModel,
        visibilityStatus: Boolean,
        hideInfoWindow: Boolean = false
    ) {
        for (marker in markerList) {
            val carInfo = marker.tag as CarInfoUIModel
            if (carInfo.carID != selectedCar.carID) {
                marker.isVisible = visibilityStatus
            } else {
                if (hideInfoWindow) {
                    marker.hideInfoWindow()
                }
            }
        }
    }

    private fun navigateToCarDetails(carInfo: CarInfoUIModel) {
        val direction =
            CarInfoMapFragmentDirections.actionFragmentCarListToCarDetailFragment(carInfo)
        findNavController().safeNavigate(direction)
    }

    private fun addOnBackPressListener() {
        view?.isFocusableInTouchMode = true
        view?.requestFocus()
        view?.setOnKeyListener { _, keyCode, _ ->
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                if (selectedCarInfo != null) {
                    showMarkerAndHideToolbarBackIcon()
                    true
                } else {
                    false
                }
            } else false
        }
    }
}
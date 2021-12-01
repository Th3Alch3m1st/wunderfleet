package com.wundermobility.codingchallenge.ui.carinfomap

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.google.android.gms.tasks.Task
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.CompositePermissionListener
import com.karumi.dexter.listener.single.PermissionListener
import com.karumi.dexter.listener.single.SnackbarOnDeniedPermissionListener
import com.wundermobility.codingchallenge.R
import com.wundermobility.codingchallenge.core.fragment.BaseFragment
import com.wundermobility.codingchallenge.databinding.FragmentCarInfoMapBinding
import com.wundermobility.codingchallenge.network.NetworkResult
import com.wundermobility.codingchallenge.network.model.CarInfoUIModel
import com.wundermobility.codingchallenge.ui.MainViewModel
import com.wundermobility.codingchallenge.utils.gone
import com.wundermobility.codingchallenge.utils.safeNavigate
import com.wundermobility.codingchallenge.utils.show
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created By Rafiqul Hasan
 */
@AndroidEntryPoint
class CarInfoMapFragment : BaseFragment<MainViewModel, FragmentCarInfoMapBinding>(),
    OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    companion object {
        const val REQUEST_CHECK_SETTINGS = 5647
    }

    private val viewModel: MainViewModel by activityViewModels()
    private val markerList by lazy { mutableListOf<Marker>() }

    private lateinit var mMap: GoogleMap
    private var mFusedLocationClient: FusedLocationProviderClient? = null
    private var carMapCameraBounds: CameraUpdate? = null

    private var selectedCarInfo: CarInfoUIModel? = null
    private var myLocationMarker: Marker? = null


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
        initClickListener()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // removing all marker
        for (marker in markerList) {
            marker.remove()
        }
        markerList.clear()
        myLocationMarker?.remove()
        carMapCameraBounds = null
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode == Activity.RESULT_OK) {
                data?.let {
                    val state = LocationSettingsStates.fromIntent(it)
                    if (state?.isGpsUsable == true) {
                        getCurrentLocation()
                    }
                }
            }
        }
    }

    override fun onMapReady(map: GoogleMap) {
        mMap = map
        mMap.setOnMarkerClickListener(this)

        //info window click listener
        mMap.setOnInfoWindowClickListener { marker ->
            val currentClickedCarInfo = marker.tag as? CarInfoUIModel
            currentClickedCarInfo?.let {
                if (selectedCarInfo?.carID == currentClickedCarInfo.carID) {
                    navigateToCarDetails(currentClickedCarInfo)
                }
            }

        }
        initCarListObserver()
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        selectedCarInfo?.let { carInfo ->
            val currentClickedCarInfo = marker.tag as? CarInfoUIModel
            currentClickedCarInfo?.let {
                if (carInfo.carID == currentClickedCarInfo.carID) {
                    navigateToCarDetails(currentClickedCarInfo)
                }
            }
        } ?: run {
            selectedCarInfo = marker.tag as? CarInfoUIModel
            //when user select a item hide all other marker
            selectedCarInfo?.let {
                setVisibilityStatusForMarker(marker.tag as CarInfoUIModel, false)
                showToolbarBackIcon()
            }
        }

        return false
    }

    private fun initClickListener() {
        dataBinding.fabMyLocation.setOnClickListener {
            checkPermissionAndEnableMyLocation()
        }

        dataBinding.fabCarLocation.setOnClickListener {
            carMapCameraBounds?.let { cameraUpdate ->
                mMap.animateCamera(cameraUpdate)
            }
        }
    }


    private fun initCarListObserver() {
        viewModel.carList.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    setMarkerOnMap(response.data)
                }

                is NetworkResult.Error -> {
                    showErrorUI(response)
                }
            }
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

        selectedCarInfo?.let { sCarInfo ->
            //setting state when fragment back from fragment details and moving camera to previous selected item
            showToolbarBackIcon()
            setVisibilityStatusForMarker(sCarInfo, false)
            carMapCameraBounds = CameraUpdateFactory.newLatLng(
                LatLng(
                    sCarInfo.latitude,
                    sCarInfo.longitude
                )
            )
        } ?: run {
            carMapCameraBounds = CameraUpdateFactory.newLatLngBounds(bounds, boundPadding)
        }

        mMap.animateCamera(carMapCameraBounds!!)
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
        selectedCarInfo?.let { carInfo ->
            setVisibilityStatusForMarker(carInfo, visibilityStatus = true, hideInfoWindow = true)
        }
        dataBinding.toolbar.navigationIcon = null
        selectedCarInfo = null
    }

    /**
     * show and hide marker
     * and when hideInfoWindow = true, hide selected marker info window when toolbar back button is presses
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

    private fun showErrorUI(response: NetworkResult.Error) {
        dataBinding.viewEmpty.root.show()
        dataBinding.viewEmpty.tvError.text = response.exception.message
        dataBinding.viewEmpty.btnRetry.setOnClickListener {
            viewModel.getCarList()
            dataBinding.viewEmpty.root.gone()
        }
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

    private fun checkPermissionAndEnableMyLocation() {
        val snackBarPermissionListener: PermissionListener =
            SnackbarOnDeniedPermissionListener.Builder
                .with(view, "Location permission is needed to show your current location")
                .withOpenSettingsButton("Settings")
                .build()

        val permissionListener = object : PermissionListener {
            override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                activity?.let { act ->
                    mFusedLocationClient = LocationServices.getFusedLocationProviderClient(act)
                    checkIfGpsIsEnable()
                }
            }

            override fun onPermissionDenied(response: PermissionDeniedResponse?) {

            }

            override fun onPermissionRationaleShouldBeShown(
                request: PermissionRequest?,
                token: PermissionToken?
            ) {
                token?.continuePermissionRequest()
            }

        }

        val compositeListener = CompositePermissionListener(
            snackBarPermissionListener,
            permissionListener
        )

        Dexter.withContext(requireContext())
            .withPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
            .withListener(compositeListener)
            .check()
    }

    @SuppressLint("MissingPermission")
    private fun checkIfGpsIsEnable() {

        activity?.let {
            val builder = LocationSettingsRequest.Builder()
                .addLocationRequest(getLocationRequestBalance())
                .setNeedBle(true)

            val client: SettingsClient = LocationServices.getSettingsClient(it)
            val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())

            task.addOnSuccessListener {
                getCurrentLocation()
            }

            task.addOnFailureListener { exception ->
                if (exception is ResolvableApiException) {
                    // Location settings are not satisfied, but this can be fixed
                    // by showing the user a dialog.
                    activity?.let {
                        exception.startResolutionForResult(
                            activity as AppCompatActivity,
                            REQUEST_CHECK_SETTINGS
                        )
                    }
                }
            }


        }
    }

    private fun getLocationRequestBalance(): LocationRequest {
        val request = LocationRequest.create()
        request.interval = 2000
        request.fastestInterval = 1000
        request.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        return request
    }

    @SuppressLint("MissingPermission")
    private fun getCurrentLocation() {
        mFusedLocationClient?.lastLocation?.addOnSuccessListener { location ->
            location?.let {
                val latLon = LatLng(it.latitude, it.longitude)
                setMyLocationMarker(latLon)
                val cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLon, 14.0f)
                mMap.animateCamera(cameraUpdate)
            }
        }
    }

    private fun setMyLocationMarker(latLon: LatLng) {
        myLocationMarker?.remove()
        myLocationMarker =
            mMap.addMarker(
                MarkerOptions()
                    .position(latLon)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                    .title("My Current Location")
            )
    }

}
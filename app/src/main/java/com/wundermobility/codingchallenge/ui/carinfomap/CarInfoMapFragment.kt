package com.wundermobility.codingchallenge.ui.carinfomap

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.wundermobility.codingchallenge.R
import com.wundermobility.codingchallenge.core.fragment.BaseFragment
import com.wundermobility.codingchallenge.databinding.FragmentCarInfoMapBinding
import com.wundermobility.codingchallenge.network.model.CarInfoUIModel
import com.wundermobility.codingchallenge.ui.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created By Rafiqul Hasan
 */
@AndroidEntryPoint
class CarInfoMapFragment : BaseFragment<MainViewModel, FragmentCarInfoMapBinding>(),
    OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var mMap: GoogleMap
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
    }

    override fun onMapReady(map: GoogleMap) {
        mMap = map
        mMap.setOnMarkerClickListener(this)
        initCarListObserver()
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        showHideMarker(marker.tag as CarInfoUIModel, false)

        dataBinding.toolbar.setNavigationIcon(R.drawable.ic_back)
        dataBinding.toolbar.setNavigationOnClickListener {
            showHideMarker(marker.tag as CarInfoUIModel,true)
            dataBinding.toolbar.navigationIcon = null
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
            builder.include(point)
        }

        val bounds = builder.build()
        val boundPadding = resources.getDimension(R.dimen._40sdp).toInt()
        val cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, boundPadding)
        mMap.animateCamera(cameraUpdate)
    }

    private fun showHideMarker(selectedCar: CarInfoUIModel, visibilityStatus: Boolean) {
        for (marker in markerList) {
            val carInfo = marker.tag as CarInfoUIModel
            if (carInfo.carID != selectedCar.carID) {
                marker.isVisible = visibilityStatus
            }
        }
    }
}
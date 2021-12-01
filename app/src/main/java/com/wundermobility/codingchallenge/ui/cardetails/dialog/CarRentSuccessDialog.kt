package com.wundermobility.codingchallenge.ui.cardetails.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.wundermobility.codingchallenge.R
import com.wundermobility.codingchallenge.databinding.BottomSheetDialogCarRentSuccessBinding
import com.wundermobility.codingchallenge.utils.autoCleared

/**
 * Created By Rafiqul Hasan
 */
class CarRentSuccessDialog : BottomSheetDialogFragment() {
    private var dataBinding: BottomSheetDialogCarRentSuccessBinding by autoCleared()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dataBinding = BottomSheetDialogCarRentSuccessBinding.inflate(inflater)
        dataBinding.lifecycleOwner = viewLifecycleOwner
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataBinding.btnClose.setOnClickListener {
            dismiss()
        }
    }
}
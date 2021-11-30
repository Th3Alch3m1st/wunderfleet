package com.wundermobility.codingchallenge.core.fragment

import androidx.appcompat.widget.Toolbar

/**
 * Created by Rafiqul Hasan
 */
interface FragmentCommunicator {
    fun setActionBar(toolbar: Toolbar, enableBackButton: Boolean)
    fun showLoader()
    fun hideLoader()
}
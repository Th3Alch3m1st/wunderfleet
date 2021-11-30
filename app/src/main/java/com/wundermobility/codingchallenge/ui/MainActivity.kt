package com.wundermobility.codingchallenge.ui

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.wundermobility.codingchallenge.R
import com.wundermobility.codingchallenge.core.fragment.FragmentCommunicator
import com.wundermobility.codingchallenge.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by Rafiqul Hasan
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity(), FragmentCommunicator {
    private lateinit var dataBinding: ActivityMainBinding

    private val loaderDialog: AlertDialog by lazy {
        val builder = MaterialAlertDialogBuilder(this@MainActivity, R.style.LoaderDialog)
        val dialogView = LayoutInflater.from(this@MainActivity)
            .inflate(R.layout.dialog_loader, findViewById(android.R.id.content), false)
        builder.setView(dialogView)
        builder.setCancelable(false)
        return@lazy builder.create().apply {
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun setActionBar(toolbar: Toolbar, enableBackButton: Boolean) {
        setSupportActionBar(toolbar)
        if (enableBackButton) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setHomeButtonEnabled(true)
            supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
        }
    }

    override fun showLoader() {
        runOnUiThread {
            if (!loaderDialog.isShowing) {
                loaderDialog.show()
            }
        }
    }

    override fun hideLoader() {
        runOnUiThread {
            if (loaderDialog.isShowing) {
                loaderDialog.dismiss()
            }
        }
    }
}
package com.wundermobility.codingchallenge.core.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.wundermobility.codingchallenge.core.viewmodel.BaseViewModel
import com.wundermobility.codingchallenge.utils.autoCleared
import kotlin.properties.Delegates

/**
 * Created by Rafiqul Hasan
 */
abstract class BaseFragment<ViewModel : BaseViewModel, DataBinding : ViewDataBinding> : Fragment() {
    protected var fragmentCallBack: FragmentCommunicator? = null
    protected var dataBinding: DataBinding by autoCleared()
    private var viewModel: ViewModel by Delegates.notNull()

    /** Override to set layout resource id
     * @return layout resource id
     */
    @get:LayoutRes
    abstract val layoutResourceId: Int

    /**
     * Override to get viewModel
     * @return view model instance
     */
    abstract fun getVM(): ViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        fragmentCallBack = context as? FragmentCommunicator
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = getVM()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = DataBindingUtil.inflate(inflater, layoutResourceId, container, false)
        dataBinding.lifecycleOwner = viewLifecycleOwner
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserver()
    }

    private fun initObserver() {
        viewModel.showLoader.observe(viewLifecycleOwner) {
            if (it)
                fragmentCallBack?.showLoader()
            else
                fragmentCallBack?.hideLoader()
        }

        viewModel.toastMessage.observe(viewLifecycleOwner) { msg ->
            msg?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                viewModel.toastMessage.postValue(null)
            }
        }
    }

}
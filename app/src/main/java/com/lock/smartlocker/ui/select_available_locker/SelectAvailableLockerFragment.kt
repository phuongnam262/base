package com.lock.smartlocker.ui.select_available_locker

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.lock.smartlocker.BR
import com.lock.smartlocker.R
import com.lock.smartlocker.data.models.ItemReturn
import com.lock.smartlocker.databinding.FragmentSelectAvailableLockerBinding
import com.lock.smartlocker.ui.base.BaseFragment
import com.lock.smartlocker.ui.input_serial_number.InputSerialNumberFragment
import com.lock.smartlocker.ui.select_available_locker.adapter.AvailableLockerItem
import com.lock.smartlocker.util.ConstantUtils
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance


class SelectAvailableLockerFragment : BaseFragment<FragmentSelectAvailableLockerBinding, SelectAvailableLockerViewModel>(),
    KodeinAware,
    View.OnClickListener,
    SelectAvailableLockerListener {

    override val kodein by kodein()
    private val factory: SelectAvailableLockerViewModelFactory by instance()
    override val layoutId: Int = R.layout.fragment_select_available_locker
    override val bindingVariable: Int
        get() = BR.viewmodel

    override val viewModel: SelectAvailableLockerViewModel
        get() = ViewModelProvider(this, factory)[SelectAvailableLockerViewModel::class.java]

    private val availableLockerAdapter = GroupAdapter<GroupieViewHolder>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.selectAvailableListener = this
        initView()
        initData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.selectAvailableListener = null
    }

    private fun initView(){
        viewModel.titlePage.postValue(getString(R.string.select_an_available_locker))
        mViewDataBinding?.bottomMenu?.rlHome?.setOnClickListener(this)
        mViewDataBinding?.bottomMenu?.btnProcess?.setOnClickListener(this)
        mViewDataBinding?.headerBar?.ivBack?.setOnClickListener(this)
        mViewDataBinding?.rvLockers?.adapter = availableLockerAdapter

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initData(){
        if (arguments?.getString(InputSerialNumberFragment.TYPE_INPUT_SERIAL) != null) {
            viewModel.isReturnFlow = arguments?.getString(InputSerialNumberFragment.TYPE_INPUT_SERIAL) == ConstantUtils.TYPE_RETURN
            viewModel.typeInput.value = arguments?.getString(InputSerialNumberFragment.TYPE_INPUT_SERIAL)
        }

        if (viewModel.isReturnFlow.not()) mViewDataBinding?.headerBar?.ivBack?.visibility = View.GONE

        viewModel.getListReturnAvailableLockers()
        viewModel.lockers.observe(viewLifecycleOwner) { lockers ->
            availableLockerAdapter.update(lockers.map { activity?.let { it1 ->
                AvailableLockerItem(
                    it1, it, viewModel)
            } })
        }
        viewModel.selectedLocker.observe(viewLifecycleOwner) {
            availableLockerAdapter.notifyDataSetChanged() // Cập nhật adapter khi locker được chọn thay đổi
        }

    }

    override fun onClick(v: View?) {
        if (checkDebouncedClick()) {
            when (v?.id) {
                R.id.rl_home -> activity?.finish()
                R.id.iv_back -> activity?.supportFragmentManager?.popBackStack()
                R.id.btn_process -> {
                    viewModel.openLocker()
                }
            }
        }
    }

    override fun sendCommandOpenLockerSuccess(doorStatus: Int) {
        val returnItem = arguments?.getSerializable(InputSerialNumberFragment.RETURN_ITEM_REQUEST_KEY) as? ItemReturn
        returnItem?.lockerId = viewModel.selectedLocker.value?.lockerId.toString()
        returnItem?.lockerName = viewModel.selectedLocker.value?.name.toString()
        returnItem?.doorStatus = doorStatus
        returnItem?.arrowPosition = viewModel.selectedLocker.value?.arrowPosition ?: 0
        val bundle = Bundle().apply {
            putSerializable( InputSerialNumberFragment.RETURN_ITEM_REQUEST_KEY, returnItem)
            putString(InputSerialNumberFragment.TYPE_INPUT_SERIAL, viewModel.typeInput.value)
        }
        if(viewModel.isReturnFlow)
            navigateTo(R.id.action_selectAvailableLockerFragment_to_depositItemFragment, bundle)
        else
            navigateTo(R.id.action_selectAvailableLockerFragment2_to_depositItemFragment2, bundle)
    }

}
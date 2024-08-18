package com.lock.smartlocker.ui.select_available_locker

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.lock.smartlocker.BR
import com.lock.smartlocker.R
import com.lock.smartlocker.data.entities.request.ReturnItemRequest
import com.lock.smartlocker.databinding.FragmentSelectAvailableLockerBinding
import com.lock.smartlocker.ui.base.BaseFragment
import com.lock.smartlocker.ui.select_available_locker.adapter.AvailableLockerItem
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


    val availableLockerAdapter = GroupAdapter<GroupieViewHolder>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.selectAvailableListener = this
        initView()
        initData()
    }

    private fun initView(){
        viewModel.titlePage.postValue(getString(R.string.select_an_available_locker))

        mViewDataBinding?.bottomMenu?.rlHome?.setOnClickListener(this)
        mViewDataBinding?.bottomMenu?.btnProcess?.setOnClickListener(this)
        mViewDataBinding?.headerBar?.ivBack?.setOnClickListener(this)

        mViewDataBinding?.rvLockers?.adapter = availableLockerAdapter

    }

    private fun initData(){
        viewModel.loadListAvailableLockers()
        viewModel.lockers.observe(viewLifecycleOwner) { lockers ->
            availableLockerAdapter.update(lockers.map { AvailableLockerItem(it, viewModel) })
        }
        viewModel.selectedLocker.observe(viewLifecycleOwner) {
            availableLockerAdapter.notifyDataSetChanged() // Cập nhật adapter khi locker được chọn thay đổi
        }

    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.rl_home -> activity?.finish()
            R.id.iv_back -> activity?.onBackPressedDispatcher?.onBackPressed()
            R.id.btn_process -> {
                viewModel.openLocker()
                val returnItemRequest = arguments?.getSerializable("return_item_request") as? ReturnItemRequest
                val bundle = Bundle().apply {
                    putSerializable( "return_item_request", returnItemRequest)
                }
                navigateTo(R.id.action_selectAvailableLockerFragment_to_depositItemFragment, bundle)
            }
        }
    }

}
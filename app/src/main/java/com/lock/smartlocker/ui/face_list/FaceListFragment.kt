package com.lock.smartlocker.ui.face_list

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.lock.smartlocker.BR
import com.lock.smartlocker.R
import com.lock.smartlocker.databinding.FragmentFaceListBinding
import com.lock.smartlocker.ui.base.BaseFragment
import com.lock.smartlocker.ui.face_list.adapter.FaceItem
import com.lock.smartlocker.ui.input_serial_number.InputSerialNumberFragment
import com.lock.smartlocker.util.view.custom.CustomConfirmDialog
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance


class FaceListFragment : BaseFragment<FragmentFaceListBinding, FaceListViewModel>(),
    KodeinAware,
    View.OnClickListener,
    FaceListListener, CustomConfirmDialog.ConfirmationDialogListener {

    override val kodein by kodein()
    private val factory: FaceListViewModelFactory by instance()
    override val layoutId: Int = R.layout.fragment_face_list
    override val bindingVariable: Int
        get() = BR.viewmodel

    override val viewModel: FaceListViewModel
        get() = ViewModelProvider(this, factory)[FaceListViewModel::class.java]

    private val faceAdapter = GroupAdapter<GroupieViewHolder>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.faceListener = this
        initView()
        initData()
    }

    private fun initView(){
        viewModel.titlePage.postValue(getString(R.string.face_list))
        mViewDataBinding?.bottomMenu?.rlHome?.setOnClickListener(this)
        mViewDataBinding?.bottomMenu?.btnProcess?.text = getString(R.string.remove_all_button)
        mViewDataBinding?.bottomMenu?.btnProcess?.setOnClickListener(this)
        mViewDataBinding?.headerBar?.ivBack?.setOnClickListener(this)
        mViewDataBinding?.rvFaces?.adapter = faceAdapter
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initData(){
        viewModel.loadListFaces()
        viewModel.faces.observe(viewLifecycleOwner) { faces ->
            faceAdapter.update(faces?.map { activity?.let { it1 ->
                FaceItem(
                    it1, it, viewModel)
            } } ?: emptyList())
        }
        viewModel.faces.observe(viewLifecycleOwner) {
            faceAdapter.notifyDataSetChanged()
            if (it.isEmpty()) dataEmpty()
        }
    }

    override fun onClick(v: View?) {
        if (checkDebouncedClick()) {
            when (v?.id) {
                R.id.rl_home -> activity?.finish()
                R.id.iv_back -> activity?.onBackPressedDispatcher?.onBackPressed()
                R.id.btn_process -> {
                    val dialog = CustomConfirmDialog.newInstance(
                        message = getString(R.string.dialog_remove_all),
                    )
                    dialog.show(
                        childFragmentManager,
                        InputSerialNumberFragment.CONFIRMATION_DIALOG_TAG
                    )
                }
            }
        }
    }

    override fun dataEmpty() {
        mViewDataBinding?.bottomMenu?.btnProcess?.isEnabled = false
        mViewDataBinding?.bottomMenu?.btnProcess?.alpha = 0.3f
    }

    override fun onDialogConfirmClick(dialogTag: String?) {
        viewModel.removeAllFace()
    }

    override fun onDialogCancelClick() {
    }

}
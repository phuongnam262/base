package com.lock.smartlocker.ui.setting

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.lock.smartlocker.BR
import com.lock.smartlocker.R
import com.lock.smartlocker.data.preference.PreferenceHelper
import com.lock.smartlocker.databinding.FragmentSettingBinding
import com.lock.smartlocker.ui.base.BaseActivity
import com.lock.smartlocker.ui.base.BaseFragment
import com.lock.smartlocker.ui.home.HomeActivity
import com.lock.smartlocker.util.AppTimer
import com.lock.smartlocker.util.ConstantUtils
import com.lock.smartlocker.util.CountdownTimer
import com.lock.smartlocker.util.KioskModeHelper
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance


class SettingFragment : BaseFragment<FragmentSettingBinding, SettingViewModel>(),
    KodeinAware, View.OnClickListener {

    override val kodein by kodein()
    private val factory: SettingViewModelFactory by instance()
    override val layoutId: Int = R.layout.fragment_setting
    override val bindingVariable: Int
        get() = BR.viewmodel
    override val viewModel: SettingViewModel
        get() = ViewModelProvider(this, factory)[SettingViewModel::class.java]
    private val REQUEST_CODE_PICK_VIDEO = 111
    private val REQUEST_CODE_PICK_MUSIC = 222

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initData()
    }

    private fun initView(){
        viewModel.titlePage.postValue(getString(R.string.setting))
        mViewDataBinding?.bottomMenu?.rlHome?.setOnClickListener(this)
        mViewDataBinding?.headerBar?.ivBack?.setOnClickListener(this)

        mViewDataBinding?.swNavigation?.isChecked = PreferenceHelper.getBoolean(ConstantUtils.NAVIGATION_ON, true)
        mViewDataBinding?.swStatus?.isChecked = PreferenceHelper.getBoolean(ConstantUtils.STATUS_ON, true)
        mViewDataBinding?.swLight?.isChecked = PreferenceHelper.getBoolean(ConstantUtils.LIGHT_ON, false)
        mViewDataBinding?.swNavigation?.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked){
                activity?.let { KioskModeHelper.sendCommand(it, KioskModeHelper.Action.SHOW_NAVIGATION_BAR) }
                PreferenceHelper.writeBoolean(ConstantUtils.NAVIGATION_ON, true)
            }else{
                activity?.let { KioskModeHelper.sendCommand(it, KioskModeHelper.Action.HIDE_NAVIGATION_BAR) }
                PreferenceHelper.writeBoolean(ConstantUtils.NAVIGATION_ON, false)
            }
        }
        mViewDataBinding?.swStatus?.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked){
                activity?.let { KioskModeHelper.sendCommand(it, KioskModeHelper.Action.SHOW_STATUS_BAR) }
                PreferenceHelper.writeBoolean(ConstantUtils.STATUS_ON, true)
            }else {
                activity?.let { KioskModeHelper.sendCommand(it, KioskModeHelper.Action.HIDE_STATUS_BAR) }
                PreferenceHelper.writeBoolean(ConstantUtils.STATUS_ON, false)
            }
        }
        mViewDataBinding?.swLight?.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked){
                PreferenceHelper.writeBoolean(ConstantUtils.LIGHT_ON, true)
            }else {
                PreferenceHelper.writeBoolean(ConstantUtils.LIGHT_ON, false)
            }
        }

        mViewDataBinding?.etMediaPath?.text = PreferenceHelper.getString(ConstantUtils.MEDIA_PATH, "")
        mViewDataBinding?.etMusic?.text = PreferenceHelper.getString(ConstantUtils.BACKGROUND_MUSIC, "")
        mViewDataBinding?.etTimeout?.setText(PreferenceHelper.getString(ConstantUtils.TIME_BACK_HOME, "1"))
        mViewDataBinding?.cbSound?.isChecked = PreferenceHelper.getBoolean(ConstantUtils.MEDIA_SOUND_ENABLE, false)
        mViewDataBinding?.swEnableMedia?.isChecked = PreferenceHelper.getBoolean(ConstantUtils.MEDIA_ENABLE, false)
        mViewDataBinding?.swEnableMusic?.isChecked = PreferenceHelper.getBoolean(ConstantUtils.BACKGROUND_MUSIC_ENABLE, false)
        if (PreferenceHelper.getString(ConstantUtils.MEDIA_PATH, "").isNullOrEmpty().not()){
            mViewDataBinding?.swEnableMedia?.isEnabled = true
        }
        if (PreferenceHelper.getString(ConstantUtils.BACKGROUND_MUSIC, "").isNullOrEmpty().not()){
            mViewDataBinding?.swEnableMusic?.isEnabled = true
        }
        mViewDataBinding?.cbSound?.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked){
                PreferenceHelper.writeBoolean(ConstantUtils.MEDIA_SOUND_ENABLE, true)
            }else {
                PreferenceHelper.writeBoolean(ConstantUtils.MEDIA_SOUND_ENABLE, false)
            }
        }
        mViewDataBinding?.swEnableMedia?.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked){
                PreferenceHelper.writeBoolean(ConstantUtils.MEDIA_ENABLE, true)
            }else {
                PreferenceHelper.writeBoolean(ConstantUtils.MEDIA_ENABLE, false)
            }
            simulateTouch()
        }
        mViewDataBinding?.swEnableMusic?.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked){
                PreferenceHelper.writeBoolean(ConstantUtils.BACKGROUND_MUSIC_ENABLE, true)
                HomeActivity.playAudioFromUrl((PreferenceHelper.getString(ConstantUtils.BACKGROUND_MUSIC, "")))
            }else {
                PreferenceHelper.writeBoolean(ConstantUtils.BACKGROUND_MUSIC_ENABLE, false)
                HomeActivity.stopMusic()
            }
        }

        mViewDataBinding?.etMediaPath?.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "video/*"
            }
            startActivityForResult(intent, REQUEST_CODE_PICK_VIDEO)
        }

        mViewDataBinding?.etMusic?.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "audio/*"
            }
            startActivityForResult(intent, REQUEST_CODE_PICK_MUSIC)
        }

        mViewDataBinding?.etTimeout?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.toString().isNotEmpty()) {
                    if (s.toString() != "0")
                        PreferenceHelper.writeString(ConstantUtils.TIME_BACK_HOME, s.toString())
                    else
                        PreferenceHelper.writeString(ConstantUtils.TIME_BACK_HOME, "1")
                }else {
                    PreferenceHelper.writeString(ConstantUtils.TIME_BACK_HOME, "1")
                }
                simulateTouch()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }

    private fun initData(){

    }

    override fun onClick(v: View?) {
        if (checkDebouncedClick()) {
            when (v?.id) {
                R.id.rl_home -> activity?.finish()
                R.id.iv_back -> activity?.supportFragmentManager?.popBackStack()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_PICK_VIDEO) {
                data?.data?.let { uri ->// Lấy đường dẫn file từ uri
                    val filePath = getRealPathFromURI(uri)
                    // Hiển thị đường dẫn file trong EditText
                    mViewDataBinding?.etMediaPath?.text = (filePath)
                    PreferenceHelper.writeString(ConstantUtils.MEDIA_PATH, filePath)
                    mViewDataBinding?.swEnableMedia?.isEnabled = true
                }
            } else if (requestCode == REQUEST_CODE_PICK_MUSIC) {
                data?.data?.let { uri ->// Lấy đường dẫn file từ uri
                    val filePath = getRealPathFromURI(uri)
                    // Hiển thị đường dẫn file trong EditText
                    mViewDataBinding?.etMusic?.text = (filePath)
                    PreferenceHelper.writeString(ConstantUtils.BACKGROUND_MUSIC, filePath)
                    mViewDataBinding?.swEnableMusic?.isEnabled = true
                    if (mViewDataBinding?.swEnableMusic?.isChecked == true){
                        HomeActivity.stopMusic()
                        if (filePath != null) {
                            HomeActivity.playAudioFromUrl(filePath)
                        }
                    }
                }
            }
        }
    }

    private fun getRealPathFromURI(uri: Uri): String? {
        val projection = arrayOf(MediaStore.Video.Media.DATA)
        val cursor = activity?.contentResolver?.query(uri, projection, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val columnIndex = it.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
                return it.getString(columnIndex)
            }
        }
        return null
    }

    private fun simulateTouch() {
        val view = activity?.window?.decorView?.rootView
        view?.dispatchTouchEvent(
            MotionEvent.obtain(
            System.currentTimeMillis(),
            System.currentTimeMillis(),
            MotionEvent.ACTION_DOWN,
            0f,
            0f,
            0
        ))
        view?.dispatchTouchEvent(MotionEvent.obtain(
            System.currentTimeMillis(),
            System.currentTimeMillis(),
            MotionEvent.ACTION_UP,
            0f,
            0f,
            0
        ))
    }
}
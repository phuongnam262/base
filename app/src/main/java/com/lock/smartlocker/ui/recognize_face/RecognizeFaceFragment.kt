package com.lock.smartlocker.ui.recognize_face

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.Surface
import android.view.View
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.google.mlkit.common.MlKitException
import com.lock.smartlocker.BR
import com.lock.smartlocker.R
import com.lock.smartlocker.data.preference.PreferenceHelper
import com.lock.smartlocker.databinding.FragmentRecognizeFaceBinding
import com.lock.smartlocker.facedetector.CameraXViewModel
import com.lock.smartlocker.facedetector.DetectResultIml
import com.lock.smartlocker.facedetector.FaceDetectorProcessor
import com.lock.smartlocker.facedetector.VisionImageProcessor
import com.lock.smartlocker.facedetector.preference.PreferenceUtils
import com.lock.smartlocker.ui.base.BaseFragment
import com.lock.smartlocker.ui.inputemail.InputEmailFragment.Companion.EMAIL_REGISTER
import com.lock.smartlocker.util.ConstantUtils
import com.lock.smartlocker.util.Coroutines
import com.lock.smartlocker.util.KioskModeHelper
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException

@SuppressLint("UnsafeOptInUsageError")
class RecognizeFaceFragment : BaseFragment<FragmentRecognizeFaceBinding, RecognizeFaceViewModel>(),
    KodeinAware, View.OnClickListener, RecognizeFaceListener, DetectResultIml {

    override val kodein by kodein()
    private val factory: RecognizeFaceViewModelFactory by instance()
    override val layoutId: Int = R.layout.fragment_recognize_face
    override val bindingVariable: Int
        get() = BR.viewmodel

    override val viewModel: RecognizeFaceViewModel
        get() = ViewModelProvider(this, factory)[RecognizeFaceViewModel::class.java]

    private var cameraProvider: ProcessCameraProvider? = null
    private var previewUseCase: Preview? = null
    private var analysisUseCase: ImageAnalysis? = null
    private var imageProcessor: VisionImageProcessor? = null
    private var needUpdateGraphicOverlayImageSourceInfo = false
    private var lensFacing = CameraSelector.LENS_FACING_EXTERNAL
    private var rotateCamera = Surface.ROTATION_270
    private var rotateDetect = Surface.ROTATION_90
    private var cameraSelector: CameraSelector? = null
    private var imageCapture: ImageCapture? = null
    private val faceDetectorProcessor = FaceDetectorProcessor

    companion object {
        private const val TAG = "RecognizeFaceFragment"
    }
    private var isClicked = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.recognizeFaceListener = this
        initData()
        initView()
    }

    private fun initView() {
        viewModel.titlePage.postValue(getString(R.string.recognize_face))
        if (viewModel.typeOpen == ConstantUtils.TYPE_CONSUMABLE_COLLECT){
            mViewDataBinding?.bottomMenu?.btnUsing?.text = getString(R.string.using_card_button)
        }
        mViewDataBinding?.bottomMenu?.rlHome?.setOnClickListener(this)
        mViewDataBinding?.bottomMenu?.btnUsing?.setOnClickListener(this)
        mViewDataBinding?.bottomMenu?.btnRetry?.setOnClickListener(this)
        mViewDataBinding?.bottomMenu?.btnProcess?.setOnClickListener(this)
        mViewDataBinding?.headerBar?.ivBack?.setOnClickListener(this)

        turnOnLight(1)

        cameraSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()
        if (mViewDataBinding?.previewView == null) {
            Log.d(TAG, "previewView is null")
        }
        if (mViewDataBinding?.graphicOverlay == null) {
            Log.d(TAG, "graphicOverlay is null")
        }
        ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(activity?.application!!)
        )[CameraXViewModel::class.java]
            .processCameraProvider
            .observe(
                requireActivity()
            ) { provider: ProcessCameraProvider? ->
                cameraProvider = provider
                bindAllCameraUseCases()
            }
        faceDetectorProcessor.setCallback(this)
    }

    override fun onResume() {
        super.onResume()
        bindAllCameraUseCases()
        isClicked = false
    }

    override fun onPause() {
        super.onPause()
        clearAll()
    }

    override fun onDestroy() {
        super.onDestroy()
        clearAll()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        faceDetectorProcessor.removeCallback()
        viewModel.recognizeFaceListener = null
    }

    private fun clearAll() {
        imageProcessor?.run { this.stop() }
        cameraProvider?.unbindAll()
        FaceDetectorProcessor.isSuccess = false
    }

    private fun initData() {
        if (arguments?.getString(ConstantUtils.TYPE_OPEN) != null) {
            viewModel.typeOpen = arguments?.getString(ConstantUtils.TYPE_OPEN)
        }
    }

    private fun turnOnLight(mode: Int){
        if (PreferenceHelper.getBoolean(ConstantUtils.LIGHT_ON)) {
            if (mode == 1) activity?.let {
                KioskModeHelper.sendCommand(
                    it,
                    KioskModeHelper.Action.OPEN_WHITE_LIGHT
                )
            }
            else activity?.let {
                KioskModeHelper.sendCommand(
                    it,
                    KioskModeHelper.Action.CLOSE_WHITE_LIGHT
                )
            }
        }
    }

    override fun onClick(v: View?) {
        if (checkDebouncedClick()) {
            when (v?.id) {
                R.id.rl_home -> activity?.finish()
                R.id.iv_back -> activity?.finish()
                R.id.btn_retry -> {
                    turnOnLight(1)
                    mViewDataBinding?.ivFrame?.setBackgroundResource(R.drawable.bg_face_register)
                    viewModel.showStatusText.value = false
                    FaceDetectorProcessor.isSuccess = false
                    bindAllCameraUseCases()
                }

                R.id.btn_process -> {
                    if (isClicked.not()) {
                        isClicked = true
                        if(viewModel.typeOpen == ConstantUtils.TYPE_CONSUMABLE_COLLECT) viewModel.endUserLogin()
                        else viewModel.consumerLogin()
                    }
                }

                R.id.btn_using -> {
                    imageProcessor?.run { this.stop() }
                    if (cameraProvider != null) {
                        cameraProvider!!.unbindAll()
                    }
                    val bundle = Bundle().apply {
                        putString(ConstantUtils.TYPE_OPEN, viewModel.typeOpen)
                    }
                    // Nếu là consumable thì qua màn hình nhập work card
                    if (viewModel.typeOpen == ConstantUtils.TYPE_CONSUMABLE_COLLECT) {
                        navigateTo(R.id.action_recognizeFaceFragment_to_scanWorkCardFragment, bundle)
                    }else{
                        navigateTo(R.id.action_recognizeFaceFragment_to_inputEmailFragment2, bundle)
                    }
                }
            }
        }
    }

    override fun handleSuccess(name: String?) {
        mViewDataBinding?.ivFrame?.setBackgroundResource(R.drawable.bg_face_success)
        mViewDataBinding?.bottomMenu?.tvStatus?.text = getString(R.string.welcome_back, name)
        activity?.resources?.getColor(R.color.colorGreen)
            ?.let { mViewDataBinding?.bottomMenu?.tvStatus?.setTextColor(it) }
        mViewDataBinding?.bottomMenu?.llButton?.weightSum = 2.0f
        viewModel.enableButtonProcess.postValue(true)
    }

    override fun faceNotFound() {
        mViewDataBinding?.ivFrame?.setBackgroundResource(R.drawable.bg_face_fail)
    }

    override fun consumerLoginSuccess(email: String?) {
        if (viewModel.typeOpen != ConstantUtils.TYPE_CONSUMABLE_COLLECT) {
            val bundle = Bundle().apply {
                putString(ConstantUtils.TYPE_OPEN, viewModel.typeOpen)
                putString(EMAIL_REGISTER, email)
            }
            navigateTo(R.id.action_recognizeFaceFragment_to_categoryFragment, bundle)
        }else{
            navigateTo(R.id.action_recognizeFaceFragment_to_categoryConsumableCollectFragment, null)
        }
    }

    override fun consumerLoginFail(email: String?, status: String?) {
        // Chỉ fail(yêu cầu OTP) khi typeOpen khác consumable
        if (status == ConstantUtils.REQUIRE_OTP){
            if (arguments?.getString(ConstantUtils.TYPE_OPEN) != null) {
                val bundle = Bundle().apply {
                    putString(ConstantUtils.TYPE_OPEN, arguments?.getString(ConstantUtils.TYPE_OPEN) )
                    putString(EMAIL_REGISTER, email)
                }
                navigateTo(R.id.action_recognizeFaceFragment_to_inputOTPFragment2, bundle)
            }
        }else isClicked = false
    }

    override fun faceExited() {
        mViewDataBinding?.ivFrame?.setBackgroundResource(R.drawable.bg_face_fail)
    }

    private fun bindAllCameraUseCases() {
        if (cameraProvider != null) {
            // As required by CameraX API, unbinds all use cases before trying to re-bind any of them.
            cameraProvider!!.unbindAll()
            bindPreviewUseCase()
            bindAnalysisUseCase()
        }
    }

    private fun bindPreviewUseCase() {
        if (cameraProvider == null) {
            return
        }
        if (previewUseCase != null) {
            cameraProvider!!.unbind(previewUseCase)
        }

        imageCapture =
            ImageCapture.Builder()
                .setTargetRotation(rotateCamera)
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .build()

        val previewView = mViewDataBinding?.previewView
        previewView?.implementationMode = PreviewView.ImplementationMode.COMPATIBLE

        previewUseCase = Preview.Builder()
            .setTargetRotation(rotateCamera)
            .build().apply {
                setSurfaceProvider(previewView?.surfaceProvider)
            }

        cameraSelector = CameraSelector.Builder()
            .apply {
                val cameraList = cameraProvider!!.availableCameraInfos
                if (cameraList.isNotEmpty()) {
                    requireLensFacing(lensFacing)
                } else {
                    Log.e("CameraDebug", "No available cameras on the device")
                    return@apply
                }
            }
            .build()

        try {
            cameraProvider!!.unbindAll()
            cameraProvider!!.bindToLifecycle(
                this,
                cameraSelector!!,
                previewUseCase,
                imageCapture
            )
        } catch (exc: Exception) {
            Log.e("CameraDebug", "Error binding camera: ${exc.message}")
        }
    }

    private fun bindAnalysisUseCase() {
        if (cameraProvider == null) {
            return
        }
        if (analysisUseCase != null) {
            cameraProvider!!.unbind(analysisUseCase)
        }
        if (imageProcessor != null) {
            imageProcessor!!.stop()
        }
        imageProcessor =
            try {
                Log.i(TAG, "Using Face Detector Processor")
                val faceDetectorOptions = PreferenceUtils.getFaceDetectorOptions(activity)
                activity?.let { FaceDetectorProcessor(it, faceDetectorOptions) }
            } catch (e: Exception) {
                Log.e(TAG, "Can not create image processor: FACE_DETECTION", e)
                Toast.makeText(
                    activity,
                    "Can not create image processor: " + e.localizedMessage,
                    Toast.LENGTH_LONG
                )
                    .show()
                return
            }

        val builder = ImageAnalysis.Builder()
        analysisUseCase = builder
            .setTargetRotation(rotateDetect)
            .build()

        needUpdateGraphicOverlayImageSourceInfo = true

        activity?.let { ContextCompat.getMainExecutor(it) }?.let {
            analysisUseCase?.setAnalyzer(
                // imageProcessor.processImageProxy will use another thread to run the detection underneath,
                // thus we can just runs the analyzer itself on main thread.
                it
            ) { imageProxy: ImageProxy ->
                if (needUpdateGraphicOverlayImageSourceInfo) {
                    val rotationDegrees = imageProxy.imageInfo.rotationDegrees
                    if (rotationDegrees == 0 || rotationDegrees == 180) {
                        mViewDataBinding?.graphicOverlay?.setImageSourceInfo(
                            imageProxy.width,
                            imageProxy.height,
                            false
                        )
                    } else {
                        mViewDataBinding?.graphicOverlay?.setImageSourceInfo(
                            imageProxy.height,
                            imageProxy.width,
                            false
                        )
                    }
                    needUpdateGraphicOverlayImageSourceInfo = false
                }
                try {
                    imageProcessor!!.processImageProxy(imageProxy, mViewDataBinding?.graphicOverlay)
                } catch (e: MlKitException) {
                    Log.e(TAG, "Failed to process image. Error: " + e.localizedMessage)
                    Toast.makeText(activity, e.localizedMessage, Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
        cameraProvider!!.bindToLifecycle(this,
            cameraSelector!!,
            analysisUseCase
        )
    }

    override fun onSuccess() {
        captureImage()
    }

    override fun onMultiFace() {
        Coroutines.main {
            viewModel.mStatusText.value = R.string.error_multi_face_detect
        }
    }

    override fun onOneFace() {
        Coroutines.main {
            viewModel.showStatusText.value = false
        }
    }

    override fun onFaceTooSmall() {
        Coroutines.main {
            viewModel.mStatusText.value = R.string.error_email_exited
        }
    }

    private fun captureImage() {
        val imageCapture = imageCapture ?: return

        // Tạo file để lưu ảnh chụp
        val outputDirectory = getOutputDirectory()
        val photoFile = File(outputDirectory, "IMG_User.jpg")

        // Cấu hình ImageCapture để lưu ảnh vào file
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        // Chụp ảnh
        activity?.let { ContextCompat.getMainExecutor(it) }?.let {
            imageCapture.takePicture(
                outputOptions,
                it,
                object : ImageCapture.OnImageSavedCallback {
                    override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                        imageProcessor?.run { this.stop() }
                        if (cameraProvider != null) {
                            cameraProvider!!.unbindAll()
                        }
                        val strBase64 = encodeImage(photoFile)
                        turnOnLight(0)
                        detectImageFromImage(strBase64)
                    }

                    override fun onError(exception: ImageCaptureException) {
                        Log.e(TAG, "Lỗi chụp ảnh: ${exception.message}", exception)
                    }
                })
        }
    }

    fun detectImageFromImage(strBase64: String) {
        viewModel.detectImage(strBase64)
    }

    private fun encodeImage(file: File): String {
        var fis: FileInputStream? = null
        try {
            fis = FileInputStream(file)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        val bm = BitmapFactory.decodeStream(fis)
        val baos = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.JPEG, 20, baos)
        val b = baos.toByteArray()
        val encodedImage = Base64.encodeToString(b, Base64.DEFAULT)
        bm.recycle() // Recycle the bitmap to free up memory
        return encodedImage
    }

    private fun getOutputDirectory(): File {
        val mediaDir = activity?.externalMediaDirs?.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else activity?.filesDir!!
    }
}
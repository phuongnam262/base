package com.lock.smartlocker.ui.register_face

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Matrix
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.Surface
import android.view.View
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.google.mlkit.common.MlKitException
import com.google.mlkit.vision.face.Face
import com.lock.smartlocker.BR
import com.lock.smartlocker.R
import com.lock.smartlocker.data.preference.PreferenceHelper
import com.lock.smartlocker.databinding.FragmentRegisterFaceBinding
import com.lock.smartlocker.facedetector.CameraXViewModel
import com.lock.smartlocker.facedetector.DetectResultIml
import com.lock.smartlocker.facedetector.FaceDetectorProcessor
import com.lock.smartlocker.facedetector.VisionImageProcessor
import com.lock.smartlocker.facedetector.preference.PreferenceUtils
import com.lock.smartlocker.ui.base.BaseFragment
import com.lock.smartlocker.ui.inputemail.InputEmailFragment
import com.lock.smartlocker.util.ConstantUtils
import com.lock.smartlocker.util.Coroutines
import com.lock.smartlocker.util.KioskModeHelper
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import java.io.ByteArrayOutputStream


@SuppressLint("UnsafeOptInUsageError")
class RegisterFaceFragment : BaseFragment<FragmentRegisterFaceBinding, RegisterFaceViewModel>(),
    KodeinAware, View.OnClickListener, RegisterFaceListener, DetectResultIml {

    override val kodein by kodein()
    private val factory: RegisterFaceViewModelFactory by instance()
    override val layoutId: Int = R.layout.fragment_register_face
    override val bindingVariable: Int
        get() = BR.viewmodel

    override val viewModel: RegisterFaceViewModel
        get() = ViewModelProvider(this, factory)[RegisterFaceViewModel::class.java]

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
    private var isExited: Boolean = false
    private var strBase64: String? = null
    private val faceDetectorProcessor = FaceDetectorProcessor

    companion object {
        private const val TAG = "RegisterFaceFragment"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.registerFaceListener = this
        viewModel.context = activity
        initView()
        initData()
    }

    private fun initView() {
        viewModel.titlePage.postValue(getString(R.string.register_face))
        mViewDataBinding?.bottomMenu?.rlHome?.setOnClickListener(this)
        mViewDataBinding?.bottomMenu?.btnProcess?.setOnClickListener(this)
        mViewDataBinding?.bottomMenu?.enableButtonProcess
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
        viewModel.registerFaceListener = null
        faceDetectorProcessor.removeCallback()
    }

    private fun clearAll() {
        imageProcessor?.run { this.stop() }
        cameraProvider?.unbindAll()
        FaceDetectorProcessor.isSuccess = false
    }

    private fun initData() {
        viewModel.emailRegister.value = arguments?.getString(InputEmailFragment.EMAIL_REGISTER)
        viewModel.nameEndUser.value = arguments?.getString(ConstantUtils.NAME_END_USER)
        viewModel.cardNumberRegister.value = arguments?.getString(ConstantUtils.WORK_CARD_NUMBER)
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
                R.id.rl_home -> {
                    turnOnLight(0)
                    activity?.finish()
                }
                R.id.iv_back -> {
                    turnOnLight(0)
                    activity?.supportFragmentManager?.popBackStack()
                }
                R.id.btn_process -> {
                    if (isExited) {
                        turnOnLight(1)
                        isExited = false
                        mViewDataBinding?.ivFrame?.setBackgroundResource(R.drawable.bg_face_register)
                        mViewDataBinding?.bottomMenu?.btnProcess?.text = getString(R.string.process_button)
                        viewModel.enableButtonProcess.value = false
                        viewModel.showStatusText.value = false
                        FaceDetectorProcessor.isSuccess = false
                        bindAllCameraUseCases()
                    }else {
                        viewModel.enableButtonProcess.value = false
                        strBase64?.let { viewModel.detectImage(it) }
                    }
                }
            }
        }
    }

    override fun handleSuccess(personCode: String, email: String) {
        viewModel.showButtonProcess.postValue(false)
        viewModel.titlePage.postValue(getString(R.string.face_register_success))
        mViewDataBinding?.tvHeaderInfo?.text = getString(R.string.face_hello, email)
        mViewDataBinding?.headerBar?.ivBack?.visibility = View.GONE
    }

    override fun faceNotFound() {
        failDetectFace()
    }

    override fun faceExited() {
        failDetectFace()
    }

    private fun failDetectFace(){
        mViewDataBinding?.bottomMenu?.btnProcess?.text = getString(R.string.btn_retry)
        viewModel.enableButtonProcess.value = true
        isExited = true
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

    override fun onSuccess(face: Face) {
        activity?.let { ContextCompat.getMainExecutor(it) }?.let {
            analysisUseCase?.setAnalyzer(it) { imageProxy ->
                val bitmap = imageProxy.toBitmap()
                val faceBoundingBox = face.boundingBox
                turnOnLight(0)
                viewModel.enableButtonProcess.value = true
                imageProcessor?.run { this.stop() }
                if (cameraProvider != null) {
                    cameraProvider!!.unbindAll()
                }
                val matrix = Matrix()
                matrix.postRotate(90f)
                val rotatedBitmap = Bitmap.createBitmap(
                    bitmap,
                    0,
                    0,
                    bitmap.width,
                    bitmap.height,
                    matrix,
                    true
                )
                var faceBitmap: Bitmap? = null
                try {faceBitmap = Bitmap.createBitmap(
                    rotatedBitmap,
                    faceBoundingBox.left - 35,
                    faceBoundingBox.top - 100,
                    faceBoundingBox.width() + 60,
                    faceBoundingBox.height() + 150
                )
                } catch (e: IllegalArgumentException) {
                    Log.e("FaceDetection", "Error creating face bitmap", e)
                }
                if (faceBitmap != null) {
                    val baos = ByteArrayOutputStream()
                    faceBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                    val b = baos.toByteArray()
                    strBase64 = Base64.encodeToString(b, Base64.DEFAULT)
                    imageProxy.close()
                } else {
                    viewModel.mStatusText.postValue(R.string.face_not_found)
                    failDetectFace()
                }
            }
        }
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
            viewModel.mStatusText.value = R.string.error_small_face_detect
        }
    }

    override fun onFaceTooLarger() {
        Coroutines.main {
            viewModel.mStatusText.value = R.string.error_large_face_detect
        }
    }

    override fun onNotCenterFace() {
        Coroutines.main {
            viewModel.mStatusText.value = R.string.error_center_face_detect
        }
    }

    override fun onNotStraightFace() {
        Coroutines.main {
            viewModel.mStatusText.value = R.string.error_straight_face_detect
        }
    }
}
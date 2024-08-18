package com.lock.smartlocker.ui.register_face

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.Surface
import android.view.View
import android.widget.Toast
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.google.mlkit.common.MlKitException
import com.lock.smartlocker.BR
import com.lock.smartlocker.R
import com.lock.smartlocker.databinding.FragmentRegisterFaceBinding
import com.lock.smartlocker.facedetector.CameraXViewModel
import com.lock.smartlocker.facedetector.DetectResultIml
import com.lock.smartlocker.facedetector.FaceDetectorProcessor
import com.lock.smartlocker.facedetector.VisionImageProcessor
import com.lock.smartlocker.facedetector.preference.PreferenceUtils
import com.lock.smartlocker.ui.base.BaseFragment
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException


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
    private var camera: Camera? = null
    private var previewUseCase: Preview? = null
    private var analysisUseCase: ImageAnalysis? = null
    private var imageProcessor: VisionImageProcessor? = null
    private var needUpdateGraphicOverlayImageSourceInfo = false
    private var lensFacing = CameraSelector.LENS_FACING_FRONT
    private var cameraSelector: CameraSelector? = null
    private var imageCapture: ImageCapture? = null

    companion object {
        private const val TAG = "CameraXLivePreview"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.registerFaceListener = this
        initView()
        initData()
    }

    private fun initView(){
        viewModel.titlePage.postValue(getString(R.string.register_face))
        viewModel.startTimer()
        mViewDataBinding?.bottomMenu?.rlHome?.setOnClickListener(this)
        mViewDataBinding?.bottomMenu?.btnProcess?.setOnClickListener(this)
        mViewDataBinding?.headerBar?.ivBack?.setOnClickListener(this)

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
        /*mViewDataBinding?.btnSetting?.setOnClickListener {
            val intent = Intent(activity, SettingsActivity::class.java)
            intent.putExtra(SettingsActivity.EXTRA_LAUNCH_SOURCE, LaunchSource.CAMERAX_LIVE_PREVIEW)
            startActivity(intent)
        }*/
        mViewDataBinding?.headerBar?.ivBack?.setOnClickListener {
            activity?.onBackPressedDispatcher?.onBackPressed()
        }
        val faceDetectorProcessor = FaceDetectorProcessor
        faceDetectorProcessor.setCallback(this)
    }

    public override fun onResume() {
        super.onResume()
        bindAllCameraUseCases()
    }

    override fun onPause() {
        super.onPause()
        imageProcessor?.run { this.stop() }
    }

    public override fun onDestroy() {
        super.onDestroy()
        imageProcessor?.run { this.stop() }
        FaceDetectorProcessor.isSuccess = false
    }

    private fun initData(){

    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.rl_home -> activity?.finish()
            R.id.iv_back -> activity?.onBackPressedDispatcher?.onBackPressed()
            R.id.btn_process -> {

            }
        }
    }

    override fun handleSuccess(personCode: String, email: String) {
        viewModel.showButtonProcess.postValue(false)
        viewModel.titlePage.postValue(getString(R.string.face_register_success))
        mViewDataBinding?.tvHeaderInfo?.text = getString(R.string.face_hello, personCode)
    }

    override fun faceNotFound() {
        FaceDetectorProcessor.isSuccess = false
        bindAnalysisUseCase()
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

        val builder = Preview.Builder()
        /*if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O){
            previewUseCase?.targetRotation = Surface.ROTATION_180
        }else {
            builder.setTargetRotation(Surface.ROTATION_180)
        }*/
        previewUseCase = builder.build()
        imageCapture = ImageCapture.Builder().setTargetRotation(Surface.ROTATION_0).build()
        previewUseCase!!.setSurfaceProvider(mViewDataBinding?.previewView?.getSurfaceProvider())
        camera =
            cameraProvider!!.bindToLifecycle(
                this,
                cameraSelector!!,
                previewUseCase,
                imageCapture
            )
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
        //builder.setTargetRotation(Surface.ROTATION_180)
        analysisUseCase = builder.build()

        needUpdateGraphicOverlayImageSourceInfo = true

        activity?.let { ContextCompat.getMainExecutor(it) }?.let {
            analysisUseCase?.setAnalyzer(
                // imageProcessor.processImageProxy will use another thread to run the detection underneath,
                // thus we can just runs the analyzer itself on main thread.
                it
            ) { imageProxy: ImageProxy ->
                if (needUpdateGraphicOverlayImageSourceInfo) {
                    val isImageFlipped = lensFacing == CameraSelector.LENS_FACING_EXTERNAL
                    val rotationDegrees = imageProxy.imageInfo.rotationDegrees
                    if (rotationDegrees == 0 || rotationDegrees == 180) {
                        mViewDataBinding?.graphicOverlay?.setImageSourceInfo(
                            imageProxy.width,
                            imageProxy.height,
                            isImageFlipped
                        )
                    } else {
                        mViewDataBinding?.graphicOverlay?.setImageSourceInfo(
                            imageProxy.height,
                            imageProxy.width,
                            isImageFlipped
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
        cameraProvider!!.bindToLifecycle(/* lifecycleOwner= */ this,
            cameraSelector!!,
            analysisUseCase
        )
    }

    override fun onSuccess() {
        //mViewDataBinding?.ivFaceTrue?.visibility = View.VISIBLE
        captureImage()
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
                        val savedUri = Uri.fromFile(photoFile)
                        Log.e(TAG, savedUri.toString())
                        imageProcessor?.run { this.stop() }
                        val strBase64 = encodeImage(photoFile)
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
        //val imageFile = File(path)
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
        //Base64.de
        return Base64.encodeToString(b, Base64.DEFAULT)
    }

    private fun getOutputDirectory(): File {
        val mediaDir = activity?.externalMediaDirs?.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else activity?.filesDir!!
    }
}
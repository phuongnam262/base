package com.lock.smartlocker.facedetector

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetector
import com.google.mlkit.vision.face.FaceDetectorOptions

/** Face Detector Demo.  */
class FaceDetectorProcessor(context: Context, detectorOptions: FaceDetectorOptions?) :
    VisionProcessorBase<List<Face>>(context) {

    private val detector: FaceDetector
    private val handler = Handler(Looper.getMainLooper())
    private var delayRunnable: Runnable? = null
    private var isMultiFace = false
    private val minFaceSize = 0.1f
    private val maxFaceSize = 0.12f

    init {
        val options = detectorOptions
            ?: FaceDetectorOptions.Builder()
                .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_NONE)
                .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_NONE)
                .setMinFaceSize(1f)
                .enableTracking()
                .build()

        detector = FaceDetection.getClient(options)
        Log.v(MANUAL_TESTING_LOG, "Face detector options: $options")
    }

    private fun isFaceLargeEnough(face: Face): Int {
        val boundingBox = face.boundingBox
        val faceWidth = boundingBox.width().toFloat()
        val faceHeight = boundingBox.height().toFloat()
        val imageWidth = 960f
        val imageHeight = 1280f

        val faceSize = (faceWidth * faceHeight) / (imageWidth * imageHeight)

        if (faceSize in minFaceSize..maxFaceSize)
            return 1
        else if (faceSize > maxFaceSize){
            return 2
        }
        return 0
    }

    override fun onSuccess(faces: List<Face>, graphicOverlay: GraphicOverlay) {
        /*if (faces.size > 1) {
            isMultiFace = true
            delayRunnable?.let { handler.removeCallbacks(it) }
            callback?.onMultiFace()
            for (face in faces) {
                graphicOverlay.add(FaceGraphic(graphicOverlay, face, true))
            }
            delayRunnable = Runnable {
                isMultiFace = false
            }
            handler.postDelayed(delayRunnable!!, 1000)
        } else if (faces.size == 1) {
            if (isMultiFace.not()) {
                if (isFaceLargeEnough(faces[0]) == 1) {
                    graphicOverlay.add(FaceGraphic(graphicOverlay, faces[0], false))
                    logExtrasForTesting(faces[0])
                    callback?.onOneFace()
                } else if (isFaceLargeEnough(faces[0]) == 2) {
                    graphicOverlay.add(FaceGraphic(graphicOverlay, faces[0], true))
                    callback?.onFaceTooLarger()
                } else{
                    graphicOverlay.add(FaceGraphic(graphicOverlay, faces[0], true))
                    callback?.onFaceTooSmall()
                }

            }
        }*/

        for (face in faces) {
            val faceCenterX = face.boundingBox.centerX().toFloat()
            val faceCenterY = face.boundingBox.centerY().toFloat()
            if ((faceCenterX < 260 && faceCenterX > 220) && (faceCenterY < 340 && faceCenterY > 310) ) {
                if (isFaceLargeEnough(face) == 1) {
                    graphicOverlay.add(FaceGraphic(graphicOverlay, face, false))
                    logExtrasForTesting(face)
                    callback?.onOneFace()
                } else if (isFaceLargeEnough(face) == 2) {
                    graphicOverlay.add(FaceGraphic(graphicOverlay, face, true))
                    callback?.onFaceTooLarger()
                } else{
                    graphicOverlay.add(FaceGraphic(graphicOverlay, face, true))
                    callback?.onFaceTooSmall()
                }
                callback?.onOneFace()
            }else {
                graphicOverlay.add(FaceGraphic(graphicOverlay, face, true))
                callback?.onNotCenterFace()
            }
        }
    }

    override fun stop() {
        super.stop()
        detector.close()
        delayRunnable?.let {
            handler.removeCallbacks(it)
            delayRunnable = null
        }
    }


    override fun detectInImage(image: InputImage): Task<List<Face>> {
        return detector.process(image)
    }

    override fun onFailure(e: Exception) {
        Log.e(TAG, "Face detection failed $e")
    }

    companion object {
        private const val TAG = "FaceDetectorProcessor"
        private var callback: DetectResultIml? = null
        var isSuccess = false

        fun setCallback(listener: DetectResultIml) {
            callback = listener
        }

        fun removeCallback() {
            callback = null
        }

        private fun logExtrasForTesting(face: Face?) {
            if (isSuccess.not())
                if (face != null) {
                    if ((-10 < face.headEulerAngleX && face.headEulerAngleX < 12) &&
                        (-10 < face.headEulerAngleY && face.headEulerAngleY < 10) &&
                        (-4 < face.headEulerAngleZ && face.headEulerAngleZ < 4)
                    ) {
                        isSuccess = true
                        callback?.onSuccess(face)
                    }
                }
        }
    }
}

/*
 * Copyright 2020 Google LLC. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lock.smartlocker.facedetector

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetector
import com.google.mlkit.vision.face.FaceDetectorOptions
import java.io.File
import java.io.FileOutputStream

/** Face Detector Demo.  */
class FaceDetectorProcessor(context: Context, detectorOptions: FaceDetectorOptions?) :
    VisionProcessorBase<List<Face>>(context) {

    private val detector: FaceDetector
    private val handler = Handler(Looper.getMainLooper())
    private var delayRunnable: Runnable? = null
    private var isMultiFace = false
    private val minFaceSize = 1f
    private val context = context.applicationContext

    init {
        val options = detectorOptions
            ?: FaceDetectorOptions.Builder()
                .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_NONE)
                .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_NONE)
                .setMinFaceSize(minFaceSize)
                .enableTracking()
                .build()

        detector = FaceDetection.getClient(options)

        Log.v(MANUAL_TESTING_LOG, "Face detector options: $options")
    }

    override fun onSuccess(faces: List<Face>, graphicOverlay: GraphicOverlay) {
        if (faces.size > 1) {
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
            graphicOverlay.add(FaceGraphic(graphicOverlay, faces[0], false))
            if (isMultiFace.not()) {
                if ((-10 < faces[0].headEulerAngleX && faces[0].headEulerAngleX < 12) &&
                    (-10 < faces[0].headEulerAngleY && faces[0].headEulerAngleY < 10) &&
                    (-4 < faces[0].headEulerAngleZ && faces[0].headEulerAngleZ < 4)
                ) {
                    if (isSuccess.not()) {
                        isSuccess = true
                        captureFaceBitmap(graphicOverlay, faces[0])
                        logExtrasForTesting(faces[0])
                    }
                }
                callback?.onOneFace()
            }
        }
    }

    private fun captureFaceBitmap(graphicOverlay: GraphicOverlay, face: Face) {
        val fullBitmap = Bitmap.createBitmap(graphicOverlay.width, graphicOverlay.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(fullBitmap)
        graphicOverlay.draw(canvas)
        // Cắt bitmap để chỉ chứa khuôn mặt
        val faceBitmap = Bitmap.createBitmap(
            fullBitmap,
            face.boundingBox.left,
            face.boundingBox.top,
            face.boundingBox.width(),
            face.boundingBox.height()
        )

        saveBitmapToFile(faceBitmap)
    }

    private fun saveBitmapToFile(bitmap: Bitmap) {
        val file = File(context.getExternalFilesDir(null), "captured_face.jpg")
        FileOutputStream(file).use { out ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
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
            if (face != null) {
                callback?.onSuccess()
            }
        }
    }
}

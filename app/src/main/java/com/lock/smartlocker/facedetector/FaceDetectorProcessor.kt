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
    private val minFaceSize = 0.1f // Example minimum face size, adjust as needed

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
                    logExtrasForTesting(faces[0])
                    callback?.onOneFace()

            }
        }
    }

    override fun stop() {
        super.stop()
        detector.close()
        delayRunnable?.let { handler.removeCallbacks(it) }
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

        private fun logExtrasForTesting(face: Face?) {
            if (isSuccess.not())
                if (face != null) {
                    if ((-10 < face.headEulerAngleX && face.headEulerAngleX < 12) &&
                        (-10 < face.headEulerAngleY && face.headEulerAngleY < 10) &&
                        (-4 < face.headEulerAngleZ && face.headEulerAngleZ < 4)
                    ) {
                        isSuccess = true
                        callback?.onSuccess()
                    }
                }
        }
    }
}

package com.lock.smartlocker.facedetector

import com.google.mlkit.vision.face.Face

public interface DetectResultIml {
    fun onSuccess(face: Face)
    fun onMultiFace()
    fun onOneFace()
    fun onFaceTooSmall()
    fun onFaceTooLarger()
    fun onNotCenterFace()
    fun onNotStraightFace()
}
package com.lock.smartlocker.facedetector

public interface DetectResultIml {
    fun onSuccess()
    fun onMultiFace()
    fun onOneFace()
    fun onFaceTooSmall()
}
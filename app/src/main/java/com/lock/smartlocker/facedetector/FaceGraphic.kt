package com.lock.smartlocker.facedetector

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.google.mlkit.vision.face.Face
import com.lock.smartlocker.facedetector.GraphicOverlay.Graphic

class FaceGraphic constructor(overlay: GraphicOverlay?, private val face: Face, private val isMultiFace: Boolean) : Graphic(overlay) {
  private val boxPaintRed: Paint = Paint().apply {
    color = Color.RED
    style = Paint.Style.STROKE
    strokeWidth = BOX_STROKE_WIDTH
  }

  private val boxPaintGreen: Paint = Paint().apply {
    color = Color.GREEN
    style = Paint.Style.STROKE
    strokeWidth = BOX_STROKE_WIDTH
  }

  override fun draw(canvas: Canvas) {
    val x = translateX(face.boundingBox.centerX().toFloat())
    val y = translateY(face.boundingBox.centerY().toFloat())

    val left = x - scale(face.boundingBox.width() / 2.0f)
    val top = y - scale(face.boundingBox.height() / 2.0f)
    val right = x + scale(face.boundingBox.width() / 2.0f)
    val bottom = y + scale(face.boundingBox.height() / 2.0f)

    val paint = if (isMultiFace) boxPaintRed else boxPaintGreen

    canvas.drawRect(left, top, right, bottom, paint)
  }

  companion object {
    private const val BOX_STROKE_WIDTH = 5.0f
  }
}
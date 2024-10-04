package com.lock.smartlocker.facedetector

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.google.mlkit.vision.face.Face
import com.lock.smartlocker.facedetector.GraphicOverlay.Graphic

class FaceGraphic constructor(
    overlay: GraphicOverlay?,
    private val face: Face,
    private val isFailFace: Boolean
) : Graphic(overlay) {
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

        val paint = if (isFailFace) boxPaintRed else boxPaintGreen

        val cornerLength = 80.0f

        // Top-left corner
        canvas.drawLine(left, top, left + cornerLength, top, paint)
        canvas.drawLine(left, top, left, top + cornerLength, paint)

        // Top-right corner
        canvas.drawLine(right, top, right - cornerLength, top, paint)
        canvas.drawLine(right, top, right, top + cornerLength, paint)

        // Bottom-left corner
        canvas.drawLine(left, bottom, left + cornerLength, bottom, paint)
        canvas.drawLine(left, bottom, left, bottom - cornerLength, paint)

        // Bottom-right corner
        canvas.drawLine(right, bottom, right - cornerLength, bottom, paint)
        canvas.drawLine(right, bottom, right, bottom - cornerLength, paint)

    }

    companion object {
        private const val BOX_STROKE_WIDTH = 5.0f
    }
}
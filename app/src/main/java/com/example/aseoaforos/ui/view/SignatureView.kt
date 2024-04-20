package com.example.aseoaforos.ui.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import java.io.File
import java.io.FileOutputStream

class SignatureView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private var path = Path()
    private var paint = Paint().apply {
        isAntiAlias = true
        isDither = true
        color = android.graphics.Color.BLUE
        style = Paint.Style.STROKE
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
        strokeWidth = 12f
    }
    private var mBitmap: Bitmap? = null
    private var mCanvas: Canvas? = null

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        mCanvas = Canvas(mBitmap!!)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawPath(path, paint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                path.moveTo(x, y)
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                path.lineTo(x, y)
            }
            MotionEvent.ACTION_UP -> {
            }
            else -> return false
        }
        invalidate()
        return true
    }

    fun clear() {
        path.reset()
        invalidate()
    }

    fun saveSignature(file: File?, currentPath: String): String {
        return try {
            val out = FileOutputStream(file)
            mBitmap?.compress(Bitmap.CompressFormat.PNG, 90, out)
            out.flush()
            out.close()
            currentPath
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }
}

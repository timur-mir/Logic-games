package home.howework.tictactoe.balls

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.MotionEvent
import android.view.View
import kotlin.random.Random

class BallGameView(
    context: Context
) : View(context) {
    private val paint = Paint()

    private val balls = mutableListOf<Ball>()


    private var touchX = 0f
    private var touchY = 0f
    private var lastClickTime = 0L

    init {
        paint.isAntiAlias = true
        paint.style = Paint.Style.FILL
        generateBalls()
    }

    private fun generateBalls() {
        // Создаем случайные шары
        for (i in 0..5000) {
            balls.add(
                Ball(
                    Random.nextInt(700),
                    Random.nextInt(1400),
                    Random.nextInt(20,60),
                    Color.rgb(
                        Random.nextInt(256),
                        Random.nextInt(256),
                        Random.nextInt(256)
                    )
                )
            )
        }
    }



    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.apply {
            balls.forEach { ball ->
                paint.color = ball.color
                drawCircle(ball.x.toFloat(), ball.y.toFloat(), ball.size.toFloat(), paint)
            }
        }
        invalidate()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.let {
            touchX = it.x
            touchY = it.y
            when (it.action) {
                MotionEvent.ACTION_DOWN -> {
                    checkClick()
                }
            }
        }
        return true
    }

    private fun checkClick() {
        val iter: MutableIterator<Ball> = balls.iterator()
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastClickTime > 300) {
            lastClickTime = currentTime
        while (iter.hasNext()) {
            val value = iter.next();
            if (distance(touchX, touchY, value.x, value.y) < value.size) {
                iter.remove()
                invalidate()
                if (balls.isEmpty()) {
                    //balls.clear()
                    generateBalls()
                    invalidate()
                    break
                }
            }
        }

            }
            }

    private fun distance(x1: Float, y1: Float, x2: Int, y2: Int): Float {
        return kotlin.math.sqrt(
            (x1 - x2) * (x1 - x2) + (y2 - y1) * (y2 - y1)
        ).toFloat()
    }
}
package home.howework.tictactoe


class Wheel(
    private var wheelListener: WheelListener?,
    private var frameDuration: Long,
    private var startIn: Long
) : Thread() {
    var currentIndex = 0
    private var isStarted = false

    init {
        currentIndex = 0
        isStarted = true
    }
    private val imgs = intArrayOf(
    R.drawable.slot1,R.drawable.slot2,R.drawable.slot3,R.drawable.slot4,R.drawable.slot5
    )
    fun nextImg() {
        currentIndex++
        if (currentIndex == imgs.size) {
            currentIndex = 0
        }
    }

    override fun run() {
        super.run()
        try {
            sleep(startIn)
        } catch (e: InterruptedException) {
        }
        while (isStarted) {
            try {
                sleep(frameDuration)
            } catch (e: InterruptedException) {
            }
            nextImg()
            if (wheelListener != null) {
                wheelListener!!.newImage(imgs[currentIndex])
            }
        }
    }

    fun stopWheel() {
        isStarted = false
    }
    interface WheelListener {
        fun newImage(img: Int)
    }

}
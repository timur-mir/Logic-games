package home.howe

import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.SoundPool
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.DecelerateInterpolator
import android.view.animation.RotateAnimation
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import home.howework.tictactoe.R
import home.howework.tictactoe.StartActivity
import pl.droidsonroids.gif.GifImageView
import kotlin.random.Random


class Roulette:AppCompatActivity() {
    private var tvResult: TextView? = null
    private var rul: ImageView? = null
    private var random: Random? = null
    private var imView: GifImageView? = null
    companion object {
        private var old_deegre = 0
        private var deegre = 0
        private val FACTOR = 4.86f
        private val numbers = arrayOf(
            "32 Красное", "15 Чёрное", "19 Красное", "4 Чёрное",
            "21 Красное", "2 Чёрное", "25 Красное", "17 Чёрное", "34 Красное",
            "6 Чёрное", "27 Красное", "13 Чёрное", "36 Красное", "11 Чёрное", "30 Красное",
            "8 Чёрное", "23 Красное", "10 Чёрное", "5 Красное", "24 Чёрное", "16 Красное", "33 Чёрное",
            "1 Красное", "20 Чёрное", "14 Красное", "31 Чёрное", "9 Красное", "22 Чёрное", "18 Красное",
            "29 Чёрное", "7 Красное", "28 Чёрное", "12 Красное", "35 Чёрное", "3 Красное", "26 Чёрное", "Зеро"
        )
        private var soundPool // plays sound effects
                : SoundPool? = null
        private val MAX_STREAMS = 5
        private val SOUND_QUALITY = 100
        private var volume // sound effect volume
                = 0
        private var soundMap // maps ID to soundpool
                : Map<Int, Int>? = null
        private val HIT_SOUND_ID = 1
        private val MISS_SOUND_ID = 2
        private val HELP_SOUND_ID = 3
        private val SOUND_PRIORITY = 1
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_roulette)
        supportActionBar?.title = "Казино";
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        init();

    }

    override fun onBackPressed() {
        super.onBackPressed()
        Handler().postDelayed(
            {
                val intent = Intent(this, StartActivity::class.java)
                //   intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
                //   finish()
            },
            500
        )
    }

    override fun onResume() {
        super.onResume()
        initializeSoundEffects(this)
    }
    fun pause() {
        soundPool!!.release() // release audio resources
        soundPool = null
    } // end

    override fun onPause() {
        super.onPause()
        pause()
    }

    private fun getResult(deegre: Int): String? {
        var text = ""
        var factor_x = 1
        var factor_y = 3
        for (i in 0..37) {
            if (deegre >= FACTOR * factor_x && deegre < FACTOR * factor_y) {
                text = numbers[i]
            }
            factor_x += 2
            factor_y += 2
        }
        if (deegre >= FACTOR * 73 && deegre < 360 || deegre >= 0 && deegre < FACTOR *1) text =
            numbers[numbers.size- 1]
        return text
    }

    fun init(){
        tvResult = findViewById<TextView>(R.id.tvResult)
        rul = findViewById<ImageView>(R.id.rul)
        random = random
        initializeSoundEffects(this)
    }
    fun initializeSoundEffects(context: Context){

        soundPool = SoundPool(
            3, AudioManager.STREAM_MUSIC,
            0
        )

        val manager = context.getSystemService(AUDIO_SERVICE) as AudioManager
        volume = manager.getStreamVolume(AudioManager.STREAM_MUSIC)

        soundMap = HashMap() // create new HashMap

        (soundMap as HashMap<Int, Int>).put(
            HIT_SOUND_ID,
            soundPool!!.load(context, R.raw.ruletka, SOUND_PRIORITY)
        )
        (soundMap as HashMap<Int, Int>).put(
            MISS_SOUND_ID,
            soundPool!!.load(context, R.raw.short1, SOUND_PRIORITY)
        )
    }

    fun onClickStart(view: View) {
        old_deegre = deegre % 360;
        deegre = (0..3600).random()  + 720;
        val rotate = RotateAnimation(
            old_deegre.toFloat(), deegre.toFloat(), RotateAnimation.RELATIVE_TO_SELF,
            0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f
        )
        rotate.duration = 3600
        rotate.fillAfter = true
        rotate.interpolator = DecelerateInterpolator()
        rotate.setAnimationListener(object :Animation.AnimationListener{
            override fun onAnimationStart(animation: Animation?) {
                imView =  findViewById(R.id.avat);
                tvResult?.text = "";
                soundPool?.play(HIT_SOUND_ID, 1F, 1F,
                    0, 0, 1f)
            }

            override fun onAnimationEnd(animation: Animation?) {
                tvResult!!.text = getResult(360 - deegre % 360)
                    imView!!.visibility = View.VISIBLE
                    if (soundPool != null) {
                        soundPool!!.play(
                            MISS_SOUND_ID, 1F, 1F,
                            0, 0, 1f
                        )
                    imView!!.postDelayed({ imView!!.visibility = View.INVISIBLE }, 5000)
                }
            }

            override fun onAnimationRepeat(animation: Animation?) {
                TODO("Not yet implemented")
            }
        })
        rul?.startAnimation(rotate)
    }
}
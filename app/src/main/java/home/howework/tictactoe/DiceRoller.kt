package home.howe

import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.SoundPool
import android.os.Bundle
import android.os.Handler
import android.view.animation.Animation
import android.view.animation.DecelerateInterpolator
import android.view.animation.RotateAnimation
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import home.howework.tictactoe.R
import home.howework.tictactoe.StartActivity

class DiceRoller : AppCompatActivity() {
    private var diceR1: ImageView? = null
    private var diceR2: ImageView? = null
    private var diceInfo: TextView? = null
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dice_roller)
        supportActionBar?.title = "Кубики"
        val startButton = findViewById<Button>(home.howework.tictactoe.R.id.buttonD)
        diceR1 = findViewById(R.id.dice1)
        diceR2 = findViewById(R.id.dice2)
        diceInfo = findViewById(R.id.textViewD)
        startButton.setOnClickListener {
            rollDice()
        }

    }

    private fun rollDice() {
        val dice = Dice(6)
        val diceRoll = dice.roll()
        val dice2 = Dice(6)
        val diceRoll2 = dice2.roll()

        val drawableResource = when (diceRoll) {
            1 -> R.drawable.dice_1
            2 -> R.drawable.dice_2
            3 -> R.drawable.dice_3
            4 -> R.drawable.dice_4
            5 -> R.drawable.dice_5
            else -> R.drawable.dice_6
        }
        val drawableResource2 = when (diceRoll2) {
            1 -> R.drawable.dice_1
            2 -> R.drawable.dice_2
            3 -> R.drawable.dice_3
            4 -> R.drawable.dice_4
            5 -> R.drawable.dice_5
            else -> R.drawable.dice_6
        }
        val rotate = RotateAnimation(
            0.toFloat(), 360.toFloat(), RotateAnimation.RELATIVE_TO_SELF,
            0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f
        )
        rotate.duration = 540
        rotate.fillAfter = true
        rotate.interpolator = DecelerateInterpolator()
        rotate.setAnimationListener(object : Animation.AnimationListener{
            override fun onAnimationStart(animation: Animation?) {
                diceInfo?.text=""
               soundPool?.play(
                   HIT_SOUND_ID, 1F, 1F,
                    0, 0, 1f)
            }

            override fun onAnimationEnd(animation: Animation?) {
                if (diceRoll == 6 && diceRoll2 == 6||diceRoll == 5 && diceRoll2 == 5||diceRoll == 4 && diceRoll2 == 4) {
                    if (soundPool != null) {
                        soundPool!!.play(
                            MISS_SOUND_ID, 1F, 1F,
                            0, 0, 1f
                        )
                    }
                }
                    diceInfo?.text = "${diceRoll.toString()}:${diceRoll2.toString()}"
                }


            override fun onAnimationRepeat(animation: Animation?) {

            }
        })

        diceR1?.startAnimation(rotate)
        diceR2?.startAnimation(rotate)

        diceR1?.setImageResource(drawableResource)
        diceR2?.setImageResource(drawableResource2)
       // diceInfo?.text = "Вам выпало:${diceRoll.toString()} ${diceRoll2.toString()}"

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

    override fun onBackPressed() {
        super.onBackPressed()

        Handler().postDelayed(
            {
                val intent = Intent(this, StartActivity::class.java)
                //  intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
                //   finish()
            },
            500
        )
    }

    class Dice(private val numSides: Int) {
        fun roll(): Int {
            return (1..numSides).random()
        }
    }
    fun initializeSoundEffects(context: Context){
        // initialize SoundPool to play the app's three sound effects
        // initialize SoundPool to play the app's three sound effects
        soundPool = SoundPool(
            3, AudioManager.STREAM_MUSIC,
            0
        )
        // set sound effect volume
        // set sound effect volume
        val manager = context.getSystemService(AUDIO_SERVICE) as AudioManager
        volume = manager.getStreamVolume(AudioManager.STREAM_MUSIC)
        // create sound map
        // create sound map
        soundMap = HashMap() // create new HashMap


        // add each sound effect to the SoundPool

        // add each sound effect to the SoundPool
        (soundMap as HashMap<Int, Int>).put(
            HIT_SOUND_ID,
           soundPool!!.load(context, R.raw.d1, SOUND_PRIORITY)
        )
        (soundMap as HashMap<Int, Int>).put(
            MISS_SOUND_ID,
            soundPool!!.load(context, R.raw.short1, SOUND_PRIORITY)
        )
    }
}
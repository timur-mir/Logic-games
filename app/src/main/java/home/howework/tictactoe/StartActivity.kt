package home.howework.tictactoe

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import home.howe.DiceRoller
import home.howe.Roulette

class StartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        val startButton = findViewById<Button>(home.howework.tictactoe.R.id.start)
        val radioGroup1 = findViewById<RadioGroup>(R.id.articlesRadioGroup)
        var key: Int = 0
        startButton.setOnClickListener {
            when (key) {
                1 -> dispatcherActivity(1)
                2 -> dispatcherActivity(2)
                3 -> dispatcherActivity(3)
                4 -> dispatcherActivity(4)
                5 -> dispatcherActivity(5)
                6-> dispatcherActivity(6)
                else -> if (key == 0)
                    Snackbar.make(
                        findViewById(R.id.content),
                        "Выберите игру",
                        Snackbar.LENGTH_LONG
                    )
                        .setActionTextColor(Color.GRAY)
                        .setBackgroundTint(Color.BLUE)
                        .show()

            }

        }
        radioGroup1.setOnCheckedChangeListener { _, cheked ->
            when (cheked) {
                R.id.ticTacToe -> {
                    key = 1
                }

                R.id.fifteen -> {
                    key = 2
                }

                R.id.bar_x -> {
                    key = 3
                }

                R.id.rouletteR -> {
                    key = 4
                }

                R.id.dice-> {
                    key = 5
                }
                R.id.minesweeper-> {
                    key = 6
                }
            }
        }
    }

    private fun dispatcherActivity(activity: Int) {
        when (activity) {
            1 -> {
                loadGameTicTacToe()
            }

            2 -> {
                loadGameFifteen()
            }

            3 -> {
                loadGameBarX()
            }

            4 -> {
                loadGameCasino()
            }

            5 -> {
                loadGameDiceRoller()
            }
            6-> {
                loadGameMineSweeper()
            }
        }
    }

    private fun loadGameTicTacToe() {
        loadFromIntent(MainActivity())
    }

    private fun loadGameFifteen() {
        loadFromIntent(Fifteen())
    }

    private fun loadGameBarX() {
        loadFromIntent(WheelActivity())
    }

    private fun loadGameCasino() {
        loadFromIntent(Roulette())
    }

    private fun loadGameDiceRoller() {
        loadFromIntent(DiceRoller())
    }
    private fun loadGameMineSweeper() {
        loadFromIntent(Minesweeper())
    }

    fun loadFromIntent(nameActivity: AppCompatActivity) {
        Handler().postDelayed(
            {
                val intent = Intent(this, nameActivity::class.java)
                //  intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
                //    finish()
            },
            500
        )
    }
}
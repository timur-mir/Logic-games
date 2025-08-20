package home.howework. tictactoe

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity(), View.OnClickListener {
    var buttons = Array(3) {
        arrayOfNulls<Button>(
            3
        )
    }
    private var player1Turn = true
    private var roundCount = 0
    private var player1Points = 0
    private var player2Points = 0
    private var textViewPlayer1: TextView? = null
    private var textViewPlayer2: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(home.howework.tictactoe.R.layout.activity_tictactoe)
        supportActionBar?.title = "Крестики-нолики"

        textViewPlayer1 = findViewById<TextView>(home.howework.tictactoe.R.id.text_view_p1)
        textViewPlayer2 = findViewById<TextView>(home.howework.tictactoe.R.id.text_view_p2)

        for (i in 0..2) {
            for (j in 0..2) {
                val buttonID = "button_$i$j"
                val resID = resources.getIdentifier(buttonID, "id", packageName)
                buttons[i][j] = findViewById<Button>(resID)
                buttons[i][j]?.setOnClickListener(this)
            }
        }
        val buttonReset = findViewById<Button>(home.howework.tictactoe.R.id.button_reset)
        buttonReset.setOnClickListener {
            resetGame()
        }
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
    private fun resetGame() {
        player1Points=0
        player2Points=0
        updatePointsText()
        resetBoard()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("roundCount",roundCount)
        outState.putInt("player1Points",player1Points)
        outState.putInt("player2Points",player2Points)
        outState.putBoolean("player1Turn",player1Turn)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        roundCount=savedInstanceState.getInt("roundCount")
        player1Points=savedInstanceState.getInt("player1Points")
        player2Points=savedInstanceState.getInt("player2Points")
        player1Turn=savedInstanceState.getBoolean("player1Turn")
    }
    override fun onClick(v: View?) {
        if ((v as Button).text.toString() != "") {
            return
        }

        if (player1Turn) {
            v.text = "X"
        } else {
            v.text = "O"
        }

        roundCount++

        if (checkForWin()) {
            if (player1Turn) {
                player1Wins()
            } else {
                player2Wins()
            }
        } else if (roundCount == 9) {
            draw()
        } else {
            player1Turn = !player1Turn
        }
    }
    private fun checkForWin(): Boolean {
        val field = Array(3) {
            arrayOfNulls<String>(
                3
            )
        }
        for (i in 0..2) {
            for (j in 0..2) {
                field[i][j] = buttons[i][j]!!.text.toString()
            }
        }
        for (i in 0..2) {
            if (field[i][0].equals(field[i][1]) && field[i][0].equals(field[i][2]) && field[i][0] != "") {
                return true
            }
        }
        for (i in 0..2) {
            if (field[0][i].equals(field[1][i]) && field[0][i].equals(field[2][i]) && field[0][i] != "") {
                return true
            }
        }
        if (field[0][0].equals(field[1][1]) && field[0][0].equals(field[2][2]) && field[0][0] != "") {
            return true
        }
        if (field[0][2].equals(field[1][1]) && field[0][2].equals(field[2][0]) && field[0][2] != "") {
            return  true}
        return  false
    }
    private fun player1Wins() {
        player1Points++
        Toast.makeText(this,"Победил 1 игрок",Toast.LENGTH_SHORT).show()
        updatePointsText()
        resetBoard()
    }
    private fun player2Wins() {
        player2Points++
        Toast.makeText(this,"Победил 2 игрок",Toast.LENGTH_SHORT).show()
        updatePointsText()
        resetBoard()
    }
    private fun resetBoard() {
        for (i in 0..2) {
            for (j in 0..2) {
                buttons[i][j]?.text = ""
            }
        }
        roundCount=0
        player1Turn=true
    }

    private fun updatePointsText() {
        textViewPlayer1?.text="Игрок 1: ${player1Points}"
        textViewPlayer2?.text="Игрок 2: ${player2Points}"
    }

    private fun draw() {
        Toast.makeText(this,"Ничья",Toast.LENGTH_SHORT).show()
        resetBoard()
    }
}
package home.howework.tictactoe.chess

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import home.howework.tictactoe.R
import home.howework.tictactoe.chess.core.ChessView

class ChessActivity : AppCompatActivity(),ChessView.StatusGameListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chess)
        supportActionBar?.title = "Шахматы";
        val chessView = findViewById<ChessView>(R.id.chessView)
        val btnNewGame = findViewById<Button>(R.id.btnNewGame)
        val statusGame = findViewById<TextView>(R.id.status_game)
        btnNewGame.setOnClickListener {
            chessView.resetGame()
        }
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onStatusGame(string: String) {
        val statusGame = findViewById<TextView>(R.id.status_game)
        statusGame.text=string
    }
}

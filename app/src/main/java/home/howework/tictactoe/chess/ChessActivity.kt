package home.howework.tictactoe.chess

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import home.howework.tictactoe.R
import home.howework.tictactoe.chess.core.ChessView

class ChessActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chess)

        val chessView = findViewById<ChessView>(R.id.chessView)
        val btnNewGame = findViewById<Button>(R.id.btnNewGame)
        btnNewGame.isEnabled = false
        btnNewGame.setOnClickListener {
            btnNewGame.isEnabled = true
            chessView.resetGame()
        }
    }
}

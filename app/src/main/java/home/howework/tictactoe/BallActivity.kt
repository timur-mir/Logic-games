package home.howework.tictactoe

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import home.howework.tictactoe.balls.BallGameView

class BallActivity : AppCompatActivity() {
    lateinit var ballView:BallGameView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
      //  supportActionBar?.title = "Пузыри"
      ballView = BallGameView(this)
        setContentView(ballView)
    }
}
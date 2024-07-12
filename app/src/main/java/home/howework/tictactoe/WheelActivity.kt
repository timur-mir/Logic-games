package home.howework.tictactoe

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.Random


class WheelActivity : AppCompatActivity() {
    private lateinit  var msg: TextView
    private lateinit var img1: ImageView
    private lateinit var img2: ImageView
    private lateinit var img3: ImageView
    private lateinit var img4: ImageView
    private lateinit var img5: ImageView
    private lateinit var img6: ImageView
    private lateinit var img7: ImageView
    private lateinit var img8: ImageView
    private lateinit var img9: ImageView
    private lateinit var img10: ImageView
    private lateinit var img11: ImageView
    private lateinit var img12: ImageView
    private lateinit var wheel1:Wheel
    private lateinit var wheel2:Wheel
    private lateinit var wheel3:Wheel
    private lateinit var wheel4:Wheel
    private lateinit var wheel5:Wheel
    private lateinit var wheel6:Wheel
    private lateinit var wheel7:Wheel
    private lateinit var wheel8:Wheel
    private lateinit var wheel9:Wheel
    private lateinit var wheel10:Wheel
    private lateinit var wheel11:Wheel
    private lateinit var wheel12:Wheel
    private var isStarted = false
    private val random: Random = Random()

    fun randomLong(lower: Long, upper: Long): Long {
        return lower + (random.nextDouble() * (upper - lower)).toLong()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wheel)
      supportActionBar?.title = "Колёса фортуны"
        img1 = findViewById<ImageView>(R.id.img1);
        img2 = findViewById<ImageView>(R.id.img2);
        img3 = findViewById<ImageView>(R.id.img3);
        img4 = findViewById<ImageView>(R.id.img4);
        img5 = findViewById<ImageView>(R.id.img5);
        img6 = findViewById<ImageView>(R.id.img6);
        img7 = findViewById<ImageView>(R.id.img7);
        img8 = findViewById<ImageView>(R.id.img8);
        img9 = findViewById<ImageView>(R.id.img9);
        img10 = findViewById<ImageView>(R.id.img10);
        img11 = findViewById<ImageView>(R.id.img11);
        img12 = findViewById<ImageView>(R.id.img12);
        val btn = findViewById<Button>(R.id.btn);
        msg = findViewById<TextView>(R.id.msg);

        btn.setOnClickListener {
            if (isStarted) {
                wheel1.stopWheel();
                wheel2.stopWheel();
                wheel3.stopWheel();
                wheel4.stopWheel();
                wheel5.stopWheel();
                wheel6.stopWheel();
                wheel7.stopWheel();
                wheel8.stopWheel();
                wheel9.stopWheel();
                wheel10.stopWheel();
                wheel11.stopWheel();
                wheel12.stopWheel();


                if (((wheel1.currentIndex == wheel2.currentIndex)
                            && (wheel2.currentIndex == wheel3.currentIndex)
                            && (wheel3.currentIndex == wheel4.currentIndex)))
                {
                    msg.setText("Превосходно!!!");
                }
                else if ((wheel1.currentIndex == wheel2.currentIndex && wheel2.currentIndex == wheel3.currentIndex)
                    ||(wheel2.currentIndex == wheel3.currentIndex && wheel3.currentIndex == wheel4.currentIndex)
                    ||(wheel1.currentIndex == wheel2.currentIndex && wheel1.currentIndex == wheel4.currentIndex)
                    ||(wheel1.currentIndex == wheel3.currentIndex && wheel3.currentIndex == wheel4.currentIndex))
                {
                    msg.setText("Отлично!");
                }
                else if ((wheel1.currentIndex == wheel2.currentIndex || wheel2.currentIndex == wheel3.currentIndex
                            || wheel1.currentIndex == wheel3.currentIndex||wheel1.currentIndex == wheel4.currentIndex
                            ||wheel2.currentIndex == wheel4.currentIndex||wheel3.currentIndex == wheel4.currentIndex))
                {
                    msg.setText("Нормально");
                }




                else {msg.setText("Ещё повезёт");}

                btn.setText("Start");
                isStarted = false;

            } else {
                wheel1 = Wheel(object : Wheel.WheelListener {
                    override fun newImage(img: Int) {
                        runOnUiThread(object : Runnable {
                            override fun run() {
                                img1.setImageResource(img)
                            }

                        })
                    }

                }, 50, randomLong(0, 100))
                wheel1!!.start()

                wheel2 = Wheel(object : Wheel.WheelListener {
                    override fun newImage(img: Int) {
                        runOnUiThread(object : Runnable {
                            override fun run() {
                                img2.setImageResource(img)
                            }

                        })
                    }

                }, 50, randomLong(0, 100))
                wheel2!!.start()

                wheel3 = Wheel(object : Wheel.WheelListener {
                    override fun newImage(img: Int) {
                        runOnUiThread(object : Runnable {
                            override fun run() {
                                img3.setImageResource(img)
                            }

                        })
                    }

                }, 50, randomLong(0, 100))
                wheel3!!.start()

                wheel4 = Wheel(object : Wheel.WheelListener {
                    override fun newImage(img: Int) {
                        runOnUiThread(object : Runnable {
                            override fun run() {
                                img4.setImageResource(img)
                            }

                        })
                    }

                }, 50, randomLong(0, 100))
                wheel4!!.start()

                wheel5 = Wheel(object : Wheel.WheelListener {
                    override fun newImage(img: Int) {
                        runOnUiThread(object : Runnable {
                            override fun run() {
                                img5.setImageResource(img)
                            }

                        })
                    }

                }, 50, randomLong(0, 100))
                wheel5!!.start()

                wheel6 = Wheel(object : Wheel.WheelListener {
                    override fun newImage(img: Int) {
                        runOnUiThread(object : Runnable {
                            override fun run() {
                                img6.setImageResource(img)
                            }

                        })
                    }

                }, 50, randomLong(0, 100))
                wheel6!!.start()

                wheel7 = Wheel(object : Wheel.WheelListener {
                    override fun newImage(img: Int) {
                        runOnUiThread(object : Runnable {
                            override fun run() {
                                img7.setImageResource(img)
                            }

                        })
                    }

                }, 50, randomLong(0, 100))
                wheel7!!.start()

                wheel8 = Wheel(object : Wheel.WheelListener {
                    override fun newImage(img: Int) {
                        runOnUiThread(object : Runnable {
                            override fun run() {
                                img8.setImageResource(img)
                            }

                        })
                    }

                }, 50, randomLong(0, 100))
                wheel8!!.start()

                wheel9 = Wheel(object : Wheel.WheelListener {
                    override fun newImage(img: Int) {
                        runOnUiThread(object : Runnable {
                            override fun run() {
                                img9.setImageResource(img)
                            }

                        })
                    }

                }, 50, randomLong(0, 100))
                wheel9!!.start()

                wheel10 = Wheel(object : Wheel.WheelListener {
                    override fun newImage(img: Int) {
                        runOnUiThread(object : Runnable {
                            override fun run() {
                                img10.setImageResource(img)
                            }

                        })
                    }

                }, 50, randomLong(0, 100))
                wheel10!!.start()

                wheel11= Wheel(object : Wheel.WheelListener {
                    override fun newImage(img: Int) {
                        runOnUiThread(object : Runnable {
                            override fun run() {
                                img11.setImageResource(img)
                            }

                        })
                    }

                }, 50, randomLong(0, 100))
                wheel11!!.start()

                wheel12 = Wheel(object : Wheel.WheelListener {
                    override fun newImage(img: Int) {
                        runOnUiThread(object : Runnable {
                            override fun run() {
                                img12.setImageResource(img)
                            }

                        })
                    }

                }, 50, randomLong(0, 100))
                wheel12!!.start()

                btn.setText("Stop");
                msg.setText("");
                isStarted = true;
            }
        }

        }
        }


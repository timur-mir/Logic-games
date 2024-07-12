package home.howework.tictactoe

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.GridView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import home.howework.tictactoe.Fifteen.Counter.countForWin

class Fifteen : AppCompatActivity() {
    lateinit var tiles: ArrayList<GridViewModal>
    lateinit var mirrorTilesState: ArrayList<Tile>

    sealed class Tile {
        data class CELL_FULL(val number: Int) : Tile()
        data class CELL_NONE(val number: Int) : Tile()
    }

    object Counter {
        var countForWin = 0
    }

    companion object {
        var tilesForCheck = ArrayList<GridViewModal>(15)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fifteen)
        supportActionBar?.title = "Пятнашки";
        tiles = ArrayList<GridViewModal>(15)
        mirrorTilesState = ArrayList<Tile>(15)
        for (number in 1..15) {
            tiles.add(GridViewModal(number))
            tilesForCheck.add(GridViewModal(number))
            mirrorTilesState.add(Tile.CELL_FULL(number))

        }
        tiles.add(GridViewModal(0))
        mirrorTilesState.add(Tile.CELL_NONE(0))
        tilesForCheck.add(GridViewModal(0))

//        tilesForCheck = tiles

        tiles.shuffle()
        tiles.forEachIndexed { index, it ->
            if (it.shapeNumber == 0) {
                mirrorTilesState[index] = Tile.CELL_NONE(it.shapeNumber)
            } else
                mirrorTilesState[index] = Tile.CELL_FULL(it.shapeNumber)
        }
        val mGrid = findViewById<GridView>(R.id.field)
        mGrid.numColumns = 4
        mGrid.isEnabled = true;

        val tilesAdapter = GridAdapter(this@Fifteen, tiles as ArrayList<GridViewModal>)
        mGrid.adapter = tilesAdapter
        mGrid.setOnItemClickListener { _, _, position, _ ->
            countForWin += 1
            if (position == 0 || position == 12) {
                algorithm1LeftSingleCorners(position)
                val tilesAdapter = GridAdapter(this@Fifteen, tiles)
                mGrid.adapter = tilesAdapter
                tilesAdapter.notifyDataSetChanged()
                tilesAdapter.notifyDataSetInvalidated()
            } else if (position == 3 || position == 15) {
                algorithm2RightSingleCorners(position)
                val tilesAdapter = GridAdapter(this@Fifteen, tiles)
                mGrid.adapter = tilesAdapter
                tilesAdapter.notifyDataSetChanged()
                tilesAdapter.notifyDataSetInvalidated()
            } else if (position == 4 || position == 8) {
                algorithm3LeftBarer(position)
                val tilesAdapter = GridAdapter(this@Fifteen, tiles)
                mGrid.adapter = tilesAdapter
                tilesAdapter.notifyDataSetChanged()
                tilesAdapter.notifyDataSetInvalidated()
            } else if (position == 7 || position == 11) {
                algorithm4RightBarer(position)
                val tilesAdapter = GridAdapter(this@Fifteen, tiles)
                mGrid.adapter = tilesAdapter
                tilesAdapter.notifyDataSetChanged()
                tilesAdapter.notifyDataSetInvalidated()
            } else if (position == 1 || position == 2) {
                algorithm5MiddlePosition(position)
                val tilesAdapter = GridAdapter(this@Fifteen, tiles)
                mGrid.adapter = tilesAdapter
                tilesAdapter.notifyDataSetChanged()
                tilesAdapter.notifyDataSetInvalidated()
            } else if (position == 5 || position == 6) {
                algorithm6MiddlePosition(position)
                val tilesAdapter = GridAdapter(this@Fifteen, tiles)
                mGrid.adapter = tilesAdapter
                tilesAdapter.notifyDataSetChanged()
                tilesAdapter.notifyDataSetInvalidated()
            } else if (position == 9 || position == 10) {
                algorithm7MiddlePosition(position)
                val tilesAdapter = GridAdapter(this@Fifteen, tiles)
                mGrid.adapter = tilesAdapter
                tilesAdapter.notifyDataSetChanged()
                tilesAdapter.notifyDataSetInvalidated()
            } else if (position == 13 || position == 14) {
                algorithm8MiddlePosition(position)
                val tilesAdapter = GridAdapter(this@Fifteen, tiles)
                mGrid.adapter = tilesAdapter
                tilesAdapter.notifyDataSetChanged()
                tilesAdapter.notifyDataSetInvalidated()
            }
            //checkWin()
        }
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

    fun checkWin() {
        var win = false
        var count = 0
        tilesForCheck.forEachIndexed { index, it ->
            if (it.shapeNumber == tiles[index].shapeNumber) {
                win = true
                count += 1
            }
        }
        if (win && count == 16) {
            Toast.makeText(
                applicationContext, "Ура!Вы решили пазл за ${countForWin.toString()} ходов!",
                Toast.LENGTH_SHORT
            ).show()
        }
    }


    fun algorithm1LeftSingleCorners(fieldPosition: Int) {
        if (fieldPosition == 0 && mirrorTilesState[fieldPosition] != Tile.CELL_NONE(0)) {
            if (mirrorTilesState[fieldPosition + 1] == Tile.CELL_NONE(0)) {
                val temporary = mirrorTilesState[fieldPosition]
                mirrorTilesState[fieldPosition] = Tile.CELL_NONE(0)
                mirrorTilesState[fieldPosition + 1] = temporary
                val temporaryT = tiles[fieldPosition]
                tiles[fieldPosition] = tiles[fieldPosition + 1]
                tiles[fieldPosition + 1] = temporaryT
            }
            if (fieldPosition == 0 && mirrorTilesState[fieldPosition] != Tile.CELL_NONE(0)) {
                if (mirrorTilesState[fieldPosition + 4] == Tile.CELL_NONE(0)) {
                    val temporary = mirrorTilesState[fieldPosition]
                    mirrorTilesState[fieldPosition] = Tile.CELL_NONE(0)
                    mirrorTilesState[fieldPosition + 4] = temporary
                    val temporaryT = tiles[fieldPosition]
                    tiles[fieldPosition] = tiles[fieldPosition + 4]
                    tiles[fieldPosition + 4] = temporaryT
                }
            }
        } else {
            if (fieldPosition == 0 && mirrorTilesState[fieldPosition] != Tile.CELL_NONE(0)) {
                if (mirrorTilesState[fieldPosition + 1] == Tile.CELL_FULL(0)) {
                    val temporary = mirrorTilesState[fieldPosition]
                    mirrorTilesState[fieldPosition] = Tile.CELL_FULL(0)
                    mirrorTilesState[fieldPosition + 1] = temporary
                    val temporaryT = tiles[fieldPosition]
                    tiles[fieldPosition] = tiles[fieldPosition + 1]
                    tiles[fieldPosition + 1] = temporaryT
                }

                if (mirrorTilesState[fieldPosition + 4] == Tile.CELL_FULL(0)) {
                    val temporary = mirrorTilesState[fieldPosition]
                    mirrorTilesState[fieldPosition] = Tile.CELL_FULL(0)
                    mirrorTilesState[fieldPosition + 4] = temporary
                    val temporaryT = tiles[fieldPosition]
                    tiles[fieldPosition] = tiles[fieldPosition + 4]
                    tiles[fieldPosition + 4] = temporaryT
                }
            }


        }
        if (fieldPosition == 12 && mirrorTilesState[fieldPosition] != Tile.CELL_NONE(0)) {
            if (mirrorTilesState[fieldPosition + 1] == Tile.CELL_NONE(0)) {
                val temporary = mirrorTilesState[fieldPosition]
                mirrorTilesState[fieldPosition] = Tile.CELL_NONE(0)
                mirrorTilesState[fieldPosition + 1] = temporary
                val temporaryT = tiles[fieldPosition]
                tiles[fieldPosition] = tiles[fieldPosition + 1]
                tiles[fieldPosition + 1] = temporaryT
            }
            if (fieldPosition == 12 && mirrorTilesState[fieldPosition] != Tile.CELL_NONE(0)) {
                if (mirrorTilesState[fieldPosition - 4] == Tile.CELL_NONE(0)) {
                    val temporary = mirrorTilesState[fieldPosition]
                    mirrorTilesState[fieldPosition] = Tile.CELL_NONE(0)
                    mirrorTilesState[fieldPosition - 4] = temporary
                    val temporaryT = tiles[fieldPosition]
                    tiles[fieldPosition] = tiles[fieldPosition - 4]
                    tiles[fieldPosition - 4] = temporaryT
                }
            }
        }
        checkWin()
    }

    fun algorithm2RightSingleCorners(fieldPosition: Int) {

        if (fieldPosition == 3 && mirrorTilesState[fieldPosition] != Tile.CELL_NONE(0)) {
            if (mirrorTilesState[fieldPosition - 1] == Tile.CELL_NONE(0)) {
                val temporary = mirrorTilesState[fieldPosition]
                mirrorTilesState[fieldPosition] = Tile.CELL_NONE(0)
                mirrorTilesState[fieldPosition - 1] = temporary
                val temporaryT = tiles[fieldPosition]
                tiles[fieldPosition] = tiles[fieldPosition - 1]
                tiles[fieldPosition - 1] = temporaryT
            }
            if (fieldPosition == 3 && mirrorTilesState[fieldPosition] != Tile.CELL_NONE(0)) {
                if (mirrorTilesState[fieldPosition + 4] == Tile.CELL_NONE(0)) {
                    val temporary = mirrorTilesState[fieldPosition]
                    mirrorTilesState[fieldPosition] = Tile.CELL_NONE(0)
                    mirrorTilesState[fieldPosition + 4] = temporary
                    val temporaryT = tiles[fieldPosition]
                    tiles[fieldPosition] = tiles[fieldPosition + 4]
                    tiles[fieldPosition + 4] = temporaryT
                }
            }
        } else {
            if (fieldPosition == 3 && mirrorTilesState[fieldPosition] != Tile.CELL_FULL(0)) {
                if (mirrorTilesState[fieldPosition - 1] == Tile.CELL_FULL(0)) {
                    val temporary = mirrorTilesState[fieldPosition]
                    mirrorTilesState[fieldPosition] = Tile.CELL_FULL(0)
                    mirrorTilesState[fieldPosition - 1] = temporary
                    val temporaryT = tiles[fieldPosition]
                    tiles[fieldPosition] = tiles[fieldPosition - 1]
                    tiles[fieldPosition - 1] = temporaryT
                }

                if (mirrorTilesState[fieldPosition + 4] == Tile.CELL_FULL(0)) {
                    val temporary = mirrorTilesState[fieldPosition]
                    mirrorTilesState[fieldPosition] = Tile.CELL_FULL(0)
                    mirrorTilesState[fieldPosition + 4] = temporary
                    val temporaryT = tiles[fieldPosition]
                    tiles[fieldPosition] = tiles[fieldPosition + 4]
                    tiles[fieldPosition + 4] = temporaryT
                }
            }
        }
        if (fieldPosition == 15 && mirrorTilesState[fieldPosition] != Tile.CELL_NONE(0)) {
            if (mirrorTilesState[fieldPosition - 1] == Tile.CELL_NONE(0)) {
                val temporary = mirrorTilesState[fieldPosition]
                mirrorTilesState[fieldPosition] = Tile.CELL_NONE(0)
                mirrorTilesState[fieldPosition - 1] = temporary
                val temporaryT = tiles[fieldPosition]
                tiles[fieldPosition] = tiles[fieldPosition - 1]
                tiles[fieldPosition - 1] = temporaryT
            }
            if (fieldPosition == 15 && mirrorTilesState[fieldPosition] != Tile.CELL_NONE(0)) {
                if (mirrorTilesState[fieldPosition - 4] == Tile.CELL_NONE(0)) {
                    val temporary = mirrorTilesState[fieldPosition]
                    mirrorTilesState[fieldPosition] = Tile.CELL_NONE(0)
                    mirrorTilesState[fieldPosition - 4] = temporary
                    val temporaryT = tiles[fieldPosition]
                    tiles[fieldPosition] = tiles[fieldPosition - 4]
                    tiles[fieldPosition - 4] = temporaryT
                }
            } else {
                if (fieldPosition == 15 && mirrorTilesState[fieldPosition] != Tile.CELL_FULL(0)) {
                    if (mirrorTilesState[fieldPosition - 1] == Tile.CELL_FULL(0)) {
                        val temporary = mirrorTilesState[fieldPosition]
                        mirrorTilesState[fieldPosition] = Tile.CELL_FULL(0)
                        mirrorTilesState[fieldPosition - 1] = temporary
                        val temporaryT = tiles[fieldPosition]
                        tiles[fieldPosition] = tiles[fieldPosition - 1]
                        tiles[fieldPosition - 1] = temporaryT
                    }

                    if (mirrorTilesState[fieldPosition - 4] == Tile.CELL_FULL(0)) {
                        val temporary = mirrorTilesState[fieldPosition]
                        mirrorTilesState[fieldPosition] = Tile.CELL_FULL(0)
                        mirrorTilesState[fieldPosition - 4] = temporary
                        val temporaryT = tiles[fieldPosition]
                        tiles[fieldPosition] = tiles[fieldPosition - 4]
                        tiles[fieldPosition - 4] = temporaryT
                    }
                }
            }
        }
        checkWin()
    }

    fun algorithm3LeftBarer(fieldPosition: Int) {
        if (fieldPosition == 4 && mirrorTilesState[fieldPosition] != Tile.CELL_NONE(0)) {
            if (mirrorTilesState[fieldPosition + 1] == Tile.CELL_NONE(0)) {
                val temporary = mirrorTilesState[fieldPosition]
                mirrorTilesState[fieldPosition] = Tile.CELL_NONE(0)
                mirrorTilesState[fieldPosition + 1] = temporary
                val temporaryT = tiles[fieldPosition]
                tiles[fieldPosition] = tiles[fieldPosition + 1]
                tiles[fieldPosition + 1] = temporaryT
            }
            if (fieldPosition == 4 && mirrorTilesState[fieldPosition] != Tile.CELL_NONE(0)) {
                if (mirrorTilesState[fieldPosition + 4] == Tile.CELL_NONE(0)) {
                    val temporary = mirrorTilesState[fieldPosition]
                    mirrorTilesState[fieldPosition] = Tile.CELL_NONE(0)
                    mirrorTilesState[fieldPosition + 4] = temporary
                    val temporaryT = tiles[fieldPosition]
                    tiles[fieldPosition] = tiles[fieldPosition + 4]
                    tiles[fieldPosition + 4] = temporaryT
                }
            }
            if (fieldPosition == 4 && mirrorTilesState[fieldPosition] != Tile.CELL_NONE(0)) {
                if (mirrorTilesState[fieldPosition - 4] == Tile.CELL_NONE(0)) {
                    val temporary = mirrorTilesState[fieldPosition]
                    mirrorTilesState[fieldPosition] = Tile.CELL_NONE(0)
                    mirrorTilesState[fieldPosition - 4] = temporary
                    val temporaryT = tiles[fieldPosition]
                    tiles[fieldPosition] = tiles[fieldPosition - 4]
                    tiles[fieldPosition - 4] = temporaryT
                }
            }
        }
        if (fieldPosition == 8 && mirrorTilesState[fieldPosition] != Tile.CELL_NONE(0)) {
            if (mirrorTilesState[fieldPosition + 1] == Tile.CELL_NONE(0)) {
                val temporary = mirrorTilesState[fieldPosition]
                mirrorTilesState[fieldPosition] = Tile.CELL_NONE(0)
                mirrorTilesState[fieldPosition + 1] = temporary
                val temporaryT = tiles[fieldPosition]
                tiles[fieldPosition] = tiles[fieldPosition + 1]
                tiles[fieldPosition + 1] = temporaryT
            }
            if (fieldPosition == 8 && mirrorTilesState[fieldPosition] != Tile.CELL_NONE(0)) {
                if (mirrorTilesState[fieldPosition + 4] == Tile.CELL_NONE(0)) {
                    val temporary = mirrorTilesState[fieldPosition]
                    mirrorTilesState[fieldPosition] = Tile.CELL_NONE(0)
                    mirrorTilesState[fieldPosition + 4] = temporary
                    val temporaryT = tiles[fieldPosition]
                    tiles[fieldPosition] = tiles[fieldPosition + 4]
                    tiles[fieldPosition + 4] = temporaryT
                }
            }
            if (fieldPosition == 8 && mirrorTilesState[fieldPosition] != Tile.CELL_NONE(0)) {
                if (mirrorTilesState[fieldPosition - 4] == Tile.CELL_NONE(0)) {
                    val temporary = mirrorTilesState[fieldPosition]
                    mirrorTilesState[fieldPosition] = Tile.CELL_NONE(0)
                    mirrorTilesState[fieldPosition - 4] = temporary
                    val temporaryT = tiles[fieldPosition]
                    tiles[fieldPosition] = tiles[fieldPosition - 4]
                    tiles[fieldPosition - 4] = temporaryT
                }
            }
        }
        checkWin()
    }

    fun algorithm4RightBarer(fieldPosition: Int) {
        if (fieldPosition == 11 && mirrorTilesState[fieldPosition] != Tile.CELL_NONE(0)) {
            if (mirrorTilesState[fieldPosition - 1] == Tile.CELL_NONE(0)) {
                val temporary = mirrorTilesState[fieldPosition]
                mirrorTilesState[fieldPosition] = Tile.CELL_NONE(0)
                mirrorTilesState[fieldPosition - 1] = temporary
                val temporaryT = tiles[fieldPosition]
                tiles[fieldPosition] = tiles[fieldPosition - 1]
                tiles[fieldPosition - 1] = temporaryT
            }
            if (fieldPosition == 11 && mirrorTilesState[fieldPosition] != Tile.CELL_NONE(0)) {
                if (mirrorTilesState[fieldPosition + 4] == Tile.CELL_NONE(0)) {
                    val temporary = mirrorTilesState[fieldPosition]
                    mirrorTilesState[fieldPosition] = Tile.CELL_NONE(0)
                    mirrorTilesState[fieldPosition + 4] = temporary
                    val temporaryT = tiles[fieldPosition]
                    tiles[fieldPosition] = tiles[fieldPosition + 4]
                    tiles[fieldPosition + 4] = temporaryT
                }
            }
            if (fieldPosition == 11 && mirrorTilesState[fieldPosition] != Tile.CELL_NONE(0)) {
                if (mirrorTilesState[fieldPosition - 4] == Tile.CELL_NONE(0)) {
                    val temporary = mirrorTilesState[fieldPosition]
                    mirrorTilesState[fieldPosition] = Tile.CELL_NONE(0)
                    mirrorTilesState[fieldPosition - 4] = temporary
                    val temporaryT = tiles[fieldPosition]
                    tiles[fieldPosition] = tiles[fieldPosition - 4]
                    tiles[fieldPosition - 4] = temporaryT
                }
            }
        }
        if (fieldPosition == 7 && mirrorTilesState[fieldPosition] != Tile.CELL_NONE(0)) {
            if (mirrorTilesState[fieldPosition - 1] == Tile.CELL_NONE(0)) {
                val temporary = mirrorTilesState[fieldPosition]
                mirrorTilesState[fieldPosition] = Tile.CELL_NONE(0)
                mirrorTilesState[fieldPosition - 1] = temporary
                val temporaryT = tiles[fieldPosition]
                tiles[fieldPosition] = tiles[fieldPosition - 1]
                tiles[fieldPosition - 1] = temporaryT
            }
            if (fieldPosition == 7 && mirrorTilesState[fieldPosition] != Tile.CELL_NONE(0)) {
                if (mirrorTilesState[fieldPosition + 4] == Tile.CELL_NONE(0)) {
                    val temporary = mirrorTilesState[fieldPosition]
                    mirrorTilesState[fieldPosition] = Tile.CELL_NONE(0)
                    mirrorTilesState[fieldPosition + 4] = temporary
                    val temporaryT = tiles[fieldPosition]
                    tiles[fieldPosition] = tiles[fieldPosition + 4]
                    tiles[fieldPosition + 4] = temporaryT
                }
            }
            if (fieldPosition == 7 && mirrorTilesState[fieldPosition] != Tile.CELL_NONE(0)) {
                if (mirrorTilesState[fieldPosition - 4] == Tile.CELL_NONE(0)) {
                    val temporary = mirrorTilesState[fieldPosition]
                    mirrorTilesState[fieldPosition] = Tile.CELL_NONE(0)
                    mirrorTilesState[fieldPosition - 4] = temporary
                    val temporaryT = tiles[fieldPosition]
                    tiles[fieldPosition] = tiles[fieldPosition - 4]
                    tiles[fieldPosition - 4] = temporaryT
                }
            }
        }
        checkWin()
    }

    fun algorithm5MiddlePosition(fieldPosition: Int) {
        if (fieldPosition == 1 && mirrorTilesState[fieldPosition] != Tile.CELL_NONE(0)) {
            if (mirrorTilesState[fieldPosition + 1] == Tile.CELL_NONE(0)) {
                val temporary = mirrorTilesState[fieldPosition]
                mirrorTilesState[fieldPosition] = Tile.CELL_NONE(0)
                mirrorTilesState[fieldPosition + 1] = temporary
                val temporaryT = tiles[fieldPosition]
                tiles[fieldPosition] = tiles[fieldPosition + 1]
                tiles[fieldPosition + 1] = temporaryT
            }
            if (fieldPosition == 1 && mirrorTilesState[fieldPosition] != Tile.CELL_NONE(0)) {
                if (mirrorTilesState[fieldPosition + 4] == Tile.CELL_NONE(0)) {
                    val temporary = mirrorTilesState[fieldPosition]
                    mirrorTilesState[fieldPosition] = Tile.CELL_NONE(0)
                    mirrorTilesState[fieldPosition + 4] = temporary
                    val temporaryT = tiles[fieldPosition]
                    tiles[fieldPosition] = tiles[fieldPosition + 4]
                    tiles[fieldPosition + 4] = temporaryT
                }
            }
            if (fieldPosition == 1 && mirrorTilesState[fieldPosition] != Tile.CELL_NONE(0)) {
                if (mirrorTilesState[fieldPosition - 1] == Tile.CELL_NONE(0)) {
                    val temporary = mirrorTilesState[fieldPosition]
                    mirrorTilesState[fieldPosition] = Tile.CELL_NONE(0)
                    mirrorTilesState[fieldPosition - 1] = temporary
                    val temporaryT = tiles[fieldPosition]
                    tiles[fieldPosition] = tiles[fieldPosition - 1]
                    tiles[fieldPosition - 1] = temporaryT
                }
            }
        } else {
            if (fieldPosition == 2 && mirrorTilesState[fieldPosition] != Tile.CELL_NONE(0)) {
                if (mirrorTilesState[fieldPosition - 1] == Tile.CELL_NONE(0)) {
                    val temporary = mirrorTilesState[fieldPosition]
                    mirrorTilesState[fieldPosition] = Tile.CELL_NONE(0)
                    mirrorTilesState[fieldPosition - 1] = temporary
                    val temporaryT = tiles[fieldPosition]
                    tiles[fieldPosition] = tiles[fieldPosition - 1]
                    tiles[fieldPosition - 1] = temporaryT
                }

                if (mirrorTilesState[fieldPosition + 4] == Tile.CELL_NONE(0)) {
                    val temporary = mirrorTilesState[fieldPosition]
                    mirrorTilesState[fieldPosition] = Tile.CELL_NONE(0)
                    mirrorTilesState[fieldPosition + 4] = temporary
                    val temporaryT = tiles[fieldPosition]
                    tiles[fieldPosition] = tiles[fieldPosition + 4]
                    tiles[fieldPosition + 4] = temporaryT
                }
                if (mirrorTilesState[fieldPosition + 1] == Tile.CELL_NONE(0)) {
                    val temporary = mirrorTilesState[fieldPosition]
                    mirrorTilesState[fieldPosition] = Tile.CELL_NONE(0)
                    mirrorTilesState[fieldPosition + 1] = temporary
                    val temporaryT = tiles[fieldPosition]
                    tiles[fieldPosition] = tiles[fieldPosition + 1]
                    tiles[fieldPosition + 1] = temporaryT
                }
            }

        }
        checkWin()
    }
    fun algorithm6MiddlePosition(fieldPosition: Int) {
        if (fieldPosition == 5 && mirrorTilesState[fieldPosition] != Tile.CELL_NONE(0)) {
            if (mirrorTilesState[fieldPosition + 1] == Tile.CELL_NONE(0)) {
                val temporary = mirrorTilesState[fieldPosition]
                mirrorTilesState[fieldPosition] = Tile.CELL_NONE(0)
                mirrorTilesState[fieldPosition + 1] = temporary
                val temporaryT = tiles[fieldPosition]
                tiles[fieldPosition] = tiles[fieldPosition + 1]
                tiles[fieldPosition + 1] = temporaryT
            }
            if (fieldPosition == 5 && mirrorTilesState[fieldPosition] != Tile.CELL_NONE(0)) {
                if (mirrorTilesState[fieldPosition + 4] == Tile.CELL_NONE(0)) {
                    val temporary = mirrorTilesState[fieldPosition]
                    mirrorTilesState[fieldPosition] = Tile.CELL_NONE(0)
                    mirrorTilesState[fieldPosition + 4] = temporary
                    val temporaryT = tiles[fieldPosition]
                    tiles[fieldPosition] = tiles[fieldPosition + 4]
                    tiles[fieldPosition + 4] = temporaryT
                }
            }
            if (fieldPosition == 5 && mirrorTilesState[fieldPosition] != Tile.CELL_NONE(0)) {
                if (mirrorTilesState[fieldPosition - 4] == Tile.CELL_NONE(0)) {
                    val temporary = mirrorTilesState[fieldPosition]
                    mirrorTilesState[fieldPosition] = Tile.CELL_NONE(0)
                    mirrorTilesState[fieldPosition - 4] = temporary
                    val temporaryT = tiles[fieldPosition]
                    tiles[fieldPosition] = tiles[fieldPosition - 4]
                    tiles[fieldPosition - 4] = temporaryT
                }
            }
            if (fieldPosition == 5 && mirrorTilesState[fieldPosition] != Tile.CELL_NONE(0)) {
                if (mirrorTilesState[fieldPosition - 1] == Tile.CELL_NONE(0)) {
                    val temporary = mirrorTilesState[fieldPosition]
                    mirrorTilesState[fieldPosition] = Tile.CELL_NONE(0)
                    mirrorTilesState[fieldPosition - 1] = temporary
                    val temporaryT = tiles[fieldPosition]
                    tiles[fieldPosition] = tiles[fieldPosition - 1]
                    tiles[fieldPosition - 1] = temporaryT
                }
            }
        } else {
            if (fieldPosition == 6 && mirrorTilesState[fieldPosition] != Tile.CELL_NONE(0)) {
                if (mirrorTilesState[fieldPosition - 1] == Tile.CELL_NONE(0)) {
                    val temporary = mirrorTilesState[fieldPosition]
                    mirrorTilesState[fieldPosition] = Tile.CELL_NONE(0)
                    mirrorTilesState[fieldPosition - 1] = temporary
                    val temporaryT = tiles[fieldPosition]
                    tiles[fieldPosition] = tiles[fieldPosition - 1]
                    tiles[fieldPosition - 1] = temporaryT
                }

                if (mirrorTilesState[fieldPosition + 4] == Tile.CELL_NONE(0)) {
                    val temporary = mirrorTilesState[fieldPosition]
                    mirrorTilesState[fieldPosition] = Tile.CELL_NONE(0)
                    mirrorTilesState[fieldPosition + 4] = temporary
                    val temporaryT = tiles[fieldPosition]
                    tiles[fieldPosition] = tiles[fieldPosition + 4]
                    tiles[fieldPosition + 4] = temporaryT
                }
                if (mirrorTilesState[fieldPosition - 4] == Tile.CELL_NONE(0)) {
                    val temporary = mirrorTilesState[fieldPosition]
                    mirrorTilesState[fieldPosition] = Tile.CELL_NONE(0)
                    mirrorTilesState[fieldPosition - 4] = temporary
                    val temporaryT = tiles[fieldPosition]
                    tiles[fieldPosition] = tiles[fieldPosition - 4]
                    tiles[fieldPosition - 4] = temporaryT
                }
                if (mirrorTilesState[fieldPosition + 1] == Tile.CELL_NONE(0)) {
                    val temporary = mirrorTilesState[fieldPosition]
                    mirrorTilesState[fieldPosition] = Tile.CELL_NONE(0)
                    mirrorTilesState[fieldPosition + 1] = temporary
                    val temporaryT = tiles[fieldPosition]
                    tiles[fieldPosition] = tiles[fieldPosition + 1]
                    tiles[fieldPosition + 1] = temporaryT
                }
            }
        }
        checkWin()
    }

    fun algorithm7MiddlePosition(fieldPosition: Int) {
        if (fieldPosition == 9 && mirrorTilesState[fieldPosition] != Tile.CELL_NONE(0)) {
            if (mirrorTilesState[fieldPosition + 1] == Tile.CELL_NONE(0)) {
                val temporary = mirrorTilesState[fieldPosition]
                mirrorTilesState[fieldPosition] = Tile.CELL_NONE(0)
                mirrorTilesState[fieldPosition + 1] = temporary
                val temporaryT = tiles[fieldPosition]
                tiles[fieldPosition] = tiles[fieldPosition + 1]
                tiles[fieldPosition + 1] = temporaryT
            }
            if (fieldPosition == 9 && mirrorTilesState[fieldPosition] != Tile.CELL_NONE(0)) {
                if (mirrorTilesState[fieldPosition + 4] == Tile.CELL_NONE(0)) {
                    val temporary = mirrorTilesState[fieldPosition]
                    mirrorTilesState[fieldPosition] = Tile.CELL_NONE(0)
                    mirrorTilesState[fieldPosition + 4] = temporary
                    val temporaryT = tiles[fieldPosition]
                    tiles[fieldPosition] = tiles[fieldPosition + 4]
                    tiles[fieldPosition + 4] = temporaryT
                }
            }
            if (fieldPosition == 9 && mirrorTilesState[fieldPosition] != Tile.CELL_NONE(0)) {
                if (mirrorTilesState[fieldPosition - 4] == Tile.CELL_NONE(0)) {
                    val temporary = mirrorTilesState[fieldPosition]
                    mirrorTilesState[fieldPosition] = Tile.CELL_NONE(0)
                    mirrorTilesState[fieldPosition - 4] = temporary
                    val temporaryT = tiles[fieldPosition]
                    tiles[fieldPosition] = tiles[fieldPosition - 4]
                    tiles[fieldPosition - 4] = temporaryT
                }
            }
            if (fieldPosition == 9 && mirrorTilesState[fieldPosition] != Tile.CELL_NONE(0)) {
                if (mirrorTilesState[fieldPosition - 1] == Tile.CELL_NONE(0)) {
                    val temporary = mirrorTilesState[fieldPosition]
                    mirrorTilesState[fieldPosition] = Tile.CELL_NONE(0)
                    mirrorTilesState[fieldPosition - 1] = temporary
                    val temporaryT = tiles[fieldPosition]
                    tiles[fieldPosition] = tiles[fieldPosition - 1]
                    tiles[fieldPosition - 1] = temporaryT
                }
            }
        } else {
            if (fieldPosition == 10 && mirrorTilesState[fieldPosition] != Tile.CELL_NONE(0)) {
                if (mirrorTilesState[fieldPosition - 1] == Tile.CELL_NONE(0)) {
                    val temporary = mirrorTilesState[fieldPosition]
                    mirrorTilesState[fieldPosition] = Tile.CELL_NONE(0)
                    mirrorTilesState[fieldPosition - 1] = temporary
                    val temporaryT = tiles[fieldPosition]
                    tiles[fieldPosition] = tiles[fieldPosition - 1]
                    tiles[fieldPosition - 1] = temporaryT
                }

                if (mirrorTilesState[fieldPosition + 4] == Tile.CELL_NONE(0)) {
                    val temporary = mirrorTilesState[fieldPosition]
                    mirrorTilesState[fieldPosition] = Tile.CELL_NONE(0)
                    mirrorTilesState[fieldPosition + 4] = temporary
                    val temporaryT = tiles[fieldPosition]
                    tiles[fieldPosition] = tiles[fieldPosition + 4]
                    tiles[fieldPosition + 4] = temporaryT
                }
                if (mirrorTilesState[fieldPosition - 4] == Tile.CELL_NONE(0)) {
                    val temporary = mirrorTilesState[fieldPosition]
                    mirrorTilesState[fieldPosition] = Tile.CELL_NONE(0)
                    mirrorTilesState[fieldPosition - 4] = temporary
                    val temporaryT = tiles[fieldPosition]
                    tiles[fieldPosition] = tiles[fieldPosition - 4]
                    tiles[fieldPosition - 4] = temporaryT
                }
                if (mirrorTilesState[fieldPosition + 1] == Tile.CELL_NONE(0)) {
                    val temporary = mirrorTilesState[fieldPosition]
                    mirrorTilesState[fieldPosition] = Tile.CELL_NONE(0)
                    mirrorTilesState[fieldPosition + 1] = temporary
                    val temporaryT = tiles[fieldPosition]
                    tiles[fieldPosition] = tiles[fieldPosition + 1]
                    tiles[fieldPosition + 1] = temporaryT
                }
            }

        }
        checkWin()
    }

    fun algorithm8MiddlePosition(fieldPosition: Int) {
        if (fieldPosition == 13 && mirrorTilesState[fieldPosition] != Tile.CELL_NONE(0)) {
            if (mirrorTilesState[fieldPosition + 1] == Tile.CELL_NONE(0)) {
                val temporary = mirrorTilesState[fieldPosition]
                mirrorTilesState[fieldPosition] = Tile.CELL_NONE(0)
                mirrorTilesState[fieldPosition + 1] = temporary
                val temporaryT = tiles[fieldPosition]
                tiles[fieldPosition] = tiles[fieldPosition + 1]
                tiles[fieldPosition + 1] = temporaryT
            }

            if (fieldPosition == 13 && mirrorTilesState[fieldPosition] != Tile.CELL_NONE(0)) {
                if (mirrorTilesState[fieldPosition - 4] == Tile.CELL_NONE(0)) {
                    val temporary = mirrorTilesState[fieldPosition]
                    mirrorTilesState[fieldPosition] = Tile.CELL_NONE(0)
                    mirrorTilesState[fieldPosition - 4] = temporary
                    val temporaryT = tiles[fieldPosition]
                    tiles[fieldPosition] = tiles[fieldPosition - 4]
                    tiles[fieldPosition - 4] = temporaryT
                }
            }
            if (fieldPosition == 13 && mirrorTilesState[fieldPosition] != Tile.CELL_NONE(0)) {
                if (mirrorTilesState[fieldPosition - 1] == Tile.CELL_NONE(0)) {
                    val temporary = mirrorTilesState[fieldPosition]
                    mirrorTilesState[fieldPosition] = Tile.CELL_NONE(0)
                    mirrorTilesState[fieldPosition - 1] = temporary
                    val temporaryT = tiles[fieldPosition]
                    tiles[fieldPosition] = tiles[fieldPosition - 1]
                    tiles[fieldPosition - 1] = temporaryT
                }
            }
        } else {
            if (fieldPosition == 14 && mirrorTilesState[fieldPosition] != Tile.CELL_NONE(0)) {
                if (mirrorTilesState[fieldPosition - 1] == Tile.CELL_NONE(0)) {
                    val temporary = mirrorTilesState[fieldPosition]
                    mirrorTilesState[fieldPosition] = Tile.CELL_NONE(0)
                    mirrorTilesState[fieldPosition - 1] = temporary
                    val temporaryT = tiles[fieldPosition]
                    tiles[fieldPosition] = tiles[fieldPosition - 1]
                    tiles[fieldPosition - 1] = temporaryT
                }

                if (mirrorTilesState[fieldPosition - 4] == Tile.CELL_NONE(0)) {
                    val temporary = mirrorTilesState[fieldPosition]
                    mirrorTilesState[fieldPosition] = Tile.CELL_NONE(0)
                    mirrorTilesState[fieldPosition - 4] = temporary
                    val temporaryT = tiles[fieldPosition]
                    tiles[fieldPosition] = tiles[fieldPosition - 4]
                    tiles[fieldPosition - 4] = temporaryT
                }
                if (mirrorTilesState[fieldPosition + 1] == Tile.CELL_NONE(0)) {
                    val temporary = mirrorTilesState[fieldPosition]
                    mirrorTilesState[fieldPosition] = Tile.CELL_NONE(0)
                    mirrorTilesState[fieldPosition + 1] = temporary
                    val temporaryT = tiles[fieldPosition]
                    tiles[fieldPosition] = tiles[fieldPosition + 1]
                    tiles[fieldPosition + 1] = temporaryT
                }
            }


        }
        checkWin()
//        Toast.makeText(
//            applicationContext,
//            "Выбрана ячейка с номером ${
//                tiles[fieldPosition].shapeNumber.toString()
//            } со статусом ${mirrorTilesState[fieldPosition]}  и позиции в массиве:$fieldPosition",
//            Toast.LENGTH_SHORT
//        ).show()
    }


}
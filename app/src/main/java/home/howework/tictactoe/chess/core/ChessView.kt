package home.howework.tictactoe.chess.core

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Build
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import home.howework.tictactoe.chess.model.ChessBoard
import home.howework.tictactoe.chess.model.PieceColor
import home.howework.tictactoe.chess.model.PieceType
import java.lang.Integer.min

class ChessView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    interface StatusGameListener {
        fun onStatusGame(string: String)
    }
    lateinit var statusGameListener: StatusGameListener
    init {
        statusGameListener=context as StatusGameListener
    }
    var chessBoard = ChessBoard()
    private var promotionDialog: AlertDialog? = null
    private val paint = Paint()
    private var cellSize = 0f
    private var selectedRow: Int = -1
    private var selectedCol: Int = -1
    private var padding = 0
    private val boardColors = intArrayOf(Color.GRAY, Color.DKGRAY)
    @RequiresApi(Build.VERSION_CODES.O)
    private val textPaint = Paint().apply {
        color = Color.argb(100, 200, 11, 27)
        textSize = 60f
        textAlign = Paint.Align.CENTER
        isAntiAlias = true
    }
    // Отрисовка букв
    private val letterPaint = Paint().apply {
        color = Color.YELLOW
        textSize = cellSize * 0.4f
        textAlign = Paint.Align.CENTER
    }

    // Отрисовка цифр
    private val numberPaint = Paint().apply {
        color = Color.YELLOW
        textSize = cellSize * 0.4f
        textAlign = Paint.Align.CENTER
    }

    private val letters = "HGFEDCBA"

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        val minSize = min(w, h)
       // padding = (max(w, h) - minSize) / 2
        cellSize = minSize / 8f

        // Обновляем размер текста для обозначений
        letterPaint.textSize = cellSize* 0.3f
        numberPaint.textSize = cellSize* 0.3f
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        cellSize = width / 8f
        drawChessBoard(canvas)
        drawPieces(canvas)

        // Подсвечиваем выбранную фигуру (если есть)
        if (selectedRow != -1 && selectedCol != -1) {
            drawSelectedCell(canvas)
        }

        // Проверяем шах, мат и пат и рисуем уведомление
        drawGameStatus(canvas)
    }

    private fun drawChessBoard(canvas: Canvas) {
        for (row in 0..7) {
            for (col in 0..7) {
                paint.color =
                    if ((row + col) % 2 == 0) Color.LTGRAY else Color.GRAY
                canvas.drawRect(
                    col * cellSize,
                    row * cellSize,
                    (col + 1) * cellSize,
                    (row + 1) * cellSize,
                    paint
                )
            }
        }
        // Отрисовываем буквенные обозначения
        for (i in 0..7) {
            canvas.drawText(
                letters[i].toString(),
                ((i + 0.5) * cellSize).toFloat(),
                (7.5*cellSize+cellSize).toFloat(),
                letterPaint
            )
        }

        // Отрисовываем цифровые обозначения
        for (i in 0..7) {
            canvas.drawText(
                (1+ i).toString(),
                numberPaint.measureText("8") / 2,
                ((i + 0.5) * cellSize).toFloat(),
                numberPaint
            )
        }
    }

    fun resetGame() {
        chessBoard = ChessBoard() // Пересоздаём доску
        setStatusGame("Удачной игры")
        currentPlayer = PieceColor.WHITE
        selectedRow = -1
        selectedCol = -1
        invalidate() // Перерисовываем
    }

    private fun drawPieces(canvas: Canvas) {
        for (row in 0..7) {
            for (col in 0..7) {
                val piece = chessBoard.getPiece(row, col) ?: continue
                paint.color = if (piece.color == PieceColor.WHITE) Color.BLUE else Color.BLACK
                paint.textSize = cellSize * 0.8f
                paint.textAlign = Paint.Align.CENTER
                canvas.drawText(
                    getPieceSymbol(piece.type),
                    col * cellSize + cellSize / 2,
                    row * cellSize + cellSize * 0.8f,
                    paint
                )
            }
        }
    }

    private fun getPieceSymbol(type: PieceType): String {
        return when (type) {
            PieceType.KING -> "♔"
            PieceType.QUEEN -> "♕"
            PieceType.ROOK -> "♖"
            PieceType.BISHOP -> "♗"
            PieceType.KNIGHT -> "♘"
            PieceType.PAWN -> "♙"
        }
    }

    private fun drawSelectedCell(canvas: Canvas) {
        paint.color = Color.GREEN
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 5f
        if (selectedRow != -1 && selectedCol != -1) {
            canvas.drawRect(
                selectedCol * cellSize,
                selectedRow * cellSize,
                (selectedCol + 1) * cellSize,
                (selectedRow + 1) * cellSize,
                paint
            )
            paint.style = Paint.Style.FILL
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun drawGameStatus(canvas: Canvas) {
        if (chessBoard.isCheckmate(PieceColor.WHITE)) {
            setStatusGame("Мат! Чёрные победили")
            currentPlayer = PieceColor.WHITE
        } else if (chessBoard.isCheckmate(PieceColor.BLACK)) {
            setStatusGame("Мат! Белые победили")
            currentPlayer = PieceColor.WHITE
        } else if (chessBoard.isStalemate(chessBoard.currentPlayer)) {
            setStatusGame("Пат! Ничья")
            currentPlayer = PieceColor.WHITE
        } else if (chessBoard.isKingInCheck(chessBoard.currentPlayer)) {
           setStatusGame("Шах!")
        }
        else    setStatusGame("")
    }
    private fun setStatusGame(statusGame: String) {
    statusGameListener?.onStatusGame(statusGame)
    }
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {

        if (event.action == MotionEvent.ACTION_DOWN) {
            val col = (event.x / cellSize).toInt()
            val row = (event.y / cellSize).toInt()
            // Если выбрана своя фигура (игрок кликает на свою фигуру)
            val piece = chessBoard.getPiece(row, col)
            if (piece != null && piece.color == currentPlayer) {
                selectedRow = row
                selectedCol = col
                invalidate() // Перерисовываем доску
            }
            // Если выбрана пустая клетка или фигура противника — пробуем сделать ход
            else if (selectedRow != -1 && selectedCol != -1) {
                if (chessBoard.movePiece(selectedRow, selectedCol, row, col)) {
                    val piece = chessBoard.getPiece(row, col)


                    if (piece?.type == PieceType.PAWN && (row == 0 || row == 7)) {
                        showPromotionDialog(row, col)
                    }
                    currentPlayer =
                        if (currentPlayer == PieceColor.WHITE) PieceColor.BLACK else PieceColor.WHITE
                    selectedRow = -1
                    selectedCol = -1
                    invalidate()
                }

            }

        }
        return true
    }

    private fun showPromotionDialog(row: Int, col: Int) {
        val pieces = listOf(
            PieceType.QUEEN to "Ферзь",
            PieceType.ROOK to "Ладья",
            PieceType.BISHOP to "Слон",
            PieceType.KNIGHT to "Конь"
        )

        val options = pieces.map { it.second }.toTypedArray()

        promotionDialog = AlertDialog.Builder(context)
            .setTitle("Выберите фигуру")
            .setItems(options) { _, which ->
                val selectedType = pieces[which].first
                chessBoard.promotePawn(row, col, selectedType)
                invalidate()
                promotionDialog = null
            }
            .setOnCancelListener {
                promotionDialog = null
            }
            .show()
    }
}

private var currentPlayer: PieceColor = PieceColor.WHITE // Текущий игрок
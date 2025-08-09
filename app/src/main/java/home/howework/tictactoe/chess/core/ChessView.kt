package home.howework.tictactoe.chess.core

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Build
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.RequiresApi
import home.howework.tictactoe.chess.model.ChessBoard
import home.howework.tictactoe.chess.model.ChessPiece
import home.howework.tictactoe.chess.model.PieceColor
import home.howework.tictactoe.chess.model.PieceType

class ChessView  @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    var chessBoard = ChessBoard()
    private val paint = Paint()
    private var cellSize = 0f
    private var selectedPiece: ChessPiece? = null
    private var selectedRow: Int = -1
    private var selectedCol: Int = -1
    fun resetGame() {
        chessBoard = ChessBoard() // Пересоздаём доску
        currentPlayer = PieceColor.WHITE
        selectedRow = -1
        selectedCol = -1
        invalidate() // Перерисовываем
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private val textPaint = Paint().apply {
        color =Color.argb(100,200,11,27)
        textSize = 60f
        textAlign = Paint.Align.CENTER
        isAntiAlias = true
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
                paint.color = if ((row + col) % 2 == 0) Color.WHITE else Color.argb(100,15,166,110)
                canvas.drawRect(
                    col * cellSize,
                    row * cellSize,
                    (col + 1) * cellSize,
                    (row + 1) * cellSize,
                    paint
                )
            }
        }
    }
    private fun drawPieces(canvas: Canvas) {
        for (row in 0..7) {
            for (col in 0..7) {
                val piece = chessBoard.getPiece(row, col) ?: continue
                paint.color = if (piece.color == PieceColor.WHITE) Color.RED else Color.BLACK
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
        canvas.drawRect(
            selectedCol * cellSize,
            selectedRow * cellSize,
            (selectedCol + 1) * cellSize,
            (selectedRow + 1) * cellSize,
            paint
        )
        paint.style = Paint.Style.FILL
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun drawGameStatus(canvas: Canvas) {
       // val currentPlayer = if (chessBoard.currentPlayer == PieceColor.WHITE) "Белые" else "Чёрные"
        val centerX = width / 2f
        val centerY = height / 2f

        if (chessBoard.isCheckmate(PieceColor.WHITE)) {
            canvas.drawText("Мат! Чёрные победили", centerX, centerY, textPaint)
            currentPlayer=PieceColor.WHITE
        } else if (chessBoard.isCheckmate(PieceColor.BLACK)) {
            canvas.drawText("Мат! Белые победили", centerX, centerY, textPaint)
            currentPlayer=PieceColor.WHITE
        } else if (chessBoard.isStalemate(chessBoard.currentPlayer)) {
            canvas.drawText("Пат! Ничья", centerX, centerY, textPaint)
            currentPlayer=PieceColor.WHITE
        } else if (chessBoard.isKingInCheck(chessBoard.currentPlayer)) {
            canvas.drawText("Шах! Ходят $currentPlayer", centerX, 50f, textPaint)
        }
    }

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
                    currentPlayer =
                        if (currentPlayer == PieceColor.WHITE) PieceColor.BLACK else PieceColor.WHITE
                }
                selectedRow = -1
                selectedCol = -1
                invalidate()
            }
        }
        return true

    }
}
private var currentPlayer: PieceColor = PieceColor.WHITE // Текущий игрок
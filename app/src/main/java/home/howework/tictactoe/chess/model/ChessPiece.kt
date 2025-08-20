package home.howework.tictactoe.chess.model

enum class PieceType {
    PAWN, ROOK, KNIGHT, BISHOP, QUEEN, KING
}

enum class PieceColor {
    WHITE, BLACK;

    // Метод возвращает противоположный цвет
    fun opposite(): PieceColor {
        return when (this) {
            WHITE -> BLACK
            BLACK -> WHITE
        }
    }
}
data class ChessPiece(
    val type: PieceType,
    val color: PieceColor,
    var row: Int,
    var col: Int,
    var hasMoved: Boolean = false,
    var lastMoveWasDouble:Boolean=false
)
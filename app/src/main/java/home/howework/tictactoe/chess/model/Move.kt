package home.howework.tictactoe.chess.model

/**
 * Класс, представляющий шахматный ход
 * @property fromRow начальная строка (0-7)
 * @property fromCol начальный столбец (0-7)
 * @property toRow конечная строка (0-7)
 * @property toCol конечный столбец (0-7)
 * @property promotion тип фигуры при превращении пешки (null если нет превращения)
 * @property isCastling флаг, указывающий является ли ход рокировкой
 * @property isEnPassant флаг, указывающий является ли ход взятием на проходе
 */
data class Move(
    val fromRow: Int,
    val fromCol: Int,
    val toRow: Int,
    val toCol: Int,
    val promotion: PieceType? = null,
    val isCastling: Boolean = false,
    val isEnPassant: Boolean = false
) {
    /**
     * Преобразует ход в шахматную нотацию (алгебраическую нотацию)
     */
    fun toAlgebraicNotation(): String {
        if (isCastling) {
            return if (toCol == 6) "O-O" else "O-O-O" // Короткая или длинная рокировка
        }

        val fileFrom = 'a' + fromCol
        val rankFrom = '1' + fromRow
        val fileTo = 'a' + toCol
        val rankTo = '1' + toRow

        return "$fileFrom$rankFrom-$fileTo$rankTo"
    }

    /**
     * Проверяет является ли ход превращением пешки
     */
    fun isPromotion(): Boolean {
        return promotion != null
    }

    companion object {
        /**
         * Создает ход из строки в формате "e2-e4"
         */
        fun fromString(moveString: String): Move? {
            if (moveString == "O-O") return Move(0, 4, 0, 6, isCastling = true)
            if (moveString == "O-O-O") return Move(0, 4, 0, 2, isCastling = true)

            val parts = moveString.split('-')
            if (parts.size != 2) return null

            val from = parts[0]
            val to = parts[1]
            if (from.length != 2 || to.length != 2) return null

            val fromCol = from[0] - 'a'
            val fromRow = from[1] - '1'
            val toCol = to[0] - 'a'
            val toRow = to[1] - '1'

            if (fromCol !in 0..7 || fromRow !in 0..7 || toCol !in 0..7 || toRow !in 0..7) {
                return null
            }

            return Move(fromRow, fromCol, toRow, toCol)
        }
    }
}
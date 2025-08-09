package home.howework.tictactoe.chess.model

class ChessBoard {
    private val board = Array(8) { arrayOfNulls<ChessPiece>(8) }
    var currentPlayer:PieceColor=PieceColor.WHITE
    init {
        setupPieces()
    }

    private fun setupPieces() {
        // Белые фигуры
        board[0][0] = ChessPiece(PieceType.ROOK, PieceColor.WHITE, 0, 0)
        board[0][1] = ChessPiece(PieceType.KNIGHT, PieceColor.WHITE, 0, 1)
        board[0][2] = ChessPiece(PieceType.BISHOP, PieceColor.WHITE, 0, 2)
        board[0][3] = ChessPiece(PieceType.KING, PieceColor.WHITE, 0, 3)
        board[0][4] = ChessPiece(PieceType.QUEEN, PieceColor.WHITE, 0, 4)
        board[0][5] = ChessPiece(PieceType.BISHOP, PieceColor.WHITE, 0, 5)
        board[0][6] = ChessPiece(PieceType.KNIGHT, PieceColor.WHITE, 0, 6)
        board[0][7] = ChessPiece(PieceType.ROOK, PieceColor.WHITE, 0, 7)
        for (i in 0..7) {
            board[1][i] = ChessPiece(PieceType.PAWN, PieceColor.WHITE, 1, i)
        }

        // Чёрные фигуры (зеркально)
        board[7][0] = ChessPiece(PieceType.ROOK, PieceColor.BLACK, 7, 0)
        board[7][1] = ChessPiece(PieceType.KNIGHT, PieceColor.BLACK, 7, 1)
        board[7][2] = ChessPiece(PieceType.BISHOP, PieceColor.BLACK, 7, 2)
        board[7][3] = ChessPiece(PieceType.KING, PieceColor.BLACK, 7, 3)
        board[7][4] = ChessPiece(PieceType.QUEEN, PieceColor.BLACK, 7, 4)
        board[7][5] = ChessPiece(PieceType.BISHOP, PieceColor.BLACK, 7, 5)
        board[7][6] = ChessPiece(PieceType.KNIGHT, PieceColor.BLACK, 7, 6)
        board[7][7] = ChessPiece(PieceType.ROOK, PieceColor.BLACK, 7, 7)
        for (i in 0..7) {
            board[6][i] = ChessPiece(PieceType.PAWN, PieceColor.BLACK, 6, i)
        }
    }

    fun getPiece(row: Int, col: Int): ChessPiece? {
        return if (row in 0..7 && col in 0..7) board[row][col] else null
    }

    fun movePiece(fromRow: Int, fromCol: Int, toRow: Int, toCol: Int): Boolean {
        if (!isValidMove(fromRow, fromCol, toRow, toCol)) return false

        val piece = getPiece(fromRow, fromCol) ?: return false
        val color = piece.color

        // Временный ход
        val capturedPiece = getPiece(toRow, toCol)
        board[toRow][toCol]  = piece
        board[fromRow][fromCol] = null
        piece.row = toRow
        piece.col = toCol

        // Проверяем, остался ли король под шахом
        if (isKingInCheck(color)) {
            // Отменяем ход
            board[fromRow][fromCol]= piece
            board[toRow][toCol]  = capturedPiece
            piece.row = fromRow
            piece.col = fromCol
            return false
        }
        currentPlayer = if (currentPlayer == PieceColor.WHITE) PieceColor.BLACK else PieceColor.WHITE
        return true
    }

    fun isValidMove(fromRow: Int, fromCol: Int, toRow: Int, toCol: Int): Boolean {
        val piece = getPiece(fromRow, fromCol) ?: return false
        val targetPiece = getPiece(toRow, toCol)

        // Нельзя есть свои фигуры
        if (targetPiece != null && targetPiece.color == piece.color) return false

        return when (piece.type) {
            PieceType.PAWN -> isValidPawnMove(piece, fromRow, fromCol, toRow, toCol, targetPiece)
            PieceType.ROOK -> isValidRookMove(fromRow, fromCol, toRow, toCol)
            PieceType.KNIGHT -> isValidKnightMove(fromRow, fromCol, toRow, toCol)
            PieceType.BISHOP -> isValidBishopMove(fromRow, fromCol, toRow, toCol)
            PieceType.QUEEN -> isValidQueenMove(fromRow, fromCol, toRow, toCol)
            PieceType.KING -> isValidKingMove(fromRow, fromCol, toRow, toCol)
        }
    }
    private fun isValidKingMove(fromRow: Int, fromCol: Int, toRow: Int, toCol: Int): Boolean {
        val rowDiff = Math.abs(toRow - fromRow)
        val colDiff = Math.abs(toCol - fromCol)
        return rowDiff <= 1 && colDiff <= 1
    }
    private fun isValidQueenMove(fromRow: Int, fromCol: Int, toRow: Int, toCol: Int): Boolean {
        return isValidRookMove(fromRow, fromCol, toRow, toCol) ||
                isValidBishopMove(fromRow, fromCol, toRow, toCol)
    }
    private fun isValidBishopMove(fromRow: Int, fromCol: Int, toRow: Int, toCol: Int): Boolean {
        if (Math.abs(toRow - fromRow) != Math.abs(toCol - fromCol)) return false // Не диагональ
        return isPathClear(fromRow, fromCol, toRow, toCol)
    }

    private fun isValidPawnMove(
        pawn: ChessPiece,
        fromRow: Int,
        fromCol: Int,
        toRow: Int,
        toCol: Int,
        targetPiece: ChessPiece?
    ): Boolean {
        val direction = if (pawn.color == PieceColor.WHITE) 1 else -1
        val startRow = if (pawn.color == PieceColor.WHITE) 1 else 6

        // 1. Ход вперёд на 1 клетку
        if (toCol == fromCol && toRow == fromRow + direction && targetPiece == null) {
            return true
        }
        // 2. Первый ход — на 2 клетки
        if (fromRow == startRow && toCol == fromCol && toRow == fromRow + 2 * direction &&
            targetPiece == null && getPiece(fromRow + direction, fromCol) == null
        ) {
            return true
        }
        // 3. Взятие по диагонали
        if (Math.abs(toCol - fromCol) == 1 && toRow == fromRow + direction && targetPiece != null) {
            return true
        }
        return false
    }

    private fun isValidRookMove(fromRow: Int, fromCol: Int, toRow: Int, toCol: Int): Boolean {
        if (fromRow != toRow && fromCol != toCol) return false // Не по прямой
        return isPathClear(fromRow, fromCol, toRow, toCol)
    }

    private fun isValidKnightMove(fromRow: Int, fromCol: Int, toRow: Int, toCol: Int): Boolean {
        val rowDiff = Math.abs(toRow - fromRow)
        val colDiff = Math.abs(toCol - fromCol)
        return (rowDiff == 2 && colDiff == 1) || (rowDiff == 1 && colDiff == 2)
    }

// Аналогично для других фигур (Bishop, Queen, King)...

    private fun isPathClear(fromRow: Int, fromCol: Int, toRow: Int, toCol: Int): Boolean {
        val rowStep = if (toRow > fromRow) 1 else if (toRow < fromRow) -1 else 0
        val colStep = if (toCol > fromCol) 1 else if (toCol < fromCol) -1 else 0

        var row = fromRow + rowStep
        var col = fromCol + colStep

        while (row != toRow || col != toCol) {
            if (getPiece(row, col) != null) return false
            row += rowStep
            col += colStep
        }
        return true
    }
    private fun findKingPosition(color: PieceColor): Pair<Int, Int>? {
        for (row in 0..7) {
            for (col in 0..7) {
                val piece = getPiece(row, col)
                if (piece?.type == PieceType.KING && piece.color == color) {
                    return Pair(row, col)
                }
            }
        }
        return null
    }
    fun isKingInCheck(color: PieceColor): Boolean {
        // Находим позицию короля
        val kingPos = findKingPosition(color) ?: return false

        // Проверяем, может ли любая фигура противника атаковать короля
        for (row in 0..7) {
            for (col in 0..7) {
                val piece = getPiece(row, col)
                if (piece != null && piece.color != color) {
                    if (isValidMove(row, col, kingPos.first, kingPos.second)) {
                        return true
                    }
                }
            }
        }
        return false
    }
    fun isCheckmate(color: PieceColor): Boolean {
        if (!isKingInCheck(color)) return false

        // Проверяем, есть ли хоть один ход, который убирает шах
        for (fromRow in 0..7) {
            for (fromCol in 0..7) {
                val piece = getPiece(fromRow, fromCol) ?: continue
                if (piece.color != color) continue

                // Пробуем все возможные ходы этой фигурой
                for (toRow in 0..7) {
                    for (toCol in 0..7) {
                        if (isValidMove(fromRow, fromCol, toRow, toCol)) {
                            // Делаем временный ход
                            val capturedPiece = getPiece(toRow, toCol)
                            board[toRow][toCol] = piece
                            board[fromRow][fromCol] = null
                            piece.row = toRow
                            piece.col = toCol

                            // Проверяем, остался ли шах
                            val stillInCheck = isKingInCheck(color)

                            // Отменяем ход
                            board[fromRow][fromCol] = piece
                            board[toRow][toCol] = capturedPiece
                            piece.row = fromRow
                            piece.col = fromCol

                            if (!stillInCheck) {
                                return false // Нашёлся ход, убирающий шах
                            }
                        }
                    }
                }
            }
        }
        return true // Нет ходов, убирающих шах — мат!
    }
    fun isStalemate(color: PieceColor): Boolean {
        if (isKingInCheck(color)) return false

        // Проверяем, есть ли хоть один допустимый ход
        for (fromRow in 0..7) {
            for (fromCol in 0..7) {
                val piece = getPiece(fromRow, fromCol) ?: continue
                if (piece.color != color) continue

                for (toRow in 0..7) {
                    for (toCol in 0..7) {
                        if (isValidMove(fromRow, fromCol, toRow, toCol)) {
                            return false // Есть допустимый ход
                        }
                    }
                }
            }
        }
        return true // Нет допустимых ходов — пат
    }
}

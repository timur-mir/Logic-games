package home.howework.tictactoe.chess.model

import java.lang.Math.abs

class ChessBoard {
    private val board = Array(8) { arrayOfNulls<ChessPiece>(8) }
    var currentPlayer:PieceColor=PieceColor.WHITE
    private var lastMovedPawn: ChessPiece? = null
    var selectedPiece:ChessPiece?=null
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

    //fun selectPiece(row: Int, col: Int){
    //        selectedPiece=board[row][col]
    //  if(selectedPiece!=null&& selectedPiece!!.color!=currentPlayer){
    //          selectedPiece=null
    //       }

    fun getPiece(row: Int, col: Int): ChessPiece? {
        return if (row in 0..7 && col in 0..7) board[row][col] else null
    }
    fun movePiece(fromRow: Int, fromCol: Int, toRow: Int, toCol: Int): Boolean {
        val piece = getPiece(fromRow, fromCol) ?: return false
        val color = piece.color
        if (piece.type == PieceType.PAWN) {
            // Проверяем двойной ход
            val rowDiff = Math.abs(piece.row - toRow)
            if (rowDiff == 2) {
                piece.lastMoveWasDouble = true
                lastMovedPawn = piece
            } else {
                piece.lastMoveWasDouble = false
            }
        }
        // Проверка на взятие на проходе
        if (piece.type == PieceType.PAWN) {
        // Проверяем, находится ли пешка на нужной горизонтали для взятия
        val isOnSecondRank = (piece.color == PieceColor.WHITE && toRow == 5) ||
                (piece.color == PieceColor.BLACK && toRow == 2)

        if (Math.abs(toCol - piece.col) == 1 &&
            isOnSecondRank &&
            lastMovedPawn != null &&
            lastMovedPawn!!.type == PieceType.PAWN &&
            lastMovedPawn!!.lastMoveWasDouble &&
            lastMovedPawn!!.col == toCol) {

            // Удаляем пешку, которую взяли на проходе
            board[lastMovedPawn!!.row][lastMovedPawn!!.col] = null

            // Перемещаем нашу пешку на место взятой пешки
            board[piece.row][piece.col] = null
            board[toRow][toCol] = piece
            piece.row = toRow
            piece.col = toCol
            piece.hasMoved = true

            // Сброс флага после выполнения взятия
            lastMovedPawn!!.lastMoveWasDouble = false
            lastMovedPawn = null

            // Смена игрока
            currentPlayer = currentPlayer.opposite()
           // selectedPiece = null
            return true
        }
        }

        if(piece.type==PieceType.KING&&abs(piece.col - toCol) ==2){
            return performCastling(toCol<piece.col)
        }
        if (!isValidMove(fromRow, fromCol, toRow, toCol)) return false
        // Временный ход
        val capturedPiece = getPiece(toRow, toCol)
        board[toRow][toCol]  = piece
        board[fromRow][fromCol] = null
        piece.row = toRow
        piece.col = toCol
        piece.hasMoved=true

        // Проверяем, остался ли король под шахом
        if (isKingInCheck(color)) {
            // Отменяем ход
            board[fromRow][fromCol]= piece
            board[toRow][toCol]  = capturedPiece
            piece.row = fromRow
            piece.col = fromCol
            return false
        }
        currentPlayer = currentPlayer.opposite()
      //  selectedPiece = null
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
    private fun performCastling(isKingside: Boolean): Boolean {
        val row = if (currentPlayer == PieceColor.WHITE) 0 else 7
        val king = board[row][3] ?: return false
        val rookCol = if (isKingside) 0 else 7
        val rook = board[row][rookCol] ?: return false

        // Проверка условий для рокировки
        if (king.hasMoved || rook.hasMoved) return false
        if (isKingInCheck(currentPlayer)) return false

        // Проверка пустоты промежуточных клеток
        val betweenCols = if (isKingside)  1..2 else 4..6
        for (col in betweenCols) {
            if (board[row][col] != null) return false
        }

        // Проверка безопасности клеток для короля
        val kingPassCols = if (isKingside) 1..2 else  4..5
        for (col in kingPassCols) {
            if (isSquareUnderAttack(row, col, currentPlayer.opposite())) {
                return false
            }
        }

        // Выполняем рокировку
        val newKingCol = if (isKingside) 1 else 5
        val newRookCol = if (isKingside) 2 else 4

        // Перемещаем короля
        board[row][3] = null
        board[row][newKingCol] = king
        king.col = newKingCol
        king.hasMoved = true

        // Перемещаем ладью
        board[row][rookCol] = null
        board[row][newRookCol] = rook
        rook.col = newRookCol
        rook.hasMoved = true

        currentPlayer = currentPlayer.opposite()
        return true
    }
    private fun isSquareUnderAttack(row: Int, col: Int, attackerColor: PieceColor): Boolean {
        // Проверяем все клетки на доске
        for (r in 0..7) {
            for (c in 0..7) {
                val piece = board[r][c]

                // Если фигура нужного цвета
                if (piece?.color == attackerColor) {
                    // Проверяем, может ли фигура атаковать заданную клетку
                    when (piece.type) {
                        PieceType.PAWN -> {
                            // Пешка атакует по диагонали
                            if (piece.color == PieceColor.WHITE) {
                                if (r + 1 == row && (c + 1 == col || c - 1 == col)) {
                                    return true
                                }
                            } else {
                                if (r - 1 == row && (c + 1 == col || c - 1 == col)) {
                                    return true
                                }
                            }
                        }
                        PieceType.KNIGHT -> {
                            // Конь атакует буквой Г
                            val rowDiff = Math.abs(r - row)
                            val colDiff = Math.abs(c - col)
                            if ((rowDiff == 2 && colDiff == 1) || (rowDiff == 1 && colDiff == 2)) {
                                return true
                            }
                        }
                        PieceType.BISHOP -> {
                            // Слон атакует по диагонали
                            if (Math.abs(r - row) == Math.abs(c - col)) {
                                if (canMoveAlongDiagonal(piece, row, col)) {
                                    return true
                                }
                            }
                        }
                        PieceType.ROOK -> {
                            // Ладья атакует по прямой
                            if (r == row || c == col) {
                                if (canMoveAlongLine(piece, row, col)) {
                                    return true
                                }
                            }
                        }
                        PieceType.QUEEN -> {
                            // Ферзь атакует как ладья и слон
                            if (r == row || c == col || Math.abs(r - row) == Math.abs(c - col)) {
                                if ((r == row || c == col) && canMoveAlongLine(piece, row, col) ||
                                    Math.abs(r - row) == Math.abs(c - col) && canMoveAlongDiagonal(piece, row, col)) {
                                    return true
                                }
                            }
                        }
                        PieceType.KING -> {
                            // Король атакует на расстоянии 1
                            if (Math.abs(r - row) <= 1 && Math.abs(c - col) <= 1) {
                                return true
                            }
                        }
                    }
                }
            }
        }
        return false
    }

    // Вспомогательные функции для проверки пути
    private fun canMoveAlongLine(piece: ChessPiece, targetRow: Int, targetCol: Int): Boolean {
        val rowStep = if (targetRow > piece.row) 1 else -1
        val colStep = if (targetCol > piece.col) 1 else -1

        var r = piece.row + rowStep
        var c = piece.col + colStep

        while (r != targetRow || c != targetCol) {
            if (board[r][c] != null) {
                return false
            }
            if (r != targetRow) r += rowStep
            if (c != targetCol) c += colStep
            if(r==-1||c==-1){
                return false
            }
        }
        return true
    }
    private fun canMoveAlongDiagonal(piece: ChessPiece, targetRow: Int, targetCol: Int): Boolean {
        // Определяем направление движения по диагонали
        val rowStep = if (targetRow > piece.row) 1 else -1
        val colStep = if (targetCol > piece.col) 1 else -1

        // Начинаем проверку с первой клетки после текущей позиции фигуры
        var r = piece.row + rowStep
        var c = piece.col + colStep

        // Проверяем все клетки до целевой позиции
        while (r != targetRow && c != targetCol) {
            // Если на пути есть фигура, путь заблокирован
            if (board[r][c] != null) {
                return false
            }
            // Переходим к следующей клетке по диагонали
            r += rowStep
            c += colStep
            if(r==-1||c==-1){
               return false
            }
        }

        // Если дошли до целевой клетки без препятствий - путь свободен
        return true
    }

    fun promotePawn(row: Int, col: Int, newType: PieceType) {
        val piece = board[row][col] ?: return
        if (piece.type != PieceType.PAWN) return

        board[row][col] = ChessPiece(
            type = newType,
            color = piece.color,
            row = row,
            col = col,
            hasMoved = true
        )
    }
}

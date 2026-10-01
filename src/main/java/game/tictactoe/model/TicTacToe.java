package game.tictactoe.model;

public final class TicTacToe {

  public enum Mark {
    EMPTY, X, O
  }

  public static final int BOARD_SIZE = 3;

  private final Mark[][] board = new Mark[BOARD_SIZE][BOARD_SIZE];
  private Mark currentPlayer;
  private Mark winner;
  private int moves;
  private boolean gameOver;

  public TicTacToe() {
    reset();
  }

  public void reset() {
    for (int row = 0; row < BOARD_SIZE; row++) {
      for (int column = 0; column < BOARD_SIZE; column++) {
        board[row][column] = Mark.EMPTY;
      }
    }
    currentPlayer = Mark.X;
    winner = Mark.EMPTY;
    moves = 0;
    gameOver = false;
  }

  public boolean makeMove(int row, int column) {
    if (!inBounds(row, column) || gameOver || board[row][column] != Mark.EMPTY) {
      return false;
    }

    board[row][column] = currentPlayer;
    moves++;

    if (hasWon(currentPlayer)) {
      winner = currentPlayer;
      gameOver = true;
    } else if (moves == BOARD_SIZE * BOARD_SIZE) {
      gameOver = true;
    } else {
      switchPlayer();
    }
    return true;
  }

  public void skipTurn() {
    if (!gameOver) {
      switchPlayer();
    }
  }

  public Mark getCell(int row, int column) {
    if (!inBounds(row, column)) {
      throw new IndexOutOfBoundsException("Board coordinates must be between 0 and 2");
    }
    return board[row][column];
  }

  public Mark getCurrentPlayer() {
    return currentPlayer;
  }

  public Mark getWinner() {
    return winner;
  }

  public boolean isGameOver() {
    return gameOver;
  }

  public boolean isDraw() {
    return gameOver && winner == Mark.EMPTY;
  }

  private boolean inBounds(int row, int column) {
    return row >= 0 && row < BOARD_SIZE && column >= 0 && column < BOARD_SIZE;
  }

  private void switchPlayer() {
    currentPlayer = currentPlayer == Mark.X ? Mark.O : Mark.X;
  }

  private boolean hasWon(Mark mark) {
    for (int index = 0; index < BOARD_SIZE; index++) {
      if (board[index][0] == mark && board[index][1] == mark && board[index][2] == mark) {
        return true;
      }
      if (board[0][index] == mark && board[1][index] == mark && board[2][index] == mark) {
        return true;
      }
    }

    return board[0][0] == mark && board[1][1] == mark && board[2][2] == mark
        || board[0][2] == mark && board[1][1] == mark && board[2][0] == mark;
  }
}


package game.tictactoe.model;

import static game.tictactoe.model.TicTacToe.Mark.EMPTY;
import static game.tictactoe.model.TicTacToe.Mark.O;
import static game.tictactoe.model.TicTacToe.Mark.X;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TicTacToeTest {

  private TicTacToe game;

  @BeforeEach
  void setUp() {
    game = new TicTacToe();
  }

  @Test
  void startsWithAnEmptyBoardAndXToMove() {
    assertEquals(X, game.getCurrentPlayer());
    assertEquals(EMPTY, game.getWinner());
    assertFalse(game.isGameOver());

    for (int row = 0; row < TicTacToe.BOARD_SIZE; row++) {
      for (int column = 0; column < TicTacToe.BOARD_SIZE; column++) {
        assertEquals(EMPTY, game.getCell(row, column));
      }
    }
  }

  @Test
  void alternatesPlayersAfterValidMoves() {
    assertTrue(game.makeMove(0, 0));
    assertEquals(O, game.getCurrentPlayer());
    assertTrue(game.makeMove(1, 1));
    assertEquals(X, game.getCurrentPlayer());
  }

  @Test
  void rejectsOccupiedAndOutOfBoundsMoves() {
    assertTrue(game.makeMove(0, 0));
    assertFalse(game.makeMove(0, 0));
    assertFalse(game.makeMove(-1, 0));
    assertFalse(game.makeMove(3, 0));
    assertThrows(IndexOutOfBoundsException.class, () -> game.getCell(0, 3));
  }

  @Test
  void detectsAWinningRow() {
    game.makeMove(0, 0);
    game.makeMove(1, 0);
    game.makeMove(0, 1);
    game.makeMove(1, 1);
    game.makeMove(0, 2);

    assertTrue(game.isGameOver());
    assertEquals(X, game.getWinner());
    assertFalse(game.isDraw());
    assertFalse(game.makeMove(2, 2));
  }

  @Test
  void detectsADiagonalWin() {
    game.makeMove(0, 0);
    game.makeMove(0, 1);
    game.makeMove(1, 1);
    game.makeMove(2, 1);
    game.makeMove(2, 2);

    assertTrue(game.isGameOver());
    assertEquals(X, game.getWinner());
  }

  @Test
  void detectsADraw() {
    int[][] moves = {
        {0, 0}, {0, 1}, {0, 2},
        {1, 1}, {1, 0}, {1, 2},
        {2, 1}, {2, 0}, {2, 2}
    };
    for (int[] move : moves) {
      assertTrue(game.makeMove(move[0], move[1]));
    }

    assertTrue(game.isGameOver());
    assertTrue(game.isDraw());
    assertEquals(EMPTY, game.getWinner());
  }

  @Test
  void skipsTheCurrentPlayersTurn() {
    game.skipTurn();
    assertEquals(O, game.getCurrentPlayer());
    game.skipTurn();
    assertEquals(X, game.getCurrentPlayer());
  }

  @Test
  void resetRestoresInitialState() {
    game.makeMove(0, 0);
    game.makeMove(1, 0);
    game.reset();

    assertEquals(X, game.getCurrentPlayer());
    assertEquals(EMPTY, game.getCell(0, 0));
    assertFalse(game.isGameOver());
  }
}

package game.tictactoe.view;

import game.tictactoe.model.TicTacToe;
import game.tictactoe.model.TicTacToe.Mark;
import javafx.animation.AnimationTimer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public final class GameView extends BorderPane {

  private static final double TURN_SECONDS = 10.0;

  private final TicTacToe game = new TicTacToe();
  private final Button[][] cells = new Button[TicTacToe.BOARD_SIZE][TicTacToe.BOARD_SIZE];
  private final Label statusLabel = new Label();
  private final Label timerLabel = new Label();
  private final ProgressBar timerBar = new ProgressBar(1);
  private final Label resultLabel = new Label();
  private final StackPane boardStack = new StackPane();
  private final VBox resultOverlay = new VBox(22);
  private final AnimationTimer timer;

  private long previousFrame;
  private double timeLeft = TURN_SECONDS;

  public GameView(Runnable onMenu) {
    getStyleClass().add("game-screen");

    GridPane board = createBoard();
    resultOverlay.getStyleClass().add("result-overlay");
    resultOverlay.setAlignment(Pos.CENTER);
    resultOverlay.setVisible(false);
    resultOverlay.setManaged(false);

    resultLabel.getStyleClass().add("result-label");
    Button playAgainButton = createButton("PLAY AGAIN", this::resetGame);
    resultOverlay.getChildren().addAll(resultLabel, playAgainButton);

    boardStack.getChildren().addAll(board, resultOverlay);
    boardStack.setPadding(new Insets(48));
    setCenter(boardStack);

    Label heading = new Label("TIC-TAC-TOE");
    heading.getStyleClass().add("panel-heading");
    statusLabel.getStyleClass().add("status-label");
    timerLabel.getStyleClass().add("timer-label");
    timerBar.setMaxWidth(Double.MAX_VALUE);

    Button newGameButton = createButton("NEW GAME", this::resetGame);
    Button menuButton = createButton("MENU", onMenu);
    Label hint = new Label("R: new game   ESC: menu");
    hint.getStyleClass().add("hint");

    VBox panel = new VBox(18, heading, statusLabel, timerLabel, timerBar,
        spacer(), newGameButton, menuButton, spacer(), hint);
    panel.setAlignment(Pos.TOP_LEFT);
    panel.setPadding(new Insets(54, 38, 40, 38));
    panel.setPrefWidth(300);
    panel.getStyleClass().add("side-panel");
    setRight(panel);

    timer = new AnimationTimer() {
      @Override
      public void handle(long now) {
        updateTimer(now);
      }
    };

    refresh();
  }

  public void startTimer() {
    previousFrame = 0;
    timer.start();
  }

  public void stopTimer() {
    timer.stop();
  }

  public void resetGame() {
    game.reset();
    timeLeft = TURN_SECONDS;
    previousFrame = 0;
    resultOverlay.setVisible(false);
    resultOverlay.setManaged(false);
    refresh();
  }

  private GridPane createBoard() {
    GridPane board = new GridPane();
    board.getStyleClass().add("board");
    board.setAlignment(Pos.CENTER);
    board.setMaxSize(520, 520);

    for (int row = 0; row < TicTacToe.BOARD_SIZE; row++) {
      for (int column = 0; column < TicTacToe.BOARD_SIZE; column++) {
        int targetRow = row;
        int targetColumn = column;
        Button cell = new Button();
        cell.getStyleClass().add("cell");
        cell.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        cell.setPrefSize(170, 170);
        cell.setOnAction(event -> playAt(targetRow, targetColumn));
        cells[row][column] = cell;
        board.add(cell, column, row);
      }
    }
    return board;
  }

  private void playAt(int row, int column) {
    if (game.makeMove(row, column)) {
      timeLeft = TURN_SECONDS;
      previousFrame = 0;
      refresh();
    }
  }

  private void updateTimer(long now) {
    if (game.isGameOver()) {
      return;
    }
    if (previousFrame == 0) {
      previousFrame = now;
      return;
    }

    double elapsed = (now - previousFrame) / 1_000_000_000.0;
    previousFrame = now;
    if (elapsed > 0 && elapsed < 1) {
      timeLeft -= elapsed;
    }
    if (timeLeft <= 0) {
      game.skipTurn();
      timeLeft = TURN_SECONDS;
    }
    updateStatus();
  }

  private void refresh() {
    for (int row = 0; row < TicTacToe.BOARD_SIZE; row++) {
      for (int column = 0; column < TicTacToe.BOARD_SIZE; column++) {
        Button cell = cells[row][column];
        Mark mark = game.getCell(row, column);
        cell.setText(mark == Mark.EMPTY ? "" : mark.name());
        cell.getStyleClass().removeAll("x-cell", "o-cell");
        if (mark == Mark.X) {
          cell.getStyleClass().add("x-cell");
        } else if (mark == Mark.O) {
          cell.getStyleClass().add("o-cell");
        }
        cell.setDisable(game.isGameOver() || mark != Mark.EMPTY);
      }
    }

    if (game.isGameOver()) {
      resultLabel.setText(game.isDraw() ? "IT'S A DRAW" : game.getWinner() + " WINS!");
      resultOverlay.setVisible(true);
      resultOverlay.setManaged(true);
    }
    updateStatus();
  }

  private void updateStatus() {
    statusLabel.setText(game.isGameOver() ? "GAME OVER" : game.getCurrentPlayer() + "'S TURN");
    timerLabel.setText(String.format("%.1f s", Math.max(0, timeLeft)));
    timerBar.setProgress(Math.max(0, timeLeft) / TURN_SECONDS);
  }

  private Button createButton(String text, Runnable action) {
    Button button = new Button(text);
    button.getStyleClass().add("panel-button");
    button.setMaxWidth(Double.MAX_VALUE);
    button.setOnAction(event -> action.run());
    return button;
  }

  private javafx.scene.layout.Region spacer() {
    javafx.scene.layout.Region spacer = new javafx.scene.layout.Region();
    VBox.setVgrow(spacer, javafx.scene.layout.Priority.ALWAYS);
    return spacer;
  }
}


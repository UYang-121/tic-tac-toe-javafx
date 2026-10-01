package game.tictactoe;

import game.tictactoe.view.GameView;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class App extends Application {

  private static final double WINDOW_WIDTH = 960;
  private static final double WINDOW_HEIGHT = 640;

  private Stage stage;
  private GameView gameView;

  @Override
  public void start(Stage primaryStage) {
    stage = primaryStage;
    stage.setTitle("Tic-Tac-Toe");
    stage.setMinWidth(760);
    stage.setMinHeight(520);
    showTitleScreen();
    stage.show();
  }

  private void showTitleScreen() {
    stopGameTimer();

    Label title = new Label("TIC-TAC-TOE");
    title.getStyleClass().add("title");

    Label subtitle = new Label("Two players. Ten seconds each turn.");
    subtitle.getStyleClass().add("subtitle");

    Button startButton = new Button("START GAME");
    startButton.getStyleClass().add("primary-button");
    startButton.setOnAction(event -> showGameScreen());

    VBox content = new VBox(24, title, subtitle, startButton);
    content.setAlignment(Pos.CENTER);
    content.getStyleClass().add("title-screen");

    Scene scene = createScene(content);
    scene.setOnKeyPressed(event -> {
      switch (event.getCode()) {
        case ENTER, SPACE -> showGameScreen();
        case ESCAPE -> stage.close();
        default -> { }
      }
    });
    stage.setScene(scene);
  }

  private void showGameScreen() {
    stopGameTimer();
    gameView = new GameView(this::showTitleScreen);
    Scene scene = createScene(gameView);
    scene.setOnKeyPressed(event -> {
      switch (event.getCode()) {
        case R -> gameView.resetGame();
        case ESCAPE -> showTitleScreen();
        default -> { }
      }
    });
    stage.setScene(scene);
    gameView.startTimer();
  }

  private Scene createScene(javafx.scene.Parent root) {
    Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
    scene.getStylesheets().add(App.class.getResource("/styles.css").toExternalForm());
    return scene;
  }

  private void stopGameTimer() {
    if (gameView != null) {
      gameView.stopTimer();
      gameView = null;
    }
  }

  public static void main(String[] args) {
    launch(args);
  }
}


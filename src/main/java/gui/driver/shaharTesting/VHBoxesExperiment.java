package gui.driver.shaharTesting;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class VHBoxesExperiment {

	static Stage window;
	static Scene scene;

	public static void display(final Stage primaryStage) {

		window = primaryStage;
		window.setTitle("Main Screen");
		window.setOnCloseRequest(λ -> closeProgram());

		final HBox topMenu = new HBox();
		final Button buttonA = new Button("buttonA"), buttonB = new Button("buttonB");
		topMenu.getChildren().addAll(buttonA, buttonB, new Button("buttonC"));

		final VBox leftMenu = new VBox();
		final Button buttonD = new Button("buttonD"), buttonE = new Button("buttonE");
		leftMenu.getChildren().addAll(buttonD, buttonE, new Button("buttonF"));

		final HBox middleMenu = new HBox();
		final Button button = new Button("Close Program");
		button.setOnAction(λ -> {
			λ.consume(); // Hey, I'm gonna take care of this event manually.
			closeProgram();
		});
		middleMenu.getChildren().addAll(button);

		final BorderPane borderPane = new BorderPane();
		borderPane.setTop(topMenu);
		borderPane.setLeft(leftMenu);
		borderPane.setBottom(middleMenu);

		// StackPane layout = new StackPane();
		// borderPane.getChildren().add(button);
		scene = new Scene(borderPane, 300, 150);
		window.setScene(scene);
		window.show();

	}

	private static void closeProgram() {
		if (!ConfirmBox.display("Question", "Are you sure?"))
			System.out.println("Did Not Exit");
		else {
			System.out.println("Saving File...");
			window.close();
		}
	}

	public static Scene getScene() {
		return scene;
	}

}
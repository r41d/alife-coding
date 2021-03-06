package core;

import javafx.application.Application;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

public abstract class Context extends Application {
	// static colors
	public final Color FRONT = (Color) Paint.valueOf("#000000");
	public final Color BACK = (Color) Paint.valueOf("#FFFFFF");
	public final Color BRIGHT = (Color) Paint.valueOf("688D99");
	public final Color DARK = (Color) Paint.valueOf("#34464D");
	public final static Color CELLS_BW[] = { (Color) Paint.valueOf("#FFFFFF"), // white
			(Color) Paint.valueOf("#000000") }; // white
	public final static Color CELLS_FOREST[] = { (Color) Paint.valueOf("#8B7355"), // brown
			(Color) Paint.valueOf("#5AA664"), // green
			(Color) Paint.valueOf("#A6645A") }; // red

	public final static int N = 101;
	public final static int M = 82;

	// properties
	public final IntegerProperty windowWidth = new SimpleIntegerProperty();
	public final IntegerProperty windowHeight = new SimpleIntegerProperty();
	public final IntegerProperty barWidth = new SimpleIntegerProperty();
	public final IntegerProperty barHeight = new SimpleIntegerProperty();
	public final IntegerProperty gridWidth = new SimpleIntegerProperty();
	public final IntegerProperty gridHeight = new SimpleIntegerProperty();
	public final IntegerProperty tileSize = new SimpleIntegerProperty();

	// instances from the master classes
	protected final SceneMaster sceneMaster;


	protected Scene scene;

	// make the stage acessible
	private ReadOnlyStringWrapper title = new ReadOnlyStringWrapper();
	protected Stage stage;

	public Context(String title) {
		this.title.set(title);

		this.sceneMaster = new SceneMaster(this);
	}

	public SceneMaster getSceneMaster() {
		return sceneMaster;
	}

	public String getTitle() {
		return title.get();
	}

	@Override
	public void init() {
	}

	

	private void initStage() {
		// stage settings
		stage.setTitle(title.get());
		stage.setResizable(true);

		// event handling
		stage.setOnCloseRequest(event -> {
			stop();
		});
	}

	public void setScene(Scene scene) {
		this.scene = scene;
		stage.setScene(scene);
	}

	@Override
	public void start(Stage stage) {
		this.stage = stage;
		initStage();
		// MainMenuScreen s = new MainMenuScreen(null);
		// stage.setScene(s.getScene());

		stage.show();
		// animationTimer.start();
	}

	@Override
	public void stop() {
		// animationTimer.stop();
	}

	public ReadOnlyStringProperty titleProperty() {
		return title.getReadOnlyProperty();
	}
}

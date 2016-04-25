package core;

import javafx.animation.AnimationTimer;
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
	public final Color CELLS[] = { (Color) Paint.valueOf("#8B7355"), // brown
			(Color) Paint.valueOf("#5AA664"), // green
			(Color) Paint.valueOf("#A6645A") }; // red

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
	protected AnimationTimer animationTimer;
	protected Ticker ticker;

	private int passedTicks = 0;
	private double lastNanoTime = System.nanoTime();
	private double time = 0;

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
		initAnimationTimer();
	}

	private void initAnimationTimer() {
		final double fps = 60.0;
		animationTimer = new AnimationTimer() {
			@Override
			public void handle(long currentNanoTime) {
				// calculate time since last update.
				time += (currentNanoTime - lastNanoTime) / 1000000000.0;
				lastNanoTime = currentNanoTime;
				passedTicks = (int) Math.floor(time * fps);
				time -= passedTicks / fps;

				if (ticker != null) {
					ticker.tick(passedTicks);
				}
			}
		};
	}

	private void initStage() {
		// stage settings
		stage.setTitle(title.get());
		stage.setResizable(false);

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

package program;

import algorithms.Algo;
import core.Context;
import javafx.animation.AnimationTimer;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class CellularAutomatonTools extends Context {
	private static final String TITLE = "Artificial Life";

	public boolean playing = false;
	public IntegerProperty speed = new SimpleIntegerProperty(3);
	public int blocked;
	
	public AnimationTimer animationTimer;
	private int passedTicks = 0;
	private double lastNanoTime = System.nanoTime();
	private double time = 0;

	public CellularAutomatonTools() {
		super(TITLE);

		// general settings
		windowWidth.set(1200); // 1200
		windowHeight.set(900); // 800
		tileSize.set(10);
		
		initAnimationTimer();
	}

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void init() {
		super.init();

		// add new scene
		// scene = new Scene(new VBox(), windowWidth.get(), windowHeight.get());

		// init screens
		sceneMaster.addScreen("menu", new MenuScene(this));
		sceneMaster.addScreen("game of life", new GridScene(this, Algo.GAME_OF_LIFE));
		sceneMaster.addScreen("forest fire", new GridScene(this, Algo.FOREST_FIRE));
		sceneMaster.addScreen("lantons ant", new GridScene(this, Algo.LANGTONS_ANT));
	}

	@Override
	public void start(Stage stage) {
		super.start(stage);

		sceneMaster.showScreen("menu");
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

				Scene scene = sceneMaster.getScreen();
				if (scene instanceof GridScene) {
					((GridScene) scene).tick(passedTicks >= 1 ? 1 : 0);
				}
			}
		};
	}
}

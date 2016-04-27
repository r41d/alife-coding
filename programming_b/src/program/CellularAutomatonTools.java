package program;

import algorithms.Algo;
import core.Context;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.stage.Stage;

public class CellularAutomatonTools extends Context {
	private static final String TITLE = "Artificial Life";

	public boolean playing = false;
	public IntegerProperty speed = new SimpleIntegerProperty(3);
	public int blocked;

	public CellularAutomatonTools() {
		super(TITLE);

		// general settings
		windowWidth.set(1200); // 1200
		windowHeight.set(900); // 800
		tileSize.set(10);
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

	public void updateGrid(Algo algo) {

	}

	@Override
	public void start(Stage stage) {
		super.start(stage);

		sceneMaster.showScreen("menu");
	}
}

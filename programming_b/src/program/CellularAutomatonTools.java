package program;

import algorithms.Algo;
import algorithms.Grid;
import core.Context;
import core.Ticker;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.stage.Stage;

public class CellularAutomatonTools extends Context {
	private static final String TITLE = "Artificial Life";
	private Grid grid;

	public boolean playing = true;
	public IntegerProperty speed = new SimpleIntegerProperty(3);
	public int blocked;

	public CellularAutomatonTools() {
		super(TITLE);

		// general settings
		windowWidth.set(1200); // 1200
		windowHeight.set(900); // 800
		tileSize.set(10);
	}

	private void setTicker() {
		ticker = new Ticker() {
			@Override
			public void tick(int ticks) {
				blocked -= ticks;
				while (blocked <= 0) {
					blocked -= ticks;
					while (blocked <= 0) {
//						grid.step();
//						grid.render();
						blocked += Math.ceil(Math.pow(10 - speed.get(), 2));
					}
				}
			}
		};
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

		setTicker();
		playing = false;

		sceneMaster.showScreen("menu");
	}
}

package program;

import algorithms.Algo;
import algorithms.ForestFire;
import algorithms.GameOfLife;
import algorithms.Grid;
import algorithms.LangtonsAnt;
import core.Context;
import core.Ticker;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CellularAutomatonTools extends Context {
	private static final String TITLE = "Artificial Life";
	private Grid grid;

	private Scene scene;
	private MenuBar bar = new MenuBar();
	private Canvas canvas = new Canvas();
	private TilePane buttons = new TilePane(20, 20);

	private boolean playing = true;
	private IntegerProperty speed = new SimpleIntegerProperty(3);
	private int blocked;

	public CellularAutomatonTools() {
		super(TITLE);

		// general settings
		windowWidth.set(1200); // 1200
		windowHeight.set(800); // 800
		tileSize.set(15);
	}

	private void setTicker() {
		ticker = new Ticker() {
			@Override
			public void tick(int ticks) {
				blocked -= ticks;
				while (blocked <= 0) {
					blocked -= ticks;
					while (blocked <= 0) {
						grid.step();
						grid.render(canvas);
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
		scene = new Scene(new VBox(bar, canvas, buttons), windowWidth.get(), windowHeight.get());

		setMenu(bar);
		setButtons(buttons);

		// add menu panel
		barWidth.bind(bar.widthProperty());
		barHeight.bind(bar.heightProperty());
		gridWidth.bind(windowWidth);
		gridHeight.bind(windowHeight.subtract(bar.heightProperty()).subtract(buttons.heightProperty()));

		// init screens
		sceneMaster.addScreen("menu", new MenuScene(this));
		sceneMaster.addScreen("grid", scene);
	}

	public void updateGrid(Algo algo) {
		switch (algo) {
		case LANGTONS_ANT:
			grid = new LangtonsAnt(this);
			break;
		case GAME_OF_LIFE:
			grid = new GameOfLife(this);
			break;
		case FOREST_FIRE:
			grid = new ForestFire(this);
			break;
		default:
			break;
		}
	}

	private void setMenu(MenuBar menuBar) {
		// --- Menu Start

		Menu menuGrid = new Menu("Grid");
		MenuItem empty = new MenuItem("Empty grid");
		MenuItem corner = new MenuItem("Objects on grid");
		MenuItem chess = new MenuItem("Chess grid");
		MenuItem rnd = new MenuItem("Random grid");
		MenuItem load = new MenuItem("Load grid from file");
		load.setDisable(true);
		menuGrid.getItems().addAll(empty, corner, chess, rnd, load);

		empty.setOnAction(e -> {
			grid.emptyGrid();
			grid.render(canvas);
		});

		corner.setOnAction(e -> {
			grid.cornerGrid();
			grid.render(canvas);
		});

		chess.setOnAction(e -> {
			grid.chessGrid();
			grid.render(canvas);
		});

		rnd.setOnAction(e -> {
			grid.randomGrid();
			grid.render(canvas);
		});

		// --- Menu Edit

		Menu menuEdit = new Menu("Edit");
		MenuItem invert = new MenuItem("Invert grid");
		MenuItem mirrorVertical = new MenuItem("Mirror vertically");
		MenuItem mirrorHorizontal = new MenuItem("Mirror grid horizontally");
		menuEdit.getItems().addAll(invert, mirrorVertical, mirrorHorizontal);

		invert.setOnAction(e -> {
			grid.invertGrid();
			grid.render(canvas);

		});
		mirrorVertical.setOnAction(e -> {
			grid.mirrorVerticalGrid();
			grid.render(canvas);
		});
		mirrorHorizontal.setOnAction(e -> {
			grid.mirrorHorizontalGrid();
			grid.render(canvas);
		});

		// set the menus to the menu bar
		menuBar.getMenus().addAll(menuGrid, menuEdit);
	}

	public void setButtons(TilePane tilePaneButtons) {
		tilePaneButtons.setPrefRows(1);
		tilePaneButtons.setMaxHeight(200);
		tilePaneButtons.setPrefTileWidth(200);
		tilePaneButtons.setPrefTileHeight(40);
		tilePaneButtons.setAlignment(Pos.CENTER);

		Slider slider = new Slider(0, 9, speed.get());
		slider.valueProperty().bindBidirectional(speed);

		slider.setMajorTickUnit(1);
		slider.setMinorTickCount(0);
		slider.setShowTickMarks(true);
		slider.setSnapToTicks(true);

		Button buttonPlay = new Button("Start");
		Button buttonStep = new Button("Single step");
		// Button buttonSimulation = new Button("Simulate # steps");
		tilePaneButtons.getChildren().addAll(buttonPlay, slider, buttonStep);

		buttonPlay.setOnAction(e -> {
			if (playing) {
				buttonPlay.setText("Start");
				animationTimer.stop();
			} else {
				buttonPlay.setText("Stop");
				animationTimer.start();
				blocked = 0;
			}
			playing = !playing;
		});
		buttonPlay.fire();

		buttonStep.setOnAction(e -> {
			grid.step();
			grid.render(canvas);
			if (playing) {
				buttonPlay.fire();
			}
		});
	}

	@Override
	public void start(Stage stage) {
		super.start(stage);

		scene.getRoot().setVisible(false);
		sceneMaster.showScreen("grid");

		// grid can just be created after knowing the free space
		canvas.setWidth(gridWidth.get());
		canvas.setHeight(gridHeight.get());
		updateGrid(Algo.FOREST_FIRE);

		setTicker();
		playing = false;
		grid.render(canvas);

		sceneMaster.showScreen("menu");
		scene.getRoot().setVisible(true);
	}
}

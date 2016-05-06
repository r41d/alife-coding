package program;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import core.Context;
import algorithms.Algo;
import algorithms.ForestFire;
import algorithms.GameOfLife;
import algorithms.Grid;
import algorithms.LangtonsAnt;
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

public class GridScene extends Scene {
	private CellularAutomatonTools context;
	private Grid grid;
	private MenuBar bar;
	private Canvas canvas;
	private TilePane buttons;
	private PrintWriter writer;

	public GridScene(CellularAutomatonTools context, Algo algo) {
		super(new VBox(), context.windowWidth.get(), context.windowHeight.get());

		this.context = context;
		canvas = new Canvas(Context.N * context.tileSize.get(), Context.M * context.tileSize.get());
		switch (algo) {
		case LANGTONS_ANT:
			grid = new LangtonsAnt(context, canvas);
			break;
		case GAME_OF_LIFE:
			grid = new GameOfLife(context, canvas);
			break;
		case FOREST_FIRE:
			grid = new ForestFire(context, canvas);
			break;
		}
		grid.emptyGrid();
		grid.render();

		bar = new MenuBar();
		setMenu(bar);
		buttons = new TilePane(20, 20);
		setButtons(buttons);

		VBox vboxMenu = (VBox) this.getRoot();
		vboxMenu.setAlignment(Pos.TOP_CENTER);
		vboxMenu.getChildren().addAll(bar, canvas, buttons);

		try {
			writer = new PrintWriter("log.txt", "ASCII");
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public void tick(int ticks) {
		context.blocked -= ticks;
		while (context.blocked <= 0) {
			context.blocked -= ticks;
			while (context.blocked <= 0) {
				grid.step();
				grid.render();
				context.blocked += Math.ceil(Math.pow(10 - context.speed.get(), 2));
				writer.println(grid.lineString());
				writer.flush();
			}
		}
	}

	private void setMenu(MenuBar menuBar) {
		// --- Menu Start

		Menu menuGrid = new Menu("Grid");
		grid.genMenu(menuGrid);

		// --- Menu Edit

		Menu menuEdit = new Menu("Edit");
		MenuItem invert = new MenuItem("Invert grid");
		MenuItem mirrorVertical = new MenuItem("Mirror vertically");
		MenuItem mirrorHorizontal = new MenuItem("Mirror grid horizontally");
		menuEdit.getItems().addAll(invert, mirrorVertical, mirrorHorizontal);

		invert.setOnAction(e -> {
			grid.invertGrid();
			grid.render();

		});
		mirrorVertical.setOnAction(e -> {
			grid.mirrorVerticalGrid();
			grid.render();
		});
		mirrorHorizontal.setOnAction(e -> {
			grid.mirrorHorizontalGrid();
			grid.render();
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

		Slider slider = new Slider(0, 9, context.speed.get());
		slider.valueProperty().bindBidirectional(context.speed);

		slider.setMajorTickUnit(1);
		slider.setMinorTickCount(0);
		slider.setShowTickMarks(true);
		slider.setSnapToTicks(true);

		Button buttonPlay = new Button("Start");
		Button buttonStep = new Button("Single step");
		// Button buttonSimulation = new Button("Simulate # steps");
		tilePaneButtons.getChildren().addAll(buttonPlay, slider, buttonStep);

		buttonPlay.setOnAction(e -> {
			if (context.playing) {
				buttonPlay.setText("Start");
				context.animationTimer.stop();
			} else {
				buttonPlay.setText("Stop");
				context.animationTimer.start();
				context.blocked = 0;
			}
			context.playing = !context.playing;
		});

		buttonStep.setOnAction(e -> {
			grid.step();
			grid.render();
			if (context.playing) {
				buttonPlay.fire();
			}
		});
	}
}

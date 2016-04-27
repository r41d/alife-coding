package program;

import java.util.ArrayList;

import algorithms.Algo;
import core.Context;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;

//import static game.State.*;

public class MenuScene extends Scene {
	protected CellularAutomatonTools context;

	protected ArrayList<String> menuPoints;
	private BorderPane background;
	private BorderPane foreground;

	public MenuScene(CellularAutomatonTools context) {
		super(new StackPane(), context.windowWidth.get(), context.windowHeight.get());

		this.context = context;
		init_scene();
	}

	private void init_scene() {
		background = new BorderPane();
		foreground = new BorderPane();

		((StackPane) getRoot()).getChildren().addAll(background, foreground);
		StackPane.setAlignment(background, Pos.CENTER);
		StackPane.setAlignment(foreground, Pos.CENTER);

		BorderPane foreground = this.getForeground();

		// set foreground

		VBox vboxMenu = new VBox(40);
		vboxMenu.setMaxWidth(400);
		vboxMenu.setPrefWidth(400);
		vboxMenu.setFillWidth(true);
		vboxMenu.setAlignment(Pos.CENTER);
		foreground.setCenter(vboxMenu);

		TilePane tilePaneButtons = new TilePane(20, 20);
		tilePaneButtons.setPrefColumns(1);
		tilePaneButtons.setMaxWidth(200);
		tilePaneButtons.setPrefTileWidth(200);
		tilePaneButtons.setPrefTileHeight(40);
		tilePaneButtons.setAlignment(Pos.CENTER);
		vboxMenu.getChildren().add(tilePaneButtons);

		Button buttonAnt = new Button("Langton's Ant");
		Button buttonGol = new Button("Game of Life");
		Button buttonForest = new Button("Forest Fire");
		Button buttonExit = new Button("Exit");
		tilePaneButtons.getChildren().addAll(buttonAnt, buttonGol, buttonForest, buttonExit);

		buttonAnt.setPrefWidth(Double.MAX_VALUE);
		buttonAnt.setPrefHeight(Double.MAX_VALUE);
		buttonGol.setPrefWidth(Double.MAX_VALUE);
		buttonGol.setPrefHeight(Double.MAX_VALUE);
		buttonForest.setPrefWidth(Double.MAX_VALUE);
		buttonForest.setPrefHeight(Double.MAX_VALUE);
		buttonExit.setPrefWidth(Double.MAX_VALUE);
		buttonExit.setPrefHeight(Double.MAX_VALUE);

		buttonAnt.setOnAction((e) -> {
			// update this
				context.getSceneMaster().showScreen("grid");
				context.updateGrid(Algo.LANGTONS_ANT);
			});
		buttonGol.setOnAction((e) -> {
			context.getSceneMaster().showScreen("grid");
			context.updateGrid(Algo.GAME_OF_LIFE);
		});
		buttonForest.setOnAction((e) -> {
			context.getSceneMaster().showScreen("grid");
			context.updateGrid(Algo.FOREST_FIRE);
		});
		buttonExit.setOnAction((e) -> {
			getContext().stop();
		});
	}

	public BorderPane getBackground() {
		return background;
	}

	public BorderPane getForeground() {
		return foreground;
	}

	public final Context getContext() {
		return context;
	}
}

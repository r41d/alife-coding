package algorithms;

import core.Context;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import core.Context;

public class LangtonsAnt extends Grid {

	protected int x;
	protected int y;
	protected int dir; // 0=north 1=east 2=south 3=west

	public LangtonsAnt(Context context, Canvas canvas) {
		super(context, canvas);
		this.x = n / 2;
		this.y = m / 2;
		this.dir = 0;
	}

	@Override
	public void step() {
		// SCAN
		int curField = getCell(this.x, this.y);
		// TURN
		switch (curField) {
		case 0:
			this.dir = (this.dir + 1) % 4; // turn right
			break;
		case 1:
			this.dir = this.dir - 1; // turn left
			if (this.dir < 0) {
				this.dir = 3;
			}
			break;
		}
		// FLIP
		cells[x][y] = 1 - cells[x][y];
		// MOVE
		switch (this.dir) {
		case 0: // north
			this.y -= 1;
			break;
		case 1: // east
			this.x += 1;
			break;
		case 2: // south
			this.y += 1;
			break;
		case 3: // west
			this.x -= 1;
			break;
		}
	}

	public void render() {
		GraphicsContext gc = canvas.getGraphicsContext2D();
		for (int x = 0; x < this.n; x++) {
			for (int y = 0; y < this.m; y++) {
				// change color
				gc.setFill(Context.CELLS_BW[this.getCell(x, y)]);
				// draw rect
				gc.fillRect(x * this.context.tileSize.get(), y * this.context.tileSize.get(),
						this.context.tileSize.get() - 1, this.context.tileSize.get() - 1);
			}
		}
		// TODO: Draw the Ant
	}

	@Override
	public void genMenu(Menu menu) {
		/*
		 * Make the user choose the initial configuration of the grid from the
		 * following list of 5 possibilities: all white, all black, checker
		 * board, horizontal stripes, random setting and make the user chose the
		 * starting position and orientation for the ant. Write the total number
		 * of cells living for each time step into a file (one ASCII value per
		 * line).
		 */

		MenuItem empty = new MenuItem("White grid");
		MenuItem corner = new MenuItem("Black grid");
		MenuItem chess = new MenuItem("Chess grid");
		MenuItem horizontal = new MenuItem("Horizontal stripes");
		MenuItem rnd = new MenuItem("Random grid");
		// MenuItem load = new MenuItem("Load grid from file");
		// load.setDisable(true);

		menu.getItems().addAll(empty, corner, chess, rnd/* , load */);

		empty.setOnAction(e -> {
			emptyGrid();
			render();
		});

		corner.setOnAction(e -> {
			cornerGrid();
			render();
		});

		chess.setOnAction(e -> {
			chessGrid();
			render();
		});

		horizontal.setOnAction(e -> {
			horizontalLines();
			render();
		});

		rnd.setOnAction(e -> {
			randomGrid();
			render();
		});

	}

}

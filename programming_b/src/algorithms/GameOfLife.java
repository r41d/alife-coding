package algorithms;

import core.Context;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

public class GameOfLife extends Grid {

	// internal variables
	private int[][] next;
	protected int living;

	public GameOfLife(Context context, Canvas canvas) {
		super(context, canvas);

		this.next = new int[n][m];
	}

	/**
	 * central function of functionality execute one iteration of the game of
	 * life rule set
	 */
	public void step() {
		for (int y = 0; y < m; y++) {
			for (int x = 0; x < n; x++) {
				checkNeighborhood(x, y);
			}
		}

		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				cells[i][j] = next[i][j];
			}
		}
		living = countCells(1);
	}

	private void checkNeighborhood(int x, int y) {
		int count = 0;

		for (int a = -1; a <= 1; a++) {
			for (int b = -1; b <= 1; b++) {
				if (a == 0 && b == 0) {
					continue;
				}

				if (getCell(x + a, y + b) == 1) {
					count++;
				}
			}
		}

		if (count == 3) {
			next[x][y] = 1;
		} else if (cells[x][y] == 1 && count == 2) {
			next[x][y] = 1;
		} else {
			next[x][y] = 0;
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
	}

	@Override
	public void genMenu(Menu menu) {
		/*
		 * Make the user choose the initial pattern to start with, from the
		 * following list of 5 possibilities: blinker, glider, r-pentomino,
		 * Gosper’s Glider Gun, and a pattern (class 3 or 4) of your own choice.
		 * Write the total number of cells living for each time step into a file
		 * (one ASCII value per line).
		 */

		MenuItem blinker = new MenuItem("Blinker");
		MenuItem glider = new MenuItem("Glider");
		MenuItem rpentomino = new MenuItem("r-pentomino");
		MenuItem gosper = new MenuItem("Gosper's Glider Gun");
		MenuItem inf1 = new MenuItem("Infinite 1");
		// MenuItem load = new MenuItem("Load grid from file");
		// load.setDisable(true);

		menu.getItems().addAll(blinker, glider, rpentomino, gosper, inf1);

		blinker.setOnAction(e -> {
			blinker();
			living = countCells(1);
			render();
		});

		glider.setOnAction(e -> {
			glider();
			living = countCells(1);
			render();
		});

		rpentomino.setOnAction(e -> {
			rpentomino();
			living = countCells(1);
			render();
		});

		gosper.setOnAction(e -> {
			gosper();
			living = countCells(1);
			render();
		});

		inf1.setOnAction(e -> {
			inf1();
			living = countCells(1);
			render();
		});

	}

	private void blinker() {
		emptyGrid();
		for (int i = -1; i <= 1; i++) {
			cells[n/2][m/2 + i] = 1;
		}
	}

	private void glider() {
		emptyGrid();
		cells[5][5] = 1;
		cells[6][6] = 1;
		for (int i = 4; i <= 6; i++) {
			cells[i][7] = 1;
		}
	}

	private void rpentomino() {
		emptyGrid();
		cells[n/2-1][m/2] = 1;
		cells[n/2+1][m/2-1] = 1;
		for (int i = -1; i <= 1; i++) {
			cells[n/2][m/2+i] = 1;
		}
	}

	private void gosper() {
		emptyGrid();
		// @formatter:off
		String gun[] = {
						"........................O...........",
						"......................O.O...........",
						"............OO......OO............OO",
						"...........O...O....OO............OO",
						"OO........O.....O...OO..............",
						"OO........O...O.OO....O.O...........",
						"..........O.....O.......O...........",
						"...........O...O....................",
						"............OO......................"
						} ;
		// @formatter:on
		pattern2cells(gun, 10, 10);
	}

	private void inf1() {
		emptyGrid();
		// @formatter:off
		String inf1[] = {
						"......O.",
						"....O.OO",
						"....O.O.",
						"....O...",
						"..O.....",
						"O.O....."
						} ;
		// @formatter:on
		pattern2cells(inf1, n/2, m/2);
	}

	private void pattern2cells(String[] gun, int xoffset, int yoffset) {
		for (int line = 0; line < gun.length; line++) {
			for (int cell = 0; cell < gun[line].length(); cell++) {
				cells[xoffset+cell][yoffset+line] = (gun[line].charAt(cell) == 'O') ? 1 : 0;
			}
		}
	}

	@Override
	public String lineString() {
		return ""+living;
	}

}

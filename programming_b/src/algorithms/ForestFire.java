package algorithms;

import core.Context;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Menu;

public class ForestFire extends Grid {
	// internal variables
	private int[][] next;
	private double igniteTree = 0.002; // probability f -- tree to fire
	private double growTree = 0.05; // probability p -- ash to tree
	private double induceTree = 0.05; // probability q

	protected int ashes;
	protected int trees;
	protected int fires;

	public ForestFire(Context context, Canvas canvas) {
		super(context, canvas);

		this.next = new int[n][m];
	}

	@Override
	public void step() {
		for (int y = 0; y < m; y++) {
			for (int x = 0; x < n; x++) {
				updateCell(x, y);
			}
		}

		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				cells[i][j] = next[i][j];
			}
		}
		ashes = countCells(0);
		trees = countCells(1);
		fires = countCells(2);
	}

	private void updateCell(int x, int y) {
		switch (getCell(x, y)) {
		// fill with probability growth
		case 0:
			next[x][y] = rnd.nextDouble() < growTree ? 1 : 0;

			boolean hasTreeNeighbor = false;

			for (int a = -1; a <= 1; a++) {
				for (int b = -1; b <= 1; b++) {
					if (a == 0 && b == 0) {
						continue;
					}

					if (getCell(x + a, y + b) == 2) {
						hasTreeNeighbor = true;
					}
				}
			}

			if (hasTreeNeighbor && rnd.nextDouble() < induceTree) {
				next[x][y] = 1;
			}

			break;

		//
		case 1:
			// check if neighbor is buring
			for (int a = -1; a <= 1; a++) {
				for (int b = -1; b <= 1; b++) {
					if (a == 0 && b == 0) {
						continue;
					}

					if (getCell(x + a, y + b) == 2) {
						next[x][y] = 2;
					}
				}
			}

			if (rnd.nextDouble() < igniteTree) {
				next[x][y] = 2;
			}
			break;

		// buring -> ashes
		case 2:
			next[x][y] = 0;
			break;
		}
	}

	public void render() {
		GraphicsContext gc = canvas.getGraphicsContext2D();

		for (int x = 0; x < this.n; x++) {
			for (int y = 0; y < this.m; y++) {
				// change color
				gc.setFill(Context.CELLS_FOREST[this.getCell(x, y)]);
				// draw rect
				gc.fillRect(x * this.context.tileSize.get(), y * this.context.tileSize.get(),
						this.context.tileSize.get() - 1, this.context.tileSize.get() - 1);
			}
		}
	}

	@Override
	public void genMenu(Menu menu) {
		// don't generate anything here...
	}

	@Override
	public String lineString() {
		return ashes + " " + trees + " " + fires;
	}

}

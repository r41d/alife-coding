package algorithms;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import core.Context;

public class GameOfLife extends Grid {

	// internal variables
	private int[][] next;

	public GameOfLife(Context context) {
		super(context);

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

	public void render(Canvas canvas) {
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

}

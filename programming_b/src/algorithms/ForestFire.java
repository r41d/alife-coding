/**
 * 
 */
package algorithms;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import core.Context;

/**
 * @author danny
 * 
 *
 */
public class ForestFire extends Grid {
	// internal variables
	private int[][] next;
	private double igniteTree = 0.002; // probability f -- tree to fire
	private double growTree = 0.05; // probability p -- ash to tree

	public ForestFire(Context context) {
		super(context);

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
	}

	private void updateCell(int x, int y) {
		switch (getCell(x, y)) {
		// fill with probability growth
		case 0:
			next[x][y] = rnd.nextDouble() < growTree ? 1 : 0;

			// System.out.println(rnd.nextDouble() < growTree ? 1 : 0 + ": " +
			// rnd.nextDouble() + " - " + growTree);
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

	public void render(Canvas canvas) {
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

}

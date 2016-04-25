package algorithms;

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
}

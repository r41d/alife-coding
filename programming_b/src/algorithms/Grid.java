/**
 * 
 */
package algorithms;

import java.util.Arrays;
import java.util.Random;

import javafx.scene.canvas.Canvas;
import javafx.scene.control.Menu;
import core.Context;

public abstract class Grid {

	protected Context context;
	protected Canvas canvas;

	protected static final Random rnd = new Random();

	public int[][] cells;

	// parameter of the array length
	public final int n, m;

	public Grid(Context context, Canvas canvas) {
		this.context = context;
		this.canvas = canvas;

		// n = context.gridWidth.get() / size;
		// m = context.gridHeight.get() / size;

		// task
		n = Context.N;
		m = Context.M;

		// init properties
		this.cells = new int[n][m];
	}

	public void print() {
		for (int y = 0; y < n; y++) {
			System.out.println(Arrays.toString(cells[y]));
		}
		System.out.println();
	}

	public int getCell(int x, int y) {
		if (x < 0)
			x += n;
		
		if (x >= n)
			x -= n;
		
		if (y < 0)
			y += m;
		
		if (y >= m)
			y -= m;

		return cells[x][y];
	}

	// function which clears the grid completely
	public void emptyGrid() {
		for (int x = 0; x < cells.length; x++) {
			for (int y = 0; y < cells[x].length; y++) {
				cells[x][y] = 0;
			}
		}
	}

	// function which fills the grid completely
	public void fullGrid() {
		for (int x = 0; x < cells.length; x++) {
			for (int y = 0; y < cells[x].length; y++) {
				cells[x][y] = 1;
			}
		}
	}

	// public void objectsGrid() {
	// for (int x = 0; x < cells.length; x++) {
	// for (int y = 0; y < cells[x].length; y++) {
	// boolean obj1 = x > 40 && x < 100 && y > 10 && y < 20;
	// boolean obj2 = x + y < 30;
	// boolean obj3 = Math.pow((x - 30), 2) + Math.pow((y - 60), 2) < 100;
	//
	// cells[x][y] = obj1 || obj2 || obj3 ? 1 : 0;
	// }
	// }
	// }

	// function which sets a chess grid in which each cell is different from all
	// L_4 neighbours
	public void chessGrid() {
		for (int x = 0; x < cells.length; x++) {
			for (int y = 0; y < cells[x].length; y++) {
				cells[x][y] = ((x + y) % 2 == 0) ? 1 : 0;
			}
		}
	}

	// function which sets the grid to horizontal lines
	public void horizontalLines() {
		for (int x = 0; x < cells.length; x++) {
			for (int y = 0; y < cells[x].length; y++) {
				cells[x][y] = (y % 2 == 0) ? 1 : 0;
			}
		}
	}

	// function which sets each cell independently random from each other
	// thereby the old cell values are erased
	public void randomGrid() {
		for (int j = 0; j < cells.length; j++) {
			for (int i = 0; i < cells[j].length; i++) {
				cells[j][i] = rnd.nextDouble() > 0.5 ? 1 : 0;
			}
		}
	}

	public void invertGrid() {
		for (int x = 0; x < cells.length; x++) {
			for (int y = 0; y < cells[x].length; y++) {
				cells[x][y] = cells[x][y] == 1 ? 0 : 1;
			}
		}
	}

	public void mirrorVerticalGrid() {
		for (int x = 0; x < n / 2; x++) {
			for (int y = 0; y < m; y++) {
				int temp = cells[n - 1 - x][y];
				cells[n - 1 - x][y] = cells[x][y];
				cells[x][y] = temp;
			}
		}
	}

	public void mirrorHorizontalGrid() {
		for (int x = 0; x < n; x++) {
			for (int y = 0; y < m / 2; y++) {
				int temp = cells[x][m - 1 - y];
				cells[x][m - 1 - y] = cells[x][y];
				cells[x][y] = temp;
			}
		}
	}

	public abstract void step();

	public abstract void render();

	public abstract void genMenu(Menu menu);

	public abstract String lineString();

	protected int countCells(int n) {
		int alive = 0;
		for (int x = 0; x < cells.length; x++)
			for (int y = 0; y < cells[x].length; y++)
				if (cells[x][y] == n)
					alive++;
		return alive;
	}

}

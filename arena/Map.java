package arena;

import configuration.ArenaConstant;

public class Map {

	public Cell[][] grid;

	public Map() {
		grid = new Cell[ArenaConstant.ROWS][ArenaConstant.COLS];
		for (int row = 0; row < grid.length; row++) {
			for (int col = 0; col < grid[row].length; col++) {
				grid[row][col] = new Cell();
			}
		}
	}

	public void importMap() {

	}

	public void generateMapDescriptor() {

	}

}

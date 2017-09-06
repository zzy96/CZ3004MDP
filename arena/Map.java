package arena;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import configuration.ArenaConstant;

public class Map {

	/*
	 * map layout:
	 * 
	 * row . . . 3 2 1 0 * 0 1 2 3 ... column
	 * 
	 */

	public boolean[][] blocked;
	// blocked grid represents the real(explored) map
	public boolean[][] explored;
	// explored grid stores the cell status
	public boolean[][] reachable;
	// grid that the robot center can reach

	public Map() {
		blocked = new boolean[ArenaConstant.ROWS][ArenaConstant.COLS];
		explored = new boolean[ArenaConstant.ROWS][ArenaConstant.COLS];
		reachable = new boolean[ArenaConstant.ROWS][ArenaConstant.COLS];
		for (int row = 0; row < ArenaConstant.ROWS; row++) {
			for (int col = 0; col < ArenaConstant.COLS; col++) {
				blocked[row][col] = false;
				explored[row][col] = false;
				reachable[row][col] = true;
			}
		}
	}

	public void notReachable(int row, int col) {
		reachable[row][col] = false;
		if (row == ArenaConstant.ROWS - 1) {
			if (col == ArenaConstant.COLS - 1) {
				reachable[row][col - 1] = false;
				reachable[row - 1][col] = false;
				reachable[row - 1][col - 1] = false;
			} else if (col == 0) {
				reachable[row][col + 1] = false;
				reachable[row - 1][col] = false;
				reachable[row - 1][col + 1] = false;
			} else {
				reachable[row][col - 1] = false;
				reachable[row][col + 1] = false;
				reachable[row - 1][col] = false;
				reachable[row - 1][col - 1] = false;
				reachable[row - 1][col + 1] = false;
			}
		} else if (row == 0) {
			if (col == ArenaConstant.COLS - 1) {
				reachable[row][col - 1] = false;
				reachable[row + 1][col] = false;
				reachable[row + 1][col - 1] = false;
			} else if (col == 0) {
				reachable[row][col + 1] = false;
				reachable[row + 1][col] = false;
				reachable[row + 1][col + 1] = false;
			} else {
				reachable[row][col - 1] = false;
				reachable[row][col + 1] = false;
				reachable[row + 1][col] = false;
				reachable[row + 1][col - 1] = false;
				reachable[row + 1][col + 1] = false;
			}
		} else {
			if (col == ArenaConstant.COLS - 1) {
				reachable[row][col - 1] = false;
				reachable[row - 1][col] = false;
				reachable[row - 1][col - 1] = false;
				reachable[row + 1][col] = false;
				reachable[row + 1][col - 1] = false;
			} else if (col == 0) {
				reachable[row][col + 1] = false;
				reachable[row - 1][col] = false;
				reachable[row - 1][col + 1] = false;
				reachable[row + 1][col] = false;
				reachable[row + 1][col + 1] = false;
			} else {
				reachable[row][col - 1] = false;
				reachable[row][col + 1] = false;
				reachable[row - 1][col] = false;
				reachable[row - 1][col - 1] = false;
				reachable[row - 1][col + 1] = false;
				reachable[row + 1][col] = false;
				reachable[row + 1][col - 1] = false;
				reachable[row + 1][col + 1] = false;
			}
		}
	}

	// for testing
	public void printRealMap() {
		for (int i = ArenaConstant.ROWS - 1; i >= 0; i--) {
			for (int j = 0; j < ArenaConstant.COLS; j++) {
				if (blocked[i][j]) {
					System.out.print(1);
				} else {
					System.out.print(0);
				}
			}
			System.out.println();
		}
	}

	public boolean loadMap(String filename) {
		try {
			InputStream inputStream = new FileInputStream("maps/" + filename + ".txt");
			BufferedReader buf = new BufferedReader(new InputStreamReader(inputStream));

			String line = buf.readLine();
			StringBuilder sb = new StringBuilder();
			while (line != null) {
				sb.append(line);
				line = buf.readLine();
			}

			String bin = sb.toString();
			int binPtr = 0;
			for (int row = ArenaConstant.ROWS - 1; row >= 0; row--) {
				for (int col = 0; col < ArenaConstant.COLS; col++) {
					explored[row][col] = true;
					if (bin.charAt(binPtr) == '1') {
						blocked[row][col] = true;
						notReachable(row, col);
					}
					binPtr++;
				}
			}

			buf.close();

		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public String[] generateMapDescriptor() {
		String[] ret = new String[2];

		StringBuilder Part1 = new StringBuilder();
		Part1.append("11");

		for (int r = 0; r < ArenaConstant.ROWS; r++) {
			for (int c = 0; c < ArenaConstant.COLS; c++) {
				if (explored[r][c])
					Part1.append("1");
				else
					Part1.append("0");
			}
		}

		Part1.append("11");
		ret[0] = binToHex(Part1.toString());
		System.out.println("Part1: " + ret[0]);

		StringBuilder Part2 = new StringBuilder();
		for (int r = 0; r < ArenaConstant.ROWS; r++) {
			for (int c = 0; c < ArenaConstant.COLS; c++) {
				if (explored[r][c]) {
					if (blocked[r][c])
						Part2.append("1");
					else
						Part2.append("0");
				}
			}
		}
		if (Part2.length() % 4 != 0) {
			Part2.append("0");
		}

		ret[1] = binToHex(Part2.toString());
		System.out.println("Part2: " + ret[1]);

		return ret;
	}

	private String binToHex(String bin) {
		int pointer = 0;
		String ret = "";
		String partial;
		// 1 Hex digits each time to prevent overflow and recognize leading 0000
		while (bin.length() - pointer > 4) {
			partial = bin.substring(pointer, pointer + 4);
			ret = ret.concat(Integer.toHexString(Integer.parseInt(partial, 2)));
			pointer += 4;
		}
		partial = bin.substring(pointer, bin.length());
		ret = ret.concat(Integer.toHexString(Integer.parseInt(partial, 2)));
		return ret;
	}

}

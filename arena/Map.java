package arena;

import java.awt.Color;
import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;

import javax.swing.JPanel;

import configuration.ArenaConstant;
import configuration.RobotConstant;
import configuration.RobotConstant.ACTION;
import configuration.SimulatorConstant;
import simulator.Simulator;
import robot.Robot;

public class Map extends JPanel { ///SQ ADDED EXTENDS JPANEL FOR SIMULATOR PURPOSES

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

	public Map(){
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

	public boolean isBlocked(int row, int col) {
		if (row >= 0 && row < ArenaConstant.ROWS && col >= 0 && col < ArenaConstant.COLS) {
			return blocked[row][col];
		} else {
			return true;
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

	//start and goal zones.
    private boolean inStartZone(int row, int col) {
        return row >= 0 && row <= 2 && col >= 0 && col <= 2;
    }

    private boolean inGoalZone(int row, int col) {
        return (row <= ArenaConstant.GOAL_ROW + 1 && row >= ArenaConstant.GOAL_ROW - 1 && col <= ArenaConstant.GOAL_COL + 1 && col >= ArenaConstant.GOAL_COL - 1);
    }
    
	//Simulation portion.
    public void paintComponent(Graphics g) {
    	LinkedList<ACTION> path = new LinkedList<ACTION>();
    	Simulator s = new Simulator();
    	path = s.fastestPathDisplay();
    
        // Create a two-dimensional array of _DisplayCell objects for rendering.
        _DisplayCell[][] _mapCells = new _DisplayCell[ArenaConstant.ROWS][ArenaConstant.COLS];
        for (int mapRow = 0; mapRow < ArenaConstant.ROWS; mapRow++) {
            for (int mapCol = 0; mapCol < ArenaConstant.COLS; mapCol++) {
                _mapCells[mapRow][mapCol] = new _DisplayCell(mapCol * ArenaConstant.CELL_SIZE, mapRow * ArenaConstant.CELL_SIZE, ArenaConstant.CELL_SIZE);
            }
        }

        // Paint the cells with the appropriate colors.
        for (int mapRow = 0; mapRow < ArenaConstant.ROWS; mapRow++) {
            for (int mapCol = 0; mapCol < ArenaConstant.COLS; mapCol++) {
                Color cellColor;

                //IN START ZONE
                if (inStartZone(mapRow, mapCol))
                    cellColor = SimulatorConstant.C_START;
                else if (inGoalZone(mapRow, mapCol))
                    cellColor = SimulatorConstant.C_GOAL;
                else {
                    if (!explored[mapRow][mapCol])		// NOT EXPLORED
                        cellColor = SimulatorConstant.C_UNEXPLORED;
                    else if (blocked[mapRow][mapCol])	// OBSTACLE
                        cellColor = SimulatorConstant.C_OBSTACLE;
                    else
                        cellColor = SimulatorConstant.C_FREE;
                }

                g.setColor(cellColor);
                g.fillRect(_mapCells[mapRow][mapCol].cellX + SimulatorConstant.MAP_X_OFFSET, _mapCells[mapRow][mapCol].cellY, _mapCells[mapRow][mapCol].cellSize, _mapCells[mapRow][mapCol].cellSize);

            }
        }

        // Paint the robot on-screen.
        g.setColor(SimulatorConstant.C_ROBOT);
        Robot bot = new Robot(false);
        int r = bot.getRow();
        int c = bot.getCol();

        g.fillOval((c - 1) * SimulatorConstant.CELL_SIZE + SimulatorConstant.ROBOT_X_OFFSET + SimulatorConstant.MAP_X_OFFSET, SimulatorConstant.MAP_H - (r * ArenaConstant.CELL_SIZE + SimulatorConstant.ROBOT_Y_OFFSET), SimulatorConstant.ROBOT_W, SimulatorConstant.ROBOT_H);

        // Paint the robot's direction indicator on-screen.
        g.setColor(SimulatorConstant.C_ROBOT_DIR);
        
        RobotConstant.DIRECTION d = bot.getCurDir();
        
			//process the linked list and update map.
	        switch (d) {
            case NORTH:
                g.fillOval(c * ArenaConstant.CELL_SIZE + 10 + SimulatorConstant.MAP_X_OFFSET, SimulatorConstant.MAP_H - r * ArenaConstant.CELL_SIZE - 15, SimulatorConstant.ROBOT_DIR_W, SimulatorConstant.ROBOT_DIR_H);
                break;
            case EAST:
                g.fillOval(c * ArenaConstant.CELL_SIZE + 35 + SimulatorConstant.MAP_X_OFFSET, SimulatorConstant.MAP_H - r * ArenaConstant.CELL_SIZE + 10, SimulatorConstant.ROBOT_DIR_W, SimulatorConstant.ROBOT_DIR_H);
                break;
            case SOUTH:
                g.fillOval(c * ArenaConstant.CELL_SIZE + 10 + SimulatorConstant.MAP_X_OFFSET, SimulatorConstant.MAP_H - r * ArenaConstant.CELL_SIZE + 35, SimulatorConstant.ROBOT_DIR_W, SimulatorConstant.ROBOT_DIR_H);
                break;
            case WEST:
                g.fillOval(c * ArenaConstant.CELL_SIZE - 15 + SimulatorConstant.MAP_X_OFFSET, SimulatorConstant.MAP_H - r * ArenaConstant.CELL_SIZE + 10, SimulatorConstant.ROBOT_DIR_W, SimulatorConstant.ROBOT_DIR_H);
                break;
        }
    }

    private class _DisplayCell {
        public final int cellX;
        public final int cellY;
        public final int cellSize;

        public _DisplayCell(int borderX, int borderY, int borderSize) {
            this.cellX = borderX + SimulatorConstant.CELL_LINE_WEIGHT;
            this.cellY = SimulatorConstant.MAP_H - (borderY - SimulatorConstant.CELL_LINE_WEIGHT);
            this.cellSize = borderSize - (SimulatorConstant.CELL_LINE_WEIGHT * 2);
        }
    }
}

package simulator;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

import arena.Map;
import configuration.ArenaConstant;
import configuration.RobotConstant;
import configuration.RobotConstant.DIRECTION;
import configuration.SimulatorConstant;
import robot.Robot;

public class UI extends JPanel {
	private boolean[][] blocked;
	// blocked grid represents the real(explored) map
	private boolean[][] explored;
	// explored grid stores the cell status
	private int robotRow;
	private int robotCol;
	private DIRECTION robotDirection;

	public void update(Map map, Robot robot) {
		blocked = map.blocked;
		explored = map.explored;
		robotRow = robot.row;
		robotCol = robot.col;
		robotDirection = robot.direction;
	}

	private boolean inStartZone(int row, int col) {
		return row >= 0 && row <= 2 && col >= 0 && col <= 2;
	}

	private boolean inGoalZone(int row, int col) {
		return (row <= RobotConstant.GOAL_ROW + 1 && row >= RobotConstant.GOAL_ROW - 1
				&& col <= RobotConstant.GOAL_COL + 1 && col >= RobotConstant.GOAL_COL - 1);
	}

	// Simulation portion.
	public void paintComponent(Graphics g) {

		// Create a two-dimensional array of _DisplayCell objects for
		// rendering.
		_DisplayCell[][] _mapCells = new _DisplayCell[ArenaConstant.ROWS][ArenaConstant.COLS];
		for (int mapRow = 0; mapRow < ArenaConstant.ROWS; mapRow++) {
			for (int mapCol = 0; mapCol < ArenaConstant.COLS; mapCol++) {
				_mapCells[mapRow][mapCol] = new _DisplayCell(mapCol * SimulatorConstant.CELL_SIZE,
						mapRow * SimulatorConstant.CELL_SIZE, SimulatorConstant.CELL_SIZE);
			}
		}

		// Paint the cells with the appropriate colors.
		for (int mapRow = 0; mapRow < ArenaConstant.ROWS; mapRow++) {
			for (int mapCol = 0; mapCol < ArenaConstant.COLS; mapCol++) {

				Color cellColor;

				// IN START ZONE
				if (inStartZone(mapRow, mapCol))
					cellColor = SimulatorConstant.C_START;
				else if (inGoalZone(mapRow, mapCol))
					cellColor = SimulatorConstant.C_GOAL;
				else {
					if (!explored[mapRow][mapCol]) // NOT EXPLORED
						cellColor = SimulatorConstant.C_UNEXPLORED;
					else if (blocked[mapRow][mapCol]) // OBSTACLE
						cellColor = SimulatorConstant.C_OBSTACLE;
					else if (mapRow == ArenaConstant.WAYPOINT_ROW && mapCol == ArenaConstant.WAYPOINT_COL)
						cellColor = SimulatorConstant.C_WAYPOINT;
					else
						cellColor = SimulatorConstant.C_FREE;
				}

				g.setColor(cellColor);
				g.fillRect(_mapCells[mapRow][mapCol].cellX + SimulatorConstant.MAP_X_OFFSET,
						_mapCells[mapRow][mapCol].cellY, _mapCells[mapRow][mapCol].cellSize,
						_mapCells[mapRow][mapCol].cellSize);

			}
		}

		// get row, col, direction from simulator.

		// Paint the robot on-screen.
		g.setColor(SimulatorConstant.C_ROBOT);

		int r = robotRow;
		int c = robotCol;
		RobotConstant.DIRECTION d = robotDirection;

		// robot movement
		g.fillOval(
				(c - 1) * SimulatorConstant.CELL_SIZE + SimulatorConstant.ROBOT_X_OFFSET
						+ SimulatorConstant.MAP_X_OFFSET,
				SimulatorConstant.MAP_H - (r * SimulatorConstant.CELL_SIZE + SimulatorConstant.ROBOT_Y_OFFSET),
				SimulatorConstant.ROBOT_W, SimulatorConstant.ROBOT_H);

		// Paint the robot's direction indicator on-screen.
		g.setColor(SimulatorConstant.C_ROBOT_DIR);
		// direction indicator of robot, shows with direction robot is going
		// toward
		switch (d) {
		case NORTH:
			g.fillOval(c * SimulatorConstant.CELL_SIZE + 10 + SimulatorConstant.MAP_X_OFFSET,
					SimulatorConstant.MAP_H - r * SimulatorConstant.CELL_SIZE - 15, SimulatorConstant.ROBOT_DIR_W,
					SimulatorConstant.ROBOT_DIR_H);
			break;
		case EAST:
			g.fillOval(c * SimulatorConstant.CELL_SIZE + 35 + SimulatorConstant.MAP_X_OFFSET,
					SimulatorConstant.MAP_H - r * SimulatorConstant.CELL_SIZE + 10, SimulatorConstant.ROBOT_DIR_W,
					SimulatorConstant.ROBOT_DIR_H);
			break;
		case SOUTH:
			g.fillOval(c * SimulatorConstant.CELL_SIZE + 10 + SimulatorConstant.MAP_X_OFFSET,
					SimulatorConstant.MAP_H - r * SimulatorConstant.CELL_SIZE + 35, SimulatorConstant.ROBOT_DIR_W,
					SimulatorConstant.ROBOT_DIR_H);
			break;
		case WEST:
			g.fillOval(c * SimulatorConstant.CELL_SIZE - 15 + SimulatorConstant.MAP_X_OFFSET,
					SimulatorConstant.MAP_H - r * SimulatorConstant.CELL_SIZE + 10, SimulatorConstant.ROBOT_DIR_W,
					SimulatorConstant.ROBOT_DIR_H);
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

	public void printRobotPos() {
		System.out.println(robotRow + ", " + robotCol + ", " + robotDirection);
	}
}

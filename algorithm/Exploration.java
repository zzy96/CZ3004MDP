package algorithm;

import arena.Map;
import configuration.ArenaConstant;
import configuration.RobotConstant;
import configuration.RobotConstant.ACTION;
import robot.Robot;

public class Exploration {

	public static ACTION preAction = null;
	public static int rowToReach = RobotConstant.START_ROW;
	public static int colToReach = RobotConstant.START_COL;

	public static boolean hasMore(Map map) {
		for (int row = 0; row < ArenaConstant.ROWS; row++) {
			for (int col = 0; col < ArenaConstant.COLS; col++) {
				if (!map.explored[row][col]) {
					if (reachNear(map, row, col)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	private static boolean reachNear(Map map, int row, int col) {
		if (row - 1 >= 0 && map.explored[row - 1][col] && map.reachable[row - 1][col]) {
			rowToReach = row - 1;
			colToReach = col;
			return true;
		} else if (row + 1 < ArenaConstant.ROWS && map.explored[row + 1][col] && map.reachable[row + 1][col]) {
			rowToReach = row + 1;
			colToReach = col;
			return true;
		} else if (col + 1 < ArenaConstant.COLS && map.explored[row][col + 1] && map.reachable[row][col + 1]) {
			rowToReach = row;
			colToReach = col + 1;
			return true;
		} else if (col - 1 >= 0 && map.explored[row][col - 1] && map.reachable[row][col - 1]) {
			rowToReach = row;
			colToReach = col - 1;
			return true;
		} else if (row - 2 >= 0 && map.explored[row - 2][col] && map.reachable[row - 2][col]
				&& !map.blocked[row - 1][col]) {
			rowToReach = row - 2;
			colToReach = col;
			return true;
		} else if (row + 2 <= ArenaConstant.ROWS && map.explored[row + 2][col] && map.reachable[row + 2][col]
				&& !map.blocked[row + 1][col]) {
			rowToReach = row + 2;
			colToReach = col;
			return true;
		} else if (col + 2 <= ArenaConstant.COLS && map.explored[row][col + 2] && map.reachable[row][col + 2]
				&& !map.blocked[row][col + 1]) {
			rowToReach = row;
			colToReach = col + 2;
			return true;
		} else if (col - 2 >= 0 && map.explored[row][col - 2] && map.reachable[row][col - 2]
				&& !map.blocked[row][col - 1]) {
			rowToReach = row;
			colToReach = col - 2;
			return true;
		} else {
			return false;
		}
	}

	public static Robot nextMove(Map map, Robot robot) {
		if (preAction != ACTION.RIGHT && canTurnRight(map, robot)) {
			robot.act(ACTION.RIGHT);
			preAction = ACTION.RIGHT;
		} else if (canMoveForward(map, robot)) {
			robot.act(ACTION.FORWARD);
			preAction = ACTION.FORWARD;
		} else if (canTurnRight(map, robot)) {
			robot.act(ACTION.RIGHT);
			preAction = ACTION.RIGHT;
		} else {
			robot.act(ACTION.LEFT);
			preAction = ACTION.LEFT;
		}
		return robot;
	}

	public static boolean canTurnRight(Map map, Robot robot) {
		switch (robot.direction) {
		case NORTH:
			if (robot.col + 1 < ArenaConstant.COLS && map.explored[robot.row - 1][robot.col + 1]
					&& map.explored[robot.row][robot.col + 1] && map.explored[robot.row + 1][robot.col + 1]
					&& map.reachable[robot.row][robot.col + 1]) {
				return true;
			} else {
				return false;
			}
		case EAST:
			if (robot.row - 1 > 0 && map.explored[robot.row - 1][robot.col - 1]
					&& map.explored[robot.row - 1][robot.col] && map.explored[robot.row - 1][robot.col + 1]
					&& map.reachable[robot.row - 1][robot.col]) {
				return true;
			} else {
				return false;
			}
		case SOUTH:
			if (robot.col - 1 > 0 && map.explored[robot.row - 1][robot.col - 1]
					&& map.explored[robot.row][robot.col - 1] && map.explored[robot.row + 1][robot.col - 1]
					&& map.reachable[robot.row][robot.col - 1]) {
				return true;
			} else {
				return false;
			}
		case WEST:
			if (robot.row + 1 < ArenaConstant.ROWS && map.explored[robot.row + 1][robot.col - 1]
					&& map.explored[robot.row + 1][robot.col] && map.explored[robot.row + 1][robot.col + 1]
					&& map.reachable[robot.row + 1][robot.col]) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	public static boolean canMoveForward(Map map, Robot robot) {
		switch (robot.direction) {
		case NORTH:
			if (robot.row + 1 < ArenaConstant.ROWS && map.explored[robot.row + 1][robot.col - 1]
					&& map.explored[robot.row + 1][robot.col] && map.explored[robot.row + 1][robot.col + 1]
					&& map.reachable[robot.row + 1][robot.col]) {
				return true;
			} else {
				return false;
			}
		case EAST:
			if (robot.col + 1 < ArenaConstant.COLS && map.explored[robot.row - 1][robot.col + 1]
					&& map.explored[robot.row][robot.col + 1] && map.explored[robot.row + 1][robot.col + 1]
					&& map.reachable[robot.row][robot.col + 1]) {
				return true;
			} else {
				return false;
			}
		case SOUTH:
			if (robot.row - 1 > 0 && map.explored[robot.row - 1][robot.col - 1]
					&& map.explored[robot.row - 1][robot.col] && map.explored[robot.row - 1][robot.col + 1]
					&& map.reachable[robot.row - 1][robot.col]) {
				return true;
			} else {
				return false;
			}
		case WEST:
			if (robot.col - 1 > 0 && map.explored[robot.row - 1][robot.col - 1]
					&& map.explored[robot.row][robot.col - 1] && map.explored[robot.row + 1][robot.col - 1]
					&& map.reachable[robot.row][robot.col - 1]) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

}

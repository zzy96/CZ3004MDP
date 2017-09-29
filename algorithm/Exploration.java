package algorithm;

import arena.Map;
import configuration.ArenaConstant;
import configuration.RobotConstant.ACTION;
import robot.Robot;

public class Exploration {

	public static ACTION preAction = null;

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

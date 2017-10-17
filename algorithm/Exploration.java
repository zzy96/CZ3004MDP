package algorithm;

import arena.Map;
import configuration.ArenaConstant;
import configuration.RobotConstant;
import configuration.RobotConstant.ACTION;
import robot.Robot;

public class Exploration {

	public static ACTION preAction = null;
	public static int instance = 1;
	public static int rowToReach = RobotConstant.START_ROW;
	public static int colToReach = RobotConstant.START_COL;

	public static int forwardInstance(Map map, Robot robot) {
		int i = 1;
		robot.act(ACTION.FORWARD);
		int left_limit;
		int right_limit;
		boolean flag = false;
		switch (robot.direction) {
		case EAST:
			left_limit = Math.min(robot.row + 5, ArenaConstant.ROWS - 1);
			right_limit = Math.max(robot.row - 3, 0);
			while (true) {
				if (canTurnRight(map, robot)) {
					break;
				}
				for (int k = robot.row + 2; k <= left_limit; k++) {
					// System.out.println("check:(" + k + "," + (robot.col + 1)
					// + ")");
					if (!map.explored[k][robot.col + 1]) {
						flag = true;
						break;
					} else {
						if (map.blocked[k][robot.col + 1]) {
							flag = false;
							break;
						}
					}
				}
				// System.out.println("flag: " + flag);
				if (!flag) {
					for (int k = robot.row - 2; k >= right_limit; k--) {
						if (!map.explored[k][robot.col + 1]) {
							flag = true;
							break;
						} else {
							if (map.blocked[k][robot.col + 1]) {
								flag = false;
								break;
							}
						}
					}
				}
				if (flag) {
					break;
				}
				if (checkReachable(map, robot.row, robot.col + 1)) {
					i++;
					robot.act(ACTION.FORWARD);
				} else {
					break;
				}
			}
			break;
		case SOUTH:
			left_limit = Math.min(robot.col + 5, ArenaConstant.COLS - 1);
			right_limit = Math.max(robot.col - 3, 0);
			while (true) {
				if (canTurnRight(map, robot)) {
					break;
				}
				for (int k = robot.col + 2; k <= left_limit; k++) {
					if (!map.explored[robot.row - 1][k]) {
						flag = true;
						break;
					} else {
						if (map.blocked[robot.row - 1][k]) {
							flag = false;
							break;
						}
					}
				}
				if (!flag) {
					for (int k = robot.col - 2; k >= right_limit; k--) {
						if (!map.explored[robot.row - 1][k]) {
							flag = true;
							break;
						} else {
							if (map.blocked[robot.row - 1][k]) {
								flag = false;
								break;
							}
						}
					}
				}
				if (flag) {
					break;
				}
				if (checkReachable(map, robot.row - 1, robot.col)) {
					i++;
					robot.act(ACTION.FORWARD);
				} else {
					break;
				}
			}
			break;
		case WEST:
			left_limit = Math.max(robot.row - 5, 0);
			right_limit = Math.min(robot.row + 3, ArenaConstant.ROWS - 1);
			while (true) {
				if (canTurnRight(map, robot)) {
					break;
				}
				for (int k = robot.row - 2; k >= left_limit; k--) {
					if (!map.explored[k][robot.col - 1]) {
						flag = true;
						break;
					} else {
						if (map.blocked[k][robot.col - 1]) {
							flag = false;
							break;
						}
					}
				}
				if (!flag) {
					for (int k = robot.row + 2; k <= right_limit; k++) {
						if (!map.explored[k][robot.col - 1]) {
							flag = true;
							break;
						} else {
							if (map.blocked[k][robot.col - 1]) {
								flag = false;
								break;
							}
						}
					}
				}
				if (flag) {
					break;
				}
				if (checkReachable(map, robot.row, robot.col - 1)) {
					i++;
					robot.act(ACTION.FORWARD);
				} else {
					break;
				}
			}
			break;
		case NORTH:
			left_limit = Math.max(robot.col - 5, 0);
			right_limit = Math.min(robot.col + 3, ArenaConstant.COLS - 1);
			while (true) {
				if (canTurnRight(map, robot)) {
					break;
				}
				for (int k = robot.col - 2; k >= left_limit; k--) {
					if (!map.explored[robot.row + 1][k]) {
						flag = true;
						break;
					} else {
						if (map.blocked[robot.row + 1][k]) {
							flag = false;
							break;
						}
					}
				}
				if (!flag) {
					for (int k = robot.col + 2; k <= right_limit; k++) {
						if (!map.explored[robot.row + 1][k]) {
							flag = true;
							break;
						} else {
							if (map.blocked[robot.row + 1][k]) {
								flag = false;
								break;
							}
						}
					}
				}
				if (flag) {
					break;
				}
				if (checkReachable(map, robot.row + 1, robot.col)) {
					i++;
					robot.act(ACTION.FORWARD);
				} else {
					break;
				}
			}
			break;
		}
		return i;
	}

	public static Robot nextMoveOptimized(Map map, Robot robot) {
		if (preAction != ACTION.RIGHT && canTurnRight(map, robot)) {
			System.out.println("instance: " + instance);
			instance = 1;
			robot.act(ACTION.RIGHT);
			preAction = ACTION.RIGHT;
		} else if (canMoveForward(map, robot)) {
			instance = forwardInstance(map, robot);
			System.out.println("instance: " + instance);
			preAction = ACTION.FORWARD;
		} else if (canTurnRight(map, robot)) {
			System.out.println("instance: " + instance);
			instance = 1;
			robot.act(ACTION.RIGHT);
			preAction = ACTION.RIGHT;
		} else {
			System.out.println("instance: " + instance);
			instance = 1;
			robot.act(ACTION.LEFT);
			preAction = ACTION.LEFT;
		}
		System.out.println(preAction);
		return robot;
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
		System.out.println(preAction);
		return robot;
	}

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
		// if (row - 1 >= 0 && map.explored[row - 1][col] && map.reachable[row -
		// 1][col]) {
		// rowToReach = row - 1;
		// colToReach = col;
		// return true;
		// } else if (row + 1 < ArenaConstant.ROWS && map.explored[row + 1][col]
		// && map.reachable[row + 1][col]) {
		// rowToReach = row + 1;
		// colToReach = col;
		// return true;
		// } else if (col + 1 < ArenaConstant.COLS && map.explored[row][col + 1]
		// && map.reachable[row][col + 1]) {
		// rowToReach = row;
		// colToReach = col + 1;
		// return true;
		// } else if (col - 1 >= 0 && map.explored[row][col - 1] &&
		// map.reachable[row][col - 1]) {
		// rowToReach = row;
		// colToReach = col - 1;
		// return true;
		// }else
		if (row - 2 >= 0 && map.explored[row - 2][col] && map.reachable[row - 2][col] && !map.blocked[row - 1][col])

		{
			rowToReach = row - 2;
			colToReach = col;
			return true;
		} else if (row + 2 < ArenaConstant.ROWS && map.explored[row + 2][col] && map.reachable[row + 2][col]
				&& !map.blocked[row + 1][col])

		{
			rowToReach = row + 2;
			colToReach = col;
			return true;
		} else if (col + 2 < ArenaConstant.COLS && map.explored[row][col + 2] && map.reachable[row][col + 2]
				&& !map.blocked[row][col + 1])

		{
			rowToReach = row;
			colToReach = col + 2;
			return true;
		} else if (col - 2 >= 0 && map.explored[row][col - 2] && map.reachable[row][col - 2]
				&& !map.blocked[row][col - 1])

		{
			rowToReach = row;
			colToReach = col - 2;
			return true;
		} else

		{
			return false;
		}

	}

	public static boolean checkReachable(Map map, int row, int col) {
		return map.reachable[row][col];
	}

	public static boolean canTurnRight(Map map, Robot robot) {
		switch (robot.direction) {
		case NORTH:
			if (robot.col + 1 < ArenaConstant.COLS && checkReachable(map, robot.row, robot.col + 1)) {
				return true;
			} else {
				return false;
			}
		case EAST:
			if (robot.row - 1 > 0 && checkReachable(map, robot.row - 1, robot.col)) {
				return true;
			} else {
				return false;
			}
		case SOUTH:
			if (robot.col - 1 > 0 && checkReachable(map, robot.row, robot.col - 1)) {
				return true;
			} else {
				return false;
			}
		case WEST:
			if (robot.row + 1 < ArenaConstant.ROWS && checkReachable(map, robot.row + 1, robot.col)) {
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
			if (robot.row + 1 < ArenaConstant.ROWS && checkReachable(map, robot.row + 1, robot.col)) {
				return true;
			} else {
				return false;
			}
		case EAST:
			if (robot.col + 1 < ArenaConstant.COLS && checkReachable(map, robot.row, robot.col + 1)) {
				return true;
			} else {
				return false;
			}
		case SOUTH:
			if (robot.row - 1 > 0 && checkReachable(map, robot.row - 1, robot.col)) {
				return true;
			} else {
				return false;
			}
		case WEST:
			if (robot.col - 1 > 0 && checkReachable(map, robot.row, robot.col - 1)) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

}

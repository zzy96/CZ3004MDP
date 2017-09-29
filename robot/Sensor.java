package robot;

import arena.Map;
import configuration.ArenaConstant;
import configuration.RobotConstant;

public class Sensor {

	public int lowerRange;
	public int upperRange;
	public String id;

	public Sensor(int range, String id) {
		if (range == 0) {
			lowerRange = RobotConstant.SENSOR_SHORT_RANGE_L;
			upperRange = RobotConstant.SENSOR_SHORT_RANGE_H;
		} else {
			lowerRange = RobotConstant.SENSOR_LONG_RANGE_L;
			upperRange = RobotConstant.SENSOR_LONG_RANGE_H;
		}
		this.id = id;
	}

	private Boolean shortRangeNotBlockedSimulation(Map map, int x_close, int x_far, int y, boolean horizontal) {
		if (horizontal) {
			if (x_far < ArenaConstant.ROWS && x_far >= 0 && y < ArenaConstant.COLS && y >= 0
					&& !map.isBlocked(x_close, y) && !map.isBlocked(x_far, y)) {
				return true;
			} else {
				return false;
			}
		} else {
			if (x_far < ArenaConstant.COLS && x_far >= 0 && y < ArenaConstant.ROWS && y >= 0
					&& !map.isBlocked(y, x_close) && !map.isBlocked(y, x_far)) {
				return true;
			} else {
				return false;
			}
		}
	}

	private Map updateMapSimulation(Map map, int x_close, int x_far, int y, boolean horizontal) {
		if (horizontal) {
			if (x_far < ArenaConstant.ROWS && x_far >= 0 && y < ArenaConstant.COLS && y >= 0) {
				if (map.isBlocked(x_close, y)) {
					map.explored[x_close][y] = true;
					map.blocked[x_close][y] = true;
					map.notReachable(x_close, y);
				} else if (map.isBlocked(x_far, y)) {
					map.explored[x_close][y] = true;
					map.blocked[x_close][y] = false;
					map.explored[x_far][y] = true;
					map.blocked[x_far][y] = true;
					map.notReachable(x_far, y);
				} else {
					map.explored[x_close][y] = true;
					map.blocked[x_close][y] = false;
					map.explored[x_far][y] = true;
					map.blocked[x_far][y] = false;
				}
			} else if (x_close < ArenaConstant.ROWS && x_close >= 0 && y < ArenaConstant.COLS && y >= 0) {
				if (map.isBlocked(x_close, y)) {
					map.explored[x_close][y] = true;
					map.blocked[x_close][y] = true;
					map.notReachable(x_close, y);
				} else {
					map.explored[x_close][y] = true;
					map.blocked[x_close][y] = false;
				}
			}
		} else {
			if (x_far < ArenaConstant.COLS && x_far >= 0 && y < ArenaConstant.ROWS && y >= 0) {
				if (map.isBlocked(y, x_close)) {
					map.explored[y][x_close] = true;
					map.blocked[y][x_close] = true;
					map.notReachable(y, x_close);
				} else if (map.isBlocked(y, x_far)) {
					map.explored[y][x_close] = true;
					map.blocked[y][x_close] = false;
					map.explored[y][x_far] = true;
					map.blocked[y][x_far] = true;
					map.notReachable(y, x_far);
				} else {
					map.explored[y][x_close] = true;
					map.blocked[y][x_close] = false;
					map.explored[y][x_far] = true;
					map.blocked[y][x_far] = false;
				}
			} else if (x_close < ArenaConstant.COLS && x_close >= 0 && y < ArenaConstant.ROWS && y >= 0) {
				if (map.isBlocked(y, x_close)) {
					map.explored[y][x_close] = true;
					map.blocked[y][x_close] = true;
					map.notReachable(y, x_close);
				} else {
					map.explored[y][x_close] = true;
					map.blocked[y][x_close] = false;
				}
			}
		}
		return map;
	}

	public Map senseSimulation(Map map, Robot robot) {
		switch (robot.direction) {
		case NORTH:
			switch (id) {
			case "SFL":
				return updateMapSimulation(map, robot.row + 2, robot.row + 3, robot.col - 1, true);
			case "SFC":
				return updateMapSimulation(map, robot.row + 2, robot.row + 3, robot.col, true);
			case "SFR":
				return updateMapSimulation(map, robot.row + 2, robot.row + 3, robot.col + 1, true);
			case "SR":
				return updateMapSimulation(map, robot.col + 2, robot.col + 3, robot.row + 1, false);
			case "SL":
				return updateMapSimulation(map, robot.col - 2, robot.col - 3, robot.row + 1, false);
			case "LL":
				if (shortRangeNotBlockedSimulation(map, robot.col - 2, robot.col - 3, robot.row + 1, false)) {
					return updateMapSimulation(map, robot.col - 4, robot.col - 5, robot.row + 1, false);
				}
			}
			break;
		case EAST:
			switch (id) {
			case "SFL":
				return updateMapSimulation(map, robot.col + 2, robot.col + 3, robot.row + 1, false);
			case "SFC":
				return updateMapSimulation(map, robot.col + 2, robot.col + 3, robot.row, false);
			case "SFR":
				return updateMapSimulation(map, robot.col + 2, robot.col + 3, robot.row - 1, false);
			case "SR":
				return updateMapSimulation(map, robot.row - 2, robot.row - 3, robot.col + 1, true);
			case "SL":
				return updateMapSimulation(map, robot.row + 2, robot.row + 3, robot.col + 1, true);
			case "LL":
				if (shortRangeNotBlockedSimulation(map, robot.row + 2, robot.row + 3, robot.col + 1, true)) {
					return updateMapSimulation(map, robot.row + 4, robot.row + 5, robot.col + 1, true);
				}
			}
			break;
		case SOUTH:
			switch (id) {
			case "SFL":
				return updateMapSimulation(map, robot.row - 2, robot.row - 3, robot.col + 1, true);
			case "SFC":
				return updateMapSimulation(map, robot.row - 2, robot.row - 3, robot.col, true);
			case "SFR":
				return updateMapSimulation(map, robot.row - 2, robot.row - 3, robot.col - 1, true);
			case "SR":
				return updateMapSimulation(map, robot.col - 2, robot.col - 3, robot.row - 1, false);
			case "SL":
				return updateMapSimulation(map, robot.col + 2, robot.col + 3, robot.row - 1, false);
			case "LL":
				if (shortRangeNotBlockedSimulation(map, robot.col + 2, robot.col + 3, robot.row - 1, false)) {
					return updateMapSimulation(map, robot.col + 4, robot.col + 5, robot.row - 1, false);
				}
			}
			break;
		case WEST:
			switch (id) {
			case "SFL":
				return updateMapSimulation(map, robot.col - 2, robot.col - 3, robot.row - 1, false);
			case "SFC":
				return updateMapSimulation(map, robot.col - 2, robot.col - 3, robot.row, false);
			case "SFR":
				return updateMapSimulation(map, robot.col - 2, robot.col - 3, robot.row + 1, false);
			case "SR":
				return updateMapSimulation(map, robot.row + 2, robot.row + 3, robot.col - 1, true);
			case "SL":
				return updateMapSimulation(map, robot.row - 2, robot.row - 3, robot.col - 1, true);
			case "LL":
				if (shortRangeNotBlockedSimulation(map, robot.row - 2, robot.row - 3, robot.col - 1, true)) {
					return updateMapSimulation(map, robot.row - 4, robot.row - 5, robot.col - 1, true);
				}
			}
			break;
		}
		return map;
	}

	public int senseReal() {
		return 0;
	}

}

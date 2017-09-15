package robot;

import arena.Map;
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

	public int senseSimulation(Map map, Robot robot) {
		switch (robot.direction) {
		case NORTH:
			switch (id) {
			case "SFL":
				if (map.isBlocked(robot.row + 2, robot.col - 1)) {
					return 1;
				}
				if (map.isBlocked(robot.row + 3, robot.col - 1)) {
					return 2;
				}
			case "SFC":
				if (map.isBlocked(robot.row + 2, robot.col)) {
					return 1;
				}
				if (map.isBlocked(robot.row + 3, robot.col)) {
					return 2;
				}
			case "SFR":
				if (map.isBlocked(robot.row + 2, robot.col + 1)) {
					return 1;
				}
				if (map.isBlocked(robot.row + 3, robot.col + 1)) {
					return 2;
				}
			case "SR":
				if (map.isBlocked(robot.row + 1, robot.col + 2)) {
					return 1;
				}
				if (map.isBlocked(robot.row + 1, robot.col + 3)) {
					return 2;
				}
			case "SL":
				if (map.isBlocked(robot.row + 1, robot.col - 2)) {
					return 1;
				}
				if (map.isBlocked(robot.row + 1, robot.col - 3)) {
					return 2;
				}
			case "LL":
				if (map.isBlocked(robot.row, robot.col - 4)) {
					return 3;
				}
				if (map.isBlocked(robot.row, robot.col - 5)) {
					return 4;
				}
			}
			break;
		case EAST:
			switch (id) {
			case "SFL":
				if (map.isBlocked(robot.row + 1, robot.col + 2)) {
					return 1;
				}
				if (map.isBlocked(robot.row + 1, robot.col + 3)) {
					return 2;
				}
			case "SFC":
				if (map.isBlocked(robot.row, robot.col + 2)) {
					return 1;
				}
				if (map.isBlocked(robot.row, robot.col + 3)) {
					return 2;
				}
			case "SFR":
				if (map.isBlocked(robot.row - 1, robot.col + 2)) {
					return 1;
				}
				if (map.isBlocked(robot.row - 1, robot.col + 3)) {
					return 2;
				}
			case "SR":
				if (map.isBlocked(robot.row - 2, robot.col + 1)) {
					return 1;
				}
				if (map.isBlocked(robot.row - 3, robot.col + 1)) {
					return 2;
				}
			case "SL":
				if (map.isBlocked(robot.row + 2, robot.col + 1)) {
					return 1;
				}
				if (map.isBlocked(robot.row + 3, robot.col + 1)) {
					return 2;
				}
			case "LL":
				if (map.isBlocked(robot.row + 4, robot.col)) {
					return 3;
				}
				if (map.isBlocked(robot.row + 5, robot.col)) {
					return 4;
				}
			}
			break;
		case SOUTH:
			switch (id) {
			case "SFL":
				if (map.isBlocked(robot.row - 2, robot.col + 1)) {
					return 1;
				}
				if (map.isBlocked(robot.row - 3, robot.col + 1)) {
					return 2;
				}
			case "SFC":
				if (map.isBlocked(robot.row - 2, robot.col)) {
					return 1;
				}
				if (map.isBlocked(robot.row - 3, robot.col)) {
					return 2;
				}
			case "SFR":
				if (map.isBlocked(robot.row - 2, robot.col - 1)) {
					return 1;
				}
				if (map.isBlocked(robot.row - 3, robot.col - 1)) {
					return 2;
				}
			case "SR":
				if (map.isBlocked(robot.row - 1, robot.col - 2)) {
					return 1;
				}
				if (map.isBlocked(robot.row - 1, robot.col - 3)) {
					return 2;
				}
			case "SL":
				if (map.isBlocked(robot.row - 1, robot.col + 2)) {
					return 1;
				}
				if (map.isBlocked(robot.row - 1, robot.col + 3)) {
					return 2;
				}
			case "LL":
				if (map.isBlocked(robot.row, robot.col + 4)) {
					return 3;
				}
				if (map.isBlocked(robot.row, robot.col + 5)) {
					return 4;
				}
			}
			break;
		case WEST:
			switch (id) {
			case "SFL":
				if (map.isBlocked(robot.row - 1, robot.col - 2)) {
					return 1;
				}
				if (map.isBlocked(robot.row - 1, robot.col - 3)) {
					return 2;
				}
			case "SFC":
				if (map.isBlocked(robot.row, robot.col - 2)) {
					return 1;
				}
				if (map.isBlocked(robot.row, robot.col - 3)) {
					return 2;
				}
			case "SFR":
				if (map.isBlocked(robot.row + 1, robot.col - 2)) {
					return 1;
				}
				if (map.isBlocked(robot.row + 1, robot.col - 3)) {
					return 2;
				}
			case "SR":
				if (map.isBlocked(robot.row + 2, robot.col - 1)) {
					return 1;
				}
				if (map.isBlocked(robot.row + 3, robot.col - 1)) {
					return 2;
				}
			case "SL":
				if (map.isBlocked(robot.row - 2, robot.col - 1)) {
					return 1;
				}
				if (map.isBlocked(robot.row - 3, robot.col - 1)) {
					return 2;
				}
			case "LL":
				if (map.isBlocked(robot.row - 4, robot.col)) {
					return 3;
				}
				if (map.isBlocked(robot.row - 5, robot.col)) {
					return 4;
				}
			}
			break;
		}
		return 0;
	}

	public int senseReal() {
		return 0;
	}

}

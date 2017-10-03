package configuration;

public class RobotConstant {
	public static final int GOAL_ROW = 18;
	// row no. of goal cell
	public static final int GOAL_COL = 13;
	// col no. of goal cell
	public static final int START_ROW = 1;
	// row no. of start cell
	public static final int START_COL = 1;
	// col no. of start cell
	public static final int MOVE_COST = 10;
	// cost of FORWARD, BACKWARD movement
	public static final int TURN_COST = 20;
	// cost of RIGHT, LEFT movement
	public static final int SPEED = 100;
	public static final int TIMELIMIT = 1000000;
	public static final int COVERAGE = 100;
	// delay between movements (ms)
	public static final DIRECTION START_DIR = DIRECTION.EAST;
	// start direction
	public static final int SENSOR_SHORT_RANGE_L = 1;
	// range of short range sensor (cells)
	public static final int SENSOR_SHORT_RANGE_H = 2;
	// range of short range sensor (cells)
	public static final int SENSOR_LONG_RANGE_L = 3;
	// range of long range sensor (cells)
	public static final int SENSOR_LONG_RANGE_H = 4;
	// range of long range sensor (cells)

	public enum DIRECTION {
		NORTH, EAST, SOUTH, WEST;

		public static DIRECTION right(DIRECTION d) {
			switch (d) {
			case NORTH:
				return DIRECTION.EAST;
			case EAST:
				return DIRECTION.SOUTH;
			case SOUTH:
				return DIRECTION.WEST;
			case WEST:
				return DIRECTION.NORTH;
			}
			return null;
		}

		public static DIRECTION left(DIRECTION d) {
			switch (d) {
			case NORTH:
				return DIRECTION.WEST;
			case EAST:
				return DIRECTION.NORTH;
			case SOUTH:
				return DIRECTION.EAST;
			case WEST:
				return DIRECTION.SOUTH;
			}
			return null;
		}

		public static char encoding(DIRECTION d) {
			switch (d) {
			case NORTH:
				return 'N';
			case EAST:
				return 'E';
			case SOUTH:
				return 'S';
			case WEST:
				return 'W';
			default:
				return 'X';
			}
		}
	}

	public enum ACTION {
		FORWARD, RIGHT, LEFT, TURN;

		public static char encoding(ACTION a) {
			switch (a) {
			case FORWARD:
				return 'F';
			case RIGHT:
				return 'R';
			case LEFT:
				return 'L';
			case TURN:
				return 'T';
			default:
				return 'X';
			}
		}
	}
}

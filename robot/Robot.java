package robot;

import arena.Map;
import configuration.ArenaConstant;
import configuration.RobotConstant;
import configuration.RobotConstant.ACTION;
import configuration.RobotConstant.DIRECTION;

public class Robot {

	public boolean real;
	public int row;
	public int col;
	public int waypointRow;
	public int waypointCol;
	public DIRECTION direction;
	public int speed;
	public Sensor SFrontLeft; // front-left SR
	public Sensor SFrontCenter; // front-center SR
	public Sensor SFrontRight; // front-right SR
	public Sensor SLeft; // left SR
	public Sensor SRight; // right SR
	public Sensor LLeft; // left LR

	public Robot(boolean real) {
		this.real = real;
		row = RobotConstant.START_ROW;
		col = RobotConstant.START_COL;
		direction = RobotConstant.START_DIR;
		SFrontLeft = new Sensor(0, "SFL");
		SFrontCenter = new Sensor(0, "SFC");
		SFrontRight = new Sensor(0, "SFR");
		SLeft = new Sensor(0, "SL");
		SRight = new Sensor(0, "SR");
		LLeft = new Sensor(1, "LL");

	}

	public void act(ACTION a) {
		switch (a) {
		case FORWARD:
			switch (direction) {
			case NORTH:
				row++;
				break;
			case EAST:
				col++;
				break;
			case SOUTH:
				row--;
				break;
			case WEST:
				col--;
				break;
			}
			break;
		case BACKWARD:
			switch (direction) {
			case NORTH:
				row--;
				break;
			case EAST:
				col--;
				break;
			case SOUTH:
				row++;
				break;
			case WEST:
				col++;
				break;
			}
			break;
		case RIGHT:
			direction = DIRECTION.right(direction);
			break;
		case LEFT:
			direction = DIRECTION.left(direction);
			break;
		default:
			System.out.println("ACTION error!");
			break;
		}

	}

	public Map updateMap(Map map) {
		for (int i = row - 1; i <= row + 1; i++) {
			for (int j = col - 1; j <= col + 1; j++) {
				map.explored[i][j] = true;
				map.blocked[i][j] = false;
			}
		}
		if (real) {
			// real sensor update map
		} else {
			// SFL
			map = SFrontLeft.senseSimulation(map, this);
			// SFC
			map = SFrontCenter.senseSimulation(map, this);
			// SFR
			map = SFrontRight.senseSimulation(map, this);
			// SR
			map = SRight.senseSimulation(map, this);
			// SL
			map = SLeft.senseSimulation(map, this);
			// LL
			map = LLeft.senseSimulation(map, this);
		}
		map = updateReachable(map);
		return map;
	}

	private Map updateReachable(Map map) {
		for (int row = 1; row < ArenaConstant.ROWS - 1; row++) {
			for (int col = 1; col < ArenaConstant.COLS - 1; col++) {
				if (map.explored[row - 1][col - 1] && map.explored[row - 1][col] && map.explored[row - 1][col + 1]
						&& map.explored[row][col - 1] && map.explored[row][col] && map.explored[row][col + 1]
						&& map.explored[row + 1][col - 1] && map.explored[row + 1][col]
						&& map.explored[row + 1][col + 1] && !map.blocked[row - 1][col - 1]
						&& !map.blocked[row - 1][col] && !map.blocked[row - 1][col + 1] && !map.blocked[row][col - 1]
						&& !map.blocked[row][col] && !map.blocked[row][col + 1] && !map.blocked[row + 1][col - 1]
						&& !map.blocked[row + 1][col] && !map.blocked[row + 1][col + 1]) {
					map.reachable[row][col] = true;
				}
			}
		}
		return map;
	}
}

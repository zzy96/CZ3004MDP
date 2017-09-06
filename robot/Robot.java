package robot;

import configuration.RobotConstant;
import configuration.RobotConstant.ACTION;
import configuration.RobotConstant.DIRECTION;

public class Robot {

	private int row;
	private int col;
	private DIRECTION direction;
	private int speed;

	public Robot() {
		row = RobotConstant.START_ROW;
		col = RobotConstant.START_COL;
		direction = RobotConstant.START_DIR;
		speed = RobotConstant.SPEED;
	}

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}

	public int getSpeed() {
		return speed;
	}

	public DIRECTION getDirection() {
		return direction;
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
		case LEFT:
			direction = DIRECTION.left(direction);
		default:
			System.out.println("ACTION error!");
			break;
		}

	}
}

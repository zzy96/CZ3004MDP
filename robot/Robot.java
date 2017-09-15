package robot;

import configuration.RobotConstant;
import configuration.RobotConstant.ACTION;
import configuration.RobotConstant.DIRECTION;

public class Robot {

	public boolean real;
	public int row;
	public int col;
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
		speed = RobotConstant.SPEED;
		SFrontLeft = new Sensor(0, "SFL");
		SFrontCenter = new Sensor(0, "SFC");
		SFrontRight = new Sensor(0, "SFR");
		SLeft = new Sensor(0, "SL");
		SRight = new Sensor(0, "SR");
		LLeft = new Sensor(1, "LL");

	}

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public DIRECTION getCurDir() {
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

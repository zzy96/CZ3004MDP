package algorithm;

import arena.Map;
import robot.Robot;

public class Exploration {

	public Map map;
	public Robot robot;
	public int coverageLimit;
	public int timeLimit;

	public Exploration(Map map, Robot robot, int coverageLimit, int timeLimit) {
		this.map = map;
		this.robot = robot;
		this.coverageLimit = coverageLimit;
		this.timeLimit = timeLimit;
	}
}

package algorithm;

import java.util.LinkedList;

import arena.Map;
import configuration.ArenaConstant;
import configuration.RobotConstant;
import configuration.RobotConstant.ACTION;
import configuration.RobotConstant.DIRECTION;

public class FastestPath {

	private int waypointRow;
	private int waypointCol;
	private Map map;
	private LinkedList<ACTION> actions;
	private DIRECTION direction;

	public FastestPath(Map map, int waypointRow, int waypointCol, DIRECTION direction) {
		this.map = map;
		// assume waypoint is reachable
		this.waypointRow = waypointRow;
		this.waypointCol = waypointCol;
		this.direction = direction;
	}

	public void run() {
		actions = new LinkedList<ACTION>();
		actions.addAll(BFS(direction, RobotConstant.START_ROW, RobotConstant.START_COL, waypointRow, waypointCol));
		actions.addAll(BFS(direction, waypointRow, waypointCol, RobotConstant.GOAL_ROW, RobotConstant.GOAL_COL));
	}

	public LinkedList<ACTION> BFS(DIRECTION startDirection, int startRow, int startCol, int goalRow, int goalCol) {

		// define a State class in the method
		class State {
			public DIRECTION d;
			public int row;
			public int col;
			public int cost;
			public LinkedList<ACTION> past_actions;

			public State(DIRECTION d, int row, int col, int cost) {
				this.d = d;
				this.row = row;
				this.col = col;
				this.cost = cost;
				past_actions = new LinkedList<ACTION>();
			}
		}

		boolean[][] queued = new boolean[ArenaConstant.ROWS][ArenaConstant.COLS];
		// initialize queued to empty
		for (int i = 0; i < ArenaConstant.ROWS; i++) {
			for (int j = 0; j < ArenaConstant.COLS; j++) {
				queued[i][j] = false;
			}
		}

		// use a linked list to store states for Breadth First Search
		LinkedList<State> states = new LinkedList<State>();
		State init = new State(startDirection, startRow, startCol, 0);
		states.add(init);
		queued[startRow][startCol] = true;

		while (states.size() > 0) {

			// for debugging, print queue
			// System.out.println("print states queue:");
			// for (int i = 0; i < states.size(); i++) {
			// System.out.print("direction: " + states.get(i).d);
			// System.out.print(" (" + states.get(i).row + "," +
			// states.get(i).col + ")");
			// System.out.print(" cost: " + states.get(i).cost);
			// printPath(states.get(i).past_actions);
			// }
			// System.out.println();

			int min_cost_index = 0;
			for (int i = 1; i < states.size(); i++) {
				if (states.get(i).cost < states.get(min_cost_index).cost) {
					min_cost_index = i;
				}
			}

			State state = states.remove(min_cost_index);
			// check if it is the goal state
			if (state.row == goalRow && state.col == goalCol) {
				direction = state.d;
				return state.past_actions;
			}

			// from any direction to north

			if (state.row + 1 < ArenaConstant.ROWS && map.reachable[state.row + 1][state.col]
					&& !queued[state.row + 1][state.col]) {

				State new_state = new State(state.d, state.row, state.col, state.cost);
				new_state.past_actions.addAll(state.past_actions);
				new_state.row = state.row + 1;
				new_state.col = state.col;
				switch (state.d) {
				case NORTH:
					new_state.past_actions.add(ACTION.FORWARD);
					new_state.cost += RobotConstant.MOVE_COST;
					new_state.d = DIRECTION.NORTH;
					break;
				case EAST:
					new_state.past_actions.add(ACTION.LEFT);
					new_state.cost += RobotConstant.TURN_COST;
					new_state.past_actions.add(ACTION.FORWARD);
					new_state.cost += RobotConstant.MOVE_COST;
					new_state.d = DIRECTION.NORTH;
					break;
				case SOUTH:
					new_state.past_actions.add(ACTION.TURN);
					new_state.cost += RobotConstant.TURN_COST + RobotConstant.TURN_COST / 4;
					new_state.past_actions.add(ACTION.FORWARD);
					new_state.cost += RobotConstant.MOVE_COST;
					new_state.d = DIRECTION.NORTH;
					break;
				case WEST:
					new_state.past_actions.add(ACTION.RIGHT);
					new_state.cost += RobotConstant.TURN_COST;
					new_state.past_actions.add(ACTION.FORWARD);
					new_state.cost += RobotConstant.MOVE_COST;
					new_state.d = DIRECTION.NORTH;
					break;
				}

				states.add(new_state);
				queued[new_state.row][new_state.col] = true;
			}

			// from any direction to east
			if (state.col + 1 < ArenaConstant.COLS && map.reachable[state.row][state.col + 1]
					&& !queued[state.row][state.col + 1]) {

				State new_state = new State(state.d, state.row, state.col, state.cost);
				new_state.past_actions.addAll(state.past_actions);

				new_state.row = state.row;
				new_state.col = state.col + 1;
				switch (state.d) {
				case NORTH:
					new_state.past_actions.add(ACTION.RIGHT);
					new_state.cost += RobotConstant.TURN_COST;
					new_state.past_actions.add(ACTION.FORWARD);
					new_state.cost += RobotConstant.MOVE_COST;
					new_state.d = DIRECTION.EAST;
					break;
				case EAST:
					new_state.past_actions.add(ACTION.FORWARD);
					new_state.cost += RobotConstant.MOVE_COST;
					new_state.d = DIRECTION.EAST;
					break;
				case SOUTH:
					new_state.past_actions.add(ACTION.LEFT);
					new_state.cost += RobotConstant.TURN_COST;
					new_state.past_actions.add(ACTION.FORWARD);
					new_state.cost += RobotConstant.MOVE_COST;
					new_state.d = DIRECTION.EAST;
					break;
				case WEST:
					new_state.past_actions.add(ACTION.TURN);
					new_state.cost += RobotConstant.TURN_COST + RobotConstant.TURN_COST / 4;
					new_state.past_actions.add(ACTION.FORWARD);
					new_state.cost += RobotConstant.MOVE_COST;
					new_state.d = DIRECTION.EAST;
					break;
				}

				states.add(new_state);
				queued[new_state.row][new_state.col] = true;
			}

			// from any direction to south
			if (state.row - 1 >= 0 && map.reachable[state.row - 1][state.col] && !queued[state.row - 1][state.col]) {

				State new_state = new State(state.d, state.row, state.col, state.cost);
				new_state.past_actions.addAll(state.past_actions);

				new_state.row = state.row - 1;
				new_state.col = state.col;
				switch (state.d) {
				case NORTH:
					new_state.past_actions.add(ACTION.TURN);
					new_state.cost += RobotConstant.TURN_COST + RobotConstant.TURN_COST / 4;
					new_state.past_actions.add(ACTION.FORWARD);
					new_state.cost += RobotConstant.MOVE_COST;
					new_state.d = DIRECTION.SOUTH;
					break;
				case EAST:
					new_state.past_actions.add(ACTION.RIGHT);
					new_state.cost += RobotConstant.TURN_COST;
					new_state.past_actions.add(ACTION.FORWARD);
					new_state.cost += RobotConstant.MOVE_COST;
					new_state.d = DIRECTION.SOUTH;
					break;
				case SOUTH:
					new_state.past_actions.add(ACTION.FORWARD);
					new_state.cost += RobotConstant.MOVE_COST;
					new_state.d = DIRECTION.SOUTH;
					break;
				case WEST:
					new_state.past_actions.add(ACTION.LEFT);
					new_state.cost += RobotConstant.TURN_COST;
					new_state.past_actions.add(ACTION.FORWARD);
					new_state.cost += RobotConstant.MOVE_COST;
					new_state.d = DIRECTION.SOUTH;
					break;
				}

				states.add(new_state);
				queued[new_state.row][new_state.col] = true;
			}

			// from any direction to west

			if (state.col - 1 >= 0 && map.reachable[state.row][state.col - 1] && !queued[state.row][state.col - 1]) {

				State new_state = new State(state.d, state.row, state.col, state.cost);
				new_state.past_actions.addAll(state.past_actions);

				new_state.row = state.row;
				new_state.col = state.col - 1;
				switch (state.d) {
				case NORTH:
					new_state.past_actions.add(ACTION.LEFT);
					new_state.cost += RobotConstant.TURN_COST;
					new_state.past_actions.add(ACTION.FORWARD);
					new_state.cost += RobotConstant.MOVE_COST;
					new_state.d = DIRECTION.WEST;
					break;
				case EAST:
					new_state.past_actions.add(ACTION.TURN);
					new_state.cost += RobotConstant.TURN_COST + RobotConstant.TURN_COST / 4;
					new_state.past_actions.add(ACTION.FORWARD);
					new_state.cost += RobotConstant.MOVE_COST;
					new_state.d = DIRECTION.WEST;
					break;
				case SOUTH:
					new_state.past_actions.add(ACTION.RIGHT);
					new_state.cost += RobotConstant.TURN_COST;
					new_state.past_actions.add(ACTION.FORWARD);
					new_state.cost += RobotConstant.MOVE_COST;
					new_state.d = DIRECTION.WEST;
					break;
				case WEST:
					new_state.past_actions.add(ACTION.FORWARD);
					new_state.cost += RobotConstant.MOVE_COST;
					new_state.d = DIRECTION.WEST;
					break;
				}
				states.add(new_state);
				queued[new_state.row][new_state.col] = true;
			}
		}

		return null;

	}

	public LinkedList<ACTION> getPath() {
		return actions;
	}

	public void printPath(LinkedList<ACTION> actions) {
		System.out.print("START--");
		for (int i = 0; i < actions.size(); i++) {
			System.out.print(ACTION.encoding(actions.get(i)) + "--");
		}
		System.out.println("END");
	}

	public void printPath() {
		System.out.print("START--");
		for (int i = 0; i < actions.size(); i++) {
			System.out.print(ACTION.encoding(actions.get(i)) + "--");
		}
		System.out.println("END");
	}

	public static LinkedList<ACTION> reverse(LinkedList<ACTION> actions) {
		LinkedList<ACTION> reversed = new LinkedList<ACTION>();
		for (int i = actions.size() - 1; i >= 0; i--) {
			switch (actions.get(i)) {
			case FORWARD:
				reversed.add(ACTION.FORWARD);
				break;
			case LEFT:
				reversed.add(ACTION.RIGHT);
				break;
			case RIGHT:
				reversed.add(ACTION.LEFT);
				break;
			case TURN:
				reversed.add(ACTION.TURN);
				break;
			default:
				break;
			}
		}
		return reversed;
	}

}

package simulator;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import javax.swing.WindowConstants;

import algorithm.Exploration;
import algorithm.FastestPath;
import arena.Map;
import configuration.RobotConstant;
import configuration.RobotConstant.ACTION;
import connection.Connection;
import robot.Robot;

public class RealRunSimulator {

	// speed of robot speed put here
	private static int speed = RobotConstant.SPEED;// default is set to 100ms;
	private static int timeLimit = RobotConstant.TIMELIMIT;// default is 1000000
															// (need to be in
															// minutes:seconds)
	private static int coverage = RobotConstant.COVERAGE;// default is 100%

	// JFrame
	private static JFrame _appFrame = null; // application JFrame
	private static JPanel _mapCards = null; // JPanel for map views
	private static JPanel _buttons = null; // JPanel for buttons

	// UI painter object
	private static UI ui = null;
	private static Connection connection;
	public static Map map = null;
	public static Robot robot = null;

	public RealRunSimulator() {
		map = new Map();
		robot = new Robot(true);
		ui = new UI();
		ui.update(map, robot);
		// open connection
	}

	public void displaySimulator(String mode) {
		// initialize main frame
		_appFrame = new JFrame();
		_appFrame.setTitle("Robot Simulator");
		_appFrame.setSize(new Dimension(690, 700));
		_appFrame.setResizable(false);

		// Center the main frame in the middle of the screen
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		_appFrame.setLocation(dim.width / 2 - _appFrame.getSize().width / 2,
				dim.height / 2 - _appFrame.getSize().height / 2);

		// Create the CardLayout for storing the different maps
		_mapCards = new JPanel(new CardLayout());

		// Create the JPanel for the buttons
		_buttons = new JPanel();

		// Add _mapCards & _buttons to the main frame's content pane
		Container contentPane = _appFrame.getContentPane();
		contentPane.add(_mapCards, BorderLayout.CENTER);
		contentPane.add(_buttons, BorderLayout.PAGE_END);

		// Initialize the main map view
		initMainLayout(mode);

		// Display the application
		_appFrame.setVisible(true);
		_appFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		run();
	}

	/**
	 * Initialises the main map view by adding the different maps as cards in
	 * the CardLayout. Displays realMap by default.
	 */
	private static void initMainLayout(String mode) {
		CardLayout cl = ((CardLayout) _mapCards.getLayout());

		// update map
		if (mode == "FP") {
			_mapCards.add(ui, "REAL_MAP");
			cl.show(_mapCards, "REAL_MAP");
		} else {
			_mapCards.add(ui, "EXPLORATION");
			cl.show(_mapCards, "EXPLORATION");
		}

	}

	private static void run() {

		class RealRunDisplay extends SwingWorker<Integer, String> {

			private void updateAndroid(Map map, Robot robot) {
				connection.sendMsg(map.generateMapDescriptor(), "MAP");
				switch (robot.direction) {
				case EAST:
					connection.sendMsg(robot.row + "," + robot.col + "0", "BOT_POS");
					break;
				case SOUTH:
					connection.sendMsg(robot.row + "," + robot.col + "90", "BOT_POS");
					break;
				case WEST:
					connection.sendMsg(robot.row + "," + robot.col + "180", "BOT_POS");
					break;
				case NORTH:
					connection.sendMsg(robot.row + "," + robot.col + "270", "BOT_POS");
					break;
				}
			}

			private void updateArduino(ACTION a) {
				switch (a) {
				case FORWARD:
					connection.sendMsg("F", "INSTR");
					break;
				case LEFT:
					connection.sendMsg("L", "INSTR");
					break;
				case RIGHT:
					connection.sendMsg("R", "INSTR");
					break;
				case TURN:
					connection.sendMsg("T", "INSTR");
					break;
				}
			}

			protected Integer doInBackground() throws Exception {

				// initialization
				map.setUnexplored();
				robot = new Robot(true);

				// send start command
				connection.sendMsg("S", "BOT_START");

				while (true) {
					robot.sensorData = connection.recvMsg();
					// robot.sensorData = "0|0|0|0|0";
					map = robot.updateMap(map);
					// updateAndroid(map, robot);

					ui.update(map, robot);
					ui.repaint(100);
					ui.printRobotPos();

					robot = Exploration.nextMove(map, robot);
					updateArduino(Exploration.preAction);

					if (robot.row == RobotConstant.START_ROW && robot.col == RobotConstant.START_COL) {
						robot.sensorData = connection.recvMsg();
						map = robot.updateMap(map);
						ui.update(map, robot);
						ui.repaint(100);
						ui.printRobotPos();

						// if (map.coverage() != 100) {
						// FastestPath fp;
						// fp = new FastestPath(map, RobotConstant.START_ROW,
						// RobotConstant.START_COL);
						// LinkedList<ACTION> actions = new
						// LinkedList<ACTION>();
						// LinkedList<ACTION> reverseActions = new
						// LinkedList<ACTION>();
						// System.out.println(Exploration.hasMore(map));
						// while (Exploration.hasMore(map)) {
						// // go to unexplored
						// actions = fp.BFS(robot.direction, robot.row,
						// robot.col, Exploration.rowToReach,
						// Exploration.colToReach);
						// fp.printPath(actions);
						// for (int i = 0; i < actions.size(); i++) {
						// updateArduino(actions.get(i));
						// robot.act(actions.get(i));
						// robot.sensorData = connection.recvMsg();
						// map = robot.updateMap(map);
						// ui.update(map, robot);
						// ui.repaint(100);
						// // lag to make robot looks like moving,
						// // delay in MS
						// try {
						// TimeUnit.MILLISECONDS.sleep(speed);
						// } catch (Exception e) {
						// e.printStackTrace();
						// }
						// }
						// reverseActions.addAll(actions);
						// }
						// // go back start
						// reverseActions = FastestPath.reverse(reverseActions);
						// reverseActions.add(0, ACTION.TURN);
						// fp.printPath(reverseActions);
						// for (int i = 0; i < reverseActions.size(); i++) {
						// robot.act(reverseActions.get(i));
						// map = robot.updateMap(map);
						// ui.update(map, robot);
						// ui.repaint(100);
						// // lag to make robot looks like moving,
						// // delay in MS
						// try {
						// TimeUnit.MILLISECONDS.sleep(speed);
						// } catch (Exception e) {
						// e.printStackTrace();
						// }
						// }
						// }
						break;
					}
				}

				System.out.println("Coverage percentage of exploration: " + map.coverage() + "%");

				// start fastest path after exploration

				LinkedList<ACTION> actions = new LinkedList<ACTION>();
				FastestPath fp;

				connection.sendMsg("XC", "INSTR");
				// hard coded way point 6,6
				fp = new FastestPath(map, 6, 6, RobotConstant.START_DIR);
				fp.run();
				actions = fp.getPath();
				fp.printPath();
				String fpString = "ARX";
				int d = 0;
				for (int i = 0; i < actions.size(); i++) {
					if (actions.get(i) == ACTION.FORWARD) {
						d++;
					} else if (d > 0) {
						if (d >= 10) {
							fpString.concat("F" + d);
						} else {
							fpString.concat("F0" + d);
						}
						fpString.concat("" + ACTION.encoding(actions.get(i)));
					} else {
						fpString.concat("" + ACTION.encoding(actions.get(i)));
					}

				}

				for (int i = 0; i < actions.size(); i++) {
					robot.act(actions.get(i));
					ui.update(map, robot);
					ui.repaint(100);
					ui.printRobotPos();
					try {
						TimeUnit.MILLISECONDS.sleep(100);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				return 111;
			}
		}

		connection = Connection.getConnection();
		connection.openConnection();

		CardLayout cl = ((CardLayout) _mapCards.getLayout());
		cl.show(_mapCards, "EXPLORATION");
		new RealRunDisplay().execute();

		// while (true)
		// {
		// String message = "EX_START";
		// // System.out.println(connection.isConnected());
		// // while (message == null) {
		// // message = connection.recvMsg();
		// // }
		// // System.out.println(message);
		//
		// if (message.equals("EX_START")) {
		// System.out.println("exploration start");
		// CardLayout cl = ((CardLayout) _mapCards.getLayout());
		// cl.show(_mapCards, "EXPLORATION");
		// new ExplorationDisplay().execute();
		// break;
		// } else if (message == "FP_START") {
		// System.out.println("fastest path start");
		// CardLayout cl = ((CardLayout) _mapCards.getLayout());
		// cl.show(_mapCards, "REAL_MAP");
		// new FastestPathDisplay().execute();
		// }
		// }
	}
}
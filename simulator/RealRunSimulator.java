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
import configuration.RobotConstant.DIRECTION;
import connection.Connection;
import robot.Robot;

public class RealRunSimulator {
	private static int coverage = RobotConstant.COVERAGE; // default is 100%

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

			private void delay(int time) {
				try {
					TimeUnit.MILLISECONDS.sleep(time);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			private void updateDisplay(Map map, Robot robot) {
				updateAndroid(map, robot);
				ui.update(map, robot);
				ui.repaint(100);
				ui.printRobotPos();
			}

			private String generateFpString(LinkedList<ACTION> actions, String prefix) {
				String fpString = prefix;
				int d = 0;
				for (int i = 0; i < actions.size(); i++) {
					if (i == actions.size() - 1 && actions.get(i) == ACTION.FORWARD) {
						d++;
						if (d >= 10) {
							fpString = fpString.concat("F" + d);
						} else {
							fpString = fpString.concat("F0" + d);
						}
						d = 0;
						break;
					}
					if (actions.get(i) == ACTION.FORWARD) {
						d++;
					} else if (d > 0) {
						if (d >= 10) {
							fpString = fpString.concat("F" + d);
						} else {
							fpString = fpString.concat("F0" + d);
						}
						d = 0;
						fpString = fpString.concat("" + ACTION.encoding(actions.get(i)));
					} else {
						fpString = fpString.concat("" + ACTION.encoding(actions.get(i)));
					}
				}
				return fpString;
			}

			private void updateAndroid(Map map, Robot robot) {
				switch (robot.direction) {
				case EAST:
					connection.sendMsg(robot.row + "," + robot.col + ",0," + map.generateMapDescriptor(), "BOT_POS");
					break;
				case SOUTH:
					connection.sendMsg(robot.row + "," + robot.col + ",90," + map.generateMapDescriptor(), "BOT_POS");
					break;
				case WEST:
					connection.sendMsg(robot.row + "," + robot.col + ",180," + map.generateMapDescriptor(), "BOT_POS");
					break;
				case NORTH:
					connection.sendMsg(robot.row + "," + robot.col + ",270," + map.generateMapDescriptor(), "BOT_POS");
					break;
				}
			}

			private void updateArduino(ACTION a, int i) {
				switch (a) {
				case FORWARD:
					if (i >= 10) {
						connection.sendMsg("F" + i, "INSTR");
					} else {
						connection.sendMsg("F0" + i, "INSTR");
					}
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
				String waypoint[] = new String[2];

				while (true) {
					System.out.println("waiting for waypoint...");
					// start from android
					String msg = connection.recvMsg();
					if (msg.matches("[0-9]+,[0-9]+")) {
						waypoint = msg.split(",");
						System.out.println("waypoint: (" + waypoint[0] + "," + waypoint[1] + ")");
						break;
					}
				}

				while (true) {
					System.out.println("waiting for EX_START");
					// start from android
					if (connection.recvMsg().equals("EX_START")) {
						System.out.println("EX_START received");
						break;
					}
				}

				// send start command
				connection.sendMsg("S", "BOT_START");

				while (true) {
					robot.sensorData = connection.recvMsg();
					// robot.sensorData = "0|0|0|0|0";
					map = robot.updateMap(map);
					updateDisplay(map, robot);

					robot = Exploration.nextMoveOptimized(map, robot);
					updateArduino(Exploration.preAction, Exploration.instance);

					// get back to start
					if (robot.row == RobotConstant.START_ROW && robot.col == RobotConstant.START_COL) {

						if (map.coverage() < 50) {
							continue;
						}

						robot.sensorData = connection.recvMsg();
						map = robot.updateMap(map);
						updateDisplay(map, robot);

						if (map.coverage() != 100) {
							// this is bad design, since fp methods are not
							// static
							FastestPath fp;
							fp = new FastestPath(map, RobotConstant.START_ROW, RobotConstant.START_COL,
									RobotConstant.START_DIR);
							LinkedList<ACTION> actions = new LinkedList<ACTION>();

							System.out.println(Exploration.hasMore(map));
							while (Exploration.hasMore(map)) {
								// go to unexplored
								actions = fp.UCS(robot.direction, robot.row, robot.col, Exploration.rowToReach,
										Exploration.colToReach);
								fp.printPath(actions);
								ACTION lastAction = actions.removeLast();
								ACTION secondLastAction = null;
								if (actions.size() != 0) {
									secondLastAction = actions.removeLast();
								}
								if (actions.size() != 0) {
									String fpString = generateFpString(actions, "Z");
									connection.sendMsg(fpString, "INSTR");
								}

								delay(500);
								// update UI in fpString
								for (int i = 0; i < actions.size(); i++) {
									robot.act(actions.get(i));
									updateDisplay(map, robot);
									delay(400);
								}

								// second last step sense
								if (secondLastAction != null) {
									robot.act(secondLastAction);
									updateArduino(secondLastAction, 1);
									robot.sensorData = connection.recvMsg();
									map = robot.updateMap(map);
									updateDisplay(map, robot);
								}

								// last step sense
								robot.act(lastAction);
								updateArduino(lastAction, 1);
								robot.sensorData = connection.recvMsg();
								map = robot.updateMap(map);
								updateDisplay(map, robot);

								actions.add(secondLastAction);
								actions.add(lastAction);
							}
							System.out.println("reverse");
							// go back start

							actions = fp.UCS(robot.direction, robot.row, robot.col, RobotConstant.START_ROW,
									RobotConstant.START_COL);
							fp.printPath(actions);
							if (actions.size() != 0) {
								String fpString = generateFpString(actions, "Z");
								connection.sendMsg(fpString, "INSTR");
							}

							delay(500);
							// display go home
							for (int i = 0; i < actions.size(); i++) {
								robot.act(actions.get(i));
								updateDisplay(map, robot);
								// lag to make robot looks like moving,
								// delay in MS
								delay(400);
							}
						}
						break;
					}
				}

				System.out.println("Coverage percentage of exploration: " + map.coverage() + "%");

				// robot direction to south
				if (robot.direction == DIRECTION.WEST) {
					connection.sendMsg("ZL", "INSTR");
				}

				// start fastest path after exploration
				connection.sendMsg("C", "INSTR");

				while (true) {
					System.out.println("waiting for FP_START...");
					// start from android
					if (connection.recvMsg().equals("FP_START")) {
						System.out.println("FP_START received");
						break;
					}
				}

				LinkedList<ACTION> actions = new LinkedList<ACTION>();
				FastestPath fp;

				// hard coded way point
				fp = new FastestPath(map, Integer.parseInt(waypoint[0]), Integer.parseInt(waypoint[1]),
						RobotConstant.START_DIR);
				robot.direction = RobotConstant.START_DIR;
				fp.run();
				actions = fp.getPath();
				fp.printPath();

				String fpString = generateFpString(actions, "X");
				System.out.println(fpString);
				connection.sendMsg(fpString, "INSTR");

				for (int i = 0; i < actions.size(); i++) {
					robot.act(actions.get(i));
					updateDisplay(map, robot);
					delay(500);
				}

				return 111;
			}
		}

		connection = Connection.getConnection();
		connection.openConnection();

		CardLayout cl = ((CardLayout) _mapCards.getLayout());
		cl.show(_mapCards, "EXPLORATION");
		new RealRunDisplay().execute();

	}
}
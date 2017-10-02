package simulator;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.WindowConstants;

import algorithm.Exploration;
import algorithm.FastestPath;
import arena.Map;
import configuration.RobotConstant;
import configuration.RobotConstant.ACTION;
import robot.Robot;

public class Simulator {

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
	public static Map map = null;
	public static Robot robot = null;

	public Simulator(Boolean real) {
		map = new Map();
		robot = new Robot(real);
		ui = new UI();
		ui.update(map, robot);
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

		if (!robot.real) {
			// Initialize the buttons
			initButtonsLayout();
		}

		// Display the application
		_appFrame.setVisible(true);
		_appFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
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

	private static void initButtonsLayout() {
		_buttons.setLayout(new GridLayout());
		addButtons();
	}

	private static void addButtons() {

		if (!robot.real) {
			// Load Map Button
			JButton btn_LoadMap = new JButton("Load Map");
			formatButton(btn_LoadMap);

			btn_LoadMap.addMouseListener(new MouseAdapter() {
				public void mousePressed(MouseEvent e) {
					// Center the main frame in the middle of the screen
					Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
					JDialog loadMapDialog = new JDialog(_appFrame, "Load Map", true);
					loadMapDialog.setSize(400, 100);
					loadMapDialog.setResizable(false);
					loadMapDialog.setResizable(false);
					loadMapDialog.setLocation(dim.width / 2 - _appFrame.getSize().width / 2,
							dim.height / 2 - _appFrame.getSize().height / 2);
					loadMapDialog.setLayout(new FlowLayout());

					final JTextField loadTF = new JTextField(15);
					JButton loadMapButton = new JButton("Load");

					loadMapButton.addMouseListener(new MouseAdapter() {
						public void mousePressed(MouseEvent e) {
							loadMapDialog.setVisible(false);
							map.loadMap(loadTF.getText());
							CardLayout cl = ((CardLayout) _mapCards.getLayout());
							cl.show(_mapCards, "REAL_MAP");

							ui.repaint();
						}
					});

					loadMapDialog.add(new JLabel("File Name: "));
					loadMapDialog.add(loadTF);
					loadMapDialog.add(loadMapButton);
					loadMapDialog.setVisible(true);
				}
			});
			_buttons.add(btn_LoadMap);
		}

		JButton btn_ChangeSpeed = new JButton("Change Speed");
		formatButton(btn_ChangeSpeed);

		btn_ChangeSpeed.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
				JDialog changeSpeedDialog = new JDialog(_appFrame, "Change Speed", true);
				changeSpeedDialog.setSize(400, 200);
				changeSpeedDialog.setLayout(new GridLayout(0, 1));
				changeSpeedDialog.setLocation(dim.width / 2 - _appFrame.getSize().width / 2,
						dim.height / 2 - _appFrame.getSize().height / 2);

				final JTextField speedTF = new JTextField(15);
				JButton changeSpeedButton = new JButton("Change Speed(ms in delay)");
				String curSpeedText = "" + speed;
				JLabel currentSpeed = new JLabel("Current Speed: " + curSpeedText + "ms\n");
				currentSpeed.setVerticalTextPosition(JLabel.BOTTOM);

				changeSpeedButton.addMouseListener(new MouseAdapter() {
					public void mousePressed(MouseEvent e) {
						changeSpeedDialog.setVisible(false);
						speed = Integer.parseInt(speedTF.getText());
						changeSpeedDialog.dispose();
					}
				});

				changeSpeedDialog.add(new JLabel("Speed"));
				changeSpeedDialog.add(speedTF);
				changeSpeedDialog.add(currentSpeed);
				changeSpeedDialog.add(changeSpeedButton);

				changeSpeedDialog.setVisible(true);
			}
		});

		_buttons.add(btn_ChangeSpeed);

		// Set time limit for exploration
		JButton btn_SetTimeLimit = new JButton("Time Limit");
		formatButton(btn_SetTimeLimit);

		btn_SetTimeLimit.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
				JDialog changeTimeDialog = new JDialog(_appFrame, "Set Time Limit", true);
				changeTimeDialog.setSize(400, 200);
				changeTimeDialog.setLayout(new GridLayout(0, 1));
				changeTimeDialog.setLocation(dim.width / 2 - _appFrame.getSize().width / 2,
						dim.height / 2 - _appFrame.getSize().height / 2);

				final JTextField timeTF = new JTextField(15);
				JButton changeTimeButton = new JButton("Set Time Limit(in min:sec)");
				String curTimeLimit = "" + timeLimit;
				JLabel currentTime = new JLabel(String.format("%d min:%d sec",
						TimeUnit.MILLISECONDS.toMinutes(Long.valueOf(curTimeLimit).longValue()),
						TimeUnit.MILLISECONDS.toSeconds(Long.valueOf(curTimeLimit).longValue()) - TimeUnit.MINUTES
								.toSeconds(TimeUnit.MILLISECONDS.toMinutes(Long.valueOf(curTimeLimit).longValue()))));

				currentTime.setVerticalTextPosition(JLabel.BOTTOM);

				changeTimeButton.addMouseListener(new MouseAdapter() {
					public void mousePressed(MouseEvent e) {
						changeTimeDialog.setVisible(false);
						String time = timeTF.getText();
						String[] timeArr = time.split(":");
						timeLimit = (Integer.parseInt(timeArr[0]) * 60000) + (Integer.parseInt(timeArr[1]) * 1000);
						changeTimeDialog.dispose();
					}
				});

				changeTimeDialog.add(new JLabel("Time Limit"));
				changeTimeDialog.add(timeTF);
				changeTimeDialog.add(currentTime);
				changeTimeDialog.add(changeTimeButton);

				changeTimeDialog.setVisible(true);
			}
		});

		_buttons.add(btn_SetTimeLimit);

		// Set coverage for exploration
		JButton btn_SetCoverage = new JButton("Coverage");
		formatButton(btn_SetCoverage);

		btn_SetCoverage.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
				JDialog coverageDialog = new JDialog(_appFrame, "Set Coverage Limit", true);
				coverageDialog.setSize(400, 200);
				coverageDialog.setLayout(new GridLayout(0, 1));
				coverageDialog.setLocation(dim.width / 2 - _appFrame.getSize().width / 2,
						dim.height / 2 - _appFrame.getSize().height / 2);

				final JTextField coverageTF = new JTextField(15);
				JButton changeSpeedButton = new JButton("Set Coverage Limit(%)");
				String curCoverage = "" + coverage;
				JLabel currentCoverage = new JLabel("Current Coverage Limit: " + curCoverage + "%\n");
				currentCoverage.setVerticalTextPosition(JLabel.BOTTOM);

				changeSpeedButton.addMouseListener(new MouseAdapter() {
					public void mousePressed(MouseEvent e) {
						coverageDialog.setVisible(false);
						coverage = Integer.parseInt(coverageTF.getText());
						coverageDialog.dispose();
					}
				});

				coverageDialog.add(new JLabel("Coverage"));
				coverageDialog.add(coverageTF);
				coverageDialog.add(currentCoverage);
				coverageDialog.add(changeSpeedButton);

				coverageDialog.setVisible(true);
			}
		});

		_buttons.add(btn_SetCoverage);

		class FastestPathDisplay extends SwingWorker<Integer, String> {
			protected Integer doInBackground() throws Exception {

				LinkedList<ACTION> actions = new LinkedList<ACTION>();
				robot.row = RobotConstant.START_ROW;
				robot.col = RobotConstant.START_COL;
				robot.direction = RobotConstant.START_DIR;

				FastestPath fp;
				if (robot.real) {
					fp = new FastestPath(map, robot.waypointRow, robot.waypointCol);
				} else {
					fp = new FastestPath(map, RobotConstant.START_ROW, RobotConstant.START_COL);
				}
				fp.run();
				actions = fp.getPath();

				for (int i = 0; i < actions.size(); i++) {

					robot.act(actions.get(i));
					ui.update(map, robot);
					ui.repaint(100);

					// lag to make robot looks like moving, delay in MS
					try {
						TimeUnit.MILLISECONDS.sleep(speed);
					} catch (Exception e) {
						e.printStackTrace();
					}

					ui.printRobotPos();
				}
				return 111;
			}
		}

		// Fastest Path Button
		JButton btn_FastestPath = new JButton("Fastest Path");
		formatButton(btn_FastestPath);
		btn_FastestPath.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				CardLayout cl = ((CardLayout) _mapCards.getLayout());
				cl.show(_mapCards, "REAL_MAP");
				new FastestPathDisplay().execute();
			}
		});
		_buttons.add(btn_FastestPath);

		class ExplorationDisplay extends SwingWorker<Integer, String> {
			protected Integer doInBackground() throws Exception {

				long startTime = System.currentTimeMillis();
				long endTime = System.currentTimeMillis() + timeLimit;

				map.setUnexplored();
				robot = new Robot(false);
				map = robot.updateMap(map);
				while (System.currentTimeMillis() <= endTime && map.coverage() <= coverage) {

					robot = Exploration.nextMove(map, robot);
					map = robot.updateMap(map);
					ui.update(map, robot);
					ui.repaint(100);
					try {
						TimeUnit.MILLISECONDS.sleep(speed);
					} catch (Exception er) {
						er.printStackTrace();
					}
					ui.printRobotPos();
					if (robot.row == RobotConstant.START_ROW && robot.col == RobotConstant.START_COL) {
						if (map.coverage() != 100) {
							FastestPath fp;
							fp = new FastestPath(map, RobotConstant.START_ROW, RobotConstant.START_COL);
							LinkedList<ACTION> actions = new LinkedList<ACTION>();
							LinkedList<ACTION> reverseActions = new LinkedList<ACTION>();
							System.out.println(Exploration.hasMore(map));
							while (Exploration.hasMore(map)) {
								// go to unexplored
								actions = fp.BFS(robot.direction, robot.row, robot.col, Exploration.rowToReach,
										Exploration.colToReach);
								fp.printPath(actions);
								for (int i = 0; i < actions.size(); i++) {
									robot.act(actions.get(i));
									map = robot.updateMap(map);
									ui.update(map, robot);
									ui.repaint(100);
									// lag to make robot looks like moving,
									// delay in MS
									try {
										TimeUnit.MILLISECONDS.sleep(speed);
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
								reverseActions.addAll(actions);
							}
							// go back start
							reverseActions = FastestPath.reverse(reverseActions);
							fp.printPath(reverseActions);
							for (int i = 0; i < reverseActions.size(); i++) {
								robot.act(reverseActions.get(i));
								map = robot.updateMap(map);
								ui.update(map, robot);
								ui.repaint(100);
								// lag to make robot looks like moving,
								// delay in MS
								try {
									TimeUnit.MILLISECONDS.sleep(speed);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}
						break;
					}
				}

				System.out.println("Coverage percentage of exploration: " + map.coverage() + "%");
				System.out.println("Current Speed: " + speed + "ms");
				System.out.println("Time taken: " + (System.currentTimeMillis() - startTime) / 1000 + " Seconds");
				return 111;
			}
		}

		JButton btn_Exploration = new JButton("Exploration");
		formatButton(btn_Exploration);
		btn_Exploration.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				CardLayout cl = ((CardLayout) _mapCards.getLayout());
				cl.show(_mapCards, "EXPLORATION");
				System.out.println("exploration");
				new ExplorationDisplay().execute();
			}
		});
		_buttons.add(btn_Exploration);

	}

	// JButton Properties
	private static void formatButton(JButton btn) {
		btn.setFont(new Font("Arial", Font.BOLD, 13));
		btn.setFocusPainted(false);
	}

}
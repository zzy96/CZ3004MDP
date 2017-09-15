package simulator;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import algorithm.FastestPath;
import arena.Map;
import configuration.ArenaConstant;
import configuration.RobotConstant.ACTION;

public class Simulator{
	
	//JFrame
    private static JFrame _appFrame = null;         // application JFrame
    private static JPanel _mapCards = null;         // JPanel for map views
    private static JPanel _buttons = null;          // JPanel for buttons
    private static final boolean realRun = false;
    private static Map map = null;
	private LinkedList<ACTION> actions;
    public static void main(String[] args) {

        if (!realRun) {
            map = new Map();
        }

        displaySimulator();
    	/*
    	//load map
		Map map = new Map();
		map.loadMap("MAP1");
        FastestPath fp;
        fp = new FastestPath(map,6,6); //assume waypoint 6,6 for now
        LinkedList<ACTION> path= new LinkedList<ACTION>();
        path = fp.run();

		 * System.out.println("print states queue:");
			for (int i = 0; i < path.size(); i++) {
			System.out.print("direction: " + path.get(i).d);
			System.out.print(" (" + path.get(i).row + "," +
			path.get(i).col + ")");
			System.out.print(" cost: " + path.get(i).cost);
			printPath(path.get(i).past_actions);
		}
		System.out.println(path.getFirst());
		for(int i=0; i<path.size(); i++){
			
			System.out.println(path.get(i));
			//process the linked list and update map.
		}
		*/
    }
   
    private static void displaySimulator() {
        //initialize main frame
        _appFrame = new JFrame();
        _appFrame.setTitle("Robot Simulator");
        _appFrame.setSize(new Dimension(690, 700));
        _appFrame.setResizable(false);

        // Center the main frame in the middle of the screen
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        _appFrame.setLocation(dim.width / 2 - _appFrame.getSize().width / 2, dim.height / 2 - _appFrame.getSize().height / 2);

        // Create the CardLayout for storing the different maps
        _mapCards = new JPanel(new CardLayout());

        // Create the JPanel for the buttons
        _buttons = new JPanel();

        // Add _mapCards & _buttons to the main frame's content pane
        Container contentPane = _appFrame.getContentPane();
        contentPane.add(_mapCards, BorderLayout.CENTER);
        contentPane.add(_buttons, BorderLayout.PAGE_END);

        // Initialize the main map view
        initMainLayout();

        // Initialize the buttons
        initButtonsLayout();

        // Display the application
        _appFrame.setVisible(true);
        _appFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
    

    /**
     * Initialises the main map view by adding the different maps as cards in the CardLayout. Displays realMap
     * by default.
     */
    private static void initMainLayout() {
        CardLayout cl = ((CardLayout) _mapCards.getLayout());
        
        // update map
        if (!realRun) {

            _mapCards.add(map, "REAL_MAP");
            cl.show(_mapCards, "REAL_MAP");
        }else {
            cl.show(_mapCards, "EXPLORATION");
        }
    }
    
    private static void initButtonsLayout() {
        _buttons.setLayout(new GridLayout());
        addButtons();
    }
    

    private static void addButtons() {
        if (!realRun) {
        	
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
                    loadMapDialog.setLocation(dim.width / 2 - _appFrame.getSize().width / 2, dim.height / 2 - _appFrame.getSize().height / 2);
                    loadMapDialog.setLayout(new FlowLayout());

                    final JTextField loadTF = new JTextField(15);
                    JButton loadMapButton = new JButton("Load");

                    loadMapButton.addMouseListener(new MouseAdapter() {
                        public void mousePressed(MouseEvent e) {
                            loadMapDialog.setVisible(false);
                            map.loadMap(loadTF.getText());
                            CardLayout cl = ((CardLayout) _mapCards.getLayout());
                            cl.show(_mapCards, "REAL_MAP");
                            map.repaint();
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
        
        // Fastest Path Button
        JButton btn_FastestPath = new JButton("Fastest Path");
        formatButton(btn_FastestPath);
        btn_FastestPath.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {


            }
        });
        _buttons.add(btn_FastestPath);

   }

    /**
     * Helper method to set particular properties for all the JButtons.
     */
    private static void formatButton(JButton btn) {
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        btn.setFocusPainted(false);
    }
    
    public LinkedList<ACTION> fastestPathDisplay(){
    	
        FastestPath fp;
        fp = new FastestPath(map,6,6); //assume waypoint 6,6 for now
        LinkedList<ACTION> path = new LinkedList<ACTION>();
        path = fp.run();
		return path;
    }

}

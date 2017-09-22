package main;

import simulator.Simulator;

public class Starter {
	public static void main(String[] args) {

		Simulator ui = new Simulator(false);
		ui.displaySimulator();
		/*
		 * //load map Map map = new Map(); map.loadMap("MAP1"); FastestPath fp;
		 * fp = new FastestPath(map,6,6); //assume waypoint 6,6 for now
		 * LinkedList<ACTION> path= new LinkedList<ACTION>(); path = fp.run();
		 * 
		 * System.out.println("print states queue:"); for (int i = 0; i <
		 * path.size(); i++) { System.out.print("direction: " + path.get(i).d);
		 * System.out.print(" (" + path.get(i).row + "," + path.get(i).col +
		 * ")"); System.out.print(" cost: " + path.get(i).cost);
		 * printPath(path.get(i).past_actions); }
		 * System.out.println(path.getFirst()); for(int i=0; i<path.size();
		 * i++){
		 * 
		 * System.out.println(path.get(i)); //process the linked list and update
		 * map. }
		 */
	}
}

package main;

import simulator.Simulator;

public class Starter {
	public static void main(String[] args) {

		boolean realRun = false;

		if (realRun) {
			Simulator simulator = new Simulator(true);
			simulator.displaySimulator("EX");
		} else {
			Simulator simulator = new Simulator(false);
			simulator.displaySimulator("FP");
		}

	}
}

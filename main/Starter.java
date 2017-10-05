package main;

import simulator.RealRunSimulator;
import simulator.Simulator;

public class Starter {
	public static void main(String[] args) {

		boolean realRun = true;

		if (realRun) {
			RealRunSimulator simulator = new RealRunSimulator();
			simulator.displaySimulator("EX");
		} else {
			Simulator simulator = new Simulator();
			simulator.displaySimulator("FP");
		}

	}
}

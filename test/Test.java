package test;

import algorithm.FastestPath;
import arena.Map;

public class Test {

	public static void main(String[] args) {
		loadMapTest();
	}

	public static void loadMapTest() {
		Map map = new Map();
		map.loadMap("Week11");
		map.printRealMap();
	}

	public static void generateMapDescriptorTest() {
		Map map = new Map();
		map.loadMap("MAP1");
		map.generateMapDescriptor();
	}

	public static void fastestPathTest() {
		Map map = new Map();
		map.loadMap("MAP1");
		FastestPath fp = new FastestPath(map, 6, 6);
		fp.run();
		fp.printPath();
	}

}

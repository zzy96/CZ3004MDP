package test;

import algorithm.FastestPath;
import arena.Map;
import configuration.RobotConstant;
import connection.Connection;

public class Test {

	public static void main(String[] args) {
		System.out.println(hexToBin("fff"));
		System.out.println(hexToBin("00f"));
		System.out.println(hexToBin("03afc"));
	}

	private static String hexToBin(String hex) {
		int pointer = 0;
		String ret = "";
		String partial;
		// 1 Hex digits each time to prevent overflow and recognize leading 0000
		while (hex.length() - pointer > 0) {
			partial = hex.substring(pointer, pointer + 1);
			String bin = Integer.toBinaryString(Integer.parseInt(partial, 16));
			for (int i = 0; i < 4 - bin.length(); i++) {
				ret = ret.concat("0");
			}
			ret = ret.concat(bin);
			pointer += 1;
		}
		return ret;
	}

	public static void receiveMsg() {
		Connection connection = Connection.getConnection();
		connection.openConnection();
		while (true) {
			System.out.println("checking...");
			String s = connection.recvMsg();
			if (s != "") {
				System.out.println(s);
				break;
			}
		}
		// String s = connection.recvMsg();
		connection.closeConnection();
	}

	public static void testConnection() {
		Connection connection = Connection.getConnection();
		connection.openConnection();
		System.out.println(connection.isConnected());

		connection.sendMsg("L", "INSTR");
		connection.recvMsg();

		connection.sendMsg("R", "INSTR");
		connection.recvMsg();

		connection.sendMsg("L", "INSTR");
		connection.recvMsg();

		connection.sendMsg("R", "INSTR");
		connection.recvMsg();

		connection.sendMsg("L", "INSTR");
		connection.recvMsg();

		connection.sendMsg("R", "INSTR");
		connection.recvMsg();

		connection.sendMsg("L", "INSTR");
		connection.recvMsg();

		connection.sendMsg("R", "INSTR");
		connection.recvMsg();

		connection.sendMsg("L", "INSTR");
		connection.recvMsg();

		connection.sendMsg("R", "INSTR");
		connection.recvMsg();

		connection.sendMsg("L", "INSTR");
		connection.recvMsg();

		connection.sendMsg("R", "INSTR");
		connection.recvMsg();

		connection.closeConnection();
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
		FastestPath fp = new FastestPath(map, 6, 6, RobotConstant.START_DIR);
		fp.run();
		fp.printPath();
	}

}

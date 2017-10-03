package test;

import algorithm.FastestPath;
import arena.Map;
import configuration.RobotConstant;
import configuration.RobotConstant.ACTION;
import connection.Connection;

public class Test {

	public static void main(String[] args) {
		System.out.println("" + ACTION.encoding(ACTION.FORWARD));
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

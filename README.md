# CZ3004 MDP

Algorithm for MDP Group 28

### Introduction

The main objective of the algorithm is to help robot make decision during exploration and find the fastest path from start zone to goal zone passing through a predefined waypoint. 

### Up and Running

There are two simulators in the simulator package namely `RealRunSimulator.java` and `Simulator.java`.

Before Running, configure `boolean realRun = true;` in the 9th line of `main/Starter.java`. This is the entry point of the program.

Other necessary configuration data is in the configuration package.

### Main Features of Algorithm

Exploration:
1. Using "Right Wall Hugging".
2. Accelerated move when going through explored area.
3. Avoid unnecessary right turn. (Detached Right Wall Hugging)
4. Second round exploration to clear any potential unexplored cell.

Fastest Path:
1. Based on uniform cost search with predefined cost of "turn" and "forward"
2. Perform two uniform cost search between Start Zone to Waypoint and Waypoint to Goal Zone. The full path is the combined action list.


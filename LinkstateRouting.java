/* Copyright (C) Sahil Mokkapati - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Written by Sahil Mokkapati <sahil.mokkapati@gmail.com>, December 2016
 */

import java.io.*;
import java.util.Scanner;

public class LinkstateRouting {
	private static int graph[][], routers;
	private static Scanner scan = new Scanner(System.in); // scan is a Scanner Object

	public static void main(String a[]) throws Exception {
		int option = 0;
		Scanner scan = new Scanner(System.in); // Instance for input declared
		System.out.println("********Please give the inpute file as topology.txt********");
		

		while (option != 6) // Main loop for execution
		{
			System.out.println( "\n" +
					"(1) Create a new Network Topology\n" +
					"(2) Build a Connection Table \n" +
					"(3) Distance to all nodes from source node\n" +
					"(4) Shortest Path to Destination Router\n" +
					"(5) Modify Topology \n" +
					"(6) Exit \n"); // printing menu
			
			System.out.print("Please enter one of the above options :");
			
			try {
				option = Integer.parseInt(scan.nextLine());// Taking user input 
			} catch (NumberFormatException ex) {
				System.out.println("\n******** Wrong Input Format! Choose a number from 1 to 6 ******** ");
				continue;
			}
						
			switch (option) // Checking for the User input from menu option
			{
				case 1:
					createTopology(); // sending control to create topology code
					break; 
				case 2:
					builConnectionTable(); // Sending control to building connection
					break; 
				case 3:
					computeDistanceToAllNodes(); // Sending control to find shortest distance to all nodes
					break;
				case 4:
					getShortestPath(); // Sending control to find shortest path
					break; 
				case 5:
					modifyTopology(); // sending control to modify the toplogy
					break; 
				case 6:
					System.out.println("\n Exit CS542-04 2016 Fall project. Good Bye!");
					break; // Exiting the main control loop and the code
				default:
					System.out.println("\n************ Enter a number from 1 to 6 **************\n ");
					break; // Printing the User input error
			}
		}
		if (scan.hasNextLine())
			return;
	}

	// METHOD FOR CREATING TOPOLOGY
	public static void createTopology()
	{
		System.out.println("==============STARTING TO CREATE TOPOLOGY=================");
		int row = 0, col = 0;
		try {
			scan = new Scanner(System.in);
			System.out.println("\nPlease enter the file name <name.txt>");
			String s_t_r = scan.nextLine();
			File inFile = new File(s_t_r); // Connecting to the input file
			
			Scanner in = new Scanner(inFile);
			String lines[] = in.nextLine().trim().split("\\s+"); // line count estimation
			in.close(); // Closing the input file
			
			routers = lines.length;
			graph = new int[routers][routers]; // declaring the adjacency matrix
			
			in = new Scanner(inFile); // scanner for each line in the input file
			int lineCount = 0;
			while (in.hasNextLine()) // Checking the size of the matrix input
			{
				String currentLine[] = in.nextLine().trim().split("\\s+"); // read each line
				for (int i = 0; i < currentLine.length; i++) {
					graph[lineCount][i] = Integer.parseInt(currentLine[i]);
				}
				lineCount++;
			}
		} catch (Exception e) { // Catching exception for error in reading the file
			System.out.println("Error in reading the file");
			return;
		}
		
		System.out.println("\nReview the original topology ");
		for (row = 0; row < routers; row++) {
			for (col = 0; col < routers; col++)
				System.out.print(graph[row][col] + "\t"); // Printing the topology that is created
			System.out.println();
		}
	}

	// METHOD FOR MODIFYING TOPOLOGY
	public static void modifyTopology()
	{
		System.out.println("===============STARTING TO MODIFY TOPOLOGY==============");
		scan = new Scanner(System.in);
		System.out.print("Please enter Router that you want to Shutdown : "); // Printing Instruction
		
		int delr = Integer.parseInt(scan.nextLine()); // Reading the router to be deleted
		while (delr < 1 || delr > routers) {
			System.out.print("\n The entered source router not present, Please enter Again : ");
			delr = Integer.parseInt(scan.nextLine());
		}
		delr = delr - 1;
		
		int[][] graph2 = new int[routers-1][routers-1];
		int p = 0;
		for (int i = 0; i < routers; ++i) { // Loop for deleting router and shifting graphay

			if (i == delr)
				continue;
			int q = 0;
			for (int j = 0; j < routers; ++j) // Control loop for shifting
			{
				if (j == delr)
					continue;
				
				graph2[p][q] = graph[i][j]; // shifting row and column
				++q;
			}
			++p;
		}
		
		routers -= 1;
		
		System.out.println("============NEW TOPLOGY============");
		for (int row = 0; row < routers; row++) {
			for (int col = 0; col < routers; col++) {
				LinkstateRouting.graph[row][col] = graph2[row][col];
				System.out.print(graph[row][col] + "\t"); // Printing the new topology again			
			}
			System.out.println();
	    }
		System.out.println("Router " + (delr + 1) + " is Shutdown");
	}

	// METHOD FOR BUILDING CONNECTION TABLE
	public static void builConnectionTable()
	{
		Scanner scan = new Scanner(System.in); 
		System.out.print("\nPlease enter the Source node to build connection: ");
		int src = scan.nextInt() - 1;
		while(src < 0 || src > routers-1)
        {
			System.out.println("The entered source node is out of range, please enter again: "); // Printing error
			src = scan.nextInt() - 1;	//re-input source
		}
		
		connectNetwork(src, -1, true);
	}
	
	// METHOD FOR PRINTING SHORTEST PATH
	public static void getShortestPath()
	{
		Scanner scan = new Scanner(System.in); //instance for input declared
		System.out.print("\nPlease enter the Source node for shortest path: ");
		int src = scan.nextInt() - 1;
		while(src < 0 || src > routers-1)
        {
			System.out.println("The entered source node is out of range, please enter again: "); // Printing error
			src = scan.nextInt() - 1;	//re-input source
		}
		
		System.out.print("Please enter the destination node for shortest path: ");
		int dest = scan.nextInt() - 1;
		while(dest < 0 || dest > routers-1)
        {
			System.out.println("The entered destination node is out of range, please enter again: "); // Printing error
			dest = scan.nextInt() - 1;	//re-input destination
		}
		
		connectNetwork(src, dest, true);
	}
	
	// METHOD FOR COMPUTING DISTANCE FROM SOURCE NODE TO ALL NODES
	public static void computeDistanceToAllNodes()
	{
		Scanner scan = new Scanner(System.in); // instance for input declared
		System.out.print("\nPlease enter the Source node to show all distances: ");
		int src = scan.nextInt() - 1; // taking source node input
		
		while(src < 0 || src > routers-1)
        {
			System.out.println("The entered source node is out of range, please enter again: "); // Printing error
			src = scan.nextInt() - 1;	//re-input source
		}
		
		connectNetwork(src, -1, false);
	}
	
	// METHOD FOR CONNECTING THE SOURCE AND DESTINATION NETWORK
	public static void connectNetwork(int src, int dest, boolean printPath)
	{
		int v = routers;
		
		int parent[] = new int[v];//parent node array
		int dist[] = new int[v];// distance array
		boolean visited[] = new boolean[v];// visited will be either true or false
		
		// initialize parent, distance arrays
		for (int i = 0; i < v; i++ ) { 
			visited[i] = false;
			parent[i] = -1;
			dist[i] = Integer.MAX_VALUE;
		}
		
		// initialize the source distance as zero
		dist[src] = 0;
		
		// get minimum edge from the unvisited nodes
		for (int count = 0; count < v-1; count++) { // loop for all v nodes
			
			int start = -1;
			int min = Integer.MAX_VALUE;
			
			for (int i = 0; i < v; i++) {
				if (visited[i] == false && dist[i] < min) {
					min = dist[i];
					start = i;
				}
			}
			
			// minimum distance is computed for all connected edges
			if (start == -1) 
				break;
			
			// update the current node as visited
			visited[start] = true;
			
			// update all adjacent nodes' distance array
			for (int end = 0; end < v; end++) {
				if ((visited[end] != true) && (graph[start][end] != -1) &&
						(dist[start] != Integer.MAX_VALUE) &&
						(dist[start] + graph[start][end] < dist[end])) {
					
					dist[end] = dist[start] + graph[start][end];
					
					// start node is the parent node in BFS tree
					parent[end] = start;
				}
			}
			
		}
		
		System.out.println();
		
		if (dest != -1) {
			
			// Case#4: shortest path between source and destination nodes 
			if (parent[dest] == -1) {
				System.out.println("There is no path from " + (src+1) + " to " + (dest+1) + " exists.");
			}  else {
				System.out.println("Shortest distance from " + (src+1) + " to " + (dest+1) + " is: " + dist[dest]);
				System.out.print("Corresponding Shortest Path is: ");
				printNodes(parent, parent[dest], src);
				System.out.println(dest+1);
			}
			
		} else {
			
            System.out.println("The Shortest Distance from node you entered, "+ (src+1) + " are\n");
            //Printing shortest path length
			System.out.println("Destination\tDistance\n========================");
            for (int i = 0; i < v; i++) {
                System.out.println("\t"+ (i+1) + "\t  "+ dist[i]); // Printing the node number and distance
            }
            
			if (printPath == true) {
				
				// case#2: Print all shortest paths from source node
				for (int i = 0; i < v; i++) {
					if (i == src)
						continue;
					else if (parent[i] == -1) {
						System.out.println("Shortest path from " + (src+1) + " to " + (i+1) + " doesn't exist.");
						continue;
					}
					
					System.out.print("Shortest Path from " + (src+1) + " to " + (i+1) + " is: ");
					printNodes(parent, parent[i], src);
					System.out.println(i+1);
				}
			}
		}
		
	}

	private static void printNodes(int[] parent, int next, int src) {
		if (next == -1)
			return;
		printNodes(parent, parent[next], src);
		System.out.print((next+1) + " --> ");
	}
}
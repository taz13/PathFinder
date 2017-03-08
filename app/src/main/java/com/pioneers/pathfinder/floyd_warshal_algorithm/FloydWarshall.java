/**
 *
 */
package com.pioneers.pathfinder.floyd_warshal_algorithm;

import java.util.Arrays;

import static java.lang.String.format;

/**
 * @author trahman
 */
public class FloydWarshall {

    /**
     * @param args
     */
    static String csvFile = "busInfo.csv";
    static double[][] distCost;
    static double[][] distTime;

    public static void main(String[] args) {
//		ArrayList<String> busStopInfo= Utilities.readCsv(csvFile);
////		ArrayList<String> busStopInfo=new ArrayList<String>();
//		
//		
//		
//		int [][] cost= new int[busStopInfo.size()][3];
//		int [][] time= new int[busStopInfo.size()][3];
//		int index=0;
//		for(String stops:busStopInfo)
//		{
//			String[] info=stops.split(",");
//			//extract cost of path
//			cost[index][0]=Integer.parseInt(info[0]);
//			cost[index][1]=Integer.parseInt(info[1]);
//			cost[index][2]=Integer.parseInt(info[2]);
//			//extract time of path
//			cost[index][0]=Integer.parseInt(info[0]);
//			cost[index][1]=Integer.parseInt(info[1]);
//			cost[index][2]=Integer.parseInt(info[2]);
//		}
//		
//		int numVertices=cost.length;
//		System.out.println(cost.length);
//
//        floydWarshall(cost, numVertices,distCost);
//        floydWarshall(cost, numVertices,distTime);

        int[][] weights = {{1, 3, -2}, {2, 1, 4}, {2, 3, 3}, {3, 4, 2}, {4, 2, -1}};
        int numVertices = 4;

        floydWarshall(weights, numVertices);
    }

    //calculates all pair shortest path
    static void floydWarshall(int[][] weights, int numVertices, double[][] dist) {


        for (double[] row : dist)
            Arrays.fill(row, Double.POSITIVE_INFINITY);

        for (int[] w : weights)
            dist[w[0] - 1][w[1] - 1] = w[2];

        int[][] next = new int[numVertices][numVertices];
        for (int i = 0; i < next.length; i++) {
            for (int j = 0; j < next.length; j++)
                if (i != j)
                    next[i][j] = j + 1;
        }

        for (int k = 0; k < numVertices; k++)
            for (int i = 0; i < numVertices; i++)
                for (int j = 0; j < numVertices; j++)
                    if (dist[i][k] + dist[k][j] < dist[i][j]) {
                        dist[i][j] = dist[i][k] + dist[k][j];
                        next[i][j] = next[i][k];
                    }

        printResult(dist, next);
    }

    static void floydWarshall(int[][] weights, int numVertices) {

        double[][] dist = new double[numVertices][numVertices];
        for (double[] row : dist)
            Arrays.fill(row, Double.POSITIVE_INFINITY);

        for (int[] w : weights)
            dist[w[0] - 1][w[1] - 1] = w[2];

        int[][] next = new int[numVertices][numVertices];
        for (int i = 0; i < next.length; i++) {
            for (int j = 0; j < next.length; j++)
                if (i != j)
                    next[i][j] = j + 1;
        }

        for (int k = 0; k < numVertices; k++)
            for (int i = 0; i < numVertices; i++)
                for (int j = 0; j < numVertices; j++)
                    if (dist[i][k] + dist[k][j] < dist[i][j]) {
                        dist[i][j] = dist[i][k] + dist[k][j];
                        next[i][j] = next[i][k];
                    }

        printResult(dist, next);
    }

    static void printResult(double[][] dist, int[][] next) {
        System.out.println("pair     dist    path");
        for (int i = 0; i < next.length; i++) {
            for (int j = 0; j < next.length; j++) {
                if (i != j) {
                    int u = i + 1;
                    int v = j + 1;
                    String path = format("%d -> %d    %2d     %s", u, v,
                            (int) dist[i][j], u);
                    do {
                        u = next[u - 1][v - 1];
                        path += " -> " + u;
                    } while (u != v);
                    System.out.println(path);
                }
            }
        }
    }

    //saving tree to database
    static void savePaths(double[][] dist, int[][] next) {
        System.out.println("pair     dist    path");
        for (int i = 0; i < next.length; i++) {
            for (int j = 0; j < next.length; j++) {
                if (i != j) {
                    int u = i + 1;
                    int v = j + 1;
                    String path = format("%d -> %d    %2d     %s", u, v,
                            (int) dist[i][j], u);
                    do {
                        u = next[u - 1][v - 1];
                        path += " -> " + u;
                    } while (u != v);
                    System.out.println(path);
                }
            }
        }
    }


}

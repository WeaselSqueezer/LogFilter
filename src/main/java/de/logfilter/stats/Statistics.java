package de.logfilter.stats;

import org.mcstats.Metrics;
import org.mcstats.Metrics.Graph;
import org.mcstats.Metrics.Plotter;

public class Statistics {
	
	/* Numbers of entries */
	public static int total    = 0;
	public static int filtered = 0;
	
	/* Metrics */
	private Metrics metrics;
	
	/* Constructor */
	public Statistics(Metrics metrics) {
		this.metrics = metrics;
		
		/* Setup Graphs */
		this.setupGraphs();
	}
	
	private void setupGraphs() {
		/* Create graph */
		Graph graph = metrics.createGraph("Log entries per day");
		
		/* Add plotter for total entries*/
		graph.addPlotter(new Plotter("Total entries") {

			@Override
			public int getValue() {
				/* Return static value */
				return total;
			}
			
		});
		
		/* Add plotter for filtered entries */
		graph.addPlotter(new Plotter("Filtered entries") {

			@Override
			public int getValue() {
				/* Return static value */
				return filtered;
			}
			
		});
	}
	
	public static void incrementTotal() {
		/* Increment total entries */
		total++;
	}
	
	public static void incrementFiltered() {
		/* Increment filtered entries */
		filtered++;
	}
}
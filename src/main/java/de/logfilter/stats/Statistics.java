package de.logfilter.stats;

import org.mcstats.Metrics;
import org.mcstats.Metrics.Graph;
import org.mcstats.Metrics.Plotter;

public class Statistics {
	
	public static int total    = 0;
	public static int filtered = 0;
	
	private Metrics metrics;
	
	public Statistics(Metrics metrics) {
		this.metrics = metrics;
		this.setupGraphs();
	}
	
	private void setupGraphs() {
		Graph graph = metrics.createGraph("Log entries per day");
		
		graph.addPlotter(new Plotter("Total entries") {

			@Override
			public int getValue() {
				return total;
			}
			
		});
		
		graph.addPlotter(new Plotter("Filtered entries") {

			@Override
			public int getValue() {
				return filtered;
			}
			
		});
	}
	
	public static void incrementTotal() {
		total++;
	}
	
	public static void incrementFiltered() {
		filtered++;
	}
}
package de.logfilter.stats;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mcstats.Metrics;
import org.mcstats.Metrics.Graph;
import org.mcstats.Metrics.Plotter;

import de.logfilter.LogFilter;

public class Statistics {
	
	/* Le logger */
	private final Logger logger = LogManager.getLogger();
	
	/* LogFilter instance */
	private LogFilter logfilter;
	
	/* Values */
	private long total    = 0;
	private long filtered = 0;
	private long replaced = 0;
	
	/* Metrics instance */
	private Metrics metrics;
	
	/* Statistics instance */
	private static Statistics instance = null;
	
	public Statistics(LogFilter logfilter) {
		/* Set LogFilter instance */
		this.logfilter = logfilter;
		
		/* Set statistics instance */
		instance = this;
	}
	
	public void start() {
		try {
			/* Metrics instance */
			this.metrics = new Metrics(this.logfilter);
			
			/* Check if user allows to collect data or not */
			if(this.metrics.isOptOut()) {
				return;
			}
		
			/* Create Graph of filtered log entries */
			Graph graph = metrics.createGraph("Log entries");
		
			/* Add total log entries */
			graph.addPlotter(new Plotter("Total entries") {

				@Override
				public int getValue() {
					return (int) total;
				}
			
			});
		
			/* Add total filtered log entries */
			graph.addPlotter(new Plotter("Filtered entries") {

				@Override
				public int getValue() {
					return (int) filtered;
				}
			
			});
		
			/* Add total modified log entries */
			graph.addPlotter(new Plotter("Replaced entries") {

				@Override
				public int getValue() {
					return (int) replaced;
				}
			
			});
			
			/* Start Metrics */
			this.metrics.start();
			
		} catch(Throwable t) {
			this.logger.error("[LogFilter] Could not start Metrics correctly! :(");
		}
	}
	
	public static Statistics getInstance() {
		if(instance == null) {
			throw new IllegalStateException("Statistics instance not Initialized!");
		}
		return instance;
	}
	
	public void incrementTotal() {
		this.total++;
	}
	
	public void incrementFiltered() {
		this.filtered++;
	}
	
	public void incrementReplaced() {
		this.replaced++;
	}
}
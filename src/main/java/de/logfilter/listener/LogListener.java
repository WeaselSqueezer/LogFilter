package de.logfilter.listener;

import java.util.regex.Pattern;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import de.logfilter.LogFilter;
import de.logfilter.events.LoggingEvent;
import de.logfilter.stats.Statistics;

public class LogListener implements Listener {
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onLogging(LoggingEvent event) {
		/*  General */
		String message = event.getMessage();
		
		/* Iterate over all patterns */
		for(Pattern pattern : LogFilter.rules) {
			/* Check if message matches pattern */
			if(pattern.matcher(message).matches())
				event.setCancelled(true);
				break;
		}
				
		/* Increment total statistics */
		Statistics.incrementTotal();
		
		/* Increment filtered entries if event was cancelled */
		if(event.isCancelled())
			Statistics.incrementFiltered();
	}
}
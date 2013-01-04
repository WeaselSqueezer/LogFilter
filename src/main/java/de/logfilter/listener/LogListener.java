package de.logfilter.listener;

import java.util.List;

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
		
		/* Get rules */
		List<String> rules = LogFilter.config.getStringList("filter-rules");
		
		/* Check if message match a rule */
		for(String rule : rules) {
			/* Check if message matches rule */
			if(message.matches(rule)) 
				event.setCancelled(true);
				break;
		}
		
		/* Increment statistics */
		if(event.isCancelled()) {
			Statistics.incrementFiltered();
		} else {
			Statistics.incrementTotal();
		}
	}
}
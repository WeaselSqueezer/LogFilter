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
		if(!LogFilter.enabled) 
			return;
		
		String message = event.getMessage();
		
		for(Pattern pattern : LogFilter.rules) {
			if(pattern.matcher(message).matches())
				event.setCancelled(true);
				break;
		}
				
		Statistics.incrementTotal();
		
		if(event.isCancelled())
			Statistics.incrementFiltered();
	}
}
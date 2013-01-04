package de.logfilter.filter;

import java.util.logging.Filter;
import java.util.logging.LogRecord;

import org.bukkit.plugin.PluginManager;

import de.logfilter.events.LoggingEvent;

public class ConsoleFilter implements Filter {
	
	private PluginManager pm;
	
	public ConsoleFilter(PluginManager pm) {
		this.pm = pm;
	}

	public boolean isLoggable(LogRecord record) {
		/* Construct event */
		LoggingEvent event = new LoggingEvent(record.getMessage());
		
		/* Call event */
		pm.callEvent(event);
		
		/* Check if event is cancelled and return it*/
		return (event.isCancelled()) ? false : true;
	}
}
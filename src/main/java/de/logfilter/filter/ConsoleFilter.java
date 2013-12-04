package de.logfilter.filter;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.filter.AbstractFilter;
import org.apache.logging.log4j.message.Message;
import org.bukkit.plugin.PluginManager;

import de.logfilter.events.LoggingEvent;

public class ConsoleFilter extends AbstractFilter {
	
	private PluginManager pm;
	
	public ConsoleFilter(PluginManager pm) {
		this.pm = pm;
	}
	
	public Result filter(String message, Level level) {
		LoggingEvent event = new LoggingEvent(message);
		pm.callEvent(event);
		
		if(event.isModified()) {
			LogManager.getRootLogger().log(level, message);
			return Result.DENY;
		}
		
		return event.isCancelled() ? Result.DENY : Result.NEUTRAL;
	}

	public Result filter(LogEvent event) {
		return this.filter(event.getMessage().getFormattedMessage(), event.getLevel());
	}

	public Result filter(Logger logger, Level level, Marker marker, String message, Object... parameters) {
		return this.filter(message, level);
	}

	public Result filter(Logger logger, Level level, Marker marker, Object message, Throwable t) {
		return this.filter(message.toString(), level);
	}

	public Result filter(Logger logger, Level level, Marker marker, Message message, Throwable t) {
		return this.filter(message.getFormattedMessage(), level);
	}

}
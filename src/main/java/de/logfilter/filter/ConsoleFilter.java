package de.logfilter.filter;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.filter.AbstractFilter;
import org.apache.logging.log4j.message.Message;
import org.bukkit.plugin.PluginManager;

import de.logfilter.LogFilter;
import de.logfilter.events.LoggingEvent;

public class ConsoleFilter extends AbstractFilter {
	
	private PluginManager pm;
	
	public ConsoleFilter(PluginManager pm) {
		this.pm = pm;
	}
	
	public Result filter(String logger, String message, Level level) {
		/* Default to accept if null parameters were given */
		if (logger == null || message == null || level == null) {
			return Result.ACCEPT;
		}
		/* Do not filter our own filtered logger :D */
		if(logger.equals("logfilter-modified")) {
			return Result.ACCEPT;
		}
		
		/* Ignore messages triggered from us */
		if(message.startsWith("[LogFilter]")) {
			return Result.ACCEPT;
		}
		
		/* Create and call event */
		LoggingEvent event = new LoggingEvent(logger, message);
		pm.callEvent(event);
		
		if(event.isModified()) {
			LogManager.getLogger("logfilter-modified").log(level, event.getMessage());
			return Result.DENY;
		}
		
		if(event.isCancelled() && LogFilter.DEBUG) {
			LogManager.getLogger().log(Level.WARN, "[LogFilter] Event cancelled: " + message);
		}
		
		return event.isCancelled() ? Result.DENY : Result.NEUTRAL;
	}

	public Result filter(LogEvent event) {
		return this.filter(event.getLoggerName(), event.getMessage().getFormattedMessage(), event.getLevel());
	}

	public Result filter(Logger logger, Level level, Marker marker, String message, Object... parameters) {
		return this.filter(logger.getName(), message, level);
	}

	public Result filter(Logger logger, Level level, Marker marker, Object message, Throwable t) {
		return this.filter(logger.getName(), message.toString(), level);
	}

	public Result filter(Logger logger, Level level, Marker marker, Message message, Throwable t) {
		return this.filter(logger.getName(), message.getFormattedMessage(), level);
	}

}

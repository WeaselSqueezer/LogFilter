package de.logfilter.events;

import java.util.logging.LogRecord;

import lombok.Getter;
import lombok.Setter;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class LoggingEvent extends Event {
	
	public static final HandlerList handlers = new HandlerList();
	
	private @Getter LogRecord record;
	private @Getter @Setter String message;
	private @Getter @Setter boolean isCancelled = false;
	
	public LoggingEvent(LogRecord record) {
		this.record = record;
		this.message = record.getMessage();
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
	    return handlers;
	}
}
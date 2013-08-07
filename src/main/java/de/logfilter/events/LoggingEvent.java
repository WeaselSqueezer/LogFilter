package de.logfilter.events;

import java.util.logging.LogRecord;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class LoggingEvent extends Event {
	
	public static final HandlerList handlers = new HandlerList();
	private LogRecord record;
	private String message;
	private boolean isCancelled = false;
	
	public LoggingEvent(LogRecord record) {
		this.record = record;
		this.message = record.getMessage();
	}
	
	/**
	 * Returns message of LogRecord
	 * @return
	 */
	public String getMessage() {
		return this.message;
	}
	
	/**
	 * Replaces log message with given
	 * @param message
	 */
	public void setMessage(String message) {
		this.record.setMessage(message);
	}
	
	/**
	 * Set event cancelled or not
	 * @param state
	 */
	public void setCancelled(boolean state) {
		this.isCancelled = state;
	}
	
	/**
	 * Get state of event
	 * @return
	 */
	public boolean isCancelled() {
		return this.isCancelled;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
	    return handlers;
	}
}
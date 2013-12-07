package de.logfilter.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class LoggingEvent extends Event {
	
	public static final HandlerList handlers = new HandlerList();
	
	private boolean isCancelled = false;
	private boolean isModified = false;
	
	private String logger;
	private String message;
	
	public LoggingEvent(String logger, String message) {
		this.logger = logger;
		this.message = message;
	}
	
	public String getMessage() {
		return this.message;
	}
	
	public void setMessage(String message) {
		this.message = message;
		this.isModified = true;
	}
	
	public String getLoggerName() {
		return this.logger;
	}
	
	public boolean isModified() {
		return this.isModified;
	}
	
	public boolean isCancelled() {
		return this.isCancelled;
	}
	
	public void setCancelled(boolean cancelled) {
		this.isCancelled = cancelled;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
	    return handlers;
	}
}
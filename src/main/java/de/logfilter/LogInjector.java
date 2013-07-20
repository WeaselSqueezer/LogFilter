package de.logfilter;

import java.util.logging.Filter;
import java.util.logging.Handler;
import java.util.logging.Logger;

import org.bukkit.plugin.PluginManager;

import de.logfilter.filter.ConsoleFilter;

public class LogInjector {
	
	private PluginManager pm;
	
	protected LogInjector(PluginManager pm) {
		this.pm = pm;
	}
	
	protected void inject() {
		/* Loggers */
		Logger minecraft = Logger.getLogger("Minecraft");
		Logger global    = Logger.getLogger("");
		
		/* Define Filter */
		Filter filter = new ConsoleFilter(pm);
		 
		
		/* Set filters for minecraft logger */
		for(Handler handler : minecraft.getHandlers()) {
			handler.setFilter(filter);
		}
		
		/* Set filters for global logger */
		for(Handler handler : global.getHandlers()) {
			handler.setFilter(filter);
		}
	}
	
	protected void remove() {
		/* Loggers */
		Logger minecraft = Logger.getLogger("Minecraft");
		Logger global    = Logger.getLogger("");
		
		/* Remove filters for minecraft logger */
		for(Handler handler : minecraft.getHandlers()) {
			handler.setFilter(null);
		}
		
		/* Remove filters for global logger */
		for(Handler handler : global.getHandlers()) {
			handler.setFilter(null);
		}
	}
}
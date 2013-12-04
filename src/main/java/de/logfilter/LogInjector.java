package de.logfilter;

import org.apache.logging.log4j.LogManager;
import org.bukkit.plugin.PluginManager;

import de.logfilter.filter.ConsoleFilter;

public class LogInjector {
	
	private PluginManager pm;
	
	protected LogInjector(PluginManager pm) {
		this.pm = pm;
	}
	
	protected void inject() {
		/* Get root logger */
		org.apache.logging.log4j.core.Logger logger = (org.apache.logging.log4j.core.Logger) LogManager.getRootLogger();
		
		/* Add filter */
		logger.addFilter(new ConsoleFilter(pm));
				
		/* Loggers */
		//Logger minecraft = Logger.getLogger("Minecraft");
		//Logger global    = Logger.getLogger("");
		
		/* Define Filter */
		//Filter filter = new ConsoleFilter(pm);
		 
		
		/* Set filters for minecraft logger */
		//for(Handler handler : minecraft.getHandlers()) {
			//handler.setFilter(filter);
		//}
		
		/* Set filters for global logger */
		//for(Handler handler : global.getHandlers()) {
			//handler.setFilter(filter);
		//}
	}
	
	protected void remove() {
		/* Get root logger */
		org.apache.logging.log4j.core.Logger logger = (org.apache.logging.log4j.core.Logger) LogManager.getRootLogger();
		logger.addFilter(new ConsoleFilter(pm));

		/* Loggers */
		//Logger minecraft = Logger.getLogger("Minecraft");
		//Logger global    = Logger.getLogger("");
		
		/* Remove filters for minecraft logger */
		//for(Handler handler : minecraft.getHandlers()) {
		//	handler.setFilter(null);
		//}
		
		/* Remove filters for global logger */
		//for(Handler handler : global.getHandlers()) {
		//	handler.setFilter(null);
		//}
	}
}
package de.logfilter.listener;

import java.util.regex.Matcher;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import de.logfilter.LogFilter;
import de.logfilter.LoggingRule;
import de.logfilter.events.LoggingEvent;
import de.logfilter.stats.Statistics;

public class LogListener implements Listener {
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onLogging(LoggingEvent event) {
		/* Check if filtering is enabled at all */
		if(!LogFilter.enabled) {
			return;
		}
		
		String message = event.getMessage();
		
		/* Ignore all messages prefixed with [LogFilter], 
		 * we do not want to filter our own outputs 
		 */
		if(message.startsWith("[LogFilter]")) {
			return;
		}
		
		/* Testing */
		LogManager.getLogger().log(Level.INFO, "[LogFilter] Received log entry: " + message);
		
		for(LoggingRule rule : LogFilter.rules) {
			
			Matcher matcher = rule.getPattern().matcher(message);
			
			if(!matcher.matches())
				continue;
			
			if(!rule.shouldReplace()) {
				event.setCancelled(true);
				Statistics.incrementFiltered();
				break;
			}
			
			message = matcher.replaceAll(rule.getReplacement());
			event.setMessage(message);
			Statistics.incrementReplaced();
		}
				
		Statistics.incrementTotal();
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		
		if(!player.hasPermission("logfilter.notify")) {
			return;
		}
		
		//TODO -> Notify if an update is available
	}
}
package de.logfilter.listener;

import java.util.regex.Matcher;

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
		if(!LogFilter.enabled) 
			return;
		
		String message = event.getMessage();
		
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
		
		if(!player.hasPermission("logfilter.notify"))
			return;
	}
}
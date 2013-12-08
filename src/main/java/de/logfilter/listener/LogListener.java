package de.logfilter.listener;

import java.util.regex.Matcher;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import de.logfilter.LogFilter;
import de.logfilter.LoggingRule;
import de.logfilter.events.LoggingEvent;
import de.logfilter.stats.Statistics;
import de.logfilter.updater.UpdateChecker;
import de.logfilter.updater.UpdateChecker.UpdateData;

public class LogListener implements Listener {
	
	private Logger logger = LogManager.getLogger();
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onLogging(LoggingEvent event) {
		/* Check if filtering is enabled at all */
		if(!LogFilter.ENABLED) {
			return;
		}
		
		/* Get message */
		String message = event.getMessage();
		
		/* Output debug message for debugging */
		if(LogFilter.DEBUG) {
			logger.log(Level.INFO, "[LogFilter] Received log entry. Logger: {}, Entry: {}", event.getLoggerName(), message);
		}
		
		for(LoggingRule rule : LogFilter.getInstance().getRules()) {
			
			Matcher matcher = rule.getPattern().matcher(message);
			
			if(!matcher.matches()) {
				continue;
			}
			
			if(!rule.shouldReplace()) {
				event.setCancelled(true);
				Statistics.getInstance().incrementFiltered();
				break;
			}
			
			message = matcher.replaceAll(rule.getReplacement());
			event.setMessage(message);
			Statistics.getInstance().incrementReplaced();
		}
				
		Statistics.getInstance().incrementTotal();
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		LogFilter logfilter = LogFilter.getInstance();
		
		if(!player.hasPermission("logfilter.notify")) {
			return;
		}
		
		UpdateChecker checker = LogFilter.getInstance().getUpdateChecker();
		
		if(!checker.isUpdateAvailable()) {
			return;
		}
		
		UpdateData updateData = checker.getUpdateData();
		
		player.sendMessage(ChatColor.GOLD + "[LogFilter] Update available!");
		player.sendMessage(ChatColor.GOLD + "[LogFilter] Current version: "
								+ ChatColor.GREEN + logfilter.getDescription().getVersion()
								+ ChatColor.GOLD + " Newest version: "
								+ ChatColor.GREEN + updateData.getVersion());
		player.sendMessage(ChatColor.GOLD + "[LogFilter] Download available here: http://dev.bukkit.org/bukkit-plugins/logfilter/");
	}
}
package de.logfilter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import org.mcstats.Metrics;

import de.logfilter.commands.LogFilterCommands;
import de.logfilter.listener.LogListener;
import de.logfilter.stats.Statistics;

public class LogFilter extends JavaPlugin {
	
	public static boolean enabled = true;
	public static Logger log = Logger.getLogger("Minecraft");
	public static final ArrayList<LoggingRule> rules = new ArrayList<LoggingRule>();
	
	private LogInjector injector;
	private YamlConfiguration config;
	public Statistics statistics;
	
	
	@Override
	public void onEnable() {
		/* Time */
		long time = System.currentTimeMillis();
		
		/* Load configuration */
		this.loadConfiguration();
				
		/* Injecting LogFilter */
		try {
			injector = new LogInjector(this.getServer().getPluginManager());
			injector.inject();
		} catch(Exception ex) {
			log.severe("[LogFilter] Error while injecting filters..!");
			return;
		}
		
		/* Metrics */
		try {
			
			Metrics metrics = new Metrics(this);
			statistics = new Statistics(metrics);
			metrics.start();
			
		} catch(Exception ex) {
			/* Ignore Exception */
		}
		
		/* Register listeners */
		this.getServer().getPluginManager().registerEvents(new LogListener(), this);
		
		/* Register commands */
		this.getCommand("logfilter").setExecutor(new LogFilterCommands());
		
		/* Elapsed time */
		long elapsed = System.currentTimeMillis() - time;
		log.info("[LogFilter] LogFilter version 0.1 enabled! (" + elapsed + "ms)");
	}
	
	@Override
	public void onDisable() {
		log.info("[LogFilter] LogFilter disabled!");
		
		/* Remove filters */
		log.info("[LogFilter] Remove filters..");
		injector.remove();
		log.info("[LogFilter] Filters removed!");
	}
	
	public void loadConfiguration() {
		/* Define file */
		File configuration_file = new File(this.getDataFolder(), "config.yml");
		
		/* Check if configuration exists */
		if(!configuration_file.exists())
			this.saveDefaultConfig();
		
		/* Load configuration */
		this.config = YamlConfiguration.loadConfiguration(configuration_file);
		
		/* Get list of rules from configuration */		
		List<Map<?, ?>> filter_rules = this.config.getMapList("filter-rules");
		
		/* Iterate over rules */
		for(Map<?, ?> map : filter_rules) {
					
			String rule = (String) map.get("rule");
			boolean replace = map.containsKey("replace") ? (Boolean) map.get("replace") : false;
			
			if(!replace) {
				LogFilter.rules.add(new LoggingRule(rule));
				continue;
			}
			
			String replacement = (String) map.get("replacement");
			LogFilter.rules.add(new LoggingRule(rule, true, replacement));
		}
	}
}
package de.logfilter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import de.logfilter.commands.LogFilterCommands;
import de.logfilter.filter.ConsoleFilter;
import de.logfilter.listener.LogListener;
import de.logfilter.stats.Statistics;
import de.logfilter.updater.UpdateChecker;

public class LogFilter extends JavaPlugin {
	
	/* Logger */
	private Logger log = LogManager.getLogger();
	
	/* Array with all parsed rules */
	private ArrayList<LoggingRule> rules = new ArrayList<LoggingRule>();
	
	/* Current Plugin Build number */
	public static final int BUILD = 6;
	
	/* Static state booleans */
	public static boolean DEBUG = false;
	public static boolean ENABLED = true;
	
	/* Configuration */
	private FileConfiguration config;
	
	/* LogFilter Instance */
	private static LogFilter instance = null;
	
	/* UpdateChecker */
	private UpdateChecker updateChecker;
	
	/* Statistic Module */
	private Statistics statistics;
	
	@Override
	public void onEnable() {
		/* Time */
		long time = System.currentTimeMillis();
		
		/* Load configuration and rules */
		this.loadConfiguration();
				
		/* Injecting LogFilter */
		org.apache.logging.log4j.core.Logger logger = (org.apache.logging.log4j.core.Logger) LogManager.getRootLogger();
		logger.addFilter(new ConsoleFilter(this.getServer().getPluginManager()));
		
		/* McStats */
		this.statistics = new Statistics(this);
		this.statistics.start();
		
		/* Register our listeners */
		this.getServer().getPluginManager().registerEvents(new LogListener(), this);
		
		/* Register main command */
		this.getCommand("logfilter").setExecutor(new LogFilterCommands());
		
		/* Start Update Checker */
		this.updateChecker = new UpdateChecker(this);
		this.updateChecker.start();
		
		/* Elapsed time */
		long elapsed = System.currentTimeMillis() - time;
		log.info("[LogFilter] LogFilter version " + this.getDescription().getVersion() + " enabled! (" + elapsed + "ms)");
		
		/* Set instance for static access */
		instance = this;
	}
	
	@Override
	public void onDisable() {
		/* Set enabled to false */
		LogFilter.ENABLED = false;
	}
	
	private void loadConfiguration() {
		File configFile = new File(this.getDataFolder(), "config.yml");
		
		if(!configFile.exists()) {
			this.saveDefaultConfig();
		}
		
		this.config = YamlConfiguration.loadConfiguration(configFile);
		
		List<Map<?, ?>> filter_rules = this.config.getMapList("filter-rules");
		
		for(Map<?, ?> map : filter_rules) {
					
			String rule = (String) map.get("rule");
			boolean replace = map.containsKey("replace") ? (Boolean) map.get("replace") : false;
			
			if(!replace) {
				this.rules.add(new LoggingRule(rule));
				continue;
			}
			
			String replacement = (String) map.get("replacement");
			this.rules.add(new LoggingRule(rule, true, replacement));
		}
	}
	
	public ArrayList<LoggingRule> getRules() {
		return this.rules;
	}
	
	public static LogFilter getInstance() {
		if(instance == null) {
			throw new IllegalStateException("LogFilter instance not Initialized!");
		}
		return instance;
	}
	
	public UpdateChecker getUpdateChecker() {
		return this.updateChecker;
	}
	
	public Statistics getStatistics() {
		return this.statistics;
	}
	
	public FileConfiguration getConfiguration() {
		return this.config;
	}
}
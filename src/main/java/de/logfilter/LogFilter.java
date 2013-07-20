package de.logfilter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import org.mcstats.Metrics;

import de.logfilter.commands.LogFilterCommands;
import de.logfilter.listener.LogListener;
import de.logfilter.stats.Statistics;

public class LogFilter extends JavaPlugin {
	
	public static final String VERSION = "0.2";
	public static boolean enabled = true;
	public static Logger log = Logger.getLogger("Minecraft");
	public static final ArrayList<Pattern> rules = new ArrayList<Pattern>();
	
	private LogInjector injector;
	public static YamlConfiguration config;
	public Statistics statistics;
	
	
	@Override
	public void onEnable() {
		/* Time */
		long time = System.currentTimeMillis();
		
		/* Load configuration */
		this.loadConfiguration();
				
		/* Injection formatter */
		log.info("[LogFilter] Injecting filters...");
		
		try {
			injector = new LogInjector(this.getServer().getPluginManager());
			injector.inject();
		} catch(Exception ex) {
			log.severe("[LogFilter] Error while injecting filters..!");
			return;
		}
		
		log.info("[LogFilter] Injected filters successfully!");
				
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
		LogFilter.config = YamlConfiguration.loadConfiguration(configuration_file);
		
		/* Get list of rules from configuration */
		List<String> filter_rules = LogFilter.config.getStringList("filter-rules");
		
		/* Compile patterns for better efficiency */
		for(String rule : filter_rules) {
			
			/* Add rules to list and precompile it */
			rules.add(Pattern.compile(rule));
		}
	}
}
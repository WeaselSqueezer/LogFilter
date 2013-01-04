package de.logfilter;

/* Java related */
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/* Bukkit related */
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

/* McStats related */
import org.mcstats.Metrics;

/* LogFilter related */
import de.logfilter.listener.LogListener;
import de.logfilter.stats.Statistics;

public class LogFilter extends JavaPlugin {
	
	/* Version */
	public static final String VERSION = "0.1";
	
	/* Logger */
	public static Logger log = Logger.getLogger("Minecraft");
	
	/* Filter rules */
	public static final List<Pattern> rules = new ArrayList<Pattern>();
	
	/* LogInjector */
	private LogInjector injector;
	
	/* Configuration */
	public static YamlConfiguration config;
	
	/* Statistics for mcstats.org */
	public Statistics statistics;
	
	
	@Override
	public void onEnable() {
		/* Time */
		long time = System.currentTimeMillis();
				
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
		
		/* Load configuration */
		this.loadConfiguration();
		
		/* Metrics */
		try {
			
			/* Define metrics object */
			Metrics metrics = new Metrics(this);
			
			/* Construct statistic object */
			statistics = new Statistics(metrics);
			
			/* Start metrics */
			metrics.start();
			
		} catch(Exception ex) {
			/* Ignore Exception */
		}
		
		/* Register listeners */
		this.getServer().getPluginManager().registerEvents(new LogListener(), this);
		
		/* Estimated time */
		long estimated = System.currentTimeMillis() - time;
		
		log.info("[LogFilter] LogFilter version 0.1 enabled! (" + estimated + "ms)");
	}
	
	@Override
	public void onDisable() {
		/* Disable LogFilter */
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
		
		/* Compile patterns for better efficiency */
		List<String> filter_rules = LogFilter.config.getStringList("filter-rules");
		for(String rule : filter_rules) {
			rules.add(Pattern.compile(rule));
		}
	}
}
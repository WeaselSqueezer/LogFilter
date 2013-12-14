package de.logfilter.updater;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.craftbukkit.libs.com.google.gson.Gson;

public class UpdateChecker {
	
	/* Le Logger */
	private Logger logger = LogManager.getLogger();
	
	/* Necessary URLs */
	private final String API_URL = "https://api.curseforge.com/servermods/";
	private final String QUERY_URL = API_URL + "files?projectIds=%d";
	
	/* Time to check for updates in ticks (currently every six hours) */
	private final long CHECK_INTERVAL = 432000;
		
	/* Plugin instance */
	private Updatable plugin;
	
	/* Boolean if update is available or not */
	private boolean isAvailable = false;
	
	/* GSON instance for JSON */
	private Gson gson;
	
	/* Latest version - if version is newer than used one */
	private VersionData latestVersion = null;
	
	public UpdateChecker(Updatable plugin) {
		this.plugin = plugin;
	}

	public void start() {
		/* Check if user allows updates at all */
		if(!this.plugin.getConfig().getBoolean("update-check", true)) {
			/* Stop here! User does not want any update check! :/ */
			return;
		}
		
		/* Initialize GSON object */
		this.gson = new Gson();
		
		/* Start Checker Task */
		this.plugin.getServer().getScheduler().runTaskTimerAsynchronously(this.plugin, new Runnable() {

			public void run() {
				check();				
			}
			
		}, 400, this.CHECK_INTERVAL);
	}
	
	public void check() {
		try {
			/* First of all, get current version */
			String currentVersion = this.plugin.getDescription().getVersion();
						
			/* Open connection */
			URLConnection connection = (new URL(String.format(QUERY_URL, this.plugin.getProjectId()))).openConnection();
			
			/* Set User- Agent */
			connection.addRequestProperty("User-Agent", this.plugin.getName() + "/v" + currentVersion);
			
			/* Read output data */
			String content = (new BufferedReader(new InputStreamReader(connection.getInputStream()))).readLine();

			/* Parse JSON- encoded data */
			VersionData[] versions = this.gson.fromJson(content, VersionData[].class);
			
			/* Latest version */
			VersionData latest = null;
			
			/* Iterate over versions to find the newest */
			for(VersionData version : versions) {
				
				/* Initialize version object to parse additional information */
				version.init(this.plugin.getName());
				
				/* Check if current selected version is newer than the last discovered */
				if(latest == null || version.getBuild() > latest.getBuild()) {
					latest = version;
					continue;
				}
			}
		
			/* Check if latest version is newer than our version */
			if(latest.getBuild() > this.plugin.getBuild()) {
				
				/* Some information */
				logger.info("[LogFilter] Update available!");
				logger.info("[LogFilter] Current version: {}, Newest version: {}", currentVersion, latest.toString());
				logger.info("[LogFilter] Download available here: http://dev.bukkit.org/bukkit-plugins/logfilter/");
				
				/* Update variables */
				this.isAvailable = true;
				this.latestVersion = latest;
			}
			
		} catch(Throwable t) {
			logger.error("[LogFilter] Error while checking for update!", t);
		}
	}
	
	public boolean isUpdateAvailable() {
		return this.isAvailable;
	}
	
	public VersionData getLatestVersion() {
		if(this.latestVersion == null) {
			throw new IllegalStateException("No update available!");
		}
		return this.latestVersion;
	}
	
	public final class VersionData {
		
		/* Defined values from Curse output */
		private String downloadUrl;
		private String name;
		
		/* Additional parsed values */
		private int build;
		private String version;
		
		private VersionData() {}
		
		protected void init(String pluginName) {
			/* Pattern to check that version is named correctly */
			Matcher checker = Pattern.compile("^" + pluginName + "-v([0-9][0-9.]*)-b(\\d+)$").matcher(this.name);
			
			/* Check if name of version is correct */
			if(!checker.matches()) {
				throw new IllegalStateException("Version name not valid. Please contact the author!");
			}
			
			/* Set build number and version string */
			this.version = checker.group(1);
			this.build = Integer.parseInt(checker.group(2));
		}
		
		public String getURL() {
			return this.downloadUrl;
		}
		
		public String getName() {
			return this.name;
		}
		
		public int getBuild() {
			return this.build;
		}
		
		@Override
		public String toString() {
			return this.version;
		}
	}
}

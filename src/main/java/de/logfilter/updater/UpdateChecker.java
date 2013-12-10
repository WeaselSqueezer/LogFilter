package de.logfilter.updater;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.craftbukkit.libs.com.google.gson.Gson;

import de.logfilter.LogFilter;

public class UpdateChecker {
	
	/* Le Logger */
	private Logger logger = LogManager.getLogger();
	
	/* Check URL */
	private final String CHECK_URL = "https://raw.github.com/DaAllexx/LogFilter/master/VERSION";
	
	/* Time to check for updates in ticks (currently every six hours) */
	private final long CHECK_INTERVAL = 432000;
		
	/* LogFilter main instance */
	private LogFilter logfilter;
	
	/* Boolean if update is available or not */
	private boolean isAvailable = false;
	
	/* UpdateData - if present */
	private UpdateData updateData = null;
	
	public UpdateChecker(LogFilter logfilter) {
		this.logfilter = logfilter;
	}

	public void start() {
		/* Check if user allows updates at all */
		if(!this.logfilter.getConfiguration().getBoolean("update-check", true)) {
			/* Stop here! User does not want any update check! :/ */
			return;
		}
		
		/* Start Checker Task */
		this.logfilter.getServer().getScheduler().runTaskTimerAsynchronously(this.logfilter, new Runnable() {

			public void run() {
				check();				
			}
			
		}, 400, this.CHECK_INTERVAL);
	}
	
	public void check() {
		try {
			/* Open connection */
			URL url = new URL(CHECK_URL);
			URLConnection conn = url.openConnection();
			
			/* Read data */
			String line;
			StringBuilder builder = new StringBuilder();
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			while((line = reader.readLine()) != null) {
				builder.append(line);
			}

			/* Parse JSON- encoded data */
			Gson gson = new Gson();
			UpdateData data = gson.fromJson(builder.toString(), UpdateData.class);
		
			/* Check if an update is available */
			if(data.getBuild() > LogFilter.BUILD) {
				
				/* Some information */
				logger.info("[LogFilter] Update available!");
				logger.info("[LogFilter] Current version: " + this.logfilter.getDescription().getVersion()
								+ " Newest version: " + data.getVersion());
				logger.info("[LogFilter] Download available here: http://dev.bukkit.org/bukkit-plugins/logfilter/");
				
				/* Update variables */
				this.isAvailable = true;
				this.updateData = data;
			}
			
		} catch(Throwable t) {
			logger.error("[LogFilter] Error while checking for update!", t);
		}
	}
	
	public boolean isUpdateAvailable() {
		return this.isAvailable;
	}
	
	public UpdateData getUpdateData() {
		if(this.updateData == null) {
			throw new IllegalStateException("No update available!");
		}
		return this.updateData;
	}
	
	public final class UpdateData {
		
		private String version;
		private int build;
		
		UpdateData(String version, int build) {
			this.version = version;
			this.build = build;
		}
		
		public String getVersion() {
			return this.version;
		}
		
		public int getBuild() {
			return this.build;
		}
		
	}
}

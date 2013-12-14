package de.logfilter.updater;

import org.bukkit.plugin.java.JavaPlugin;

public abstract class Updatable extends JavaPlugin {
	
	public abstract int getBuild();
	public abstract int getProjectId();
	
}

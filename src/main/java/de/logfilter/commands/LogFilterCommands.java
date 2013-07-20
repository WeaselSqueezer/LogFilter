package de.logfilter.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import de.logfilter.LogFilter;

public class LogFilterCommands implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!cmd.getName().equalsIgnoreCase("logfilter"))
			return false;
		
		if(!sender.hasPermission("LogFilter.commands"))
			return false;
		
		if(args.length != 1)
			return false;
		
		if(args[0].equalsIgnoreCase("enable")) {
			
			if(LogFilter.enabled) {
				sender.sendMessage(ChatColor.BOLD + "[LogFilter] LogFilter already enabled!");
				return true;
			}
				
			LogFilter.enabled = true;
			
			sender.sendMessage(ChatColor.BOLD + "[LogFilter] LogFilter enabled!");
			return true;
		}
		
		if(args[0].equalsIgnoreCase("disable")) {
			
			if(!LogFilter.enabled) {
				sender.sendMessage(ChatColor.BOLD + "[LogFilter] LogFilter is already disabled!");
				return true;
			}
			
			LogFilter.enabled = false;
			sender.sendMessage(ChatColor.BOLD + "[LogFilter] LogFilter disabled!");
			return true;
		}
		
		return false;
	}
}
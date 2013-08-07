package de.logfilter.updater;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.bukkit.plugin.java.JavaPlugin;

public class Updater {
	
	private final static String UPDATE_SERVICE = "http://service.daallexx.eu/mcupdate/";
	private final static String CHECK_URL = "check/%s/%s";
	private final static String UPDATE_URL = "get/%s/%s/";
	private final static String SERVER_MOD = "bukkit";
	
	private final JavaPlugin plugin;
	
	public Updater(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	
	public void checkForUpdates() {
		try {
			URL url = new URL(String.format(UPDATE_SERVICE.concat(CHECK_URL), SERVER_MOD, plugin.getName()));
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.connect();
			
			if(conn.getResponseCode() != 200)
				return;
						
		} catch(IOException ex) {
			
		}
	}
	
	public void downloadUpdate(File target, String platform, String version) {		
		try {
			URL url = new URL(String.format(UPDATE_URL, platform, version));
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.connect();
			
			if(conn.getResponseCode() != 200) 
				return;
			
			InputStream in = conn.getInputStream();
			OutputStream out = new FileOutputStream(target);
			
			byte[] buffer = new byte[1024];
			int length;
			
			long bytes = 0;
			long start = System.currentTimeMillis();
			
			while((length = in.read(buffer)) > 0) {
				out.write(buffer, 0, length);
				bytes = bytes + length;
			}
			
			long time = System.currentTimeMillis() - start;
			boolean success = true;
			//TODO
			
			out.close();
			in.close();
		} catch (IOException e) {
			//TODO
		}
	}
}

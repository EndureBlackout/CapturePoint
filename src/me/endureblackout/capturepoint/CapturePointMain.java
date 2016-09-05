package me.endureblackout.capturepoint;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class CapturePointMain extends JavaPlugin implements Listener {
	
	private File f = new File(this.getDataFolder(), "regions.yml");
	public YamlConfiguration y;
	
	public void onEnable() {
		Bukkit.getServer().getPluginManager().registerEvents(new CapturePointRegion(this), this);
		Bukkit.getServer().getPluginManager().registerEvents(new PlayerListener(this), this);

			getCommand("create").setExecutor(new CapturePointRegion(this));
			getCommand("removepoint").setExecutor(new CapturePointRegion(this));
			File dataFolder = getDataFolder();
				if(!dataFolder.exists()) {
					dataFolder.mkdir();
				}
				if(!new File(dataFolder, "regions.yml").exists()) {
					saveResource("regions.yml", false);
				}
				
				y = YamlConfiguration.loadConfiguration(f);
	}
}

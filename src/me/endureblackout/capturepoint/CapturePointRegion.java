package me.endureblackout.capturepoint;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;



public class CapturePointRegion implements CommandExecutor, Listener {
	
	CapturePointMain plugin;
	public CapturePointRegion(CapturePointMain instance) {
		this.plugin = instance;
	}
	
	public WorldEditPlugin getWorldEdit() {
		Plugin p = Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
		if(p instanceof WorldEditPlugin) return (WorldEditPlugin) p;
		else return null;
	}
	
	@EventHandler
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		Player p = (Player) sender;
		
		if(cmd.getName().equalsIgnoreCase("create")) {
			
			File regions = new File(plugin.getDataFolder(), "regions.yml");
			YamlConfiguration y = new YamlConfiguration();
			
			if(regions.exists()) {
				try {
					y.load(regions);
				} catch (IOException | InvalidConfigurationException e) {
					e.printStackTrace();
				}
			}
			
			if(args.length == 0 && p.hasPermission("capture.admin")) {
				p.sendMessage(ChatColor.DARK_RED + "You must specify the name of the CapturePoint");
			} else if(args.length == 2 && p.hasPermission("capture.admin")) {
				if(args[1].equalsIgnoreCase("pos1")) {

					
					y.set("Regions." + args[0] + ".World", p.getWorld().getName());
					y.set("Regions." + args[0] + ".Pos1.x", p.getLocation().getBlockX());
					y.set("Regions." + args[0] + ".Pos1.y", p.getLocation().getBlockY());
					y.set("Regions." + args[0] + ".Pos1.z", p.getLocation().getBlockZ());
					
					try {
						y.save(regions);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else if(args[1].equalsIgnoreCase("pos2")) {
					y.set("Regions." + args[0] + ".Pos2.x", p.getLocation().getBlockX());
					y.set("Regions." + args[0] + ".Pos2.y", p.getLocation().getBlockY());
					y.set("Regions." + args[0] + ".Pos2.z", p.getLocation().getBlockZ());
					
					try {
						y.save(regions);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		
		if(cmd.getName().equalsIgnoreCase("removepoint")) {
			if(args.length == 0 && p.hasPermission("capture.admin")) {
				p.sendMessage(ChatColor.DARK_RED + "You must specify a name of a CapturePoint to remove");
			} else if(args.length == 1 && p.hasPermission("capture.admin")) { 
				File regionsFile = new File(Bukkit.getServer().getPluginManager().getPlugin("CapturePoint").getDataFolder(), "regions.yml");
				
				if(regionsFile.exists()) {
					YamlConfiguration y = new YamlConfiguration();
					
					try {
						y.load(regionsFile);
					} catch (Exception e) {
						e.printStackTrace();
						p.sendMessage(ChatColor.RED + args[0] + " doesn't exist!");
					}
					
					if(y.getConfigurationSection("Regions").contains(args[0])) {
						y.set("Regions." + args[0], null);
						try {
							y.save(regionsFile);
						} catch (Exception e) {
							e.printStackTrace();
							p.sendMessage(ChatColor.RED + "[Error] Couldn't save file! Please contact plugin dev!");
						}
						p.sendMessage(ChatColor.GREEN + args[0] + " has been removed!");
					}
				}
			}
		}
		
		return false;
	}

}
package me.endureblackout.capturepoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import net.md_5.bungee.api.ChatColor;

public class PlayerListener implements Listener {
	CapturePointMain plugin;
	
	public PlayerListener(CapturePointMain instance) {
		this.plugin = instance;
	}
	
	Map<UUID, String> capturing = new HashMap<UUID, String>();
	
	private Vector p1;
	private Vector p2;
	
	public PlayerListener(Vector p1, Vector p2) {
		int x1 = Math.min(p1.getBlockX(), p2.getBlockX());
		int y1 = Math.min(p1.getBlockY(), p2.getBlockY());
		int z1 = Math.min(p1.getBlockZ(), p2.getBlockZ());
		int x2 = Math.max(p1.getBlockX(), p2.getBlockX());
		int y2 = Math.max(p1.getBlockY(), p2.getBlockY());
		int z2 = Math.max(p1.getBlockZ(), p2.getBlockZ());
		
		this.p1 = new Vector(x1, y1, z1);
		this.p2 = new Vector(x2, y2, z2);
	}
	
	public boolean contains(Location loc) {
		if(loc == null) {
			return false;
		}
		
		return loc.getBlockX() >= p1.getBlockX() && loc.getBlockX() <= p2.getBlockX()
				&& loc.getBlockY() >= p1.getBlockY() && loc.getBlockY() <= p2.getBlockY()
				&& loc.getBlockZ() >= p1.getBlockZ() && loc.getBlockZ() <= p2.getBlockZ();
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e) {


		Player p = e.getPlayer();
			

			List<String> regions = new ArrayList<>();
			for(String key : plugin.y.getConfigurationSection("Regions").getKeys(false)) {
				if(key == null) {
					return;
				} else {
					regions.add(key);
				}
			
			for(int i = 0; i != regions.size(); i++) {
					int x1 = plugin.y.getInt("Regions." + regions.get(i) + ".Pos1.x");
					int y1 = plugin.y.getInt("Regions." + regions.get(i) + ".Pos1.y");
					int z1 = plugin.y.getInt("Regions." + regions.get(i) + ".Pos1.z");
					int x2 = plugin.y.getInt("Regions." + regions.get(i) + ".Pos2.x");
					int y2 = plugin.y.getInt("Regions." + regions.get(i) + ".Pos2.y");
					int z2 = plugin.y.getInt("Regions." + regions.get(i) + ".Pos2.z");
					
					PlayerListener border = new PlayerListener(new Vector(x1, y1, z1), new Vector(x2, y2, z2));
					
					if(!(border.contains(p.getLocation()))) {
						if(capturing.containsKey(p.getUniqueId())) {
							capturing.remove(p.getUniqueId());

							
							if(!capturing.containsValue(regions.get(i))) {
								p.sendMessage(ChatColor.GREEN + "You no longer have control over the point!");
								Bukkit.broadcastMessage(ChatColor.GREEN + "The point " + ChatColor.RED +  regions.get(i) + ChatColor.GREEN + " is no longer being defended! "
									+ "Go capture it!");
							} else {
								p.sendMessage(ChatColor.RED + "You are no longer contesting point " + ChatColor.GREEN + regions.get(i));
							}
						}
					}
					
					if(border.contains(p.getLocation())) {
						if(!capturing.containsKey(p.getUniqueId())) {
							if(capturing.containsValue(regions.get(i))) {
								capturing.put(p.getUniqueId(), regions.get(i));
								p.sendMessage(ChatColor.RED + "The point is contested! Kill the other players to capture");
								Bukkit.broadcastMessage(ChatColor.RED + "The point " + ChatColor.GREEN + regions.get(i) + ChatColor.RED + 
										" has been contested!");
							} else {
								capturing.put(p.getUniqueId(), regions.get(i));
								p.sendMessage(ChatColor.GREEN + "Captured the point " + ChatColor.RED + regions.get(i));
								p.sendMessage(ChatColor.GREEN + "Defend this point with your life!");
								Bukkit.broadcastMessage(ChatColor.GREEN + "The point " + ChatColor.RED + regions.get(i) + ChatColor.GREEN + " has been taken by " + p.getName());
							}
						}
					}
				}
			}
	}
}

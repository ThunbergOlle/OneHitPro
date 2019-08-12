package me.bukkit.onehitpro;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class Counter {
	
	int LobbyCountDown = 10;
	static int count;
	Plugin plugin;
	public Counter(Plugin plugin) {
		this.plugin = plugin;
	}
	
	public void LobbyCountdown(List<Player> teamone, List<Player> teamtwo) {
		count = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {

			@Override
			public void run() {
				if(LobbyCountDown > 0) {
					Bukkit.getWorld(plugin.getConfig().getString("lobby")).getPlayers().forEach(player -> player.sendMessage(ChatColor.RED + "[1HP]" + ChatColor.YELLOW + " Match begins in: " + LobbyCountDown));
					LobbyCountDown--;
				}else {
					Finish(teamone, teamtwo);
					Bukkit.getScheduler().cancelTask(count);
				}
				// TODO Auto-generated method stub
				
			}
		
		}, 0L, 20L);
	}
	public void Finish(List<Player> teamone, List<Player> teamtwo) {
		System.out.println(teamone);
		Bukkit.getWorld(plugin.getConfig().getString("lobby")).getPlayers().forEach(player -> player.sendMessage(ChatColor.RED + "[1HP]" + ChatColor.BLUE + " Starting game..."));
		for(int i = 0; i < teamone.size(); i++) {
			((Entity) teamone.get(i)).teleport((Location) plugin.getConfig().get("game.1"));
		}
		for(int i = 0; i < teamtwo.size(); i++) {
			((Entity) teamtwo.get(i)).teleport((Location) plugin.getConfig().get("game.2"));
		}
		
	}
	
	public void GameCountDown(List<Player> teamone, List<Player> teamtwo) {
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {

			@Override
            public void run() {
                EndGame(teamone, teamtwo);
            }
		//2400
		}, 2400);
	}
	public void EndGame(List<Player> teamone, List<Player> teamtwo) {
		System.out.println("Game ended. Teleporting player back to lobby");
	
		for(Player p : Bukkit.getServer().getWorld(plugin.getConfig().getString("game.world")).getPlayers()){
			p.getInventory().clear();
			p.getInventory().setArmorContents(new ItemStack[4]);
			p.teleport((Location) plugin.getConfig().get("finishlocation"));
			p.sendMessage(ChatColor.RED + "[1HP]" + ChatColor.BLUE + " Game ended.");
			p.sendMessage(ChatColor.RED + "[1HP]" + ChatColor.BOLD + ChatColor.BLUE + " Final score: " + ChatColor.GREEN + Game.teamonepoints + " - " + Game.teamtwopoints);

		}
		/*
		if(teamone.size() > 0 || teamtwo.size() > 0 ) {
			for(int i = 0; i < teamone.size(); i++) {
				if(teamone.get(i).getWorld().getName().equals(plugin.getConfig().getString("game.world"))) {
					((Entity) teamone.get(i)).sendMessage(ChatColor.RED + "[1HP]" + ChatColor.BLUE + " Game ended.");
					((Entity) teamone.get(i)).teleport((Location) plugin.getConfig().get("finishlocation"));
					teamone.get(i).getInventory().clear();
					teamone.get(i).getInventory().setArmorContents(new ItemStack[4]);

				}

			}
			
			for(int i = 0; i < teamtwo.size(); i++) {
				if(teamtwo.get(i - 1).getWorld().getName().equals(plugin.getConfig().getString("game.world"))){
					teamtwo.get(i).sendMessage(ChatColor.RED + "[1HP]" + ChatColor.BLUE + " Game Ended!");
					((Entity) teamtwo.get(i)).teleport((Location) plugin.getConfig().get("finishlocation"));
					teamtwo.get(i).getInventory().clear();
					teamtwo.get(i).getInventory().setArmorContents(new ItemStack[4]);
				}

			}	
		}
		*/

		
	}
	
}

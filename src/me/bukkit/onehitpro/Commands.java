package me.bukkit.onehitpro;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;



public class Commands implements Listener, CommandExecutor{

	Plugin plugin;
	public Commands(Plugin plugin) {
		this.plugin = plugin;
	}
	
	public String cmd1 = "ohp_players";
	public String cmd2 = "ohp_setup";
	public String cmd3 = "ohp_setmaxplayers";
	public String cmd4 = "ohp_setupgame";
	public String cmd5 = "ohp_setteamspawn";
	public String cmd6 = "ohp_setupfinishlocation";
	
	private static int PlayersInLobby = 0;

	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			if(cmd.getName().equalsIgnoreCase(cmd1)) {	
					sender.sendMessage("There are " + getPlayersInLobby() + " players in the lobby");
					return false;
			}
			else if(cmd.getName().equalsIgnoreCase(cmd2)) {	
				String lobbyWorld = p.getWorld().getName();

				plugin.getConfig().set("lobby", lobbyWorld);
				plugin.saveConfig();
				
				p.sendMessage("Set "+lobbyWorld+" to a lobby world for 1hp gamemode");
				return false;
			}else if(cmd.getName().equalsIgnoreCase(cmd3)) {
				if(args[0] == null) {
					sender.sendMessage("Please provide a maxplayer value");
					return false;
				}
				int maxPlayers = Integer.parseInt(args[0]);

				plugin.getConfig().set("maxplayers", maxPlayers);
				plugin.saveConfig();
				
				sender.sendMessage("Set max players to " + maxPlayers);
				return false;
			}else if(cmd.getName().equalsIgnoreCase(cmd4)) {
				String gameWorld = p.getWorld().getName();

				plugin.getConfig().set("game.world", gameWorld);
				plugin.saveConfig();
				
				p.sendMessage("Set "+gameWorld+" to a game world for 1hp gamemode");
				return false;
			}else if(cmd.getName().equalsIgnoreCase(cmd5)) {
				if(args[0] != null) {
					int team = Integer.parseInt(args[0]);
					if(team <= 2 && team > 0) {
						plugin.getConfig().set("game."+team, p.getLocation());
						plugin.saveConfig();
						
						p.sendMessage("Set spawn point for team: " + team+ " on location: " + p.getLocation());
						return false;	
					}else {
						p.sendMessage("Only values 1 & 2 is allowed.");
					}

				} else {
					p.sendMessage("Please provide a team (1 or 2)");
				}
				
			}else if(cmd.getName().equalsIgnoreCase(cmd6)) {

				plugin.getConfig().set("finishlocation", p.getLocation());
				plugin.saveConfig();
				
				p.sendMessage("Set the finish location on 1hp gamemode");
				return false;
			}
			
		}else {
			sender.sendMessage("Only players can use the commands");
			return false;
		}
		return false;
	}
		
	public static int getPlayersInLobby() {
		return PlayersInLobby;
	}
	public static void ChangePlayersInLobby(int change) {
		PlayersInLobby += change;
	}
}

package me.bukkit.onehitpro;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class main extends JavaPlugin implements Listener{
	

	private Commands commands = new Commands(this);
	public Game game = new Game(this);
	public static Boolean activeGame = false;


	@Override
	public void onEnable() {
		getCommand(commands.cmd1).setExecutor(commands);
		getCommand(commands.cmd2).setExecutor(commands);
		getCommand(commands.cmd3).setExecutor(commands);
		getCommand(commands.cmd4).setExecutor(commands);
		getCommand(commands.cmd5).setExecutor(commands);
		getCommand(commands.cmd6).setExecutor(commands);

		getLogger().info(ChatColor.GREEN + "One hit pro");
		
		loadConfig();
		Bukkit.getServer().getPluginManager().registerEvents(this, this);
		Bukkit.getServer().getPluginManager().registerEvents(game, this);
	}
	
	@Override
	public void onDisable() {
		
	}
	//EVENT LISTENERS
	@SuppressWarnings("static-access")
	@EventHandler
	public void onPlayerChangedWorldEvent (PlayerChangedWorldEvent event) {
		Player p = (Player) event.getPlayer();
		System.out.println(p.getWorld().getName().toString().equals(getConfig().getString("lobby").toString()));
	    if(p.getWorld().getName().toString().equals(getConfig().getString("lobby").toString())) { //OM SPELAREN HAR JOINAT LOBBYN
	    	commands.ChangePlayersInLobby(1);
	    	p.getInventory().clear();
	    	if(activeGame && Bukkit.getWorld(getConfig().getString("game.world")).getPlayers().size() > 0) {
	    		
				Bukkit.getWorld(getConfig().getString("lobby")).getPlayers().forEach(player -> player.sendMessage(ChatColor.RED + "[1HP]" + ChatColor.BLUE + " Game in progress"));

	    	}else {
				Bukkit.getWorld(getConfig().getString("lobby")).getPlayers().forEach(player -> player.sendMessage(ChatColor.RED + "[1HP]" + ChatColor.BLUE + " Players [" + commands.getPlayersInLobby() + "/"  + getConfig().getInt("maxplayers") + "]"));

		    	if(commands.getPlayersInLobby() == getConfig().getInt("maxplayers")) {
			    	game.StartNewGame(p.getWorld());
			    	activeGame = true;

		    	}
		    	p.sendMessage(String.valueOf(commands.getPlayersInLobby()));
	    	}

	    }else if(event.getFrom().getName().toString().equals( getConfig().getString("lobby").toString())) { //OM SPELAREN HAR LÄMNAT LOBBYN
	    	if(commands.getPlayersInLobby() > 0) {
		    	commands.ChangePlayersInLobby(-1);
	    	}
	    } else if(event.getFrom().getName().equals(getConfig().getString("game.world"))) {
	    	if(event.getFrom().getPlayers().size() == 0) {
	    		activeGame = false;
	    		game.teamtwo = null;
	    		game.teamone = null;
	    	}else {
		    	for(int i = 0; i < game.teamone.size(); i++) {
		    		if(activeGame == true) {
			    		if(game.teamone.get(i).getName().equals(p.getName())){
			    			game.teamone.remove(i);
			    		}
		    		}

		    	}
		    	for(int i = 0; i < game.teamtwo.size(); i++) {
		    		if(activeGame == true) {
			    		if(game.teamtwo.get(i).getName().equals(p.getName())){
			    			game.teamtwo.remove(i);
			    		}
		    		}
		    	}	
	    	}


	    }
	}
	
    @EventHandler
    public void onLeave(PlayerQuitEvent e){
       
        Player p = e.getPlayer();
        if(p.getWorld().getName().toString().equals( getConfig().getString("lobby").toString())) {
	    	if(commands.getPlayersInLobby() > 0) {
		    	commands.ChangePlayersInLobby(-1);
				Bukkit.getWorld(getConfig().getString("lobby")).getPlayers().forEach(player -> player.sendMessage(ChatColor.RED + "[1HP]" + ChatColor.BLUE + " Players [" + commands.getPlayersInLobby() + "/"  + getConfig().getInt("maxplayers") + "]"));

	    	}
        }
        
    }
    

	/*
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(cmd.getName().equalsIgnoreCase("players") && sender instanceof Player) {
			System.out.println(cmd.getName());
			System.out.println(args[0]);
			if(args[0] == "setup") {
				Player p = (Player) sender;
				String lobbyWorld = p.getWorld().getName();

				getConfig().set("lobby", lobbyWorld);
				saveConfig();
				
				p.sendMessage("Set "+lobbyWorld+" to a lobby world for 1hp gamemode");
				return false;
			}else if(args[0] == "delete") {
				Player p = (Player) sender;

				getConfig().set("Lobby", null);
				saveConfig();
				
				p.sendMessage("Removed Lobby World");
				return false;
			}else if(args[0] == "setupmaxplayers") {
				int maxPlayers = Integer.parseInt(args[1]);

				getConfig().set("maxplayers", maxPlayers);
				saveConfig();
				
				sender.sendMessage("Set max players to " + maxPlayers);
				return false;
			}else if(args[0] == "players") {
				

			}
			
		}
		return false;
	}
	
	*/
	
	
	public void loadConfig() {
		getConfig().options().copyDefaults(true);	
		saveConfig();
	}
}

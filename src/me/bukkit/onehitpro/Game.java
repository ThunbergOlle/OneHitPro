package me.bukkit.onehitpro;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Game implements Listener{
	
	static List<Player> teamone = new ArrayList<Player>();
	static List<Player> teamtwo = new ArrayList<Player>();
	public static int teamonepoints = 0;
	public static int teamtwopoints = 0;
	static Plugin plugin;
	public Game(Plugin plugin) {
		this.plugin = plugin;
	}

	public static void StartNewGame(World world) {
		
		List<Player> players = world.getPlayers();
		teamone = new ArrayList<Player>();
		teamtwo = new ArrayList<Player>();
		teamonepoints = 0;
		teamtwopoints = 0;
		
		System.out.println(players);	
		for(int i = 0; i < players.size(); i+=2) {
			if(i <= players.size()) {
				teamone.add(players.get(i));
			}
		}
		for(int i = 1; i < players.size(); i+=2) {
			if(i <= players.size()) {
				teamtwo.add(players.get(i));
			}
		}
		
		System.out.println(teamone);
		System.out.println(teamtwo);
		
		for(int i = 0; i < teamone.size(); i++) {
			
			//CHESTPLATE
			ItemStack ChestPlate = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
	        LeatherArmorMeta armorMetaChestplate = (LeatherArmorMeta) ChestPlate.getItemMeta();
	        armorMetaChestplate.setColor(Color.BLUE);
	        ChestPlate.setItemMeta(armorMetaChestplate);
			teamone.get(i).getInventory().setChestplate(ChestPlate);

			//Helmet
			ItemStack Helmet = new ItemStack(Material.LEATHER_HELMET, 1);
	        LeatherArmorMeta metaHelmet = (LeatherArmorMeta) Helmet.getItemMeta();
	        metaHelmet.setColor(Color.BLUE);
	        Helmet.setItemMeta(metaHelmet);
			teamone.get(i).getInventory().setHelmet(Helmet);
			
			ItemStack Bow = new ItemStack(Material.BOW, 1);
			ItemMeta bowMeta = Bow.getItemMeta();
			bowMeta.addEnchant(Enchantment.ARROW_INFINITE, 1, false);
			bowMeta.setUnbreakable(true);
			
			Bow.setItemMeta(bowMeta);
			teamone.get(i).getInventory().addItem(Bow);
			teamone.get(i).getInventory().addItem(new ItemStack(Material.ARROW, 1));

		}

		for(int i = 0; i < teamtwo.size(); i++) {
			
			//CHESTPLATE
			ItemStack ChestPlate = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
	        LeatherArmorMeta armorMetaChestplate = (LeatherArmorMeta) ChestPlate.getItemMeta();
	        armorMetaChestplate.setColor(Color.RED);
	        ChestPlate.setItemMeta(armorMetaChestplate);
	        teamtwo.get(i).getInventory().setChestplate(ChestPlate);

			//Helmet
			ItemStack Helmet = new ItemStack(Material.LEATHER_HELMET, 1);
	        LeatherArmorMeta metaHelmet = (LeatherArmorMeta) Helmet.getItemMeta();
	        metaHelmet.setColor(Color.RED);
	        Helmet.setItemMeta(metaHelmet);
	        teamtwo.get(i).getInventory().setHelmet(Helmet);
			
			ItemStack Bow = new ItemStack(Material.BOW, 1);
			ItemMeta bowMeta = Bow.getItemMeta();
			bowMeta.addEnchant(Enchantment.ARROW_INFINITE, 1, false);
			bowMeta.setUnbreakable(true);

			Bow.setItemMeta(bowMeta);
			
			teamtwo.get(i).getInventory().addItem(Bow);
			teamtwo.get(i).getInventory().addItem(new ItemStack(Material.ARROW, 1));

		}
		Counter c = new Counter(plugin);
		c.LobbyCountdown(teamone, teamtwo);
		c.GameCountDown(teamone, teamtwo);
	}
    
	
	@EventHandler
	public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent e) {
		
		if (e.getEntity() instanceof  Player) {
			Player p = (Player) e.getEntity();
			
			if(p.getWorld().getName().equals(plugin.getConfig().getString("game.world"))) {
				for(int i = 0; i < teamone.size(); i++) {
					if(teamone.get(i) ==  p) {
						Arrow a = (Arrow) e.getDamager();
						LivingEntity killer = (LivingEntity) a.getShooter();
						
						for(int x = 0; x < teamone.size(); x++) {
							if(teamone.get(x).equals(killer)) {
								killer.sendMessage(ChatColor.RED + "You can not attack this player");
								e.setCancelled(true);
								return;
							}else if(x == teamone.size() - 1) {
								p.sendMessage(ChatColor.DARK_RED + "You died!");
								p.setHealth(20);
								p.setExhaustion(0);
								e.setCancelled(true);
								p.teleport((Location) plugin.getConfig().get("game.1"));
								p.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, 60, 2));							}
						}
						
						if(a.getShooter() instanceof Player){
							if(a.getShooter() == p) {
								e.setCancelled(true);
								return;
							}
							for(int x = 0; x < teamone.size(); x++) {
								if(p.getName().equals(teamone.get(x).getName())){
									teamonepoints++;
									((Player) killer).playSound(killer.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 2, 1);
									((Player) a.getShooter()).sendMessage(ChatColor.GREEN + "+ 1 Kill,  "+ ChatColor.BLUE + "Team 1: "+ChatColor.GREEN + teamonepoints + ChatColor.WHITE + " | "+ ChatColor.BLUE + "Team 2: " + ChatColor.RED + teamtwopoints);		
								}
							}
			

						}

						

					}
				}
				for(int i = 0; i < teamtwo.size(); i++) {
					if(teamtwo.get(i) ==  p) {
						Arrow a = (Arrow) e.getDamager();
						LivingEntity killer = (LivingEntity) a.getShooter();
						
						for(int x = 0; x < teamtwo.size(); x++) {
							if(teamtwo.get(x).equals(killer)) {
								killer.sendMessage(ChatColor.RED + "You can not attack this player");								
								e.setCancelled(true);
								return;
							}else if(x == teamtwo.size() - 1) {
								p.sendMessage(ChatColor.DARK_RED + "You died!");
								p.setHealth(20);
								p.setExhaustion(0);
								e.setCancelled(true);
								p.teleport((Location) plugin.getConfig().get("game.2"));
								p.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, 60, 2));
							}
						}
						
						if(a.getShooter() instanceof Player){
							if(a.getShooter() == p) {
								e.setCancelled(true);
								return;
							}
							
							for(int x = 0; x < teamtwo.size(); x++) {
								killer.sendMessage(killer.getName() + teamtwo.get(x).getName());
								if(p.getName().equals(teamtwo.get(x).getName())){
									teamtwopoints++;
									((Player) killer).playSound(killer.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 2, 1);
									((Player) a.getShooter()).sendMessage(ChatColor.GREEN + "+ 1 Kill,  "+ ChatColor.BLUE + "Team 1: "+ChatColor.RED + teamonepoints + ChatColor.WHITE + " | "+ ChatColor.BLUE + "Team 2: " + ChatColor.GREEN + teamtwopoints);
	
								}else if(teamtwo.size() - 1 == x) {
									killer.sendMessage(ChatColor.RED + "You can not damage this player");

								}
							}

						}

						

					}
				}
			}
		}
	}
}

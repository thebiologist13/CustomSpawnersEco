package com.github.thebiologist13;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.thebiologist13.api.IConverter;
import com.github.thebiologist13.api.ISpawner;

public class CustomSpawnersEco extends JavaPlugin {

	public static ConcurrentHashMap<Location, Spawner> toApply = new ConcurrentHashMap<Location, Spawner>();
	
	private Economy economy = null;
	
	private CustomSpawners customSpawners = null;
	
	private Logger log = Logger.getLogger("Minecraft");
	
	@Override
	public void onDisable() {
		sendMessage(log, "CustomSpawnersEco by thebiologist13 disabled!");
	}
	
	@Override
	public void onEnable() {
		
		try {
			economy = getEconomy();
		} finally {
			if(economy == null) {
				sendMessage(log, "Cannot load CustomSpawnersEco: No Vault detected for economy services.");
				getServer().getPluginManager().disablePlugin(this);
				return;
			}
		}
		
		try {
			customSpawners = getCustomSpawners();
		} finally {
			if(customSpawners == null) {
				sendMessage(log, "Cannot load CustomSpawnersEco: No CustomSpawners detected for spawner services.");
				getServer().getPluginManager().disablePlugin(this);
				return;
			}
		}
		
		getCommand("customspawnerseco").setExecutor(new EcoCommandExecutor(this));
		
		getServer().getPluginManager().registerEvents(new SpawnerPlaceEvent(this), this);
		
		getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {

			@Override
			public void run() {
				
				Iterator<Location> apply = toApply.keySet().iterator();
				while(apply.hasNext()) {
					Location l = apply.next();
					
					ISpawner spawner = CustomSpawners.getSpawnerAt(l);
					IConverter converter = CustomSpawners.getConverter();
					
					converter.convert(spawner);
					toApply.remove(l);
				}
				
			}
			
		}, 0, 1);
		
		sendMessage(log, "CustomSpawnersEco by thebiologist13 enabled!");
	}
	
	public void sendMessage(CommandSender sender, String message) {

		if(sender == null) 
			return;

		Player p = null;

		if(sender instanceof Player)
			p = (Player) sender;

		if(p == null) {
			message = "[CustomSpawnersEco] " + ChatColor.stripColor(message);
			log.info(message);
		} else {
			p.sendMessage(message);
		}

	}
	
	public void sendMessage(CommandSender sender, String[] message) {

		if(sender == null) 
			return;

		Player p = null;

		if(sender instanceof Player)
			p = (Player) sender;

		if(p == null) {

			for(String s : message) {
				s = "[CustomSpawnersEco] " + ChatColor.stripColor(s);
				log.info(s);
			}

		} else {
			p.sendMessage(message);
		}

	}
	
	public void sendMessage(Logger log, String message) {
		message = "[CustomSpawnersEco] " + ChatColor.stripColor(message);
		log.info(message);
	}
	
	public void sendMessage(Logger log, String[] message) {
		for(String s : message) {
			s = "[CustomSpawnersEco] " + ChatColor.stripColor(s);
			log.info(s);
		}
	}

	public Economy getEconomy() {
		
		if(this.economy != null)
			return this.economy;
		
		RegisteredServiceProvider<Economy> economyProvider = 
				getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
		
        if (economyProvider != null) {
            return economyProvider.getProvider();
        }

        return null;
	}
	
	public Spawner getFromLore(ItemMeta meta) {
		String name = meta.getLore().get(0);
		
		if(!name.endsWith("CustomSpawners Spawner")) {
			return null;
		}
		
		int dashIndex = name.indexOf("-");
		String id = name.substring(2, dashIndex - 1);
		
		return CustomSpawners.getSpawner(id);
	}
	
	public CustomSpawners getCustomSpawners() {
		
		if(this.customSpawners != null)
			return this.customSpawners;
		
		Plugin cs = getServer().getPluginManager().getPlugin("CustomSpawners");
		
		if(cs == null || !(cs instanceof CustomSpawners)) {
			return null;
		}
		
		return (CustomSpawners) cs;
		
	}

}

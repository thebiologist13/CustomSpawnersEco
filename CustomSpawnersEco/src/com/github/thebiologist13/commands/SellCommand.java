package com.github.thebiologist13.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

import com.github.thebiologist13.CustomSpawnersEco;
import com.github.thebiologist13.Spawner;

public class SellCommand extends EcoSubCommand {
	
	public SellCommand(CustomSpawnersEco plugin, String mainPerm) {
		super(plugin, mainPerm);
	}

	@Override
	public void run(Spawner spawner, CommandSender sender, String subCommand, String[] args) {
		
		if(!(sender instanceof Player)) {
			CSE.sendMessage(sender, NO_CONSOLE);
			return;
		}
		
		Player p = (Player) sender;
		
		if(!hasWorth(spawner)) {
			CSE.sendMessage(p, NO_VALUE);
			return;
		}
		
		double price = getWorth(spawner);
		
		String playerName = p.getName();
		
		ECO.depositPlayer(playerName, price);
		
		PlayerInventory inv = p.getInventory();
		inv.clear(inv.getHeldItemSlot());
		
		CSE.sendMessage(p, ChatColor.GREEN + "Sold a " + ChatColor.GOLD + 
				PLUGIN.getFriendlyName(spawner) + ChatColor.GREEN + " for " + 
				ChatColor.GOLD + getPriceString(spawner) + ChatColor.GREEN + ".");
		
	}
	
}

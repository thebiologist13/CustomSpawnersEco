package com.github.thebiologist13.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.github.thebiologist13.CustomSpawnersEco;
import com.github.thebiologist13.Spawner;

public class BuyCommand extends EcoSubCommand {

	public BuyCommand(CustomSpawnersEco plugin, String mainPerm) {
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
		
		String moneyName = getMoneyName();
		
		String playerName = p.getName();
		
		if(!ECO.has(playerName, price)) {
			CSE.sendMessage(p, ChatColor.RED + "You do not have enough " + 
					moneyName + " to buy that spawner.");
			return;
		}
		
		ECO.withdrawPlayer(playerName, price);
		
		ItemStack stack = new ItemStack(Material.MOB_SPAWNER);
		ItemMeta meta = stack.getItemMeta();
		
		String spawnerName = PLUGIN.getFriendlyName(spawner);
		List<String> lore = new ArrayList<String>();
		lore.add(ChatColor.AQUA + "" + spawner.getId() + " - CustomSpawners Spawner");
		
		meta.setDisplayName(ChatColor.GREEN + spawnerName + " Spawner");
		meta.setLore(lore);
		
		stack.setItemMeta(meta);
		p.getInventory().addItem(stack);
		
		CSE.sendMessage(p, ChatColor.GREEN + "Purchased new " + ChatColor.GOLD + 
				spawnerName + ChatColor.GREEN + " spawner for " + ChatColor.GOLD + 
				getPriceString(spawner) + ChatColor.GREEN + ".");
		
	}

}

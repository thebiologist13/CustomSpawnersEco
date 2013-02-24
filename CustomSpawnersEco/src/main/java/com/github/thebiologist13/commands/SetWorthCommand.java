package com.github.thebiologist13.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.CustomSpawnersEco;
import com.github.thebiologist13.Spawner;

public class SetWorthCommand extends EcoSubCommand {

	public SetWorthCommand(CustomSpawnersEco plugin, String mainPerm) {
		super(plugin, mainPerm);
	}

	@Override
	public void run(Spawner spawner, CommandSender sender, String subCommand, String[] args) {
		
		String in = getValue(args, 0, "0");
		
		if(!CustomSpawners.isDouble(in)) {
			CSE.sendMessage(sender, NOT_INT_AMOUNT);
			return;
		}
		
		double worth = Double.parseDouble(in);
		
		setWorth(spawner, worth);
		
		CSE.sendMessage(sender, ChatColor.GREEN + "Set spawner " + ChatColor.GOLD + 
				PLUGIN.getFriendlyName(spawner) + ChatColor.GREEN + " to be worth " + 
				ChatColor.GOLD + getPriceString(spawner) + ChatColor.GREEN + ".");
		
	}

}

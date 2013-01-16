package com.github.thebiologist13.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.github.thebiologist13.CustomSpawnersEco;
import com.github.thebiologist13.Spawner;

public class WorthCommand extends EcoSubCommand {

	public WorthCommand(CustomSpawnersEco plugin, String mainPerm) {
		super(plugin, mainPerm);
	}
	
	@Override
	public void run(Spawner spawner, CommandSender sender, String subCommand, String[] args) {
			
		if(!(hasWorth(spawner))) {
			CSE.sendMessage(sender, NO_VALUE);
			return;
		}

		CSE.sendMessage(sender, ChatColor.GREEN + "Spawner " + ChatColor.GOLD +
				PLUGIN.getFriendlyName(spawner) + ChatColor.GREEN + " is worth " + 
				ChatColor.GOLD + getPriceString(spawner) + ChatColor.GREEN + ".");

	}

}

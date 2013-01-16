package com.github.thebiologist13.commands;

import org.bukkit.ChatColor;

import net.milkbowl.vault.economy.Economy;

import com.github.thebiologist13.CustomSpawnersEco;
import com.github.thebiologist13.Spawner;
import com.github.thebiologist13.commands.spawners.SpawnerCommand;

/**
 * Superclass for all commands.
 * 
 * @author thebiologist13
 */
public abstract class EcoSubCommand extends SpawnerCommand {
		
	public final String NO_VALUE = ChatColor.RED + "That spawner has no value set and cannot be bought.";
	
	public final CustomSpawnersEco CSE;
	public final Economy ECO;
	
	public EcoSubCommand(CustomSpawnersEco plugin, String mainPerm) {
		super(plugin.getCustomSpawners(), mainPerm);
		super.permission = mainPerm;
		
		this.CSE = plugin;
		this.ECO = plugin.getEconomy();
	}
	
	public boolean hasWorth(Spawner s) {
		
		if(s.hasProp("worth")) {
			double worth = getWorth(s);
			if(!(worth < 0)) {
				return true;
			}
		}
		
		return false;
	}
	
	public double getWorth(Spawner s) {
		return (Double) s.getProp("worth");
	}
	
	public void setWorth(Spawner s, double worth) {
		s.setProp("worth", worth);
	}
	
	public String getMoneyName() {
		return (ECO.currencyNamePlural().isEmpty()) ? "money" : ECO.currencyNamePlural();
	}
	
	public String getPriceString(Spawner s) {
		return getWorth(s) + " " + getMoneyName();
	}
	
}

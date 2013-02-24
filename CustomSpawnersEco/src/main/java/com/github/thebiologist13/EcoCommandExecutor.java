package com.github.thebiologist13;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.thebiologist13.commands.BuyCommand;
import com.github.thebiologist13.commands.EcoSubCommand;
import com.github.thebiologist13.commands.SellCommand;
import com.github.thebiologist13.commands.SetWorthCommand;
import com.github.thebiologist13.commands.WorthCommand;

public class EcoCommandExecutor extends EconomyExecutor implements CommandExecutor {

	public EcoCommandExecutor(CustomSpawnersEco plugin) {
		super(plugin);
		
		EcoSubCommand buy = new BuyCommand(plugin, "customspawnerseco.buy");
		EcoSubCommand sell = new SellCommand(plugin, "customspawnerseco.sell");
		EcoSubCommand worth = new WorthCommand(plugin, "customspawnerseco.worth");
		EcoSubCommand setWorth = new SetWorthCommand(plugin, "customspawnerseco.setworth");
		
		sell.setNeedsObject(false);
		
		addCommand("buy", buy, new String[] {
				"purchase",
				"accquire",
				"get"
		});
		addCommand("sell", sell, new String[] {
				"giveaway",
				"market",
				"trade"
		});
		addCommand("worth", worth, new String[] {
				"price",
				"cost",
				"expense"
		});
		addCommand("setworth", setWorth, new String[] {
				"setprice",
				"setcost",
				"setexpense"
		});
	}

	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String agr2, String[] arg3) {

		if(arg1.getName().equalsIgnoreCase("customspawnerseco")) {
			
			if(arg3.length == 0) {
				String version = CSE.getDescription().getVersion();
				String[] info = 
					{
						ChatColor.GREEN + "* * * CustomSpawnersEco v" +  ChatColor.GOLD + version + ChatColor.GREEN  + " by thebiologist13 * * *",
						ChatColor.GREEN + "CustomSpawners on BukkitDev:",
						ChatColor.AQUA + "http://dev.bukkit.org/server-mods/customspawners",
						ChatColor.GREEN + "CustomSpawnersEco on BukkitDev:",
						ChatColor.AQUA + "http://dev.bukkit.org/server-mods/customspawnerseco",
						ChatColor.GREEN + "thebiologist13 on BukkitDev:",
						ChatColor.AQUA + "http://dev.bukkit.org/profiles/thebiologist13",
						ChatColor.GREEN + "* * * * * * * * * * * * * * * *"
					};
				CSE.sendMessage(arg0, info);
				return true;
			}
			
			Spawner spawnerRef = null;
			String sub = arg3[0].toLowerCase();
			String objId = "";
			String[] params;
			
			if(arg3.length > 1)
				objId = arg3[1];
			
			EcoSubCommand cmd = (EcoSubCommand) super.getCommand(sub);
			
			if(cmd == null) {
				CSE.sendMessage(arg0, ChatColor.RED + "\"" + sub + "\" is not valid for the economy command.");
				return true;
			}
			
			sub = cmd.getCommand(sub); //Aliases
			
			if(!cmd.permissible(arg0, null)) {
				CSE.sendMessage(arg0, cmd.NO_PERMISSION);
				return true;
			}
			
			if(cmd.needsObject()) {
				
				if(arg0 instanceof Player) {
					Player p = (Player) arg0;
					
					if(!CustomSpawners.spawnerSelection.containsKey(p)) {
						if(objId.startsWith("t:")) {
							spawnerRef = CustomSpawners.getSpawner(objId.substring(2));
							params = makeParams(arg3, 2);
						} else {
							spawnerRef = CustomSpawners.getSpawner(objId);
							params = makeParams(arg3, 2);
						}
					} else {
						if(objId.startsWith("t:")) { //If they want to target a specific object
							spawnerRef = CustomSpawners.getSpawner(objId.substring(2));
							params = makeParams(arg3, 2);
						} else {
							spawnerRef = CustomSpawners.getSpawner(CustomSpawners.spawnerSelection.get(p));
							params = makeParams(arg3, 1);
						}
					}
				} else {
					if(CustomSpawners.consoleEntity == -1) {
						if(objId.startsWith("t:")) {
							spawnerRef = CustomSpawners.getSpawner(objId.substring(2));
							params = makeParams(arg3, 2);
						} else {
							spawnerRef = CustomSpawners.getSpawner(objId);
							params = makeParams(arg3, 2);
						}
					} else {
						if(objId.startsWith("t:")) { //If they want to target a specific object
							spawnerRef = CustomSpawners.getSpawner(objId.substring(2));
							params = makeParams(arg3, 2);
						} else {
							spawnerRef = CustomSpawners.getSpawner(CustomSpawners.consoleSpawner);
							params = makeParams(arg3, 1);
						}
					}
				}
				
				if(spawnerRef == null) {
					CSE.sendMessage(arg0, cmd.NO_SPAWNER);
					return true;
				}
			} else {
				params = makeParams(arg3, 1);
			}
			
			try {
				cmd.run(spawnerRef, arg0, sub, params);
			} catch(ArrayIndexOutOfBoundsException e) {
				CSE.sendMessage(arg0, ChatColor.RED + "You entered invalid parameters.");
				return true;
			} catch(Exception e) {
				PLUGIN.printDebugTrace(e);
				CSE.sendMessage(arg0, ChatColor.RED + "An error has occurred. Crash report saved to " + 
						PLUGIN.getFileManager().saveCrash(cmd.getClass(), e));
				CSE.sendMessage(arg0, cmd.GENERAL_ERROR);
				return true;
			}
			
			return true;
		}
		
		return false;
		
	}

}

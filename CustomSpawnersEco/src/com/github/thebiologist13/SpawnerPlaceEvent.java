package com.github.thebiologist13;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SpawnerPlaceEvent implements Listener {

	private final CustomSpawners PLUGIN;
	private final CustomSpawnersEco CSE;
	
	public SpawnerPlaceEvent(CustomSpawnersEco plugin) {
		this.PLUGIN = plugin.getCustomSpawners();
		this.CSE = plugin;
	}
	
	@EventHandler
	public void onPlace(BlockPlaceEvent ev) {
		ItemStack stack = ev.getItemInHand();
		Block block = ev.getBlock();
		
		if(!block.getType().equals(Material.MOB_SPAWNER)) {
			return;
		}
		
		ItemMeta meta = stack.getItemMeta();
		
		if(meta.getLore().size() < 1) {
			return;
		}
		
		Spawner spawner = CSE.getFromLore(meta);
		
		if(spawner == null) {
			return;
		}
		
		Spawner spawner1 = PLUGIN.cloneWithNewId(spawner);
		spawner1.setName("");
		spawner1.setLoc(block.getLocation());
		
		PLUGIN.getFileManager().autosave(spawner1);
		
		CustomSpawnersEco.toApply.put(block.getLocation(), spawner1);
		
	}
	
}

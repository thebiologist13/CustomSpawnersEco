package com.github.thebiologist13;

import net.minecraft.server.v1_4_R1.NBTTagCompound;
import net.minecraft.server.v1_4_R1.TileEntity;
import net.minecraft.server.v1_4_R1.TileEntityMobSpawner;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_4_R1.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.github.thebiologist13.nbt.NBTManager;
import com.github.thebiologist13.nbt.NotTileEntityException;

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
		
		Player p = ev.getPlayer();
		ItemMeta meta = stack.getItemMeta();
		
		if(meta.getLore().size() < 1) {
			return;
		}
		
		Spawner spawner = CSE.getFromLore(meta);
		
		if(spawner == null) {
			return;
		}
		
		NBTManager nbt = new NBTManager();
		CraftWorld cw = (CraftWorld) block.getWorld();
		TileEntity te = cw.getTileEntityAt(block.getX(), block.getY(), block.getZ());
		
		if(!(te instanceof TileEntityMobSpawner)) {
			block.setTypeIdAndData(52, (byte) 0, true);
			te = cw.getTileEntityAt(block.getX(), block.getY(), block.getZ());
		}
		
		Spawner spawner1 = PLUGIN.cloneWithNewId(spawner);
		spawner1.setName("");
		spawner1.setLoc(block.getLocation());
		
		PLUGIN.getFileManager().autosave(spawner1);
		
		try {
			NBTTagCompound comp = nbt.getSpawnerNBT(spawner1);
			
			if(comp == null) {
				PLUGIN.printDebugMessage("Null NBT");
				return;
			}
			
			nbt.setTileEntityMobSpawnerNBT(spawner1.getLoc().getBlock(), comp);
			
		} catch (NotTileEntityException e) {
			
			PLUGIN.printDebugMessage(e.getMessage(), this.getClass());
			CSE.sendMessage(p, ChatColor.RED + "Could not find mob spawner block. Please report this to plugin author.");
			return;
		}
		
	}
	
}

package com.github.thebiologist13;

import net.minecraft.server.v1_4_6.NBTTagCompound;
import net.minecraft.server.v1_4_6.TileEntity;
import net.minecraft.server.v1_4_6.TileEntityMobSpawner;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_4_6.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

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
		Player p = ev.getPlayer();
		
		if(stack.getItemMeta() == null) {
			return;
		}
		
		String name = stack.getItemMeta().getLore().get(0);
		
		if(!name.endsWith("CustomSpawners Spawner")) {
			return;
		}
		
		int dashIndex = name.indexOf("-");
		String id = name.substring(0, dashIndex - 1);
		
		Spawner spawner = CustomSpawners.getSpawner(id);
		
		if(spawner == null) {
			PLUGIN.printDebugMessage("No spawner with ID: \"" + id + "\"");
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
		spawner1.setActive(false);
		spawner1.setName("");
		spawner1.setLoc(block.getLocation());
		CustomSpawners.spawners.put(spawner1.getId(), spawner1);
		
		try {
			NBTTagCompound comp = nbt.getSpawnerNBT(spawner1);
			
			if(comp == null) {
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

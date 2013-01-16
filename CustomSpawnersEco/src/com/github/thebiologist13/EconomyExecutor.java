package com.github.thebiologist13;

/**
 * Superclass for command executors.
 * 
 * @author thebiologist13
 */
public class EconomyExecutor extends com.github.thebiologist13.Executor {

	public final CustomSpawnersEco CSE;
	
	public EconomyExecutor(CustomSpawnersEco plugin) {
		super(plugin.getCustomSpawners());
		
		this.CSE = plugin;
	}
	
}

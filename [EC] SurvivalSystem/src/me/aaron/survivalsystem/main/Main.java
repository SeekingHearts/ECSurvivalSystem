package me.aaron.survivalsystem.main;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;

import me.aaron.survivalsystem.commands.cmdSetup;
import me.patrick.survivalsystem.commands.cmdRandomTP;

public class Main extends JavaPlugin {

	private static Main instance;
	
	private static boolean debug;
	
	public List<String> playerInSetupMode = new ArrayList<>();
	
	@Override
	public void onEnable() {
		instance = this;
		checkForWorldEdit();
		setupConfig();
		initCommands();
		
		debug = true; // TODO CHANGE
	}
	
	@Override
	public void onDisable() {
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		return true;
	}
	
	
//	----------------------------------------------
//	----------------------------------------------

	public static boolean isDebug() {
		return debug;
	}
	
	private void initCommands() {
		getCommand("setup").setExecutor(new cmdSetup());
		getCommand("rtp").setExecutor(new cmdRandomTP());
	}
	
	private void setupConfig() {
		getConfig().options().copyDefaults(true);
		saveDefaultConfig();
	}
	
	public static WorldEditPlugin getWorldEdit() {
		Plugin pl = Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
		if (pl instanceof WorldEditPlugin)
			return (WorldEditPlugin) pl;
		else 
			return null;
	}
	
	protected void checkForWorldEdit() {
		if ((getServer().getPluginManager().getPlugin("WorldEdit") != null) && !(getServer().getPluginManager().getPlugin("WorldEdit").isEnabled())) {
			getLogger().severe("WorldEdit Plugin not found! Disabling plugin...");
			getServer().getPluginManager().disablePlugin(this);
			return;
		}
	}
	
	
	public static Main getInstance() {
		return instance;
	}
}
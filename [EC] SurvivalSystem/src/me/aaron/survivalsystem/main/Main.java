package me.aaron.survivalsystem.main;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

import me.aaron.survivalsystem.commands.*;
import me.aaron.survivalsystem.listeners.SetupInventory;
import me.aaron.survivalsystem.listeners.listenerRespawn;
import me.aaron.survivalsystem.trade.TradeMain;
import net.milkbowl.vault.economy.Economy;

public class Main extends JavaPlugin {

	private static Main instance;
	private static boolean debug = false;

	private static Economy eco;
	
	public List<String> playerInSetupMode = new ArrayList<>();
	
	public Main() {
		TradeMain.tradecurramount = 0.0;
		eco = null;
	}
	
	@Override
	public void onEnable() {
		instance = this;
		checkForDependencies();
		setupConfig();
		initCommands();
		initListeners();
		
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
		getCommand("checkarray").setExecutor(new cmdCheckArrays());
	}
	
	private void initListeners() {
		PluginManager pm = getServer().getPluginManager();
		
		pm.registerEvents(new SetupInventory(), this);
		pm.registerEvents(new listenerRespawn(), this);
	}
	
	private void setupConfig() {
		getConfig().options().copyDefaults(true);
		saveDefaultConfig();
	}
	
	public static WorldGuardPlugin getWorldGuard() {
		Plugin pl = Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");
		if (pl instanceof WorldGuardPlugin)
			return (WorldGuardPlugin) pl;
		else
			return null;
	}
	
	public static WorldEditPlugin getWorldEdit() {
		Plugin pl = Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
		if (pl instanceof WorldEditPlugin)
			return (WorldEditPlugin) pl;
		else 
			return null;
	}
	
	public static Economy getEconomy() {
		return eco;
	}
	
	protected void checkForDependencies() {
		if ((getServer().getPluginManager().getPlugin("WorldEdit") != null) && !(getServer().getPluginManager().getPlugin("WorldEdit").isEnabled())) {
			getLogger().severe("WorldEdit Plugin not found! Disabling plugin...");
			disablePlugin();
		}
		if ((getServer().getPluginManager().getPlugin("WorldGuard") != null) && !(getServer().getPluginManager().getPlugin("WorldGuard").isEnabled())) {
			getLogger().severe("WorldGuard Plugin not found! Disabling plugin...");
			disablePlugin();
		}
	}
	
	protected void disablePlugin() {
		getServer().getPluginManager().disablePlugin(this);
		return;
	}
	
	public static Main getInstance() {
		return instance;
	}
}
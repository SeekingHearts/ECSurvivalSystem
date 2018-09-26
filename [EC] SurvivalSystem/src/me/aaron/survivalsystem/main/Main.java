package me.aaron.survivalsystem.main;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

import me.aaron.survivalsystem.commands.*;
import me.aaron.survivalsystem.listeners.*;
import me.aaron.survivalsystem.listeners.trade.*;
import me.aaron.survivalsystem.trade.*;
import me.vagdedes.mysql.basic.Config;
import me.vagdedes.mysql.database.MySQL;
import me.vagdedes.mysql.database.SQL;
import net.milkbowl.vault.economy.Economy;

public class Main extends JavaPlugin {

	private static Main instance;
	private static boolean debug = false;

	private static TradeUtils tradeUtils;
	private static Economy eco;
	private static MySQL sql;
	
	public List<String> playerInSetupMode = new ArrayList<>();
	
	public Main() {
		TradeMain.tradecurramount = 0.0;
		eco = null;
	}
	
	@Override
	public void onEnable() {
		instance = this;
		tradeUtils = new TradeUtils(new TradeMain());
		sql = new MySQL();
		checkForDependencies();
		setupConfig();
		setupMySQL();
		initCommands();
		initListeners();
		
		debug = true; // TODO CHANGE
	}
	
	@Override
	public void onDisable() {
		for (final Trade tr : TradeUtils.getAllTrades()) {
			tr.cancelTrade(true);
		}
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		return true;
	}

//	----------------------------------------------
//			!!!  Main part done  !!!
//	----------------------------------------------
	
	public static boolean isDebug() {
		return debug;
	}
	
	private void initCommands() {
		getCommand("setup").setExecutor(new cmdSetup());
		getCommand("rtp").setExecutor(new cmdRandomTP());
		getCommand("checkarray").setExecutor(new cmdCheckArrays());
		getCommand("checkareas").setExecutor(new cmdInArea());
	}
	
	private void initListeners() {
		PluginManager pm = getServer().getPluginManager();
		
//		----------------------------------------------------------------
//		General
//		----------------------------------------------------------------
		pm.registerEvents(new SetupInventory(), this);
		pm.registerEvents(new listenerRespawn(), this);
		
//		----------------------------------------------------------------
//		Trading
//		----------------------------------------------------------------
		pm.registerEvents(new listenerEntityDamage(), this);
		pm.registerEvents(new listenerGameModeChange(), this);
		pm.registerEvents(new listenerInventoryClick(), this);
		pm.registerEvents(new listenerInventoryClose(), this);
		pm.registerEvents(new listenerInventoryDrag(), this);
		pm.registerEvents(new listenerPlayerInteract(), this);
	}
	
	private void setupMySQL() {
		String host = getConfig().getString("config.mysql.host");
		String port = getConfig().getString("config.mysql.port");
		String username = getConfig().getString("config.mysql.username");
		String password = getConfig().getString("config.mysql.password");
		String db = getConfig().getString("config.mysql.database");
		
		Config.setHost(host);
		Config.setPort(port);
		Config.setUser(username);
		Config.setPassword(password);
		Config.setDatabase(db);
		Config.create();
		
		MySQL.connect();
		if (MySQL.isConnected()) {
			if (!SQL.tableExists("citysystem")) {
				SQL.createTable("citysystem", "id INT (30) PRIMARY, creator TEXT, name TEXT, size TEXT, creationdate TIMESTAMP");
			}
		}
	}
	
	private void setupConfig() {
		getConfig().options().copyDefaults(true);
		saveDefaultConfig();
		
		TradeMain.tradecurr = this.getConfig().getBoolean("TradeCurrency");
		TradeMain.tradecurramount = this.getConfig().getDouble("TradeCurrencyAmount");
		if (TradeMain.tradecurramount <= 0.0)
			TradeMain.tradecurr = false;
		if (TradeMain.tradecurr)
			setupCurrency();
	}
	
	private void setupCurrency() {
		if (getServer().getPluginManager().getPlugin("Vault") != null) {
			final RegisteredServiceProvider<Economy> ecoProv = (RegisteredServiceProvider<Economy>) getServer().getServicesManager().getRegistration(Economy.class);
			if (ecoProv != null) {
				eco = ecoProv.getProvider();
				getLogger().info("Vault und Economy wurden gefunden!");
			} else
				TradeMain.tradecurr = false;
		} else
			TradeMain.tradecurr = false;
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
	
	public static MySQL getMySQL() {
		return sql;
	}
	public static Main getInstance() {
		return instance;
	}
}
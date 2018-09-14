package me.aaron.survivalsystem.inventories;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import me.aaron.survivalsystem.main.Main;
import me.aaron.survivalsystem.utils.ItemUtils;

public class Invs {
	
	public static void openTradeMenu(Player p) {
		Inventory trade = Bukkit.createInventory(null, 3 * 9, ChatColor.LIGHT_PURPLE + "TRADING " + ChatColor.AQUA + " MENU");
		ItemStack ph = ItemUtils.getItem(Material.WHITE_STAINED_GLASS_PANE, ChatColor.BLACK + "Platzhalter", "", 0, 1);
		
		for (int i = 0; i < 6; i++) {
			Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new BukkitRunnable() {
				
				@Override
				public void run() {
					 
					p.playSound(p.getLocation(), Sound.ENTITY_CHICKEN_EGG, 1, 1);
				}
			}, 2);
		}
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new BukkitRunnable() {

			@Override
			public void run() {
				trade.setItem(12, ItemUtils.getItem(Material.GRAY_DYE, ChatColor.GRAY + "Tausch akzeptieren", "", 0, 1));
				p.playSound(p.getLocation(), Sound.ENTITY_CHICKEN_EGG, 1, 1);
				
				Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new BukkitRunnable() {
					
					@Override
					public void run() {
						trade.setItem(12, ItemUtils.getItem(Material.GRAY_DYE, ChatColor.GRAY + "Tausch akzeptieren", "", 0, 1));
						p.playSound(p.getLocation(), Sound.ENTITY_CHICKEN_EGG, 1, 1);
					}
				}, 2);
			}
		}, 2);
	}
}
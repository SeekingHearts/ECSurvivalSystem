package me.aaron.survivalsystem.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.aaron.survivalsystem.main.Main;

public class PlayerUtils {

	private static Map<UUID, ItemStack[]> invs = new HashMap<>();
	private static Map<UUID, ItemStack[]> arminvs = new HashMap<>();
	
	public static Map<UUID, ItemStack[]> saveInventory(Player p) {
		try {
			invs.put(p.getUniqueId(), p.getInventory().getContents());
			arminvs.put(p.getUniqueId(), p.getInventory().getArmorContents());
			
			if (Main.isDebug()) {
				for (UUID itm : invs.keySet()) {
					Bukkit.broadcastMessage(invs.get(itm).toString());
				}
			}
		} catch (Exception e) {
			return null;
		}
		return invs;
	}
	
	public static void restoreInventory(boolean armor, Player p) {
		try {
			if (armor) {
				if (Main.isDebug()) {
					Bukkit.broadcastMessage(arminvs.get(p.getUniqueId()).toString());
				}
				
				p.getInventory().setArmorContents(arminvs.get(p.getUniqueId()));
			} else {
				if (Main.isDebug()) {
					Bukkit.broadcastMessage(invs.get(p.getUniqueId()).toString());
				}
				
				p.getInventory().setContents(invs.get(p.getUniqueId()));
			}
		} catch (Exception e) {
		}
	}
	
	public static boolean isInSetupMode(Player p) {
		return Main.getInstance().playerInSetupMode.contains(p) ? true : false;
	}
}
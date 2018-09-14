package me.aaron.survivalsystem.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.aaron.survivalsystem.main.Main;
import me.aaron.survivalsystem.utils.ItemUtils;

public class SetupInventory implements Listener {
	
	ItemStack compass = ItemUtils.getItem(Material.COMPASS, "§cCompass", "", 0, 1);
	ItemStack setSpawn = ItemUtils.getItem(Material.BEACON, "§aSetSpawn", ChatColor.GRAY+"Setze den Spawn Punkt", 0, 1);
	ItemStack setSpawnArea = ItemUtils.getItem(Material.COMPASS, "§aSetSpawnArea", ChatColor.GRAY+"Setze die Spawngegend", 0, 1);
	ItemStack setNoPVPZone = ItemUtils.getItem(Material.GREEN_WOOL, "§aSetNoPVPZone", ChatColor.GRAY+"Setze PVP freie Zone", 0, 1);
	//ItemStack  = ItemUtils.getItem(mat, name, lore, dmg, amount);
	
	@EventHandler
	public void onCompass(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		
		if (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			if (p.getItemInHand().getType() == Material.COMPASS) {
				if (Main.getInstance().playerInSetupMode.contains(p)) {
					Inventory setupInventory = Bukkit.createInventory(null, 3, "§cSetup Items");
					p.playSound(p.getLocation(), Sound.ENTITY_CHICKEN_EGG, 1, 1);
					
					setupInventory.addItem(setSpawn);
					setupInventory.addItem(setSpawnArea);
					setupInventory.addItem(setNoPVPZone);
				}
			}
		}
		
	}
	
}

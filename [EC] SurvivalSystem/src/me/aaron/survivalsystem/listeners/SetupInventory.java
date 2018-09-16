package me.aaron.survivalsystem.listeners;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.aaron.survivalsystem.main.Main;
import me.aaron.survivalsystem.utils.ItemUtils;

public class SetupInventory implements Listener {

	public static ItemStack compass = ItemUtils.getItem(Material.COMPASS, "§aSetup Inventar", null, 0, 1);
	ItemStack setSpawn = ItemUtils.getItem(Material.BEACON, "§aSetSpawn",
			Arrays.asList(ChatColor.GRAY + "Setze den Spawn Punkt"), 0, 1);
	ItemStack setSpawnArea = ItemUtils.getItem(Material.COMPASS, "§aSet Spawn Area",
			Arrays.asList(ChatColor.GRAY + "Setze die Spawngegend"), 0, 1);
	ItemStack setNoPVPZone = ItemUtils.getItem(Material.GREEN_WOOL, "§aSet No PVP Zone",
			Arrays.asList(ChatColor.GRAY + "Setze PVP freie Zone"), 0, 1);
	// ItemStack = ItemUtils.getItem(mat, name, lore, dmg, amount);

	@EventHandler
	public void onCompass(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if (!Main.getInstance().playerInSetupMode.contains(p.getName()))
			return;

		if (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			if (p.getItemInHand().getType() == Material.COMPASS && e.getItem().getItemMeta().getDisplayName().equals("§aSetup Inventar")) {
				

				Inventory setupInventory = Bukkit.createInventory(null, 9, "§cSetup Items");
				p.playSound(p.getLocation(), Sound.ENTITY_CHICKEN_EGG, 1, 1);

				setupInventory.addItem(setSpawn);
				setupInventory.addItem(setSpawnArea);
				setupInventory.addItem(setNoPVPZone);

				p.openInventory(setupInventory);
				
			}
		}
	}

	@EventHandler
	public void onCompassClick(final InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		
		if (!Main.getInstance().playerInSetupMode.contains(p.getName()))
			return;
		
		if (e.getInventory().getName().equals("§cSetup Items") && e.getInventory().getSize() == 9) {
			if (e.getSlot() == 0 && e.getCurrentItem().getItemMeta().getDisplayName().equals("§aSetSpawn")) {
				Main.getInstance().getConfig().set("location.spawn", p.getLocation());
				p.sendMessage(ChatColor.YELLOW + "Neuer Spawnpoint gesetzt!");
			} else if (e.getCurrentItem() == setSpawnArea) {	
				p.sendMessage("Tool setSpawnArea");
				//fehlt
			} else if (e.getCurrentItem() == setNoPVPZone) {
				p.sendMessage("Tool setNoPVPZone");
				//fehlt
			}
			e.setCancelled(true);
		}
	}
}
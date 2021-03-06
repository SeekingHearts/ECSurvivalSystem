package me.aaron.survivalsystem.listeners;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldguard.domains.DefaultDomain;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag.State;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;

import me.aaron.survivalsystem.main.Main;
import me.aaron.survivalsystem.utils.ItemUtils;
import me.aaron.survivalsystem.utils.WGUtils;

public class SetupInventory implements Listener {

	public static ItemStack compass = ItemUtils.getItem(Material.COMPASS, "�aSetup Inventar", null, 0, 1);
	ItemStack setSpawn = ItemUtils.getItem(Material.BEACON, "�aSetze Spawn",
			Arrays.asList(ChatColor.GRAY + "Setze den Spawn Punkt"), 0, 1);
	ItemStack setSpawnArea = ItemUtils.getItem(Material.COMPASS, "�aSetze Spawn Region",
			Arrays.asList(ChatColor.GRAY + "Setze die Spawngegend"), 0, 1);
	ItemStack setNoPVPZone = ItemUtils.getItem(Material.GREEN_WOOL, "�aSetze PVP-freie Zone",
			Arrays.asList(ChatColor.GRAY + "Setze PVP freie Zone, PVE erlaubt"), 0, 1);
	ItemStack setSaveZone = ItemUtils.getItem(Material.EMERALD_BLOCK, "�aSetze eine sichere Zone",
			Arrays.asList(ChatColor.GRAY + "Setze eine Zone, in welcher man kein Schaden bekommen kann"), 0, 1);
	ItemStack setprotectedZone = ItemUtils.getItem(Material.DIAMOND_BLOCK, "�aSetze eine gesch�tzte Zone",
			Arrays.asList(ChatColor.GRAY + "Setze eine Zone, in der man nicht abbauen und kein PVP erlaubt ist"), 0, 1);
	ItemStack setprotectedPVPZone = ItemUtils.getItem(Material.DIAMOND_BLOCK, "�aSetze eine gesch�tzte PVP Zone",
			Arrays.asList(ChatColor.GRAY + "Setze eine Zone, in der man nicht abbauen kann, aber PVP erlaubt ist"), 0,
			1);
	ItemStack setFarmZone = ItemUtils.getItem(Material.BIRCH_WOOD, "�aSetze eine farm Zone",
			Arrays.asList(ChatColor.GRAY + "Setze eine Zone, in der man nicht abbauen kann, aber PVP erlaubt ist"), 0,
			1);

	@EventHandler
	public void onCompass(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if (!Main.getInstance().playerInSetupMode.contains(p.getName()))
			return;

		if (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			if (p.getItemInHand().getType() == Material.COMPASS
					&& e.getItem().getItemMeta().getDisplayName().equals("�aSetup Inventar")) {

				Inventory setupInventory = Bukkit.createInventory(null, 9, "�cSetup Items");
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
		try {
			if (e.getInventory().getName().equals("�cSetup Items") && e.getInventory().getSize() == 9) {
				if (e.getSlot() == 0) {
					Main.getInstance().getConfig().set("locations.spawn.world", p.getLocation().getWorld().getName());
					Main.getInstance().getConfig().set("locations.spawn.x", p.getLocation().getX());
					Main.getInstance().getConfig().set("locations.spawn.y", p.getLocation().getY());
					Main.getInstance().getConfig().set("locations.spawn.z", p.getLocation().getZ());
					Main.getInstance().getConfig().set("locations.spawn.yaw", p.getLocation().getYaw());
					Main.getInstance().getConfig().set("locations.spawn.pitch", p.getLocation().getPitch());

					Main.getInstance().saveConfig();
					p.sendMessage(ChatColor.YELLOW + "Neuer Spawnpoint gesetzt!");
				} else if (e.getSlot() == 1) {
					EditSession es = Main.getWorldEdit().createEditSession(p);
					World w = new BukkitWorld(p.getWorld());
					if (WGUtils.getRegionManager(w).hasRegion("spawnarea-" + p.getWorld())) {
						p.sendMessage(ChatColor.AQUA + "L�sche alte Spawn Region in dieser Welt...");
						WGUtils.getRegionManager(w).removeRegion("spawnarea-" + p.getWorld());
					}

					org.bukkit.World sw = Bukkit.getServer()
							.getWorld(Main.getInstance().getConfig().getString("locations.spawn.world"));
					double sx = Main.getInstance().getConfig().getDouble("locations.spawn.x");
					double sy = Main.getInstance().getConfig().getDouble("locations.spawn.y");
					double sz = Main.getInstance().getConfig().getDouble("locations.spawn.z");
					float syaw = (float) Main.getInstance().getConfig().getDouble("locations.spawn.yaw");
					float spitch = (float) Main.getInstance().getConfig().getDouble("locations.spawn.pitch");

					// ProtectedCuboidRegion rg = new ProtectedCuboidRegion("spawnarea-" +
					// w.getName(),
					// sel.getMinimumPoint().toBlockVector(),
					// sel.getMaximumPoint().toBlockVector());
					ProtectedCuboidRegion rg = new ProtectedCuboidRegion("spawnarea-" + w.getName(),
							new BlockVector(sx + 200, w.getMaxY() + 1, sy + 200),
							new BlockVector(sx - 200, -(w.getMaxY() + 1), sy - 200));
					DefaultDomain dd = new DefaultDomain();
					DefaultDomain dm = new DefaultDomain();
					for (Player ap : Bukkit.getServer().getOnlinePlayers()) {
						if (ap.hasPermission("survivalsystem.regions.spawn")) {
							dd.addPlayer(Main.getWorldGuard().wrapPlayer(ap));
						} else {
							dm.addPlayer(Main.getWorldGuard().wrapPlayer(ap));
						}
					}
					dd.addPlayer(Main.getWorldGuard().wrapPlayer(p));
					rg.setOwners(dd);
					rg.setMembers(dm);
					rg.setFlag(Flags.PVP, State.DENY);
					rg.setFlag(Flags.BLOCK_BREAK, State.DENY);
					rg.setFlag(Flags.BLOCK_PLACE, State.DENY);
					rg.setFlag(Flags.BUILD, State.DENY);
					rg.setFlag(Flags.CHEST_ACCESS, State.DENY);
					rg.setFlag(Flags.ITEM_DROP, State.DENY);
					rg.setFlag(Flags.CHORUS_TELEPORT, State.DENY);
					rg.setFlag(Flags.CREEPER_EXPLOSION, State.DENY);
					rg.setFlag(Flags.DAMAGE_ANIMALS, State.DENY);
					rg.setFlag(Flags.DESTROY_VEHICLE, State.DENY);
					rg.setFlag(Flags.ENDERPEARL, State.DENY);
					rg.setFlag(Flags.WITHER_DAMAGE, State.DENY);
					rg.setFlag(Flags.MOB_DAMAGE, State.DENY);
					rg.setFlag(Flags.GRASS_SPREAD, State.DENY);
					rg.setFlag(Flags.ICE_MELT, State.DENY);
					rg.setFlag(Flags.ITEM_PICKUP, State.DENY);
					rg.setFlag(Flags.LAVA_FIRE, State.DENY);
					rg.setFlag(Flags.LAVA_FLOW, State.DENY);
					rg.setFlag(Flags.WATER_FLOW, State.DENY);
					rg.setFlag(Flags.LIGHTNING, State.DENY);
					rg.setFlag(Flags.FIRE_SPREAD, State.DENY);
					rg.setFlag(Flags.MOB_SPAWNING, State.DENY);
					rg.setFlag(Flags.MYCELIUM_SPREAD, State.DENY);
					rg.setFlag(Flags.VINE_GROWTH, State.DENY);
					rg.setFlag(Flags.TNT, State.DENY);
					rg.setFlag(Flags.SNOW_MELT, State.DENY);
					rg.setFlag(Flags.SNOW_FALL, State.DENY);
					rg.setFlag(Flags.FALL_DAMAGE, State.DENY);

					WGUtils.getRegionManager(w).addRegion(rg);
					p.sendMessage(ChatColor.GREEN + "Die Spawn Region �e'�lspawnarea-" + w.getName() + ChatColor.RESET
							+ ChatColor.GREEN + "' wurde �berfolgreich " + ChatColor.GREEN + "erstellt!");
				}
			} else if (e.getCurrentItem() == setNoPVPZone) {
				World w = new BukkitWorld(
						Bukkit.getServer().getWorld(Main.getInstance().getConfig().getString("locations.spawn.world")));
				double sx = Main.getInstance().getConfig().getDouble("locations.spawn.x");
				double sy = Main.getInstance().getConfig().getDouble("locations.spawn.y");
				double sz = Main.getInstance().getConfig().getDouble("locations.spawn.z");
				float syaw = (float) Main.getInstance().getConfig().getDouble("locations.spawn.yaw");
				float spitch = (float) Main.getInstance().getConfig().getDouble("locations.spawn.pitch");
				ProtectedCuboidRegion rg = new ProtectedCuboidRegion("spawnarea-" + w.getName(),
						new BlockVector(sx + 200, w.getMaxY() + 1, sy + 200),
						new BlockVector(sx - 200, -(w.getMaxY() + 1), sy - 200));
				DefaultDomain dd = new DefaultDomain();
				DefaultDomain dm = new DefaultDomain();
				for (Player ap : Bukkit.getServer().getOnlinePlayers()) {
					if (ap.hasPermission("survivalsystem.regions.spawn")) {
						dd.addPlayer(Main.getWorldGuard().wrapPlayer(ap));
					} else {
						dm.addPlayer(Main.getWorldGuard().wrapPlayer(ap));
					}
					rg.setOwners(dd);
					rg.setMembers(dm);
					rg.setFlag(Flags.PVP, State.DENY);
					rg.setFlag(Flags.TNT, State.DENY);
					rg.setFlag(Flags.CREEPER_EXPLOSION, State.DENY);
				}
				p.sendMessage(ChatColor.GREEN + "PVP sichere Zone gesetzt!");
			} else if (e.getCurrentItem() == setSaveZone) {
				World w = new BukkitWorld(
						Bukkit.getServer().getWorld(Main.getInstance().getConfig().getString("locations.spawn.world")));
				double sx = Main.getInstance().getConfig().getDouble("locations.spawn.x");
				double sy = Main.getInstance().getConfig().getDouble("locations.spawn.y");
				double sz = Main.getInstance().getConfig().getDouble("locations.spawn.z");
				float syaw = (float) Main.getInstance().getConfig().getDouble("locations.spawn.yaw");
				float spitch = (float) Main.getInstance().getConfig().getDouble("locations.spawn.pitch");
				ProtectedCuboidRegion rg = new ProtectedCuboidRegion("spawnarea-" + w.getName(),
						new BlockVector(sx + 200, w.getMaxY() + 1, sy + 200),
						new BlockVector(sx - 200, -(w.getMaxY() + 1), sy - 200));
				DefaultDomain dd = new DefaultDomain();
				DefaultDomain dm = new DefaultDomain();
				for (Player ap : Bukkit.getServer().getOnlinePlayers()) {
					if (ap.hasPermission("survivalsystem.regions.spawn")) {
						dd.addPlayer(Main.getWorldGuard().wrapPlayer(ap));
					} else {
						dm.addPlayer(Main.getWorldGuard().wrapPlayer(ap));
					}
					dd.addPlayer(Main.getWorldGuard().wrapPlayer(p));
					rg.setOwners(dd);
					rg.setMembers(dm);
					rg.setFlag(Flags.PVP, State.DENY);
					rg.setFlag(Flags.ITEM_DROP, State.DENY);
					rg.setFlag(Flags.CREEPER_EXPLOSION, State.DENY);
					rg.setFlag(Flags.DAMAGE_ANIMALS, State.DENY);
					rg.setFlag(Flags.DESTROY_VEHICLE, State.DENY);
					rg.setFlag(Flags.FALL_DAMAGE, State.DENY);
					rg.setFlag(Flags.WITHER_DAMAGE, State.DENY);
					rg.setFlag(Flags.MOB_DAMAGE, State.DENY);
					rg.setFlag(Flags.ICE_MELT, State.DENY);
					rg.setFlag(Flags.ITEM_PICKUP, State.DENY);
					rg.setFlag(Flags.LAVA_FIRE, State.DENY);
					rg.setFlag(Flags.LIGHTNING, State.DENY);
					rg.setFlag(Flags.MOB_SPAWNING, State.DENY);
					rg.setFlag(Flags.MYCELIUM_SPREAD, State.DENY);
					rg.setFlag(Flags.TNT, State.DENY);
					rg.setFlag(Flags.FALL_DAMAGE, State.DENY);
				}
				p.sendMessage(ChatColor.GREEN + "sichere Zone gesetzt!");
			} else if (e.getCurrentItem() == setprotectedZone) {
				World w = new BukkitWorld(
						Bukkit.getServer().getWorld(Main.getInstance().getConfig().getString("locations.spawn.world")));
				double sx = Main.getInstance().getConfig().getDouble("locations.spawn.x");
				double sy = Main.getInstance().getConfig().getDouble("locations.spawn.y");
				double sz = Main.getInstance().getConfig().getDouble("locations.spawn.z");
				float syaw = (float) Main.getInstance().getConfig().getDouble("locations.spawn.yaw");
				float spitch = (float) Main.getInstance().getConfig().getDouble("locations.spawn.pitch");
				ProtectedCuboidRegion rg = new ProtectedCuboidRegion("spawnarea-" + w.getName(),
						new BlockVector(sx + 200, w.getMaxY() + 1, sy + 200),
						new BlockVector(sx - 200, -(w.getMaxY() + 1), sy - 200));
				DefaultDomain dd = new DefaultDomain();
				DefaultDomain dm = new DefaultDomain();
				for (Player ap : Bukkit.getServer().getOnlinePlayers()) {
					if (ap.hasPermission("survivalsystem.regions.spawn")) {
						dd.addPlayer(Main.getWorldGuard().wrapPlayer(ap));
					} else {
						dm.addPlayer(Main.getWorldGuard().wrapPlayer(ap));
					}
					rg.setOwners(dd);
					rg.setMembers(dm);
					rg.setFlag(Flags.PVP, State.DENY);
					rg.setFlag(Flags.BLOCK_BREAK, State.DENY);
					rg.setFlag(Flags.BLOCK_PLACE, State.DENY);
					rg.setFlag(Flags.BUILD, State.DENY);
					rg.setFlag(Flags.ITEM_DROP, State.DENY);
					rg.setFlag(Flags.CREEPER_EXPLOSION, State.DENY);
					rg.setFlag(Flags.DAMAGE_ANIMALS, State.DENY);
					rg.setFlag(Flags.DESTROY_VEHICLE, State.DENY);
					rg.setFlag(Flags.ENDERPEARL, State.DENY);
					rg.setFlag(Flags.WITHER_DAMAGE, State.DENY);
					rg.setFlag(Flags.MOB_DAMAGE, State.DENY);
					rg.setFlag(Flags.GRASS_SPREAD, State.DENY);
					rg.setFlag(Flags.ICE_MELT, State.DENY);
					rg.setFlag(Flags.LAVA_FIRE, State.DENY);
					rg.setFlag(Flags.FIRE_SPREAD, State.DENY);
					rg.setFlag(Flags.MOB_SPAWNING, State.DENY);
					rg.setFlag(Flags.MYCELIUM_SPREAD, State.DENY);
					rg.setFlag(Flags.VINE_GROWTH, State.DENY);
					rg.setFlag(Flags.TNT, State.DENY);
					rg.setFlag(Flags.SNOW_MELT, State.DENY);
					rg.setFlag(Flags.FALL_DAMAGE, State.DENY);
				}
				p.sendMessage(ChatColor.GREEN + "gesicherte Zone gesetzt!");

			} else if (e.getCurrentItem() == setprotectedPVPZone)

			{
				World w = new BukkitWorld(
						Bukkit.getServer().getWorld(Main.getInstance().getConfig().getString("locations.spawn.world")));
				double sx = Main.getInstance().getConfig().getDouble("locations.spawn.x");
				double sy = Main.getInstance().getConfig().getDouble("locations.spawn.y");
				double sz = Main.getInstance().getConfig().getDouble("locations.spawn.z");
				float syaw = (float) Main.getInstance().getConfig().getDouble("locations.spawn.yaw");
				float spitch = (float) Main.getInstance().getConfig().getDouble("locations.spawn.pitch");
				ProtectedCuboidRegion rg = new ProtectedCuboidRegion("spawnarea-" + w.getName(),
						new BlockVector(sx + 200, w.getMaxY() + 1, sy + 200),
						new BlockVector(sx - 200, -(w.getMaxY() + 1), sy - 200));
				DefaultDomain dd = new DefaultDomain();
				DefaultDomain dm = new DefaultDomain();
				for (Player ap : Bukkit.getServer().getOnlinePlayers()) {
					if (ap.hasPermission("survivalsystem.regions.spawn")) {
						dd.addPlayer(Main.getWorldGuard().wrapPlayer(ap));
					} else {
						dm.addPlayer(Main.getWorldGuard().wrapPlayer(ap));
					}
					rg.setOwners(dd);
					rg.setMembers(dm);
					rg.setFlag(Flags.PVP, State.ALLOW);
					rg.setFlag(Flags.BLOCK_BREAK, State.DENY);
					rg.setFlag(Flags.BLOCK_PLACE, State.DENY);
					rg.setFlag(Flags.BUILD, State.DENY);
					rg.setFlag(Flags.ITEM_DROP, State.ALLOW);
					rg.setFlag(Flags.CREEPER_EXPLOSION, State.DENY);
					rg.setFlag(Flags.DAMAGE_ANIMALS, State.ALLOW);
					rg.setFlag(Flags.DESTROY_VEHICLE, State.DENY);
					rg.setFlag(Flags.ENDERPEARL, State.ALLOW);
					rg.setFlag(Flags.WITHER_DAMAGE, State.DENY);
					rg.setFlag(Flags.MOB_DAMAGE, State.ALLOW);
					rg.setFlag(Flags.GRASS_SPREAD, State.DENY);
					rg.setFlag(Flags.ICE_MELT, State.DENY);
					rg.setFlag(Flags.LAVA_FIRE, State.ALLOW);
					rg.setFlag(Flags.FIRE_SPREAD, State.DENY);
					rg.setFlag(Flags.MOB_SPAWNING, State.DENY);
					rg.setFlag(Flags.MYCELIUM_SPREAD, State.DENY);
					rg.setFlag(Flags.VINE_GROWTH, State.DENY);
					rg.setFlag(Flags.SNOW_MELT, State.DENY);
					rg.setFlag(Flags.FALL_DAMAGE, State.ALLOW);
				}
				p.sendMessage(ChatColor.GREEN + "gesch�tzte PVP Zone gesetzt!");
			} else if (e.getCurrentItem() == setFarmZone) {
				World w = new BukkitWorld(
						Bukkit.getServer().getWorld(Main.getInstance().getConfig().getString("locations.spawn.world")));
				double sx = Main.getInstance().getConfig().getDouble("locations.spawn.x");
				double sy = Main.getInstance().getConfig().getDouble("locations.spawn.y");
				double sz = Main.getInstance().getConfig().getDouble("locations.spawn.z");
				float syaw = (float) Main.getInstance().getConfig().getDouble("locations.spawn.yaw");
				float spitch = (float) Main.getInstance().getConfig().getDouble("locations.spawn.pitch");
				ProtectedCuboidRegion rg = new ProtectedCuboidRegion("spawnarea-" + w.getName(),
						new BlockVector(sx + 200, w.getMaxY() + 1, sy + 200),
						new BlockVector(sx - 200, -(w.getMaxY() + 1), sy - 200));
				DefaultDomain dd = new DefaultDomain();
				DefaultDomain dm = new DefaultDomain();
				for (Player ap : Bukkit.getServer().getOnlinePlayers()) {
					if (ap.hasPermission("survivalsystem.regions.spawn")) {
						dd.addPlayer(Main.getWorldGuard().wrapPlayer(ap));
					} else {
						dm.addPlayer(Main.getWorldGuard().wrapPlayer(ap));
					}
					dd.addPlayer(Main.getWorldGuard().wrapPlayer(p));
					rg.setOwners(dd);
					rg.setMembers(dm);
					rg.setFlag(Flags.PVP, State.DENY);
					rg.setFlag(Flags.ITEM_DROP, State.DENY);
					rg.setFlag(Flags.CREEPER_EXPLOSION, State.DENY);
					rg.setFlag(Flags.DAMAGE_ANIMALS, State.DENY);
					rg.setFlag(Flags.DESTROY_VEHICLE, State.DENY);
					rg.setFlag(Flags.FALL_DAMAGE, State.DENY);
					rg.setFlag(Flags.WITHER_DAMAGE, State.DENY);
					rg.setFlag(Flags.MOB_DAMAGE, State.DENY);
					rg.setFlag(Flags.ICE_MELT, State.DENY);
					rg.setFlag(Flags.ITEM_PICKUP, State.DENY);
					rg.setFlag(Flags.LAVA_FIRE, State.DENY);
					rg.setFlag(Flags.LIGHTNING, State.DENY);
					rg.setFlag(Flags.MOB_SPAWNING, State.DENY);
					rg.setFlag(Flags.MYCELIUM_SPREAD, State.DENY);
					rg.setFlag(Flags.TNT, State.DENY);
					rg.setFlag(Flags.FALL_DAMAGE, State.DENY);
				}
				p.sendMessage(ChatColor.GREEN + "farm Zone gesetzt!");
			}
			e.setCancelled(true);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
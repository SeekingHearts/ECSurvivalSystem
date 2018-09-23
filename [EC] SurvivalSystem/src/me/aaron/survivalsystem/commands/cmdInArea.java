package me.aaron.survivalsystem.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import me.aaron.survivalsystem.utils.WGUtils;

public class cmdInArea implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

			for (Player p : Bukkit.getServer().getOnlinePlayers()) {
				World w = new BukkitWorld(p.getWorld());
				BlockVector bv = WGUtils.convertToSk89qBV(p.getLocation());
				if (args[0].equalsIgnoreCase("spawnarea")) {
					for (ProtectedRegion rg : WGUtils.getRegionManager(w).getApplicableRegions(bv)) {
						if (rg.getId().equalsIgnoreCase("spawnarea-" + w.getName())) {
							Bukkit.broadcastMessage(p.getName() + " ist in der Spawnarea");
						}
					}
				}
			}
		return true;
	}
}
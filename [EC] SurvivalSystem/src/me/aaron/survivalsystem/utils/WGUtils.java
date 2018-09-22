package me.aaron.survivalsystem.utils;

import org.bukkit.Location;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.RegionContainer;

public class WGUtils {

	public static BlockVector convertToSk89qBV(Location loc) {
		return new BlockVector(loc.getX(), loc.getY(), loc.getZ());
	}
	
	public static RegionManager getRegionManager(World world) {
		RegionContainer rc = WorldGuard.getInstance().getPlatform().getRegionContainer();
		return rc.get(world);
	}
}
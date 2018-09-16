package me.aaron.survivalsystem.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import me.aaron.survivalsystem.main.Main;

public class listenerRespawn implements Listener {

	@EventHandler
	public void onRespawn(final PlayerRespawnEvent e) {
		Player p = e.getPlayer();
		
		World world = Bukkit.getServer().getWorld(Main.getInstance().getConfig().getString("locations.spawn.world"));
		double x = Main.getInstance().getConfig().getDouble("locations.spawn.x");
		double y = Main.getInstance().getConfig().getDouble("locations.spawn.y");
		double z = Main.getInstance().getConfig().getDouble("locations.spawn.z");
		float yaw = Main.getInstance().getConfig().getInt("locations.spawn.yaw");
		float pitch = Main.getInstance().getConfig().getInt("locations.spawn.pitch");
		Location loc = new Location(world, x, y, z, yaw, pitch);
		if (e.getPlayer().getWorld() == loc.getWorld())
			e.setRespawnLocation(loc);
	}
}
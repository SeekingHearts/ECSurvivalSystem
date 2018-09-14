package me.aaron.survivalsystem.commands;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class cmdRandomTP implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if (sender instanceof Player) {
				
		Player p = (Player)sender;
		Random rand = new Random();
		int ranX = rand.nextInt(501)+101;
		int ranZ = rand.nextInt(501)+101;
		Location ploc = p.getLocation();
		Location loc = new Location(p.getWorld(), ploc.getX() + ranX, p.getWorld().getHighestBlockAt(new Location(p.getWorld(), ploc.getX() + ranX, ploc.getY(), ploc.getZ() + ranZ)).getY(), ploc.getZ() + ranZ);
		
		p.teleport(loc);
		
		} else 
			sender.sendMessage("Command only for players avaiable!");
		return true;

			
	}
	
	
	
}

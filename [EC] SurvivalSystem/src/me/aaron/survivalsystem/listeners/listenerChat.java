package me.aaron.survivalsystem.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;

import me.aaron.survivalsystem.main.Main;
import me.aaron.survivalsystem.utils.PlayerUtils;

public class listenerChat implements Listener {

//	SETUP MODE
	
	public static void onChat(PlayerChatEvent e) {
		Player p = e.getPlayer();
		String msg = e.getMessage();
		
		if (PlayerUtils.isInSetupMode(p)) {
			if (msg.startsWith("#")) {
				e.setCancelled(true);
				String[] args = msg.split(" ");
				args[0] = args[0].substring(1, args[0].length() - 1);
				
				if (args[0].equalsIgnoreCase("exit")) {
					Main.getInstance().playerInSetupMode.remove(p);
					p.sendMessage(ChatColor.YELLOW + "Der Setup Mode wurde §cbeendet§e!");
				}
			}
		}
	}
}
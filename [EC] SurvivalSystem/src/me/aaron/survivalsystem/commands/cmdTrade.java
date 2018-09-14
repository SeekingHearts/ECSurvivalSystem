package me.aaron.survivalsystem.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.aaron.survivalsystem.main.Main;

public class cmdTrade implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		try {
			if (sender instanceof Player) {
				Player p = (Player) sender;

				if (!Main.getInstance().playerInSetupMode.contains(p.getName())) {
					
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	protected static void sendSetupHelp(Player p) {
		p.sendMessage("§4§l|| §r§cHier die Commands: §4§l||");
		p.sendMessage("");
		p.sendMessage("§e'§cexit§e' §4-- §aVerlasse den Setup Modus");
	}
}
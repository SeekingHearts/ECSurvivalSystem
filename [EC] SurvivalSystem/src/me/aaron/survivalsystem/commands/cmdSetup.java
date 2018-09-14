package me.aaron.survivalsystem.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.aaron.survivalsystem.main.Main;
import me.aaron.survivalsystem.utils.PlayerUtils;

public class cmdSetup implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		try {
			if (sender instanceof Player) {
				Player p = (Player) sender;

				if (p.hasPermission("survivalsystem.setup")) {
					if (Main.getInstance().playerInSetupMode.contains(p.getName())) {

						p.getInventory().clear();
						PlayerUtils.restoreInventory(false, p);
						PlayerUtils.restoreInventory(true, p);
						
						Main.getInstance().playerInSetupMode.remove(p.getName());
						p.sendMessage(ChatColor.RED + "Du bist nun " + ChatColor.YELLOW + "nicht" + ChatColor.RED
								+ " mehr im Setup-Mode!.");
					} else {

						PlayerUtils.saveInventory(p);
						p.getInventory().clear();
						
						Main.getInstance().playerInSetupMode.add(p.getName());
						p.sendMessage(ChatColor.GREEN
								+ "Du bist nun im Setup-Mode! §eBenutze die Items um das Survival-System einzurichten.");
					}
				} else {
					p.sendMessage(ChatColor.RED + "Du hast keine Rechte dafür!");
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
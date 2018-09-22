package me.aaron.survivalsystem.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import me.aaron.survivalsystem.main.Main;

public class cmdCheckArrays implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (args.length != 1)
			return true;
		
		if (sender.getName().equals("TheSaltyOne") || sender.getName().equals("NighthuntShadow")) {
			String array = args[0];
			try {
				
				if (array.equalsIgnoreCase("inSetupMode")) {
					for (String playername : Main.getInstance().playerInSetupMode) {
						sender.sendMessage(playername);
					}
				}
				
			} catch (Exception e) {
				sender.sendMessage("Das Array gibt es nicht!");
			}
		}
		return true;
	}
}
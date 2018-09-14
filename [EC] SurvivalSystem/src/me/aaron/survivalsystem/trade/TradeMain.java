package me.aaron.survivalsystem.trade;

import java.util.ArrayList;
import java.util.List;

import me.aaron.survivalsystem.main.Main;
import net.md_5.bungee.api.ChatColor;

public class TradeMain {

	static Main pl;
	
	public static boolean tradecurr;
	public static double tradecurramount;

	public static String getMessage(final String type) {
		String msg = "";
		msg = pl.getConfig().getString("Message." + type);
		msg = ChatColor.translateAlternateColorCodes('&', msg);
		return msg;
	}
	
	public static String getItemName(final String type) {
        String name = "";
        name = pl.getConfig().getString("Item-Names." + type);
        name = ChatColor.translateAlternateColorCodes('&', name);
        return name;
    }
	
	public static List<String> getItemLore(final String type) {
        final List<String> lore = new ArrayList<String>();
        for (final String s : pl.getConfig().getStringList("Item-Lores." + type)) {
            lore.add(ChatColor.translateAlternateColorCodes('&', s));
        }
        return lore;
    }
}
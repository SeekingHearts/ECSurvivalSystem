package me.aaron.survivalsystem.trade;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class TradeUtils {

	private TradeMain tm;
	
	private List<Trade> trList;
	
	public TradeUtils(final TradeMain tm) {
		this.trList = new ArrayList<>();
		this.tm = tm;
	}

	public void removeTrade(final Trade t) {
		final List<Trade> trades = new ArrayList<>();
		for (final Trade tr : trList) {
			if (!tr.getRequestor())
		}
	}
	
	public static Inventory setItemsLeft(final Inventory inv, final ItemStack[] items) {
		final List<ItemStack> itms = new ArrayList<>();
		for (final ItemStack itm : items) {
			itms.add(itm);
		}
		int s = 0;
		while (itms.size() > 0) {
			if (s != 3 && s != 12 && s != 21) {
				inv.setItem(s, itms.get(0));
				itms.remove(itms.get(0));
				++s;
			} else {
				inv.setItem(s, itms.get(0));
				itms.remove(itms.get(0));
				s += 6;
			}
		}
		return inv;
	}
	
	public static Inventory setItemsRight(final Inventory inv, final ItemStack[] items) {
		final List<ItemStack> itms = new ArrayList<>();
		for (final ItemStack itm : items) {
			itms.add(itm);
		}
		int s = 5;
		while (itms.size() > 0) {
			if (s != 8 && s != 17 && s != 26) {
				inv.setItem(s, itms.get(0));
				itms.remove(itms.get(0));
				++s;
			} else {
				inv.setItem(s, itms.get(0));
				itms.remove(itms.get(0));
				s += 6;
			}
		}
		return inv;
	}
	
	public List<Trade> getAllTrades() {
		return trList;
	}
}
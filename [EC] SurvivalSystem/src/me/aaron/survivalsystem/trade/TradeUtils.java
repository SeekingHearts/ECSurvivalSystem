package me.aaron.survivalsystem.trade;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class TradeUtils {

	private TradeMain tm;
	
	private static List<Trade> trList;
	
	public TradeUtils(final TradeMain tm) {
		this.trList = new ArrayList<>();
		this.tm = tm;
	}

	public static void removeTrade(final Trade t) {
		final List<Trade> trades = new ArrayList<>();
		for (final Trade tr : trList) {
			if (tr.getRequester() != t.getRequester() || tr.getAccepter() != tr.getAccepter()) {
				trades.add(tr);
			}
		}
		trList = trades;
	}
	
	public void addTrade(final Trade t) {
		if (!t.isCancelled()) {
			removeTrade(t);
			trList.add(t);
		}
	}
	
	public Trade getTradeFromRequester(final Player req) {
		Trade t = null;
		for (final Trade tr : trList) {
			if (tr.getRequester().getName().equalsIgnoreCase(req.getName()))
				t = tr;
		}
		return t;
	}
	
	public Trade getTradeFromAccepter(final Player acc) {
		Trade t = null;
		for (final Trade tr : trList) {
			if (tr.getAccepter().getName().equalsIgnoreCase(acc.getName()))
				t = tr;
		}
		return t;
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
	
	public ItemStack[] getItemsRequester(final Player p) {
		final List<ItemStack> itms = new ArrayList<>();
		return null;
	}
	
	public List<Trade> getAllTrades() {
		return trList;
	}
}
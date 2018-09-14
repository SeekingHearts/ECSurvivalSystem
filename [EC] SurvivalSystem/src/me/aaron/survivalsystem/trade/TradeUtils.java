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
	
	public Inventory setItemsLeft(final Inventory inv, final ItemStack[] items) {
		final List<ItemStack> itms = new ArrayList<>();
		for (final ItemStack itm : items) {
			itms.add(itm);
		}
		int 
	}
	
	public List<Trade> getAllTrades() {
		return trList;
	}
}
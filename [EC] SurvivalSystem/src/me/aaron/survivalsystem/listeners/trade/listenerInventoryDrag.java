package me.aaron.survivalsystem.listeners.trade;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.scheduler.BukkitRunnable;

import me.aaron.survivalsystem.main.Main;
import me.aaron.survivalsystem.trade.Trade;
import me.aaron.survivalsystem.trade.TradeUtils;

public class listenerInventoryDrag implements Listener {

	@EventHandler
	public void onInventoryDrag(final InventoryDragEvent e) {
		if (e.getWhoClicked() instanceof Player) {
			final Player p = (Player) e.getWhoClicked();
			Trade tr = null;
			if (TradeUtils.getTradeFromAccepter(p) != null)
				tr = TradeUtils.getTradeFromAccepter(p);
			if (TradeUtils.getTradeFromRequester(p) != null)
				tr = TradeUtils.getTradeFromRequester(p);
			if (tr != null && tr.hasTradeWindowOpen(p)) {
				for (final int s : e.getRawSlots()) {
					if (Trade.getInstance().canPlaceItem(p, s) && !Trade.getInstance().isCountdownInProgress()) {
						final Trade finalTrade = tr;
						new BukkitRunnable() {
							
							@Override
							public void run() {
								finalTrade.updateTradeItems();
								finalTrade.updateOpenTrade();
								TradeUtils.addTrade(finalTrade);
							}
						}.runTaskLater(Main.getInstance(), 0L);
					} else
						e.setCancelled(true);
				}
			}
		}
	}
}
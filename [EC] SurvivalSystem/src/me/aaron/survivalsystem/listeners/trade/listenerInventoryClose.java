package me.aaron.survivalsystem.listeners.trade;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import me.aaron.survivalsystem.trade.Trade;
import me.aaron.survivalsystem.trade.TradeUtils;

public class listenerInventoryClose implements Listener {

	@EventHandler
	public void onInventoryClose(final InventoryCloseEvent e) {
		if (e.getPlayer() instanceof Player) {
			final Player p = (Player) e.getPlayer();
			Trade tr = null;
			if (TradeUtils.getTradeFromAccepter(p) != null)
				tr = TradeUtils.getTradeFromAccepter(p);
			if (TradeUtils.getTradeFromRequester(p) != null)
				tr = TradeUtils.getTradeFromRequester(p);
			if (tr != null && tr.hasTradeWindowOpen(p) && !Trade.getInstance().isCancelled()) {
				final ItemStack cur = e.getView().getCursor();
				e.getView().setCursor(null);
				p.getInventory().addItem(new ItemStack[] { cur });
				tr.cancelTrade(true);
			}
			tr = null;
		}
	}
}
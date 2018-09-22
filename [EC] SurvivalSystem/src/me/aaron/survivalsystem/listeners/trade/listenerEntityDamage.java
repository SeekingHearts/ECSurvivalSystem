package me.aaron.survivalsystem.listeners.trade;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.scheduler.BukkitRunnable;

import me.aaron.survivalsystem.main.Main;
import me.aaron.survivalsystem.trade.Trade;
import me.aaron.survivalsystem.trade.TradeUtils;

public class listenerEntityDamage implements Listener {

	@EventHandler
	public void onEntityDamage(final EntityDamageEvent e) {
		if (e.getEntity() instanceof Player && e.getDamage() > 0.0 && !e.isCancelled()) {
			final Player p = (Player) e.getEntity();
			final double startHealth = p.getHealth();
			Main.getInstance().getServer().getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new BukkitRunnable() {
				
				@Override
				public void run() {
					if (p.getHealth() != startHealth) {
						Trade tr = null;
						if (TradeUtils.getTradeFromAccepter(p) != null)
							tr = TradeUtils.getTradeFromAccepter(p);
						if (TradeUtils.getTradeFromRequester(p) != null)
							tr = TradeUtils.getTradeFromRequester(p);
						if (tr != null)
							tr.cancelTrade(true);
						tr = null;
					}
				}
			}, 0L);
		}
	}
}
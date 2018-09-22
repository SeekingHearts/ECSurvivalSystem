package me.aaron.survivalsystem.listeners.trade;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import me.aaron.survivalsystem.main.Main;
import me.aaron.survivalsystem.trade.Trade;
import me.aaron.survivalsystem.trade.TradeMain;
import me.aaron.survivalsystem.trade.TradeUtils;

public class listenerPlayerInteract implements Listener {

	@EventHandler
	public void onPlayerInteractEntity(final PlayerInteractEntityEvent e) {
		if (e.getPlayer() instanceof Player) {
			final Player p = e.getPlayer();
			if (e.getRightClicked() instanceof Player) {
				final Player target = (Player) e.getRightClicked();
				if (Main.getInstance().getConfig().getBoolean("ShiftRightClick") && p.isSneaking()) {
					Trade tr = null;
					boolean accepterB = false;
					if (TradeUtils.getTradeFromAccepter(p) != null) {
						tr = TradeUtils.getTradeFromAccepter(p);
						accepterB = true;
					}
					if (TradeUtils.getTradeFromRequester(p) != null)
						tr = TradeUtils.getTradeFromRequester(p);

					if (tr == null) {
						if (!target.getName().equalsIgnoreCase(p.getName())) {
							if (p.getGameMode() != GameMode.CREATIVE
									|| Main.getInstance().getConfig().getBoolean("TradeInCreative")) {
								if (p.getWorld().getName().equalsIgnoreCase(target.getWorld().getName())
										|| Main.getInstance().getConfig().getDouble("Distance") < 0.0) {
									if ((p.getWorld().getName().equalsIgnoreCase(target.getWorld().getName())
											&& p.getLocation().distance(target.getLocation()) <= Main.getInstance()
													.getConfig().getDouble("Distance"))
											|| Main.getInstance().getConfig().getDouble("Distance") < 0.0) {
										if (TradeUtils.getTradeFromAccepter(p) == null
												&& TradeUtils.getTradeFromRequester(p) == null) {
											if (TradeUtils.getTradeFromAccepter(target) == null
													&& TradeUtils.getTradeFromRequester(target) == null) {
												tr = new Trade(Main.getInstance(), p, target);
												TradeUtils.addTrade(tr);
												tr = null;
											} else
												p.sendMessage(TradeMain.getMessage("error-accepter-different-trade").replaceAll("%accepter%", target.getName()));
										} else
											p.sendMessage(TradeMain.getMessage("error-requester-different-trade"));
									} else
										p.sendMessage(TradeMain.getMessage("error-distance"));
								} else
									p.sendMessage(TradeMain.getMessage("error-world"));
							} else
								p.sendMessage(TradeMain.getMessage("error-creative"));
						} else
							p.sendMessage(TradeMain.getMessage("error-self"));
					} else if (accepterB) {
						if (p.getGameMode() != GameMode.CREATIVE || Main.getInstance().getConfig().getBoolean("TradeInCreative")) {
							if (tr.getRequester().getGameMode() != GameMode.CREATIVE || Main.getInstance().getConfig().getBoolean("TradeInCreative")) {
								tr.setTradeAccepted(true);
								TradeUtils.addTrade(tr);
								tr = null;
							} else {
								tr.cancelTrade(true);
								p.sendMessage(TradeMain.getMessage("trade-cancelled-creative-reason"));
								tr.getRequester().sendMessage(TradeMain.getMessage("trade-cancelled-creative-reason"));
							}
						} else
							p.sendMessage(TradeMain.getMessage("error-creative"));
					} else if (!target.getName().equalsIgnoreCase(p.getName())) {
						if (p.getGameMode() != GameMode.CREATIVE || Main.getInstance().getConfig().getBoolean("TradeInCreative")) {
							if (p.getWorld().getName().equalsIgnoreCase(target.getWorld().getName()) || Main.getInstance().getConfig().getDouble("Distance") < 0.0) {
								if ((p.getWorld().getName().equalsIgnoreCase(target.getWorld().getName()) && p.getLocation().distance(target.getLocation()) <= Main.getInstance().getConfig().getDouble("Distance")) || Main.getInstance().getConfig().getDouble("Distance") < 0.0) {
									if (TradeUtils.getTradeFromAccepter(p) == null && TradeUtils.getTradeFromRequester(p) == null) {
										if (TradeUtils.getTradeFromAccepter(target) == null && TradeUtils.getTradeFromRequester(target) == null) {
											tr = new Trade(Main.getInstance(), p, target);
											TradeUtils.addTrade(tr);
											tr = null;
										} else
											p.sendMessage(TradeMain.getMessage("error-accepter-different-trade").replaceAll("%accepter%", target.getName()));
									} else
										p.sendMessage(TradeMain.getMessage("error-requester-different-trade"));
								} else
									p.sendMessage(TradeMain.getMessage("error-distance"));
							} else
								p.sendMessage(TradeMain.getMessage("error-world"));
						} else
							p.sendMessage(TradeMain.getMessage("error-creative"));
					} else
						p.sendMessage(TradeMain.getMessage("error-self"));
				}
			}
		}
	}
}
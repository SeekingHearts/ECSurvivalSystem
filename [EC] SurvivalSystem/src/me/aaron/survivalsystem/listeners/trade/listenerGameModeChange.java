package me.aaron.survivalsystem.listeners.trade;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;

import me.aaron.survivalsystem.main.Main;
import me.aaron.survivalsystem.trade.Trade;
import me.aaron.survivalsystem.trade.TradeMain;
import me.aaron.survivalsystem.trade.TradeUtils;

public class listenerGameModeChange implements Listener {

	@EventHandler
	public void onPlayerGameModeChange(final PlayerGameModeChangeEvent e) {
		final Player p = e.getPlayer();
		if (e.getNewGameMode() == GameMode.CREATIVE && !Main.getInstance().getConfig().getBoolean("TradeInCreative")) {
			Trade tr = null;
			if (TradeUtils.getTradeFromAccepter(p) != null)
				tr = TradeUtils.getTradeFromAccepter(p);
			if (TradeUtils.getTradeFromRequester(p) != null)
				tr = TradeUtils.getTradeFromRequester(p);
			if (tr != null) {
				tr.cancelTrade(true);
				tr.getAccepter().sendMessage(TradeMain.getMessage("trade-cancelled-creative-reason"));
				tr.getRequester().sendMessage(TradeMain.getMessage("trade-cancelled-creative-reason"));
			}
		}
	}
}
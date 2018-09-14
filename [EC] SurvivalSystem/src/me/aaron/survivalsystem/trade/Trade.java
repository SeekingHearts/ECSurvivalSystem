package me.aaron.survivalsystem.trade;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.aaron.survivalsystem.main.Main;
import me.aaron.survivalsystem.utils.ItemUtils;

public class Trade {

	Main pl;
	Player trReq;
	Player trAcc;
	Inventory reqInv;
	Inventory accInv;
	ItemStack[] reqTradeReqItm;
	ItemStack[] accTradeReqItm;
	boolean cdInProgress;
	boolean trAccepted;
	boolean reqRdy;
	boolean accRdy;
	boolean trRdy;
	boolean cancelled;

	public double currReq;
	public double currAcc;
	int cd;

	public Trade(final Main pl, final Player trReq, final Player trAcc) {
		this.pl = pl;

		this.trReq = null;
		this.trAcc = null;
		this.reqInv = null;
		this.accInv = null;
		this.reqTradeReqItm = null;
		this.accTradeReqItm = null;
		this.cdInProgress = false;
		this.trAccepted = false;
		this.reqRdy = false;
		this.accRdy = false;
		this.trRdy = false;
		this.cancelled = false;

		this.currReq = 0.0;
		this.currAcc = 0.0;
		this.cd = -1;

		this.trReq = trReq;
		this.trAcc = trAcc;
		trReq.sendMessage(TradeMain.getMessage("trade-request-send").replaceAll("%accepter%", trAcc.getName()));
		trAcc.sendMessage(TradeMain.getMessage("trade-request-received").replaceAll("%requester%", trReq.getName()));
//		TODO
	}

	public void openTrade() {
		if (!this.isCancelled()) {
			if (this.trReq.isOnline() && this.trAcc.isOnline() && this.trReq.getWorld().equals(this.trAcc.getWorld())
					&& (this.trReq.getLocation().distance(this.trAcc.getLocation()) <= this.pl.getConfig()
							.getDouble("Distance") || this.pl.getConfig().getDouble("Distance") < 0.0)) {
				final String invName = TradeMain.getItemName("inventory");
				int invSize = 36;
				if (TradeMain.tracecurr)
					invSize = 45;

				final Inventory inv = Bukkit.createInventory(null, invSize, invName);
				final ItemStack div = ItemUtils.getItem(Material.IRON_BARS, TradeMain.getItemName("divider"), null, 0,
						1);
				final ItemStack decTr = ItemUtils.getItem(Material.RED_WOOL, TradeMain.getItemName("decline-trade"),
						TradeMain.getItemLore("decline-trade"), 0, 1);
				final ItemStack accTr = ItemUtils.getItem(Material.GREEN_WOOL, TradeMain.getItemName("accept-trade"),
						TradeMain.getItemLore("accept-trade"), 0, 1);
				final ItemStack rdyOrNot = ItemUtils.getItem(Material.INK_SAC, TradeMain.getItemName("not-ready"), null,
						8, 1);

				inv.setItem(4, div);
				inv.setItem(13, div);
				inv.setItem(22, div);
				inv.setItem(27, accTr);
				inv.setItem(28, decTr);
				inv.setItem(30, rdyOrNot);
				inv.setItem(31, div);
				inv.setItem(35, rdyOrNot);
				if (TradeMain.tracecurr) {
					final ItemStack addCurr = ItemUtils.getItem(Material.GOLD_INGOT,
							TradeMain.getItemName("add-currenncy").replaceAll("%currencyamount%",
									new StringBuilder(String.valueOf(TradeMain.tradecurramount)).toString()),
							TradeMain.getItemLore("add-currency"), 0, 1);
					final ItemStack remCurr = ItemUtils.getItem(Material.GOLD_INGOT,
							TradeMain.getItemName("remove-currenncy").replaceAll("%currencyamount%",
									new StringBuilder(String.valueOf(TradeMain.tradecurramount)).toString()),
							TradeMain.getItemLore("remove-currency"), 0, 1);
					inv.setItem(38, addCurr);
					inv.setItem(39, remCurr);
					inv.setItem(40, div);
				}
				(this.reqInv = Bukkit.createInventory(null, invSize, invName)).setContents(inv.getContents());
				(this.accInv = Bukkit.createInventory(null, invSize, invName)).setContents(inv.getContents());
				if (TradeMain.tracecurr) {
					final ItemStack totalCurrR = ItemUtils.getItem(Material.GOLD_BLOCK,
							TradeMain.getItemName("total_currency").replaceAll("%currencyamount%",
									new StringBuilder(String.valueOf(currReq)).toString()),
							null, 0, 1);
					final ItemStack totalCurrA = ItemUtils.getItem(Material.GOLD_BLOCK,
							TradeMain.getItemName("total_currency").replaceAll("%currencyamount%",
									new StringBuilder(String.valueOf(currAcc)).toString()),
							null, 0, 1);
					final ItemStack unlit = ItemUtils.getItem(Material.TORCH,
							ChatColor.GOLD + "Erloschene Fackel der Erleuchtung", null, 0, 1);
					final ItemStack lit = ItemUtils.getItem(Material.REDSTONE_TORCH, "Fackel der Erleuchtung", null, 0,
							1);
					if (this.currReq > 0.0) {
						this.reqInv.setItem(37, lit);
						this.accInv.setItem(42, lit);
					} else {
						this.reqInv.setItem(37, unlit);
						this.accInv.setItem(42, unlit);
					}
					if (this.currAcc > 0.0) {
						this.accInv.setItem(37, lit);
						this.reqInv.setItem(42, lit);
					} else {
						this.accInv.setItem(37, unlit);
						this.reqInv.setItem(42, unlit);
					}
				}
			}
		}
	}

	public Player getTradeRequestor() {
		return trReq;
	}

	public Player getTradeAcceptor() {
		return trAcc;
	}

	public boolean isCancelled() {
		return cancelled;
	}
}

package me.aaron.survivalsystem.trade;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

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
							ChatColor.GOLD + "Fackel der Erleuchtung", null, 0, 1);
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
					this.reqInv.setItem(36, totalCurrR);
					this.reqInv.setItem(41, totalCurrA);
					this.accInv.setItem(36, totalCurrA);
					this.accInv.setItem(41, totalCurrR);
				}
				this.trReq.openInventory(this.reqInv);
				this.trAcc.openInventory(this.accInv);
			} else {
				this.cancelTrade(true);
			}
		}
	}
	
	public void closeTrade() {
		final ItemStack[] bl = new ItemStack[0];
		this.currAcc = 0.0;
		this.currReq = 0.0;
		if (this.trReq.isOnline() && this.trReq.getOpenInventory() != null && this.trAcc.getOpenInventory().getTopInventory() != null) {
			final ItemStack cursor = this.trReq.getOpenInventory().getCursor();
			this.trReq.getOpenInventory().setCursor(null);
			this.trReq.getInventory().addItem(new ItemStack[] {cursor});
			this.trReq.closeInventory();
			this.trReq.updateInventory();
		}
		if (this.trAcc.isOnline() && this.trAcc.getOpenInventory() != null && this.trAcc.getOpenInventory().getTopInventory() != null) {
			final ItemStack cursor = this.trReq.getOpenInventory().getCursor();
			this.trReq.getOpenInventory().setCursor(null);
			this.trReq.getInventory().addItem(new ItemStack[] {cursor});
			this.trReq.closeInventory();
			this.trReq.updateInventory();
		}
	}
	
	public void startTimeOutCounter() {
		new BukkitRunnable() {
			int secs = 0;
			
			@Override
			public void run() {
				if (!isCancelled()) {
					if (secs < 15) {
						if (!Trade.this.trReq.isOnline() || !Trade.this.trAcc.isOnline() || (!Trade.this.trReq.getWorld().getName().equalsIgnoreCase(Trade.this.trAcc.getWorld().getName()) && pl.getConfig().getDouble("Distance") >= 0.0) || (trReq.getWorld().getName().equalsIgnoreCase(trAcc.getWorld().getName()) && trReq.getLocation().distance(trAcc.getLocation()) > pl.getConfig().getDouble("Distance") && pl.getConfig().getDouble("Distance") >= 0.0)) {
							cancelTrade(true);
							this.cancel();
						}
						if (isTradeAccepted()) {
							this.cancel();
						}
					} else {
						cancelTrade(true);
						this.cancel();
					}
					++secs;
				} else {
					this.cancel();
				}
			}
		}.runTaskTimer(pl, 20L, 20L);
	}
	
	public void startReadyCounter() {
		cdInProgress = true;
		cd = pl.getConfig().getInt("CountdownLength");
		new BukkitRunnable() {
			int secs = 0;
			
			@Override
			public void run() {
				if (!isCancelled()) {
					
				}
			}
		};
	}
	
	public void updateOpenTrade() {
        if (!this.isCancelled() && this.trReq.getOpenInventory() != null && this.trAcc.getOpenInventory() != null) {
            if (this.trReq.isOnline() && this.trAcc.isOnline() && ((this.trReq.getWorld().getName().equals(this.trAcc.getWorld().getName()) && this.trReq.getLocation().distance(this.trAcc.getLocation()) <= TradeMain.getConfig().getDouble("Distance")) || TradeMain.getConfig().getDouble("Distance") < 0.0)) {
                final String invName = TradeMain.getItemName("inventory");
                int invsize = 36;
                if (TradeMain.tradecurr) {
                    invsize = 45;
                }
                final Inventory inv = Bukkit.createInventory(null, invsize, invName);
                int dividercd = 1;
                if (this.cd > 0) {
                    dividercd = this.cd;
                }
                final ItemStack divider = ItemUtils.getItem(Material.IRON_BARS, TradeMain.getItemName("divider"), null, dividercd, 1);
                final ItemStack declineTrade = ItemUtils.getItem(Material.RED_WOOL, TradeMain.getItemName("decline-trade"), TradeMain.getItemLore("decline-trade"), 14, 1);
                final ItemStack acceptTrade = ItemUtils.getItem(Material.GREEN_WOOL, TradeMain.getItemName("accept-trade"), TradeMain.getItemLore("accept-trade"), 5, 1);
                final ItemStack ready = ItemUtils.getItem(Material.INK_SAC, TradeMain.getItemName("ready"), null, 10, 1);
                final ItemStack notReady = ItemUtils.getItem(Material.INK_SAC, TradeMain.getItemName("not-ready"), null, 8, 1);
                inv.setItem(4, divider);
                inv.setItem(13, divider);
                inv.setItem(22, divider);
                inv.setItem(31, divider);
                inv.setItem(27, acceptTrade);
                inv.setItem(28, declineTrade);
                inv.setItem(30, notReady);
                inv.setItem(35, notReady);
                if (TradeMain.tradecurr) {
                    final ItemStack addCurrency = ItemUtils.getItem(Material.GOLD_INGOT, TradeMain.getItemName("add-currency").replaceAll("%currencyamount%", new StringBuilder(String.valueOf(TradeMain.tradecurramount)).toString()), TradeMain.getItemLore("add-currency"), 0, 1);
                    final ItemStack removeCurrency = ItemUtils.getItem(Material.GOLD_INGOT, TradeMain.getItemName("remove-currency").replaceAll("%currencyamount%", new StringBuilder(String.valueOf(TradeMain.tradecurramount)).toString()), TradeMain.getItemLore("remove-currency"), 0, 1);
                    inv.setItem(38, addCurrency);
                    inv.setItem(39, removeCurrency);
                    inv.setItem(40, divider);
                }
                Inventory requesterInventory = Bukkit.createInventory(null, invsize, invName);
                requesterInventory.setContents(inv.getContents());
                requesterInventory = TradeMain.tradeUtil.setItemsLeft(requesterInventory, this.getRequesterTradeRequestItems());
                requesterInventory = TradeMain.tradeUtil.setItemsRight(requesterInventory, this.getAccepterTradeRequestItems());
                Inventory accepterInventory = Bukkit.createInventory(null, invsize, invName);
                accepterInventory.setContents(inv.getContents());
                accepterInventory = TradeMain.tradeUtil.setItemsLeft(accepterInventory, this.getAccepterTradeRequestItems());
                accepterInventory = TradeMain.tradeUtil.setItemsRight(accepterInventory, this.getRequesterTradeRequestItems());
                if (TradeMain.tradecurr) {
                    final ItemStack totalCurrencyR = ItemUtils.getItem(Material.GOLD_BLOCK, TradeMain.getItemName("total-currency").replaceAll("%currencyamount%", new StringBuilder(String.valueOf(this.currReq)).toString()), null, 0, 1);
                    final ItemStack totalCurrencyA = ItemUtils.getItem(Material.GOLD_BLOCK, TradeMain.getItemName("total-currency").replaceAll("%currencyamount%", new StringBuilder(String.valueOf(this.currAcc)).toString()), null, 0, 1);
                    final ItemStack unlit = ItemUtils.getItem(Material.TORCH, "Fackel der Erleuchtung", null, 0, 1);
                    final ItemStack lit = ItemUtils.getItem(Material.REDSTONE_TORCH, " ", null, 0, 1);
                    if (this.currReq > 0.0) {
                        requesterInventory.setItem(37, lit);
                        accepterInventory.setItem(42, lit);
                    }
                    else {
                        requesterInventory.setItem(37, unlit);
                        accepterInventory.setItem(42, unlit);
                    }
                    if (this.currAcc > 0.0) {
                        accepterInventory.setItem(37, lit);
                        requesterInventory.setItem(42, lit);
                    }
                    else {
                        accepterInventory.setItem(37, unlit);
                        requesterInventory.setItem(42, unlit);
                    }
                    requesterInventory.setItem(36, totalCurrencyR);
                    requesterInventory.setItem(41, totalCurrencyA);
                    accepterInventory.setItem(36, totalCurrencyA);
                    accepterInventory.setItem(41, totalCurrencyR);
                }
                if (this.isRequesterReady()) {
                    requesterInventory.setItem(30, ready);
                    accepterInventory.setItem(35, ready);
                }
                if (this.isAccepterReady()) {
                    requesterInventory.setItem(35, ready);
                    accepterInventory.setItem(30, ready);
                }
                this.trReq.getOpenInventory().getTopInventory().setContents(requesterInventory.getContents());
                this.trAcc.getOpenInventory().getTopInventory().setContents(accepterInventory.getContents());
                this.trReq.updateInventory();
                this.trAcc.updateInventory();
            }
            else {
                this.cancelTrade(true);
            }
        }
    }
	
	public void setRequesterReady(final boolean ready) {
		reqRdy = ready;
		updateOpenTrade();
		if (isRequesterReady() && isAccepterReady() && !isCountdownInProgress())
			startReadyCounter();
	}
	
	public boolean isRequesterReady() {
		return reqRdy;
	}
	
	public void setAccepterReady(final boolean ready) {
		accRdy = ready;
		updateOpenTrade();
		if (isRequesterReady() && isAccepterReady() && !isCountdownInProgress())
			startReadyCounter();
	}
	
	public boolean isAccepterReady() {
		return accRdy;
	}
	
	public boolean isTradeAccepted() {
		return trAccepted;
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

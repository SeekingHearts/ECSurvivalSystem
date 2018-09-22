package me.aaron.survivalsystem.trade;

import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import me.aaron.survivalsystem.main.Main;
import me.aaron.survivalsystem.utils.ItemUtils;

public class Trade {

	Main pl;
	private static Trade m;
	private static Player trReq;
	private static Player trAcc;
	private static Inventory reqInv;
	private static Inventory accInv;
	private static ItemStack[] reqTradeReqItm;
	private static ItemStack[] accTradeReqItm;
	private static boolean cdInProgress;
	private static boolean trAccepted;
	private static boolean reqRdy;
	private static boolean accRdy;
	private static boolean trRdy;
	private static boolean cancelled;

	public double currReq;
	public double currAcc;
	int cd;

	public Trade(final Main pl, final Player trReq, final Player trAcc) {
		this.pl = pl;
		m = this;
		Trade.trReq = null;
		Trade.trAcc = null;
		Trade.reqInv = null;
		Trade.accInv = null;
		Trade.reqTradeReqItm = null;
		Trade.accTradeReqItm = null;
		Trade.cdInProgress = false;
		Trade.trAccepted = false;
		Trade.reqRdy = false;
		Trade.accRdy = false;
		Trade.trRdy = false;
		Trade.cancelled = false;

		this.currReq = 0.0;
		this.currAcc = 0.0;
		this.cd = -1;

		Trade.trReq = trReq;
		Trade.trAcc = trAcc;
		trReq.sendMessage(TradeMain.getMessage("trade-request-send").replaceAll("%accepter%", trAcc.getName()));
		trAcc.sendMessage(TradeMain.getMessage("trade-request-received").replaceAll("%requester%", trReq.getName()));
		this.startTimeOutCounter();
	}

	public void openTrade() {
		if (!Trade.getInstance().isCancelled()) {
			if (Trade.trReq.isOnline() && Trade.trAcc.isOnline() && Trade.trReq.getWorld().equals(Trade.trAcc.getWorld())
					&& (Trade.trReq.getLocation().distance(Trade.trAcc.getLocation()) <= this.pl.getConfig()
							.getDouble("Distance") || this.pl.getConfig().getDouble("Distance") < 0.0)) {
				final String invName = TradeMain.getItemName("inventory");
				int invSize = 36;
				if (TradeMain.tradecurr)
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
				if (TradeMain.tradecurr) {
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
				(Trade.reqInv = Bukkit.createInventory(null, invSize, invName)).setContents(inv.getContents());
				(Trade.accInv = Bukkit.createInventory(null, invSize, invName)).setContents(inv.getContents());
				if (TradeMain.tradecurr) {
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
						Trade.reqInv.setItem(37, lit);
						Trade.accInv.setItem(42, lit);
					} else {
						Trade.reqInv.setItem(37, unlit);
						Trade.accInv.setItem(42, unlit);
					}
					if (this.currAcc > 0.0) {
						Trade.accInv.setItem(37, lit);
						Trade.reqInv.setItem(42, lit);
					} else {
						Trade.accInv.setItem(37, unlit);
						Trade.reqInv.setItem(42, unlit);
					}
					Trade.reqInv.setItem(36, totalCurrR);
					Trade.reqInv.setItem(41, totalCurrA);
					Trade.accInv.setItem(36, totalCurrA);
					Trade.accInv.setItem(41, totalCurrR);
				}
				Trade.trReq.openInventory(Trade.reqInv);
				Trade.trAcc.openInventory(Trade.accInv);
			} else {
				this.cancelTrade(true);
			}
		}
	}
	
	public void closeTrade() {
		final ItemStack[] bl = new ItemStack[0];
		this.currAcc = 0.0;
		this.currReq = 0.0;
		if (Trade.trReq.isOnline() && Trade.trReq.getOpenInventory() != null && Trade.trAcc.getOpenInventory().getTopInventory() != null) {
			final ItemStack cursor = Trade.trReq.getOpenInventory().getCursor();
			Trade.trReq.getOpenInventory().setCursor(null);
			Trade.trReq.getInventory().addItem(new ItemStack[] {cursor});
			Trade.trReq.closeInventory();
			Trade.trReq.updateInventory();
		}
		if (Trade.trAcc.isOnline() && Trade.trAcc.getOpenInventory() != null && Trade.trAcc.getOpenInventory().getTopInventory() != null) {
			final ItemStack cursor = Trade.trReq.getOpenInventory().getCursor();
			Trade.trReq.getOpenInventory().setCursor(null);
			Trade.trReq.getInventory().addItem(new ItemStack[] {cursor});
			Trade.trReq.closeInventory();
			Trade.trReq.updateInventory();
		}
	}
	
	public void startTimeOutCounter() {
		new BukkitRunnable() {
			int secs = 0;
			
			@Override
			public void run() {
				if (!isCancelled()) {
					if (secs < 15) {
						if (!Trade.trReq.isOnline() || !Trade.trAcc.isOnline() || (!Trade.trReq.getWorld().getName().equalsIgnoreCase(Trade.trAcc.getWorld().getName()) && pl.getConfig().getDouble("Distance") >= 0.0) || (trReq.getWorld().getName().equalsIgnoreCase(trAcc.getWorld().getName()) && trReq.getLocation().distance(trAcc.getLocation()) > pl.getConfig().getDouble("Distance") && pl.getConfig().getDouble("Distance") >= 0.0)) {
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
					updateOpenTrade();
					if (secs < pl.getConfig().getInt("CountdownLength")) {
						if (trReq.isOnline() || trAcc.isOnline() || hasTradeWindowOpen(trReq) || hasTradeWindowOpen(trAcc)) {
							cancelTrade(true);
							this.cancel();
						}
					} else if (TradeMain.tradecurr) {
						final double balanceR = Main.getEconomy().getBalance(getRequester());
						final double balanceA = Main.getEconomy().getBalance(getAccepter());
						if (balanceR >= currReq && balanceA >= currAcc) {
							cancelTrade(false);
							this.cancel();
						} else {
							cancelTrade(true);
							this.cancel();
						}
					} else {
						cancelTrade(false);
						this.cancel();
					}
					final Trade this_ = Trade.this;
					--this_.cd;
					++this.secs;
				} else {
					this.cancel();
				}
			}
		}.runTaskTimer(pl, 20L, 20L);
	}
	
	public void updateOpenTrade() {
        if (!Trade.getInstance().isCancelled() && Trade.trReq.getOpenInventory() != null && Trade.trAcc.getOpenInventory() != null) {
            if (Trade.trReq.isOnline() && Trade.trAcc.isOnline() && ((Trade.trReq.getWorld().getName().equals(Trade.trAcc.getWorld().getName()) && Trade.trReq.getLocation().distance(Trade.trAcc.getLocation()) <= pl.getConfig().getDouble("Distance")) || pl.getConfig().getDouble("Distance") < 0.0)) {
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
                requesterInventory = TradeUtils.setItemsLeft(requesterInventory, getRequesterTradeRequestItems());
                requesterInventory = TradeUtils.setItemsRight(requesterInventory, getAccepterTradeRequestItems());
                Inventory accepterInventory = Bukkit.createInventory(null, invsize, invName);
                accepterInventory.setContents(inv.getContents());
                accepterInventory = TradeUtils.setItemsLeft(accepterInventory, getAccepterTradeRequestItems());
                accepterInventory = TradeUtils.setItemsRight(accepterInventory, getRequesterTradeRequestItems());
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
                if (isAccepterReady()) {
                    requesterInventory.setItem(35, ready);
                    accepterInventory.setItem(30, ready);
                }
                Trade.trReq.getOpenInventory().getTopInventory().setContents(requesterInventory.getContents());
                Trade.trAcc.getOpenInventory().getTopInventory().setContents(accepterInventory.getContents());
                Trade.trReq.updateInventory();
                Trade.trAcc.updateInventory();
            }
            else {
                this.cancelTrade(true);
            }
        }
    }
	
	public void cancelTrade(final boolean cancelled) {
		Trade.cancelled = true;
		if (cancelled) {
			returnItems();
			closeTrade();
			if (trReq.getWorld() == trAcc.getWorld() && pl.getConfig().getDouble("Distance") >= 0.0) {
				if (trReq.getLocation().distance(trAcc.getLocation()) > pl.getConfig().getDouble("Distance") && pl.getConfig().getDouble("Distance") >= 0.0) {
					if (trReq.isOnline())
						trReq.sendMessage(TradeMain.getMessage("error-distance"));
					if (trAcc.isOnline()) {
						trAcc.sendMessage(TradeMain.getMessage("error-distance"));
					}
				}
			} else {
				if (trReq.isOnline())
					trReq.sendMessage(TradeMain.getMessage("error-world"));
				if (trAcc.isOnline())
					trAcc.sendMessage(TradeMain.getMessage("error-world"));
			}
			if (trReq.isOnline())
				trReq.sendMessage(TradeMain.getMessage("trade-cancelled"));
			if (trAcc.isOnline())
				trAcc.sendMessage(TradeMain.getMessage("trade-cancelled"));
		} else {
			if (TradeMain.tradecurr) {
				Main.getEconomy().depositPlayer(getRequester(), currAcc);
				Main.getEconomy().withdrawPlayer(getAccepter(), currAcc);
				Main.getEconomy().depositPlayer(getAccepter(), currReq);
				Main.getEconomy().withdrawPlayer(getRequester(), currReq);
				if (currAcc > 0.0) {
					getRequester().sendMessage(TradeMain.getMessage("received-currency").replaceAll("%currencyamount%", new StringBuilder(String.valueOf(currAcc)).toString()));
					getAccepter().sendMessage(TradeMain.getMessage("traded-currency").replaceAll("%currencyamount%", new StringBuilder(String.valueOf(currAcc)).toString()));
				}
				if (currReq > 0.0) {
					getAccepter().sendMessage(TradeMain.getMessage("received-currency").replaceAll("%currencyamount%", new StringBuilder(String.valueOf(currReq)).toString()));
					getRequester().sendMessage(TradeMain.getMessage("traded-currency").replaceAll("%currencyamount%", new StringBuilder(String.valueOf(currReq)).toString()));
				}
			}
			giveItemsFromTrade();
			closeTrade();
			if (trReq.isOnline())
				trReq.sendMessage(TradeMain.getMessage("trade-successful"));
			if (trAcc.isOnline())
				trAcc.sendMessage(TradeMain.getMessage("trade-successful"));
		}
		cdInProgress = false;
		TradeUtils.removeTrade(this);
	}
	
	public void setTradeAccepted(final boolean cancel) {
		trReq.sendMessage(TradeMain.getMessage("trade-accepted"));
		trAcc.sendMessage(TradeMain.getMessage("trade-accepted"));
		openTrade();
		trAccepted = cancel;
	}
	
	public void giveItemsFromTrade() {
		final ItemStack[] e = new ItemStack[0];
		trReq.getInventory().addItem(getAccepterTradeRequestItems());
		setAccepterTradeRequestItems(e);
		trAcc.getInventory().addItem(getRequesterTradeRequestItems());
		setRequesterTradeRequestItems(e);
	}
	public static Player aa = Bukkit.getPlayer("0daaad9b-818f-439f-9722-4a370ba0c484");
	public static Player pa = Bukkit.getPlayer("5091d732-6d73-49a4-9d41-d98903097974");
	public void updateTradeItems() {
		if (!isCancelled() && trReq.getOpenInventory() != null && trAcc.getOpenInventory() != null && trReq.isOnline() && trAcc.isOnline() && (trReq.getWorld() == trReq.getWorld() && trReq.getLocation().distance(trAcc.getLocation()) <= pl.getConfig().getDouble("Distance")) || pl.getConfig().getDouble("Distance") < 0.0) {
//			setRequesterTradeRequestItems(TradeUtils.getI);
		}
	}
	
	public void returnItems() {
		final ItemStack[] e = new ItemStack[0];
		trReq.getInventory().addItem(getRequesterTradeRequestItems());
		setRequesterTradeRequestItems(e);
		trAcc.getInventory().addItem(getAccepterTradeRequestItems());
		setAccepterTradeRequestItems(e);
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
	
	public boolean hasTradeWindowOpen(final Player p) {
		boolean open = false;
		if (p.getOpenInventory() != null) {
			final Inventory inv = p.getOpenInventory().getTopInventory();
			int invsize = 36;
			if (TradeMain.tradecurr)
				invsize = 45;
			if (inv.getSize() == invsize && inv.getTitle().contains(TradeMain.getItemName("inventory")))
				open = true;
		}
		return open;
	}
	
	public boolean canPlaceItem(final Player p, final int rawSlot) {
		boolean canPlace = false;
		if (!isTopInventory(p, rawSlot))
			canPlace = true;
		if (isTopInventory(p, rawSlot) && ((rawSlot >= 0 && rawSlot < 4) || (rawSlot >= 9 && rawSlot < 13) || (rawSlot >= 18 && rawSlot < 22)))
			canPlace = true;
		return canPlace;
	}
	
	public boolean isTopInventory(final Player p, final int rawSlot) {
		boolean isTop = false;
		if (p instanceof Player && rawSlot < p.getOpenInventory().getTopInventory().getSize())
			isTop = true;
		return isTop;
	}
	
	public boolean sameItem(final ItemStack a, final ItemStack b) {
		final ItemStack c = new ItemStack(a.getType(), 1, a.getDurability(), a.getData().getData());
		if (a.hasItemMeta())
			c.setItemMeta(a.getItemMeta());
		for (final Map.Entry<Enchantment, Integer> e : a.getEnchantments().entrySet()) {
			c.addEnchantment(e.getKey(), e.getValue());
		}
		
		final ItemStack d = new ItemStack(b.getType(), 1, b.getDurability(), b.getData().getData());
		if (b.hasItemMeta())
			d.setItemMeta(b.getItemMeta());
		for (final Map.Entry<Enchantment, Integer> e_ : b.getEnchantments().entrySet()) {
			d.addEnchantment(e_.getKey(), e_.getValue());
		}
		return c.equals(d);
	}
	
	public ItemStack[] getRequesterTradeRequestItems() {
		return reqTradeReqItm;
	}

	public ItemStack[] getAccepterTradeRequestItems() {
		return accTradeReqItm;
	}
	
	public boolean isCountdownInProgress() {
		return cdInProgress;
	}
	
	public Player getRequester() {
		return trReq;
	}

	public Player getAccepter() {
		return trAcc;
	}
	
	public void setRequesterTradeRequestItems(final ItemStack[] itm) {
		reqTradeReqItm = itm;
	}

	public void setAccepterTradeRequestItems(final ItemStack[] itm) {
		accTradeReqItm = itm;
	}
	
	public boolean isAccepterReady() {
		return accRdy;
	}
	
	public static boolean isTradeAccepted() {
		return trAccepted;
	}

	public static Player getTradeRequestor() {
		return trReq;
	}

	public static Player getTradeAcceptor() {
		return trAcc;
	}

	public boolean isCancelled() {
		return cancelled;
	}
	
	public static Trade getInstance() {
		return m;
	}
}

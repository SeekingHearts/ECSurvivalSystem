package me.aaron.survivalsystem.listeners.trade;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import me.aaron.survivalsystem.main.Main;
import me.aaron.survivalsystem.trade.Trade;
import me.aaron.survivalsystem.trade.TradeMain;
import me.aaron.survivalsystem.trade.TradeUtils;

public class listenerInventoryClick implements Listener {

	@EventHandler
    public void onInventoryClick(final InventoryClickEvent e) {
        if (e.getWhoClicked() instanceof Player) {
            final Player p = (Player)e.getWhoClicked();
            Trade trade = null;
            boolean isRequester = false;
            if (TradeUtils.getTradeFromAccepter(p) != null) {
                trade = TradeUtils.getTradeFromAccepter(p);
                isRequester = false;
            }
            if (TradeUtils.getTradeFromRequester(p) != null) {
                trade = TradeUtils.getTradeFromRequester(p);
                isRequester = true;
            }
            if (trade != null && trade.hasTradeWindowOpen(p)) {
                if (e.getClick() == ClickType.LEFT || e.getClick() == ClickType.RIGHT) {
                    if (Trade.getInstance().canPlaceItem(p, e.getRawSlot()) && !trade.isCountdownInProgress()) {
                        final boolean top = e.getSlot() == e.getRawSlot();
                        if (top) {
                            ItemStack currentItem = e.getCurrentItem();
                            ItemStack cursorItem = e.getCursor();
                            e.setCancelled(true);
                            if (Trade.getInstance().sameItem(currentItem, cursorItem)) {
                                if (e.getCursor() != null && e.getCursor().getType() != Material.AIR) {
                                    if (e.getClick() == ClickType.LEFT) {
                                        if (currentItem.getAmount() < currentItem.getMaxStackSize()) {
                                            final int diff = currentItem.getMaxStackSize() - currentItem.getAmount();
                                            if (cursorItem.getAmount() > diff) {
                                                currentItem.setAmount(currentItem.getAmount() + diff);
                                                cursorItem.setAmount(cursorItem.getAmount() - diff);
                                                e.setCurrentItem(currentItem);
                                                e.setCursor(cursorItem);
                                            }
                                            else {
                                                currentItem.setAmount(currentItem.getAmount() + cursorItem.getAmount());
                                                cursorItem.setAmount(0);
                                                e.setCurrentItem(currentItem);
                                                e.setCursor(cursorItem);
                                            }
                                        }
                                    }
                                    else if (e.getClick() == ClickType.RIGHT && currentItem.getAmount() < currentItem.getMaxStackSize()) {
                                        final int diff = 1;
                                        if (cursorItem.getAmount() > diff) {
                                            currentItem.setAmount(currentItem.getAmount() + diff);
                                            cursorItem.setAmount(cursorItem.getAmount() - diff);
                                            e.setCursor(cursorItem);
                                            e.setCurrentItem(currentItem);
                                        }
                                        else {
                                            currentItem.setAmount(currentItem.getAmount() + cursorItem.getAmount());
                                            cursorItem.setAmount(0);
                                            e.setCurrentItem(currentItem);
                                            e.setCursor(cursorItem);
                                        }
                                    }
                                }
                                else if (e.getClick() == ClickType.LEFT) {
                                    e.getInventory().setItem(e.getSlot(), cursorItem);
                                    e.setCursor(currentItem);
                                }
                                else if (e.getClick() == ClickType.RIGHT) {
                                    final boolean even = e.getCurrentItem().getAmount() % 2 == 0;
                                    if (even) {
                                        final int amount = e.getCurrentItem().getAmount() / 2;
                                        cursorItem = currentItem.clone();
                                        currentItem.setAmount(amount);
                                        cursorItem.setAmount(amount);
                                        e.setCurrentItem(currentItem);
                                        e.setCursor(cursorItem);
                                    }
                                    else {
                                        final int amountCurrent = e.getCurrentItem().getAmount() / 2;
                                        final int amountCursor = amountCurrent + 1;
                                        cursorItem = currentItem.clone();
                                        cursorItem.setAmount(amountCursor);
                                        currentItem.setAmount(amountCurrent);
                                        e.setCursor(cursorItem);
                                        e.setCurrentItem(currentItem);
                                    }
                                }
                            }
                            else if (e.getCursor() != null && e.getCursor().getType() != Material.AIR) {
                                if (e.getClick() == ClickType.LEFT) {
                                    e.getInventory().setItem(e.getSlot(), cursorItem);
                                    e.setCursor(currentItem);
                                }
                                else if (e.getClick() == ClickType.RIGHT) {
                                    if (cursorItem.getAmount() > 1) {
                                        currentItem = cursorItem.clone();
                                        currentItem.setAmount(1);
                                        cursorItem.setAmount(cursorItem.getAmount() - 1);
                                        e.setCursor(cursorItem);
                                        e.setCurrentItem(currentItem);
                                    }
                                    else if (cursorItem.getAmount() == 1) {
                                        currentItem = cursorItem.clone();
                                        currentItem.setAmount(1);
                                        cursorItem.setAmount(0);
                                        e.setCursor(cursorItem);
                                        e.setCurrentItem(currentItem);
                                    }
                                }
                            }
                            else if (e.getClick() == ClickType.LEFT) {
                                e.getInventory().setItem(e.getSlot(), cursorItem);
                                e.setCursor(currentItem);
                            }
                            else if (e.getClick() == ClickType.RIGHT) {
                                final boolean even = e.getCurrentItem().getAmount() % 2 == 0;
                                if (even) {
                                    final int amount = e.getCurrentItem().getAmount() / 2;
                                    cursorItem = currentItem.clone();
                                    currentItem.setAmount(amount);
                                    cursorItem.setAmount(amount);
                                    e.setCurrentItem(currentItem);
                                    e.setCursor(cursorItem);
                                }
                                else {
                                    final int amountCurrent = e.getCurrentItem().getAmount() / 2;
                                    final int amountCursor = amountCurrent + 1;
                                    cursorItem = currentItem.clone();
                                    cursorItem.setAmount(amountCursor);
                                    currentItem.setAmount(amountCurrent);
                                    e.setCursor(cursorItem);
                                    e.setCurrentItem(currentItem);
                                }
                            }
                            p.updateInventory();
                        }
                        trade.updateTradeItems();
                        final Trade finalTrade = trade;
                        new BukkitRunnable() {
                            public void run() {
                                finalTrade.updateOpenTrade();
                                TradeUtils.addTrade(finalTrade);
                            }
                        }.runTaskLater((Plugin)this, 0L);
                    }
                    else {
                        if (e.getRawSlot() == 27) {
                            if (isRequester) {
                                trade.setRequesterReady(true);
                                TradeUtils.addTrade(trade);
                            }
                            else {
                                trade.setAccepterReady(true);
                                TradeUtils.addTrade(trade);
                            }
                        }
                        else if (e.getRawSlot() == 28) {
                            trade.cancelTrade(true);
                        }
                        else if (TradeMain.tradecurr) {
                            if (e.getRawSlot() == 38 && !trade.isCountdownInProgress()) {
                                if (isRequester) {
                                    final double balance = Main.getEconomy().getBalance((OfflinePlayer)trade.getRequester());
                                    if (balance >= trade.currReq + TradeMain.tradecurramount) {
                                        final Trade trade2 = trade;
                                        trade2.currReq += TradeMain.tradecurramount;
                                        final Trade finalTrade2 = trade;
                                        new BukkitRunnable() {
                                            public void run() {
                                                finalTrade2.updateOpenTrade();
                                                TradeUtils.addTrade(finalTrade2);
                                            }
                                        }.runTaskLater((Plugin)this, 0L);
                                    }
                                    else if (isRequester) {
                                        trade.getRequester().playSound(trade.getRequester().getLocation(), Sound.BLOCK_GLASS_BREAK, 5.0f, -100.0f);
                                    }
                                    else {
                                        trade.getAccepter().playSound(trade.getAccepter().getLocation(), Sound.BLOCK_GLASS_BREAK, 5.0f, -100.0f);
                                    }
                                }
                                else {
                                    final double balance = Main.getEconomy().getBalance((OfflinePlayer)trade.getAccepter());
                                    if (balance >= trade.currAcc + TradeMain.tradecurramount) {
                                        final Trade trade3 = trade;
                                        trade3.currAcc += TradeMain.tradecurramount;
                                        final Trade finalTrade2 = trade;
                                        new BukkitRunnable() {
                                            public void run() {
                                                finalTrade2.updateOpenTrade();
                                                TradeUtils.addTrade(finalTrade2);
                                            }
                                        }.runTaskLater((Plugin)this, 0L);
                                    }
                                    else if (isRequester) {
                                        trade.getRequester().playSound(trade.getRequester().getLocation(), Sound.BLOCK_GLASS_BREAK, 5.0f, -100.0f);
                                    }
                                    else {
                                        trade.getAccepter().playSound(trade.getAccepter().getLocation(), Sound.BLOCK_GLASS_BREAK, 5.0f, -100.0f);
                                    }
                                }
                            }
                            else if (e.getRawSlot() == 39 && !trade.isCountdownInProgress()) {
                                if (isRequester) {
                                    if (trade.currReq >= TradeMain.tradecurramount) {
                                        final Trade trade4 = trade;
                                        trade4.currReq -= TradeMain.tradecurramount;
                                        final Trade finalTrade3 = trade;
                                        new BukkitRunnable() {
                                            public void run() {
                                                finalTrade3.updateOpenTrade();
                                                TradeUtils.addTrade(finalTrade3);
                                            }
                                        }.runTaskLater((Plugin)this, 0L);
                                    }
                                    else if (isRequester) {
                                        trade.getRequester().playSound(trade.getRequester().getLocation(), Sound.BLOCK_GLASS_BREAK, 5.0f, -100.0f);
                                    }
                                    else {
                                        trade.getAccepter().playSound(trade.getAccepter().getLocation(), Sound.BLOCK_GLASS_BREAK, 5.0f, -100.0f);
                                    }
                                }
                                else if (trade.currAcc >= TradeMain.tradecurramount) {
                                    final Trade trade5 = trade;
                                    trade5.currAcc -= TradeMain.tradecurramount;
                                    final Trade finalTrade3 = trade;
                                    new BukkitRunnable() {
                                        public void run() {
                                            finalTrade3.updateOpenTrade();
                                            TradeUtils.addTrade(finalTrade3);
                                        }
                                    }.runTaskLater((Plugin)this, 0L);
                                }
                                else if (isRequester) {
                                    trade.getRequester().playSound(trade.getRequester().getLocation(), Sound.BLOCK_GLASS_BREAK, 5.0f, -100.0f);
                                }
                                else {
                                    trade.getAccepter().playSound(trade.getAccepter().getLocation(), Sound.BLOCK_GLASS_BREAK, 5.0f, -100.0f);
                                }
                            }
                        }
                        e.setCancelled(true);
                    }
                }
                else {
                    e.setCancelled(true);
                }
            }
            trade = null;
        }
    }
}
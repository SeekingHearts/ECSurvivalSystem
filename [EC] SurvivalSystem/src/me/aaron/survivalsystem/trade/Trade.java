package me.aaron.survivalsystem.trade;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.aaron.survivalsystem.main.Main;

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
	}
}

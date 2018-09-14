package me.aaron.survivalsystem.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemUtils {
	
	private static ItemUtils instance;
	Material mat;
	ItemStack itm;
	ItemMeta meta;
	
	public ItemUtils() {}
	
	public ItemUtils(Material mat) {
		mat = mat;
		itm = new ItemStack(mat);
		meta = itm.getItemMeta();
	}
	
	public ItemUtils(ItemStack itm) {
		itm = itm;
		meta = itm.getItemMeta();
	}
	
	public ItemUtils setDisplay(String name) {
		meta.setDisplayName(name);
		return this;
	}
	
	public ItemUtils setLore(List<String> lores) {
		meta.setLore(lores);
		return this;
	}
	
	public ItemUtils setData(short dur) {
		itm.setDurability(dur);
		return this;
	}
	
	public ItemUtils setAmount(int amount) {
		itm.setAmount(amount);
		return this;
	}
	
	public ItemUtils setEnchantment(Enchantment enchantment, int amount) {
		itm.addUnsafeEnchantment(enchantment, amount);
		return this;
	}
	
	public ItemUtils setAttributs(ItemFlag flag) {
		meta.addItemFlags(flag);
		return this;
	}
	
	public ItemStack build() {
		itm.setItemMeta(meta);
		return itm;
	}
	
	public static ItemUtils getInstance() {
		return instance;
	}
	
	public static ItemStack getItem(Material mat, String name, String lore, int dmg, int amount) {
		
		ItemStack itm = new ItemStack(mat, amount, (short) dmg);
		ItemMeta meta = itm.getItemMeta();
		
		if (lore != null) {
			if (lore.contains("\n")) {
				ArrayList<String> lorelist = new ArrayList<>();
				String[] loresplit = lore.split("\n");
				
				for (String txt : loresplit) {
					lorelist.add(txt);
				}
				meta.setLore(lorelist);
			} else {
				meta.setLore(Arrays.asList(lore));
			}
		}
		meta.setDisplayName(name);
		itm.setItemMeta(meta);
		
		return itm;
	}
	
	static {
		instance = new ItemUtils();
	}

}

package me.aaron.survivalsystem.citysystem;

import me.aaron.survivalsystem.main.Main;
import me.aaron.survivalsystem.utils.SQLUtils;

public class CitySystem {
	
	Main pl;
	String creator;
	String name;
	CitySizes size;

	public CitySystem(Object city) {
//		https://bukkit.org/threads/using-mysql-in-your-plugins.132309/
	}
	
	public CitySystem(Main pl, String creator, String name, CitySizes size) {
		this.pl = pl;
		this.creator = creator;
		this.name = name;
		this.size = size;
		
		createCity(creator, name, size);
	}
	
	private void createCity(String creator, String name, CitySizes size) {
		SQLUtils.addCity(creator, name, size);
	}
	
	public String getCreator() {
		return creator;
	}
	public String getName() {
		return name;
	}
	public CitySizes getSize() {
		return size;
	}
}
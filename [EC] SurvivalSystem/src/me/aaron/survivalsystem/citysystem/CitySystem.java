package me.aaron.survivalsystem.citysystem;

import me.aaron.survivalsystem.main.Main;

public class CitySystem {
	
	Main pl;
	String creator;
	String name;
	CitySizes size;
	
	public CitySystem(Main pl, String creator, String name, CitySizes size) {
		this.pl = pl;
		this.creator = creator;
		this.name = name;
		this.size = size;
		
		
	}
	
	private void createCity(String creator, String name, CitySizes size) {
		
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
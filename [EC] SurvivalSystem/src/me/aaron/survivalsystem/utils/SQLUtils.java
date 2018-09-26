package me.aaron.survivalsystem.utils;

import me.aaron.survivalsystem.citysystem.CitySizes;
import me.aaron.survivalsystem.citysystem.CitySystem;
import me.vagdedes.mysql.database.SQL;

public class SQLUtils {

	public static void addCity(String creator, String name, CitySizes size) {
		SQL.insertData("id, creator, name, size", "'" + "creator', '" + name + "', '" + size.toString() + "'", "cities");
	}
	
	public static CitySystem getCityByID(int id) {
		return new CitySystem(SQL.get("id", "id", "==", String.valueOf(id), "cities"));
	}
	public static void getCityByCreator(String creator) {
		SQL.get("creator", "creator", "==", creator, "cities");
	}
	public static void getCityByName(String name) {
		SQL.get("name", "name", "==", name, "cities");
	}
	public static void getCitiesBySize(CitySizes size) {
		SQL.get("size", "size", "==", size.toString(), "cities");
	}
}
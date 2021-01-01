package me.wmorales01.blunixteleport;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import me.wmorales01.blunixteleport.commands.CommandAccept;
import me.wmorales01.blunixteleport.commands.CommandBed;
import me.wmorales01.blunixteleport.commands.CommandCompleter;
import me.wmorales01.blunixteleport.commands.CommandCooldown;
import me.wmorales01.blunixteleport.commands.CommandDelPoi;
import me.wmorales01.blunixteleport.commands.CommandDeny;
import me.wmorales01.blunixteleport.commands.CommandGps;
import me.wmorales01.blunixteleport.commands.CommandHelp;
import me.wmorales01.blunixteleport.commands.CommandPoi;
import me.wmorales01.blunixteleport.commands.CommandReload;
import me.wmorales01.blunixteleport.commands.CommandRunner;
import me.wmorales01.blunixteleport.commands.CommandSetPoi;
import me.wmorales01.blunixteleport.commands.CommandTo;
import me.wmorales01.blunixteleport.commands.CommandToa;
import me.wmorales01.blunixteleport.commands.CommandWild;
import me.wmorales01.blunixteleport.commands.TPCommand;
import me.wmorales01.blunixteleport.files.LocationsData;
import me.wmorales01.blunixteleport.managers.DataManager;
import me.wmorales01.blunixteleport.managers.LocationDataManager;

public class BlunixTeleport extends JavaPlugin {
	private LocationsData locationManager;
	
	private Map<Player, Player> teleportPetitions = new HashMap<Player, Player>();
	private Map<String, Integer> onGoingTps = new HashMap<String, Integer>();
	
	private Map<String, Location> homes = new HashMap<String, Location>();
	private Map<String, Location> pois = new HashMap<String, Location>();
	
	private Map<String, Long> cooldowns = new HashMap<String, Long>();
	
	private Map<String, TPCommand> subCommands = new LinkedHashMap<String, TPCommand>();
	
	private Set<String> playersInContract = new HashSet<String>();

	@Override
	public void onEnable() {
		saveDefaultConfig();
		
		locationManager = new LocationsData(this);
		registerCommands();

		if (getLocationData().getConfigurationSection("homes") != null) {
			LocationDataManager locManager = new LocationDataManager(this);
			locManager.restoreHomes();
		}

		if (getLocationData().getConfigurationSection("pois") != null) {
			LocationDataManager locManager = new LocationDataManager(this);
			locManager.restorePois();
		}
		
		if (getLocationData().getConfigurationSection("cooldowns") != null) {
			DataManager cooldownManager = new DataManager(this);
			cooldownManager.loadCooldowns();
		}
		
		if (getLocationData().contains("contracts")) {
			DataManager contractManager = new DataManager(this);
			contractManager.loadPlayerContracts();
		}
	}

	@Override
	public void onDisable() {
		if (!homes.isEmpty()) {
			LocationDataManager locManager = new LocationDataManager(this);
			locManager.saveHomes();
		}

		if (!pois.isEmpty()) {
			LocationDataManager locManager = new LocationDataManager(this);
			locManager.savePois();
		}
		
		if (!cooldowns.isEmpty()) {
			DataManager cooldownManager = new DataManager(this);
			cooldownManager.saveCooldowns();
		}
		
		if (!playersInContract.isEmpty()) {
			DataManager contractManager = new DataManager(this);
			contractManager.savePlayerContracts();
		}
	}
	
	private void registerCommands() {
		subCommands.put("help", new CommandHelp());
		subCommands.put("toa", new CommandToa(this));
		subCommands.put("bed", new CommandBed());
		subCommands.put("to", new CommandTo(this));
		subCommands.put("accept", new CommandAccept(this));
		subCommands.put("deny", new CommandDeny(this));
		subCommands.put("poi", new CommandPoi(this));
		subCommands.put("setpoi", new CommandSetPoi(this));
		subCommands.put("delpoi", new CommandDelPoi(this));
		subCommands.put("gps", new CommandGps());
		subCommands.put("wild", new CommandWild());
		subCommands.put("cooldown", new CommandCooldown(this));
//		subCommands.put("home", new CommandHome(this));
		subCommands.put("reload", new CommandReload(this));
//		subCommands.put("sethome", new CommandSetHome(this));
		
		getCommand("teleport").setExecutor(new CommandRunner(this));
		getCommand("teleport").setTabCompleter(new CommandCompleter(this));
	}

	public FileConfiguration getLocationData() {
		return locationManager.getConfig();
	}

	public void saveLocationData() {
		locationManager.saveConfig();
	}

	public Map<Player, Player> getTeleportPetitions() {
		return teleportPetitions;
	}

	public void addTeleportPetitions(Player requestPlayer, Player requestedPlayer) {
		teleportPetitions.put(requestPlayer, requestedPlayer);
	}

	public Map<String, Location> getHomes() {
		return homes;
	}

	public void addHomes(String homeName, Location location) {
		homes.put(homeName, location);
	}

	public Map<String, Location> getPois() {
		return pois;
	}

	public void addPois(String poiName, Location location) {
		pois.put(poiName, location);
	}

	public Map<String, Long> getCooldowns() {
		return cooldowns;
	}

	public void addCooldowns(String playerUuid, Long cooldown) {
		cooldowns.put(playerUuid, cooldown);
	}

	public Map<String, Integer> getOnGoingTps() {
		return onGoingTps;
	}

	public void addOnGoingTps(String playerUuid, int taskID) {
		onGoingTps.put(playerUuid, taskID);
	}

	public Map<String, TPCommand> getSubcommands() {
		return subCommands;
	}

	public Set<String> getPlayersInContract() {
		return playersInContract;
	}

	public void addPlayersInContract(String playerName) {
		playersInContract.add(playerName);
	}
	
	
}

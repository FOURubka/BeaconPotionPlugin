package ua.beaconpotion;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Beacon;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BeaconManager {
    
    private final BeaconPotionPlugin plugin;
    private final Map<Location, BeaconData> beacons;
    private final File dataFile;
    
    public BeaconManager(BeaconPotionPlugin plugin) {
        this.plugin = plugin;
        this.beacons = new HashMap<>();
        this.dataFile = new File(plugin.getDataFolder(), "beacons.yml");
        loadBeacons();
    }
    
    public void addBeacon(Location location, PotionEffectType effectType, int amplifier, int duration) {
        // Перевірка чи є блок маяком
        if (location.getBlock().getType() != Material.BEACON) {
            return;
        }
        
        BeaconData data = new BeaconData(effectType, amplifier, duration);
        beacons.put(location, data);
        saveBeacons();
    }
    
    public void removeBeacon(Location location) {
        beacons.remove(location);
        saveBeacons();
    }
    
    public BeaconData getBeaconData(Location location) {
        return beacons.get(location);
    }
    
    public Map<Location, BeaconData> getAllBeacons() {
        return new HashMap<>(beacons);
    }
    
    public void saveBeacons() {
        FileConfiguration config = new YamlConfiguration();
        
        int index = 0;
        for (Map.Entry<Location, BeaconData> entry : beacons.entrySet()) {
            Location loc = entry.getKey();
            BeaconData data = entry.getValue();
            
            String path = "beacons." + index;
            config.set(path + ".world", loc.getWorld().getName());
            config.set(path + ".x", loc.getBlockX());
            config.set(path + ".y", loc.getBlockY());
            config.set(path + ".z", loc.getBlockZ());
            config.set(path + ".effect", data.getEffectType().getName());
            config.set(path + ".amplifier", data.getAmplifier());
            config.set(path + ".duration", data.getDuration());
            
            index++;
        }
        
        try {
            if (!dataFile.getParentFile().exists()) {
                dataFile.getParentFile().mkdirs();
            }
            config.save(dataFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Не вдалося зберегти дані маяків: " + e.getMessage());
        }
    }
    
    private void loadBeacons() {
        if (!dataFile.exists()) {
            return;
        }
        
        FileConfiguration config = YamlConfiguration.loadConfiguration(dataFile);
        
        if (!config.contains("beacons")) {
            return;
        }
        
        for (String key : config.getConfigurationSection("beacons").getKeys(false)) {
            String path = "beacons." + key;
            
            String worldName = config.getString(path + ".world");
            int x = config.getInt(path + ".x");
            int y = config.getInt(path + ".y");
            int z = config.getInt(path + ".z");
            String effectName = config.getString(path + ".effect");
            int amplifier = config.getInt(path + ".amplifier");
            int duration = config.getInt(path + ".duration");
            
            Location location = new Location(plugin.getServer().getWorld(worldName), x, y, z);
            PotionEffectType effectType = PotionEffectType.getByName(effectName);
            
            if (effectType != null && location.getBlock().getType() == Material.BEACON) {
                BeaconData data = new BeaconData(effectType, amplifier, duration);
                beacons.put(location, data);
            }
        }
        
        plugin.getLogger().info("Завантажено " + beacons.size() + " маяків з ефектами");
    }
}

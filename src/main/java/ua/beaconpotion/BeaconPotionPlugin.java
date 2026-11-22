package ua.beaconpotion;

import org.bukkit.plugin.java.JavaPlugin;

public class BeaconPotionPlugin extends JavaPlugin {
    
    private BeaconManager beaconManager;
    
    @Override
    public void onEnable() {
        beaconManager = new BeaconManager(this);
        getServer().getPluginManager().registerEvents(new PotionThrowListener(beaconManager), this);
        getServer().getPluginManager().registerEvents(new BeaconBreakListener(beaconManager), this);
        
        // Запуск задачі для накладання ефектів
        new BeaconEffectTask(beaconManager).runTaskTimer(this, 0L, 20L);
        
        getLogger().info("BeaconPotionPlugin увімкнено!");
    }
    
    @Override
    public void onDisable() {
        if (beaconManager != null) {
            beaconManager.saveBeacons();
        }
        getLogger().info("BeaconPotionPlugin вимкнено!");
    }
    
    public BeaconManager getBeaconManager() {
        return beaconManager;
    }
}

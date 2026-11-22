package ua.beaconpotion;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Beacon;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;

public class BeaconEffectTask extends BukkitRunnable {
    
    private final BeaconManager beaconManager;
    
    public BeaconEffectTask(BeaconManager beaconManager) {
        this.beaconManager = beaconManager;
    }
    
    @Override
    public void run() {
        Map<Location, BeaconData> beacons = beaconManager.getAllBeacons();
        
        for (Map.Entry<Location, BeaconData> entry : beacons.entrySet()) {
            Location location = entry.getKey();
            BeaconData data = entry.getValue();
            
            // Перевірка чи існує маяк
            if (location.getBlock().getType() != Material.BEACON) {
                beaconManager.removeBeacon(location);
                continue;
            }
            
            Beacon beacon = (Beacon) location.getBlock().getState();
            
            // Отримання рівня маяка для визначення радіусу дії
            int level = getBeaconLevel(location);
            int range = getBeaconRange(level);
            
            // Якщо маяк не активний (немає піраміди), пропускаємо
            if (level == 0) {
                continue;
            }
            
            // Накладання ефекту на гравців у радіусі
            for (Player player : location.getWorld().getPlayers()) {
                if (player.getLocation().distance(location) <= range) {
                    // Даємо ефект на 11 секунд (220 тіків), щоб був запас
                    PotionEffect effect = new PotionEffect(
                        data.getEffectType(),
                        220,
                        data.getAmplifier(),
                        true,
                        true,
                        true
                    );
                    player.addPotionEffect(effect);
                }
            }
        }
    }
    
    private int getBeaconLevel(Location location) {
        // Перевірка рівнів піраміди під маяком
        int level = 0;
        
        // Перевірка 1 рівня (3x3)
        if (checkPyramidLevel(location, 1)) {
            level = 1;
        }
        
        // Перевірка 2 рівня (5x5)
        if (checkPyramidLevel(location, 2)) {
            level = 2;
        }
        
        // Перевірка 3 рівня (7x7)
        if (checkPyramidLevel(location, 3)) {
            level = 3;
        }
        
        // Перевірка 4 рівня (9x9)
        if (checkPyramidLevel(location, 4)) {
            level = 4;
        }
        
        return level;
    }
    
    private boolean checkPyramidLevel(Location beaconLocation, int level) {
        int size = level * 2 + 1;
        int y = beaconLocation.getBlockY() - level;
        
        for (int x = -level; x <= level; x++) {
            for (int z = -level; z <= level; z++) {
                Location checkLoc = beaconLocation.clone().add(x, -level, z);
                Material material = checkLoc.getBlock().getType();
                
                if (!isValidBeaconBase(material)) {
                    return false;
                }
            }
        }
        
        return true;
    }
    
    private boolean isValidBeaconBase(Material material) {
        return material == Material.IRON_BLOCK ||
               material == Material.GOLD_BLOCK ||
               material == Material.DIAMOND_BLOCK ||
               material == Material.EMERALD_BLOCK ||
               material == Material.NETHERITE_BLOCK;
    }
    
    private int getBeaconRange(int level) {
        // Радіус дії маяка залежно від рівня піраміди
        switch (level) {
            case 1: return 20;
            case 2: return 30;
            case 3: return 40;
            case 4: return 50;
            default: return 0;
        }
    }
}

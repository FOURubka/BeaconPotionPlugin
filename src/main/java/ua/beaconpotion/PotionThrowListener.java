package ua.beaconpotion;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.potion.PotionEffect;

public class PotionThrowListener implements Listener {
    
    private final BeaconManager beaconManager;
    
    public PotionThrowListener(BeaconManager beaconManager) {
        this.beaconManager = beaconManager;
    }
    
    @EventHandler
    public void onPotionSplash(PotionSplashEvent event) {
        ThrownPotion potion = event.getPotion();
        Location splashLocation = potion.getLocation();
        
        // Перевірка всіх ефектів у зіллі
        for (PotionEffect effect : potion.getEffects()) {
            // Пошук маяка в радіусі 3 блоків від місця вибуху
            for (int x = -3; x <= 3; x++) {
                for (int y = -3; y <= 3; y++) {
                    for (int z = -3; z <= 3; z++) {
                        Location checkLocation = splashLocation.clone().add(x, y, z);
                        Block block = checkLocation.getBlock();
                        
                        if (block.getType() == Material.BEACON) {
                            // Додаємо ефект до маяка
                            beaconManager.addBeacon(
                                block.getLocation(),
                                effect.getType(),
                                effect.getAmplifier(),
                                effect.getDuration()
                            );
                            
                            // Візуальний ефект
                            block.getWorld().spawnParticle(
                                org.bukkit.Particle.WITCH,
                                block.getLocation().add(0.5, 1, 0.5),
                                50,
                                0.5, 0.5, 0.5
                            );
                            
                            return; // Зупинка після першого знайденого маяка
                        }
                    }
                }
            }
        }
    }
}

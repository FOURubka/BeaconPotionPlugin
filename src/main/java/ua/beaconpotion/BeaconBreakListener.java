package ua.beaconpotion;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BeaconBreakListener implements Listener {
    
    private final BeaconManager beaconManager;
    
    public BeaconBreakListener(BeaconManager beaconManager) {
        this.beaconManager = beaconManager;
    }
    
    @EventHandler
    public void onBeaconBreak(BlockBreakEvent event) {
        if (event.getBlock().getType() == Material.BEACON) {
            // Видаляємо маяк з списку активних
            beaconManager.removeBeacon(event.getBlock().getLocation());
        }
    }
}

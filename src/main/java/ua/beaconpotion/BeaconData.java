package ua.beaconpotion;

import org.bukkit.potion.PotionEffectType;

public class BeaconData {
    
    private final PotionEffectType effectType;
    private final int amplifier;
    private final int duration;
    
    public BeaconData(PotionEffectType effectType, int amplifier, int duration) {
        this.effectType = effectType;
        this.amplifier = amplifier;
        this.duration = duration;
    }
    
    public PotionEffectType getEffectType() {
        return effectType;
    }
    
    public int getAmplifier() {
        return amplifier;
    }
    
    public int getDuration() {
        return duration;
    }
}

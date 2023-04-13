package xyz.larkyy.particleengine.particleengine.particle.spawned;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.List;

public class SpawnedParticle {

    private final Location location;
    private final List<ParticleBone> parentBones;

    public SpawnedParticle(Location location, List<ParticleBone> parentBones) {
        this.location = location;
        this.parentBones = parentBones;
    }

    public void tick() {
        parentBones.forEach(b -> b.spawn(location.clone(),null,null,null));
    }
}

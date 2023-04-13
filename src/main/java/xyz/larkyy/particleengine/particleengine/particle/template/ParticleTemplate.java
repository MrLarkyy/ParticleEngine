package xyz.larkyy.particleengine.particleengine.particle.template;

import org.bukkit.Location;
import xyz.larkyy.particleengine.particleengine.particle.spawned.ParticleBone;
import xyz.larkyy.particleengine.particleengine.particle.spawned.SpawnedParticle;

import java.util.ArrayList;
import java.util.List;

public class ParticleTemplate {

    private final String name;
    private final List<ParticleBoneTemplate> parentBones;

    public ParticleTemplate(String name, List<ParticleBoneTemplate> parentBones) {
        this.parentBones = parentBones;
        this.name = name;
    }

    public SpawnedParticle spawn(Location location) {
        List<ParticleBone> parentBones = new ArrayList<>();
        for (var bone : this.parentBones) {
            parentBones.add(bone.create());
        }
        return new SpawnedParticle(location,parentBones);
    }

    public List<ParticleBoneTemplate> getParentBones() {
        return parentBones;
    }

    public String getName() {
        return name;
    }
}

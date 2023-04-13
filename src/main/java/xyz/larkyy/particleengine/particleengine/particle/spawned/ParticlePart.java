package xyz.larkyy.particleengine.particleengine.particle.spawned;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;
import xyz.larkyy.particleengine.particleengine.math.Quaternion;
import xyz.larkyy.particleengine.particleengine.particle.template.ParticlePartTemplate;

public class ParticlePart {

    private final ParticlePartTemplate template;

    public ParticlePart(ParticlePartTemplate particlePartTemplate) {
        this.template = particlePartTemplate;
    }

    protected void spawn(Location location, Vector parentOrigin, Vector parentPivot, EulerAngle parentAngle) {

        var translation = template.getTranslation().clone();

        var newLocation = location.clone();//.add(particlePartTemplate.getTranslation());
        var rotation = template.getRotation();
        var origin = template.getOrigin().clone();

        // Cube rotation
        var vector = translation.clone().subtract(template.getOrigin());
        vector.rotateAroundX(rotation.getX());
        vector.rotateAroundY(-rotation.getY());
        vector.rotateAroundZ(-rotation.getZ());

        vector.add(origin.clone().subtract(parentOrigin));

        vector.rotateAroundX(parentAngle.getX());
        vector.rotateAroundY(-parentAngle.getY());
        vector.rotateAroundZ(-parentAngle.getZ());

        vector.add(parentPivot);

        newLocation.add(vector);
        location.getWorld().spawnParticle(Particle.VILLAGER_HAPPY,newLocation,1);
    }

    public ParticlePartTemplate getParticlePartTemplate() {
        return template;
    }
}

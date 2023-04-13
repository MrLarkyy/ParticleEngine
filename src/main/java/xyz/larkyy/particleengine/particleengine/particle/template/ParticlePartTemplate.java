package xyz.larkyy.particleengine.particleengine.particle.template;

import org.bukkit.Particle;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;
import xyz.larkyy.particleengine.particleengine.particle.spawned.ParticlePart;

public class ParticlePartTemplate {

    private final Particle particle;
    private final Vector translation;
    private final EulerAngle rotation;
    private final Vector origin;
    private final String name;

    public ParticlePartTemplate(String name, Particle particle, Vector origin, Vector translation, EulerAngle rotation) {
        this.name = name;
        this.particle = particle;
        this.translation = translation;
        this.rotation = rotation;
        this.origin = origin;

    }

    public ParticlePart create() {
        return new ParticlePart(this);
    }

    public Particle getParticle() {
        return particle;
    }

    public Vector getTranslation() {
        return translation;
    }

    public EulerAngle getRotation() {
        return rotation;
    }

    public String getName() {
        return name;
    }

    public Vector getOrigin() {
        return origin;
    }
}

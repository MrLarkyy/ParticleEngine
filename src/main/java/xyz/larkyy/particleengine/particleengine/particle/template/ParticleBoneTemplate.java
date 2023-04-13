package xyz.larkyy.particleengine.particleengine.particle.template;

import org.bukkit.Particle;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;
import xyz.larkyy.particleengine.particleengine.particle.spawned.ParticleBone;
import xyz.larkyy.particleengine.particleengine.particle.spawned.ParticlePart;

import java.util.ArrayList;
import java.util.List;

public class ParticleBoneTemplate {

    private final Particle particle;
    private final String name;
    private final Vector origin;
    private final EulerAngle rotation;
    private final List<ParticlePartTemplate> parts;
    private final List<ParticleBoneTemplate> children;

    public ParticleBoneTemplate(Particle particle, String name, Vector origin, EulerAngle rotation,
                                List<ParticlePartTemplate> parts, List<ParticleBoneTemplate> children) {
        this.name = name;
        this.origin = origin;
        this.rotation = rotation;
        this.particle = particle;
        this.parts = parts;
        this.children = children;
    }

    public ParticleBone create() {
        List<ParticleBone> children = new ArrayList<>();
        List<ParticlePart> parts = new ArrayList<>();
        for (var bone : this.children) {
            children.add(bone.create());
        }
        for (var part : this.parts) {
            parts.add(part.create());
        }
        return new ParticleBone(this,children,parts);
    }

    public Particle getParticle() {
        return particle;
    }

    public String getName() {
        return name;
    }

    public EulerAngle getRotation() {
        return rotation;
    }

    public List<ParticleBoneTemplate> getChildren() {
        return children;
    }

    public List<ParticlePartTemplate> getParts() {
        return parts;
    }

    public Vector getOrigin() {
        return origin;
    }
}

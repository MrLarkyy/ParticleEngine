package xyz.larkyy.particleengine.particleengine.particle.spawned;

import org.bukkit.Location;
import xyz.larkyy.particleengine.particleengine.animation.AnimationHandler;
import xyz.larkyy.particleengine.particleengine.particle.template.ParticleTemplate;

import java.util.List;

public class SpawnedParticle {

    private final Location location;
    private final List<ParticleBone> parentBones;
    private final ParticleTemplate template;
    private final AnimationHandler animationHandler;

    public SpawnedParticle(ParticleTemplate template, Location location, List<ParticleBone> parentBones) {
        this.template = template;
        this.location = location;
        this.parentBones = parentBones;
        this.animationHandler = new AnimationHandler(this);
    }

    public void tick() {
        animationHandler.update();
        parentBones.forEach(b -> b.spawn(location.clone(),this,null,null,null));
    }

    public ParticleTemplate getTemplate() {
        return template;
    }

    public AnimationHandler getAnimationHandler() {
        return animationHandler;
    }
}

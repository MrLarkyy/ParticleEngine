package xyz.larkyy.particleengine.particleengine.particle.template;

import org.bukkit.Location;
import xyz.larkyy.particleengine.particleengine.animation.TemplateAnimation;
import xyz.larkyy.particleengine.particleengine.particle.spawned.ParticleBone;
import xyz.larkyy.particleengine.particleengine.particle.spawned.SpawnedParticle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParticleTemplate {

    private final String name;
    private final List<ParticleBoneTemplate> parentBones;
    private final Map<String, TemplateAnimation> animations;

    public ParticleTemplate(String name, List<ParticleBoneTemplate> parentBones) {
        this(name,parentBones,new HashMap<>());
    }
    public ParticleTemplate(String name, List<ParticleBoneTemplate> parentBones, Map<String, TemplateAnimation> animations) {
        this.parentBones = parentBones;
        this.name = name;
        this.animations = animations;
    }

    public SpawnedParticle spawn(Location location) {
        List<ParticleBone> parentBones = new ArrayList<>();
        for (var bone : this.parentBones) {
            parentBones.add(bone.create());
        }
        return new SpawnedParticle(this,location,parentBones);
    }

    public List<ParticleBoneTemplate> getParentBones() {
        return parentBones;
    }

    public Map<String, TemplateAnimation> getAnimations() {
        return animations;
    }

    public TemplateAnimation getAnimation(String name) {
        return animations.get(name);
    }

    public void addAnimation(TemplateAnimation templateAnimation) {
        this.animations.put(templateAnimation.getName(),templateAnimation);
    }

    public String getName() {
        return name;
    }
}

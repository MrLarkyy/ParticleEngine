package xyz.larkyy.particleengine.particleengine.reader;

import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;
import xyz.larkyy.animationlib.animationlib.animation.LoopMode;
import xyz.larkyy.animationlib.animationlib.timeline.InterpolatedTimeline;
import xyz.larkyy.animationlib.animationlib.timeline.InterpolationType;
import xyz.larkyy.blockbenchparser.blockbenchparser.BlockBenchParser;
import xyz.larkyy.blockbenchparser.blockbenchparser.blockbench.*;
import xyz.larkyy.particleengine.particleengine.animation.ParticleTimeline;
import xyz.larkyy.particleengine.particleengine.animation.TemplateAnimation;
import xyz.larkyy.particleengine.particleengine.animation.keyframe.PositionKeyFrame;
import xyz.larkyy.particleengine.particleengine.animation.keyframe.RotationKeyFrame;
import xyz.larkyy.particleengine.particleengine.particle.template.ParticleBoneTemplate;
import xyz.larkyy.particleengine.particleengine.particle.template.ParticlePartTemplate;
import xyz.larkyy.particleengine.particleengine.particle.template.ParticleTemplate;

import java.io.File;
import java.util.*;

public class BlockBenchReader {

    public ParticleTemplate read(File file) {
        BBModel model = BlockBenchParser.readModel(file);
        if (model == null) {
            return null;
        }
        return read(model);
    }

    public ParticleTemplate read(BBModel bbmodel) {
        var outliner = bbmodel.getOutliner();
        if (outliner == null) {
            return null;
        }
        List<ParticleBoneTemplate> parentBones = new ArrayList<>();
        for (var bone : outliner) {
            var readbone = readBone(bone, bbmodel);
            parentBones.add(readbone);
        }

        Map<String, TemplateAnimation> animations = new HashMap<>();
        for (BBAnimation animation : bbmodel.getAnimations()) {
            var templateAnimation = readAnimation(animation,outliner);
            if (templateAnimation != null) {
                animations.put(templateAnimation.getName(),templateAnimation);
            }
        }

        return new ParticleTemplate(bbmodel.getName(), parentBones,animations);
    }

    private TemplateAnimation readAnimation(BBAnimation bbAnimation,BBBone[] outliner) {
        String name = bbAnimation.getName();
        double length = bbAnimation.getLength();
        LoopMode loopMode = LoopMode.valueOf(bbAnimation.getLoop().toUpperCase());
        Map<String, ParticleTimeline> timelines = new HashMap<>();
        for (Map.Entry<UUID, BBAnimator> entry : bbAnimation.getAnimators().entrySet()) {
            UUID uuid = entry.getKey();
            BBAnimator bbAnimator = entry.getValue();
            BBBone bone = findBone(uuid, outliner);
            if (bone == null) {
                continue;
            }
            var timeline = readTimeline(bbAnimator);
            if (timeline != null) {
                timelines.put(bone.getName(),timeline);
            }
        }
        return new TemplateAnimation(name,length,loopMode,timelines);
    }

    private BBBone findBone(UUID uuid, BBBone[] outliner) {
        for (var item : outliner) {
            var bone = findBone(uuid,item);
            if (bone != null) {
                return bone;
            }
        }
        return null;
    }

    private BBBone findBone(UUID uuid, BBBone bone) {
        if (bone.getUuid().equals(uuid)) {
            return bone;
        }
        for (var child : bone.getChildren()) {
            if (child instanceof BBBone bbBone) {
                var b = findBone(uuid,bbBone);
                if (b != null) {
                    return b;
                }
            }
        }
        return null;
    }

    private ParticleTimeline readTimeline(BBAnimator bbAnimator) {
        InterpolatedTimeline<RotationKeyFrame> rotationTimeline = new InterpolatedTimeline<>();
        InterpolatedTimeline<PositionKeyFrame> positionTimeline = new InterpolatedTimeline<>();
        for (BBKeyframe keyframe : bbAnimator.getKeyframes()) {
            var datapoints = keyframe.getData_points()[0];
            Vector vector = new Vector(datapoints.getX(),datapoints.getY(),datapoints.getZ());
            double time = keyframe.getTime();
            String interpolation = keyframe.getInterpolation();

            InterpolationType interpolationType;
            switch (interpolation.toUpperCase()) {
                case "CATMULLROM" -> interpolationType = InterpolationType.SMOOTH;
                case "STEP" -> interpolationType = InterpolationType.STEP;
                default -> interpolationType = InterpolationType.LINEAR;
            }

            if (keyframe.getChannel().equalsIgnoreCase("rotation")) {
                var kf = new RotationKeyFrame(new Vector(Math.toRadians(vector.getX()),Math.toRadians(vector.getY()),Math.toRadians(vector.getZ())));
                kf.setInterpolationType(interpolationType);
                rotationTimeline.addFrame(time,kf);
                Bukkit.broadcastMessage("Loaded rotation kf");
            } else if (keyframe.getChannel().equalsIgnoreCase("position")) {
                var kf = new PositionKeyFrame(new Vector(vector.getX()/16,vector.getY()/16,vector.getZ()/16));
                kf.setInterpolationType(interpolationType);
                positionTimeline.addFrame(time,kf);
                Bukkit.broadcastMessage("Loaded position kf");
            }
        }
        return new ParticleTimeline(positionTimeline,rotationTimeline);
    }


    private ParticleBoneTemplate readBone(BBBone bone, BBModel bbModel) {
        var bbOrigin = bone.getOrigin();
        var bbRotation = bone.getRotation();
        Vector origin;
        EulerAngle rotation = readRotation(bbRotation);
        if (bbOrigin == null || bbOrigin.length < 3) {
            origin = new Vector();
        } else {
            origin = new Vector(bbOrigin[0]/16,bbOrigin[1]/16,bbOrigin[2]/16);
        }
        List<ParticlePartTemplate> parts = new ArrayList<>();
        List<ParticleBoneTemplate> children = new ArrayList<>();

        Bukkit.broadcastMessage("Children: "+bone.getChildren().length);
        for (var child : bone.getChildren()) {
            if (child instanceof BBBone childBBBone) {
                var childBone = readBone(childBBBone,bbModel);
                if (childBone != null) {
                    children.add(childBone);
                }
            }
            else if (child instanceof BBElementChildren childBBElement) {
                for (var element : bbModel.getElements()) {
                    if (element.getUuid().equals(childBBElement.getUuid())) {
                        var part = readPart(element);
                        if (part != null) {
                            parts.add(part);
                        }
                        break;
                    }
                }
            }
        }
        return new ParticleBoneTemplate(Particle.VILLAGER_HAPPY,bone.getName(),origin,rotation,parts,children);
    }

    private ParticlePartTemplate readPart(BBElement element) {
        var from = element.getFrom();
        var to = element.getTo();

        var bbRotation = element.getRotation();
        var bbOrigin = element.getOrigin();
        Vector origin;
        EulerAngle rotation = readRotation(bbRotation);
        if (bbOrigin == null || bbOrigin.length < 3) {
            origin = new Vector();
        } else {
            origin = new Vector(bbOrigin[0]/16,bbOrigin[1]/16,bbOrigin[2]/16);
        }

        double x = (from[0] + to[0])/2;
        double y = (from[1] + to[1])/2;
        double z = (from[2] + to[2])/2;
        if (Math.abs(x) < 0.001) {
            x = 0;
        }
        if (Math.abs(y) < 0.001) {
            y = 0;
        }
        if (Math.abs(z) < 0.001) {
            z = 0;
        }

        return new ParticlePartTemplate(element.getName(), Particle.VILLAGER_HAPPY,origin,new Vector(x/16,y/16,z/16),rotation);
    }

    private EulerAngle readRotation(Double[] doubles) {
        EulerAngle rotation;
        if (doubles == null || doubles.length < 3) {
            rotation = new EulerAngle(0,0,0);
        } else {
            rotation = new EulerAngle(-Math.toRadians(doubles[0]),-Math.toRadians(doubles[1]),Math.toRadians(doubles[2]));
        }
        return rotation;
    }
}

package xyz.larkyy.particleengine.particleengine.animation;

import org.bukkit.util.Vector;
import xyz.larkyy.animationlib.animationlib.animation.LoopMode;
import xyz.larkyy.animationlib.animationlib.timeline.InterpolatedTimeline;
import xyz.larkyy.animationlib.animationlib.timeline.InterpolationType;
import xyz.larkyy.particleengine.particleengine.animation.keyframe.PositionKeyFrame;
import xyz.larkyy.particleengine.particleengine.animation.keyframe.RotationKeyFrame;

public class ParticleTimeline {

    private final InterpolatedTimeline<PositionKeyFrame> positionTimeline;
    private final InterpolatedTimeline<RotationKeyFrame> rotationTimeline;

    public ParticleTimeline() {
        this(new InterpolatedTimeline<>(), new InterpolatedTimeline<>());
    }

    public ParticleTimeline(InterpolatedTimeline<PositionKeyFrame> positionTimeline, InterpolatedTimeline<RotationKeyFrame> rotationTimeline) {
        this.positionTimeline = positionTimeline;
        this.rotationTimeline = rotationTimeline;
    }

    public void addPositionFrame(double time, Vector vector, InterpolationType interpolationType) {
        var keyframe = new PositionKeyFrame(vector);
        keyframe.setInterpolationType(interpolationType);
        this.positionTimeline.addFrame(time,keyframe);
    }
    public void addRotationFrame(double time, Vector vector, InterpolationType interpolationType) {
        var keyframe = new PositionKeyFrame(vector);
        keyframe.setInterpolationType(interpolationType);
        this.positionTimeline.addFrame(time,keyframe);
    }

    public Vector getPositionFrame(double time) {
        return positionTimeline.getInterpolatedValue(time);
    }

    public Vector getRotationFrame(double time) {
        return rotationTimeline.getInterpolatedValue(time);
    }
}

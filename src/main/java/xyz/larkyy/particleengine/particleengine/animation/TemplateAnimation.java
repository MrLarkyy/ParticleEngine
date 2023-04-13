package xyz.larkyy.particleengine.particleengine.animation;

import org.bukkit.util.Vector;
import xyz.larkyy.animationlib.animationlib.animation.LoopMode;

import java.util.HashMap;
import java.util.Map;

public class TemplateAnimation {

    private final String name;
    private final double length;
    private final LoopMode loopMode;
    private final Map<String,ParticleTimeline> timelines;

    public TemplateAnimation(String name,double length,LoopMode loopMode) {
        this(name,length,loopMode,new HashMap<>());
    }

    public TemplateAnimation(String name, double length, LoopMode loopMode, Map<String,ParticleTimeline> timelines) {
        this.name = name;
        this.length = length;
        this.loopMode = loopMode;
        this.timelines = timelines;
    }

    public void addTimeline(String bone, ParticleTimeline timeline) {
        timelines.put(bone,timeline);
    }

    public ParticleTimeline getTimeline(String bone) {
        return timelines.get(bone);
    }

    public Vector getRotation(String bone, double time) {
        if (!timelines.containsKey(bone)) {
            return new Vector();
        }
        return timelines.get(bone).getRotationFrame(time);
    }
    public Vector getPosition(String bone, double time) {
        if (!timelines.containsKey(bone)) {
            return new Vector();
        }
        return timelines.get(bone).getPositionFrame(time);
    }

    public LoopMode getLoopMode() {
        return loopMode;
    }

    public double getLength() {
        return length;
    }

    public String getName() {
        return name;
    }
}

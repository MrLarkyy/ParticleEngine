package xyz.larkyy.particleengine.particleengine.animation;

import org.bukkit.Bukkit;
import org.bukkit.util.Vector;
import xyz.larkyy.particleengine.particleengine.particle.spawned.SpawnedParticle;

import java.util.HashMap;
import java.util.Map;

public class AnimationHandler {

    private final SpawnedParticle spawnedParticle;
    private RunningAnimation runningAnimation = null;

    public AnimationHandler(SpawnedParticle spawnedParticle) {
        this.spawnedParticle = spawnedParticle;
    }

    public void update() {
        if (runningAnimation != null) {
            if (!runningAnimation.update()) {
                runningAnimation = null;
            }
        }
    }

    public boolean isPlaying() {
        return runningAnimation != null;
    }

    public void playAnimation(String name, double speed) {
        var animation = spawnedParticle.getTemplate().getAnimation(name);
        if (animation == null) {
            Bukkit.broadcastMessage("Unknown animation! "+name);
            return;
        } else {
            Bukkit.broadcastMessage("Correct animation!");
        }
        this.runningAnimation = new RunningAnimation(this,animation,speed);
    }

    public Vector getPosition(String name) {
        if (runningAnimation == null) {
            return new Vector();
        }
        return runningAnimation.getPosition(name);
    }
    public Vector getRotation(String name) {
        if (runningAnimation == null) {
            return new Vector();
        }
        return runningAnimation.getRotation(name);
    }

    public boolean isRunningAnimation(String animationName) {
        if (runningAnimation == null) {
            return false;
        }
        return runningAnimation.getName().equals(animationName);
    }

    public void stop() {
        runningAnimation = null;
    }

}

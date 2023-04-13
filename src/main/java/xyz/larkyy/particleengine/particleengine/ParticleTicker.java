package xyz.larkyy.particleengine.particleengine;

import org.bukkit.scheduler.BukkitRunnable;

public class ParticleTicker extends BukkitRunnable {
    @Override
    public void run() {
        ParticleEngine.getInstance().getParticleHandler().tick();
    }
}

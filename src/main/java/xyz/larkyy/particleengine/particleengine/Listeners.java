package xyz.larkyy.particleengine.particleengine;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.larkyy.particleengine.particleengine.particle.spawned.SpawnedParticle;

public class Listeners implements Listener {

    private SpawnedParticle spawnedParticle = null;

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        var p = e.getPlayer();
        var msg = e.getMessage();
        new BukkitRunnable() {
            @Override
            public void run() {
                if (msg.contains("spawn particle")) {
                    var template = ParticleEngine.getInstance().getParticleHandler().getTemplate("test_particle");
                    if (template == null) {
                        return;
                    }
                    var spawned = ParticleEngine.getInstance().getParticleHandler().spawn(template,p.getLocation());
                    Bukkit.broadcastMessage("Particle spawned!");
                    spawnedParticle = spawned;
                }
                if (msg.contains("play animation")) {
                    if (spawnedParticle == null) return;
                    spawnedParticle.getAnimationHandler().playAnimation("idle",1);
                    Bukkit.broadcastMessage("Playing animation!");
                }
            }
        }.runTask(ParticleEngine.getInstance());
    }

}

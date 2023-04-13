package xyz.larkyy.particleengine.particleengine;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class Listeners implements Listener {

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
                    ParticleEngine.getInstance().getParticleHandler().spawn(template,p.getLocation());
                    Bukkit.broadcastMessage("Particle spawned!");
                }
            }
        }.runTask(ParticleEngine.getInstance());
    }

}

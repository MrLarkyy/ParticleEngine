package xyz.larkyy.particleengine.particleengine;

import org.bukkit.plugin.java.JavaPlugin;

public final class ParticleEngine extends JavaPlugin {

    private static ParticleEngine instance;
    private ParticleHandler particleHandler;

    @Override
    public void onEnable() {
        instance = this;
        this.particleHandler = new ParticleHandler();
        new ParticleTicker().runTaskTimer(this,1,1);
        particleHandler.loadTemplates();
        getServer().getPluginManager().registerEvents(new Listeners(),this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static ParticleEngine getInstance() {
        return instance;
    }

    public ParticleHandler getParticleHandler() {
        return particleHandler;
    }
}

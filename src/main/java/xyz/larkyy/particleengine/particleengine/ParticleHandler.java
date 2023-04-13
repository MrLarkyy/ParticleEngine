package xyz.larkyy.particleengine.particleengine;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import xyz.larkyy.particleengine.particleengine.particle.spawned.SpawnedParticle;
import xyz.larkyy.particleengine.particleengine.particle.template.ParticlePartTemplate;
import xyz.larkyy.particleengine.particleengine.particle.template.ParticleTemplate;
import xyz.larkyy.particleengine.particleengine.reader.BlockBenchReader;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParticleHandler {

    private final List<SpawnedParticle> spawnedParticles = new ArrayList<>();
    private final Map<String,ParticleTemplate> templates = new HashMap<>();
    private final BlockBenchReader reader = new BlockBenchReader();

    public SpawnedParticle spawn(ParticleTemplate template, Location location) {
        var p = template.spawn(location);
        spawnedParticles.add(p);
        return p;
    }

    public ParticleTemplate getTemplate(String name) {
        return templates.get(name);
    }

    public void tick() {
        spawnedParticles.forEach(SpawnedParticle::tick);
    }

    public void loadTemplates() {
        templates.clear();
        var dataFolder = ParticleEngine.getInstance().getDataFolder();
        dataFolder.mkdirs();
        for (File file : dataFolder.listFiles()) {
            var model = reader.read(file);
            if (model == null) {
                Bukkit.broadcastMessage("File "+file.getName()+" could not be loaded!");
                continue;
            }
            templates.put(model.getName(),model);
            Bukkit.broadcastMessage("Loaded model "+model.getName());
        }
    }

}

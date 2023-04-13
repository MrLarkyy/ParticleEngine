package xyz.larkyy.particleengine.particleengine.particle.spawned;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;
import xyz.larkyy.particleengine.particleengine.math.Quaternion;
import xyz.larkyy.particleengine.particleengine.particle.template.ParticlePartTemplate;

public class ParticlePart {

    private final ParticlePartTemplate template;

    public ParticlePart(ParticlePartTemplate particlePartTemplate) {
        this.template = particlePartTemplate;
    }

    protected void spawn(Location location, Vector parentOrigin, Vector parentPivot, EulerAngle parentAngle) {
        var newLocation = location.clone();//.add(particlePartTemplate.getTranslation());
        var rotation = template.getRotation();
        var vector = template.getTranslation().clone().subtract(template.getOrigin());

        vector.rotateAroundX(rotation.getX());
        vector.rotateAroundY(rotation.getY());
        vector.rotateAroundZ(rotation.getZ());

        vector.add(parentOrigin.clone().subtract( template.getOrigin()));

        vector.rotateAroundX(parentAngle.getX());
        vector.rotateAroundY(parentAngle.getY());
        vector.rotateAroundZ(parentAngle.getZ());

        vector.add(parentPivot);
        /*
        var v = template.getTranslation().clone().subtract(parentPivot.clone().subtract(parentOrigin.clone()));

        if (template.getName().equalsIgnoreCase("cube")) {
            Bukkit.broadcastMessage("Origin: "+parentOrigin.getX()+" "+parentOrigin.getY()+" "+parentOrigin.getZ());
            Bukkit.broadcastMessage("Translation: "+template.getTranslation().getX()+" "+template.getTranslation().getY()+" "+template.getTranslation().getZ());
            Bukkit.broadcastMessage("V: "+v.getX()+" "+v.getY()+" "+v.getZ());
        }

        if (!(v.isZero())) {
            vector = template.getTranslation().clone().subtract(parentPivot);
        }


        Quaternion quatStart = new Quaternion(parentAngle);
        Quaternion quat = new Quaternion(rotation);
        Quaternion resultQuat = quat.mul(quatStart);
        rotation = resultQuat.getEulerAnglesXYZ();

        v.rotateAroundX(rotation.getX());
        v.rotateAroundY(-rotation.getY());
        v.rotateAroundZ(-rotation.getZ());
        */

        newLocation.add(vector);
        location.getWorld().spawnParticle(Particle.VILLAGER_HAPPY,newLocation,1);
    }

    private EulerAngle getFinalAngle(EulerAngle parentAngle) {
        var rotation = template.getRotation();

        if (parentAngle != EulerAngle.ZERO) {
            Quaternion startQuat = new Quaternion(parentAngle);
            Quaternion rotationQuat = new Quaternion(rotation);

            Quaternion resultQuat = rotationQuat.mul(startQuat);
            rotation = resultQuat.getEulerAnglesXYZ();
        }
        return rotation;
    }

    public Vector getFinalPivot(Vector parentOrigin, Vector parentPivot, EulerAngle parentRotation) {
        Vector pivot = template.getTranslation().clone();

        if (!parentPivot.equals(new Vector())) {
            pivot = parentOrigin.clone().subtract(pivot);
            pivot.rotateAroundX(parentRotation.getX());
            pivot.rotateAroundY(-parentRotation.getY());
            pivot.rotateAroundZ(-parentRotation.getZ());

            pivot = parentPivot.clone().subtract(pivot);
        }
        return pivot;
    }

    public ParticlePartTemplate getParticlePartTemplate() {
        return template;
    }
}

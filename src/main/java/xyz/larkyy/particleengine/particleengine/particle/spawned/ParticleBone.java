package xyz.larkyy.particleengine.particleengine.particle.spawned;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;
import xyz.larkyy.particleengine.particleengine.math.Quaternion;
import xyz.larkyy.particleengine.particleengine.particle.template.ParticleBoneTemplate;

import java.util.List;

public class ParticleBone {

    private final ParticleBoneTemplate template;
    private final List<ParticleBone> children;
    private final List<ParticlePart> parts;

    public ParticleBone(ParticleBoneTemplate template, List<ParticleBone> children, List<ParticlePart> parts) {
        this.template = template;
        this.children = children;
        this.parts = parts;
    }

    protected void spawn(Location location, Vector parentOrigin, Vector parentPivot, EulerAngle parentAngle) {
        if (parentOrigin == null) {
            parentOrigin = new Vector();
        }
        if (parentPivot == null) {
            parentPivot = new Vector();
        }
        if (parentAngle == null) {
            parentAngle = EulerAngle.ZERO;
        }

        var rotation = getFinalAngle(parentAngle);
        var pivot = getFinalPivot(parentOrigin,parentPivot,parentAngle);

        for (ParticlePart part : parts) {
            part.spawn(location.clone(), template.getOrigin().clone(), pivot.clone(), rotation);
        }
        for (ParticleBone child : children) {
            child.spawn(location.clone(), template.getOrigin().clone(), pivot.clone(), rotation);
        }
    }

    public Vector getFinalPivot(Vector parentOrigin, Vector parentPivot, EulerAngle parentRotation) {
        Vector pivot = template.getOrigin().clone();

        if (!(parentRotation.getX() == 0 && parentRotation.getY() == 0 && parentRotation.getZ() == 0)) {
            pivot = parentOrigin.clone().subtract(pivot);
            pivot.rotateAroundX(parentRotation.getX());
            pivot.rotateAroundY(-parentRotation.getY());
            pivot.rotateAroundZ(-parentRotation.getZ());

            pivot = parentPivot.clone().subtract(pivot);
        }
        return pivot;
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

    public List<ParticleBone> getChildren() {
        return children;
    }

    public ParticleBoneTemplate getTemplate() {
        return template;
    }

    public List<ParticlePart> getParts() {
        return parts;
    }
}

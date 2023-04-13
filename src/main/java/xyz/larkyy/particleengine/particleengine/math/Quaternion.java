package xyz.larkyy.particleengine.particleengine.math;

import org.bukkit.util.EulerAngle;
import org.joml.Math;

public class Quaternion {

    /**
     * The first component of the vector part.
     */
    private float x;
    /**
     * The second component of the vector part.
     */
    private float y;
    /**
     * The third component of the vector part.
     */
    private float z;
    /**
     * The real/scalar part of the quaternion.
     */
    private float w;

    public Quaternion() {
        w = 1.0f;
    }

    public Quaternion(EulerAngle eulerAngle) {
        w = 1.0f;
        rotationXYZ((float) eulerAngle.getX(), (float) eulerAngle.getY(), (float) eulerAngle.getZ());
    }


    public Quaternion rotationXYZ(float angleX, float angleY, float angleZ) {
        double cX = Math.cos(angleX*0.5);
        double cY = Math.cos(angleY*-0.5);
        double cZ = Math.cos(angleZ*0.5);
        double sX = Math.sin(angleX*0.5);
        double sY = Math.sin(angleY*-0.5);
        double sZ = Math.sin(angleZ*0.5);

        x = (float) (sX *cY*cZ+cX*sY*sZ);
        y = (float) (cX*sY*cZ-sX*cY*sZ);
        z = (float) (cX*cY*sZ+sX*sY*cZ);
        w = (float) (cX*cY*cZ-sX*sY*sZ);
        return this;
    }

    public Quaternion rotationXYZ(EulerAngle eulerAngle) {
        return rotationXYZ((float) eulerAngle.getX(), (float) eulerAngle.getY(), (float) eulerAngle.getZ());
    }

    public Quaternion mul(Quaternion q) {
        return set(Math.fma(w, q.x(), Math.fma(x, q.w(), Math.fma(y, q.z(), -z * q.y()))),
                Math.fma(w, q.y(), Math.fma(-x, q.z(), Math.fma(y, q.w(), z * q.x()))),
                Math.fma(w, q.z(), Math.fma(x, q.y(), Math.fma(-y, q.x(), z * q.w()))),
                Math.fma(w, q.w(), Math.fma(-x, q.x(), Math.fma(-y, q.y(), -z * q.z()))));
    }

    public Quaternion set(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
        return this;
    }

    public EulerAngle getEulerAnglesXYZ() {
        double x2 = x+x, y2 = y+y, z2 = z+z;
        double xx = x * x2, xy = x * y2, xz = x * z2;
        double yy = y * y2, yz = y * z2, zz = z * z2;
        double wx = w * x2, wy = w * y2, wz = w * z2;

        double ex, ey, ez,
                m11 = 1 - (yy + zz),
                m12 = xy - wz,
                m13 = xz + wy,
                m22 = 1-(xx+zz),
                m23 = yz - wx,
                m32 = yz + wx,
                m33 = 1 - (xx + yy);

        ey = Math.asin(Math.clamp(m13,-1,1));
        if (Math.abs(m13) < 0.9999999) {
            ex = Math.atan2(-m23,m33);
            ez = Math.atan2(-m12,m11);
        }
        else {
            ex = Math.atan2(m32,m22);
            ez = 0;
        }

        return new EulerAngle(ex,-ey,ez);
    }

    public float w() {
        return w;
    }

    public float x() {
        return x;
    }

    public float y() {
        return y;
    }

    public float z() {
        return z;
    }
}

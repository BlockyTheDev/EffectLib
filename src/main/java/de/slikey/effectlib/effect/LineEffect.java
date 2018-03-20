package de.slikey.effectlib.effect;

import de.slikey.effectlib.Effect;
import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.EffectType;
import de.slikey.effectlib.util.ParticleEffect;
import org.bukkit.Location;
import org.bukkit.util.Vector;

public class LineEffect extends Effect {

    /**
     * ParticleType of spawned particle
     */
    public ParticleEffect particle = ParticleEffect.FLAME;

    /**
     * Should it do a zig zag?
     */
    public boolean isZigZag = false;

    /**
     * Number of zig zags in the line
     */
    public int zigZags = 10;

    /**
     * Direction of zig-zags
     */
    public Vector zigZagOffset = new Vector(0,0.1,0);

    /**
     * Particles per arc
     */
    public int particles = 100;

    /**
     * Length of arc
     * A non-zero value here will use a length instead of the target endpoint
     */
    public double length = 0;

    /**
     * Internal boolean
     */
    protected boolean zag = false;

    /**
     * Internal counter
     */
    protected int step = 0;

    public LineEffect(EffectManager effectManager) {
        super(effectManager);
        type = EffectType.REPEATING;
        period = 1;
        iterations = 1;
    }

    @Override
    public void reset() {
        this.step = 0;
    }

    @Override
    public void onRun() {
        Location location = getLocation();
        Location target = null;
        if (length > 0) {
            target = location.clone().add(location.getDirection().normalize().multiply(length));
        } else {
            target = getTarget();
        }
        int amount = particles / zigZags;
        if (target == null) {
            cancel();
            return;
        }
        Vector link = target.toVector().subtract(location.toVector());
        float length = (float) link.length();
        link.normalize();

        float ratio = length / particles;
        Vector v = link.multiply(ratio);
        Location loc = location.clone().subtract(v);
        for (int i = 0; i < particles; i++) {
            if (isZigZag) {
                if (zag) {
                    loc.add(zigZagOffset);
                } else {
                    loc.subtract(zigZagOffset);
                }
            }
            if (step >= amount) {
                if (zag) {
                    zag = false;
                } else {
                    zag = true;
                }
                step = 0;
            }
            step++;
            loc.add(v);
            display(particle, loc);
        }
    }

}
